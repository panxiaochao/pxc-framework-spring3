/*
 * Copyright © 2024-2025 Lypxc(潘) (545685602@qq.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.panxiaochao.spring3.qrcode.utils;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.encoder.SymbolShapeHint;
import com.google.zxing.multi.qrcode.QRCodeMultiReader;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import io.github.panxiaochao.spring3.core.utils.Base64Util;
import io.github.panxiaochao.spring3.core.utils.CharPools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.EnumMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * <p>
 * 二维码工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-09-08
 */
public class QRCodeUtil {

    /**
     * 二维码 内容
     */
    private final String content;

    /**
     * 图片 大小, 默认正方形
     */
    private int size;

    /**
     * 内容 编码格式
     */
    private Charset encode;

    /**
     * 错误修正等级 (Error Collection Level)
     */
    private ErrorCorrectionLevel errorCorrectionLevel;

    /**
     * 错误修正等级的具体值
     */
    private double errorCorrectionLevelValue;

    /**
     * 前景色
     */
    private Color foreGroundColor;

    /**
     * 背景色
     */
    private Color backGroundColor;

    /**
     * 图片的文件格式
     */
    private String imageFormat;

    /**
     * 删除图片的外白边
     */
    private boolean deleteMargin;

    /**
     * 图片的外边距大小 (Quiet Zone) 1-4
     */
    private int margin;

    /**
     * 提供给编码器额外的参数
     */
    private final Map<EncodeHintType, Object> hints;

    /**
     * 需要添加的图片
     */
    private BufferedImage qrcodeImage;

    /**
     * 创建一个带有默认值的 QRCode 生成器的格式。默认值如下
     *
     * <ul>
     * <li>图片大小: 300px</li>
     * <li>内容编码格式: UTF-8</li>
     * <li>错误修正等级: Level M (有15% 的内容可被修正)</li>
     * <li>前景色: 黑色</li>
     * <li>背景色: 白色</li>
     * <li>输出图片的文件格式: png</li>
     * <li>图片空白区域大小: 2个单位</li>
     * </ul>
     */
    private QRCodeUtil(String content) {
        this.content = content;
        this.size = 300;
        this.encode = StandardCharsets.UTF_8;
        this.errorCorrectionLevel = ErrorCorrectionLevel.M;
        this.errorCorrectionLevelValue = 0.15;
        this.foreGroundColor = Color.BLACK;
        this.backGroundColor = Color.WHITE;
        this.imageFormat = "png";
        this.margin = 2;
        this.hints = new Hashtable<>();
    }

    /**
     * 使用带默认值的「QRCode 生成器格式」来创建一个 QRCode 处理器。
     *
     * @param content 所要生成 QRCode 的内容
     * @return QRCodeUtil
     */
    public static QRCodeUtil build(final String content) {
        return new QRCodeUtil(content);
    }

    /**
     * 设置图片的大小。图片的大小等于实际内容与外边距的值（建议设置成偶数值）。
     *
     * @param size 图片的大小
     * @return QRCodeUtil
     */
    public QRCodeUtil size(int size) {
        this.size = size;
        return this;
    }

    /**
     * 设置内容编码格式。
     *
     * @param encode 内容编码格式
     * @return QRCodeUtil
     */
    public QRCodeUtil encode(Charset encode) {
        if (null != encode) {
            this.encode = encode;
        }
        return this;
    }

    /**
     * 设置错误修正等级。其定义如下
     *
     * <ul>
     * <li>L: 有 7% 的内容可被修正</li>
     * <li>M: 有 15% 的内容可被修正</li>
     * <li>Q: 有 25% 的内容可被修正</li>
     * <li>H: 有 30% 的内容可被修正</li>
     * </ul>
     *
     * @param errorCorrectionLevel 错误修正等级
     * @return QRCodeUtil
     */
    public QRCodeUtil errorCorrectionLevel(ErrorCorrectionLevel errorCorrectionLevel) {
        switch (errorCorrectionLevel) {
            case L:
                this.errorCorrectionLevel = errorCorrectionLevel;
                this.errorCorrectionLevelValue = 0.07;
                break;
            case M:
                this.errorCorrectionLevel = errorCorrectionLevel;
                this.errorCorrectionLevelValue = 0.15;
                break;
            case Q:
                this.errorCorrectionLevel = errorCorrectionLevel;
                this.errorCorrectionLevelValue = 0.25;
                break;
            case H:
                this.errorCorrectionLevel = errorCorrectionLevel;
                this.errorCorrectionLevelValue = 0.3;
                break;
            default:
                this.errorCorrectionLevel = ErrorCorrectionLevel.M;
                this.errorCorrectionLevelValue = 0.15;
        }
        return this;
    }

    /**
     * 设置前景色。值为十六进制的颜色值（与 CSS 定义颜色的值相同，不支持简写），可以忽略「#」符号。
     *
     * @param foreGroundColor 前景色的值
     * @return QRCodeUtil
     */
    public QRCodeUtil foreGroundColor(String foreGroundColor) {
        try {
            this.foreGroundColor = getColor(foreGroundColor);
        } catch (NumberFormatException e) {
            this.foreGroundColor = Color.BLACK;
        }
        return this;
    }

    /**
     * 设置前景色。
     *
     * @param foreGroundColor 前景色的值
     * @return QRCodeUtil
     */
    public QRCodeUtil foreGroundColor(Color foreGroundColor) {
        this.foreGroundColor = foreGroundColor;
        return this;
    }

    /**
     * 设置背景色。值为十六进制的颜色值（与 CSS 定义颜色的值相同，不支持简写），可以忽略「#」符号。
     *
     * @param backGroundColor 前景色的值
     * @return QRCodeUtil
     */
    public QRCodeUtil backGroundColor(String backGroundColor) {
        try {
            this.backGroundColor = getColor(backGroundColor);
        } catch (NumberFormatException e) {
            this.backGroundColor = Color.WHITE;
        }
        return this;
    }

    /**
     * 设置背景色。
     *
     * @param backGroundColor 前景色的值
     * @return QRCodeUtil
     */
    public QRCodeUtil backGroundColor(Color backGroundColor) {
        this.backGroundColor = backGroundColor;
        return this;
    }

    /**
     * 设置图片的文件格式 。
     *
     * @param imageFormat 图片的文件格式
     * @return QRCodeUtil
     */
    public QRCodeUtil imageFormat(String imageFormat) {
        if (imageFormat != null) {
            this.imageFormat = imageFormat.toLowerCase();
        }
        return this;
    }

    /**
     * 删除白边。
     *
     * @param deleteMargin 删除白边
     * @return QRCodeUtil
     */
    public QRCodeUtil deleteMargin(boolean deleteMargin) {
        this.deleteMargin = deleteMargin;
        return this;
    }

    /**
     * 设置图片的外边距大小 。
     *
     * @param margin 图片的外边距大小
     * @return QRCodeUtil
     */
    public QRCodeUtil margin(int margin) {
        this.margin = margin;
        return this;
    }

    /**
     * 返回提供给编码器额外的参数。
     *
     * @return 提供给编码器额外的参数
     */
    public Map<EncodeHintType, ?> getHints() {
        // 先清空
        hints.clear();
        // 配置纠错级别
        hints.put(EncodeHintType.ERROR_CORRECTION, this.errorCorrectionLevel);
        // 配置编码
        hints.put(EncodeHintType.CHARACTER_SET, this.encode);
        // 配置边距
        hints.put(EncodeHintType.MARGIN, this.margin);
        // 设置样式：不设置,正方形,矩形
        hints.put(EncodeHintType.DATA_MATRIX_SHAPE, SymbolShapeHint.FORCE_SQUARE);
        return hints;
    }

    /**
     * 设置添加的图片。
     *
     * @param qrcodeImage 添加的图片
     * @return QRCodeUtil
     */
    public QRCodeUtil logo(BufferedImage qrcodeImage) {
        this.qrcodeImage = qrcodeImage;
        return this;
    }

    /**
     * 设置添加的图片。
     *
     * @param logo 添加的图片
     * @return QRCodeUtil
     */
    public QRCodeUtil logo(File logo) {
        try {
            return logo(ImageIO.read(logo));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置添加的图片。
     *
     * @param url 添加的图片
     * @return QRCodeUtil
     */
    public QRCodeUtil logo(URL url) {
        try {
            return logo(ImageIO.read(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置添加的图片。
     *
     * @param iconPath 添加的图片
     * @return QRCodeUtil
     */
    public QRCodeUtil logo(String iconPath) {
        try {
            BufferedImage bufferedImage = isHttpUrl(iconPath) ? ImageIO.read(new URL(iconPath))
                    : ImageIO.read(new File(iconPath));
            return logo(bufferedImage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 设置添加的图片。
     *
     * @param logoStream 添加的图片流
     * @return QRCodeUtil
     */
    public QRCodeUtil logo(InputStream logoStream) {
        try {
            return logo(ImageIO.read(logoStream));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 写出二维码
     *
     * @param output OutputStream
     * @return 是否成功
     */
    public boolean write(OutputStream output) {
        try {
            BufferedImage bufferedImage = this.toImage();
            return ImageIO.write(bufferedImage, this.imageFormat, output);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 把指定的内容生成为一个 QRCode 的图片，之后保存到指定的文件中。
     *
     * @param f 指定的文件
     * @return 文件
     */
    public File toFile(String f) {
        return toFile(new File(f));
    }

    /**
     * 把指定的内容生成为一个 QRCodeUtil 的图片，之后保存到指定的文件中。
     *
     * @param qrCodeFile 指定的文件
     * @return 文件
     */
    public File toFile(File qrCodeFile) {
        if (!qrCodeFile.exists()) {
            qrCodeFile.getParentFile().mkdirs();
        }
        try {
            BufferedImage bufferedImage = this.toImage();
            ImageIO.write(bufferedImage, this.imageFormat, qrCodeFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return qrCodeFile;
    }

    /**
     * 使用带默认值的「QRCodeUtil 生成器格式」，把指定的内容生成为一个 QRCodeUtil 的 base64 image。
     *
     * @return base64 字符串
     */
    public String toBase64() {
        return "data:image/png;base64," + Base64Util.encodeToString(toBytes());
    }

    /**
     * 使用带默认值的「QRCodeUtil 生成器格式」，把指定的内容生成为一个 QRCodeUtil 的 byte 数组。
     *
     * @return byte array
     */
    public byte[] toBytes() {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = this.toImage();
            if (ImageIO.write(bufferedImage, this.imageFormat, output)) {
                return output.toByteArray();
            }
            throw new IllegalArgumentException("ImageWriter formatName " + this.imageFormat + " writer is null.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用带默认值的「QRCodeUtil 生成器格式」，把指定的内容生成为一个 QRCodeUtil 的流。
     *
     * @return QRCodeUtil 的图像流
     */
    public ByteArrayInputStream toStream() {
        try (ByteArrayOutputStream output = new ByteArrayOutputStream()) {
            BufferedImage bufferedImage = this.toImage();
            if (ImageIO.write(bufferedImage, this.imageFormat, output)) {
                return new ByteArrayInputStream(output.toByteArray());
            }
            throw new IllegalArgumentException("ImageWriter formatName " + this.imageFormat + " writer is null.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用带默认值的「QRCodeUtil 生成器格式」，把指定的内容生成为一个 QRCodeUtil 的图像对象。
     *
     * @return QRCodeUtil 的图像对象
     */
    public BufferedImage toImage() {
        BitMatrix matrix;
        try {
            // 图像数据转换，使用了矩阵转换
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, this.size, this.size,
                    this.getHints());
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        if (this.deleteMargin) {
            matrix = deleteWhite(matrix);
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int fgColor = this.foreGroundColor.getRGB();
        int bgColor = this.backGroundColor.getRGB();
        BufferedImage image = new BufferedImage(width, height, ColorSpace.TYPE_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? fgColor : bgColor);
            }
        }
        if (null != this.qrcodeImage) {
            appendImage(image, this.qrcodeImage, this);
        }
        return image;
    }

    /**
     * 从指定的 QRCode 图片文件中解析出其内容。
     *
     * @param qrCodeFile QRCode 文件
     * @return QRCodeUtil 中的内容
     */
    public static String read(String qrCodeFile) {
        try {
            BufferedImage bufferedImage = isHttpUrl(qrCodeFile) ? ImageIO.read(new URL(qrCodeFile))
                    : ImageIO.read(new File(qrCodeFile));
            return read(bufferedImage, (Map<DecodeHintType, ?>) null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定的 QRCode 图片文件中解析出其内容。
     *
     * @param qrCodeFile QRCode 图片文件
     * @return QRCodeUtil 中的内容
     */
    public static String read(File qrCodeFile) {
        try {
            return read(ImageIO.read(qrCodeFile), (Map<DecodeHintType, ?>) null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定的 QRCode 图片链接中解析出其内容。
     *
     * @param qrCodeUrl QRCode 图片链接
     * @return QRCodeUtil 中的内容
     */
    public static String read(URL qrCodeUrl) {
        try {
            return read(ImageIO.read(qrCodeUrl), (Map<DecodeHintType, ?>) null);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定的 QRCode 图像对象中解析出其内容。
     *
     * @param qrCodeImage QRCode 图像对象
     * @return QRCodeUtil 中的内容
     */
    public static String read(BufferedImage qrCodeImage) {
        return read(qrCodeImage, (Map<DecodeHintType, ?>) null);
    }

    /**
     * 从指定的 QRCode 图片文件中解析出其内容。
     *
     * @param qrCodeFile QRCode 文件
     * @return QRCodeUtil 中的内容
     */
    public static String read(String qrCodeFile, Charset encode) {
        try {
            BufferedImage bufferedImage = isHttpUrl(qrCodeFile) ? ImageIO.read(new URL(qrCodeFile))
                    : ImageIO.read(new File(qrCodeFile));
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.CHARACTER_SET, encode);
            return read(bufferedImage, hints);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定的 QRCode 图片文件中解析出其内容。
     *
     * @param qrCodeFile QRCode 图片文件
     * @return QRCodeUtil 中的内容
     */
    public static String read(File qrCodeFile, Charset encode) {
        try {
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.CHARACTER_SET, encode);
            return read(ImageIO.read(qrCodeFile), hints);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定的 QRCode 图片链接中解析出其内容。
     *
     * @param qrCodeUrl QRCode 图片链接
     * @return QRCodeUtil 中的内容
     */
    public static String read(URL qrCodeUrl, Charset encode) {
        try {
            Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
            hints.put(DecodeHintType.CHARACTER_SET, encode);
            return read(ImageIO.read(qrCodeUrl), hints);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 从指定的 QRCode 图像对象中解析出其内容。
     *
     * @param qrCodeImage QRCode 图像对象
     * @return QRCodeUtil 中的内容
     */
    public static String read(BufferedImage qrCodeImage, Charset encode) {
        Map<DecodeHintType, Object> hints = new EnumMap<>(DecodeHintType.class);
        hints.put(DecodeHintType.CHARACTER_SET, encode);
        return read(qrCodeImage, hints);
    }

    /**
     * 从指定的 QRCode 图像对象中解析出其内容。
     *
     * @param qrCodeImage QRCode 图像对象
     * @param hints       hints
     * @return QRCodeUtil 中的内容
     */
    public static String read(BufferedImage qrCodeImage, Map<DecodeHintType, ?> hints) {
        LuminanceSource source = new BufferedImageLuminanceSource(qrCodeImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new QRCodeMultiReader().decode(bitmap, hints);
            return result.getText();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            throw new RuntimeException(e);
        } finally {
            qrCodeImage.getGraphics().dispose();
        }
    }

    /**
     * 从指定的 QRCode 图片文件中解析出其内容。
     *
     * @param qrCodeFile QRCode 文件
     * @return QRCodeUtil 中的内容
     */
    public static byte[] readRawBytes(String qrCodeFile) {
        return readRawBytes(read(qrCodeFile));
    }

    /**
     * 从指定的 QRCode 图片文件中解析出其内容。
     *
     * @param qrCodeFile QRCode 图片文件
     * @return QRCodeUtil 中的内容
     */
    public static byte[] readRawBytes(File qrCodeFile) {
        return readRawBytes(read(qrCodeFile));
    }

    /**
     * 从指定的 QRCode 图片链接中解析出其内容。
     *
     * @param qrCodeUrl QRCode 图片链接
     * @return QRCodeUtil 中的内容
     */
    public static byte[] readRawBytes(URL qrCodeUrl) {
        return readRawBytes(read(qrCodeUrl));
    }

    /**
     * 从指定的 QRCode 图像对象中解析出其内容。
     *
     * @param qrCodeImage QRCode 图像对象
     * @return QRCodeUtil 中的内容
     */
    public static byte[] readRawBytes(BufferedImage qrCodeImage) {
        LuminanceSource source = new BufferedImageLuminanceSource(qrCodeImage);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            Result result = new QRCodeMultiReader().decode(bitmap);
            return result.getRawBytes();
        } catch (NotFoundException | ChecksumException | FormatException e) {
            throw new RuntimeException(e);
        } finally {
            qrCodeImage.getGraphics().dispose();
        }
    }

    /**
     * 追加图片
     *
     * @param appendImage QRCode 图像对象
     */
    private void appendImage(BufferedImage appendImage) {
        appendImage(this.qrcodeImage, appendImage, this);
    }

    /**
     * 追加图片
     *
     * @param qrCodeImage QRCode 图像对象
     * @param appendImage QRCode 追加图像对象
     * @param qrCodeUtil  自我对象
     */
    private static void appendImage(BufferedImage qrCodeImage, BufferedImage appendImage, QRCodeUtil qrCodeUtil) {
        int baseWidth = qrCodeImage.getWidth();
        int baseHeight = qrCodeImage.getHeight();
        // 计算 icon 的最大边长
        // 公式为 二维码面积*错误修正等级*0.4 的开方
        int maxWidth = (int) Math.sqrt(baseWidth * baseHeight * qrCodeUtil.errorCorrectionLevelValue * 0.4);
        // 获取 icon 的实际边长
        int logoRectWidth = Math.min(maxWidth, appendImage.getWidth());
        int logoRectHeight = Math.min(maxWidth, appendImage.getHeight());
        // 圆角矩形
        BufferedImage logoRect = new BufferedImage(logoRectWidth, logoRectHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = logoRect.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        // 画 appendImage 区域
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, logoRectWidth, logoRectHeight);
        g2.setComposite(AlphaComposite.SrcAtop);
        // 画 灰色 框框 2 px
        g2.setColor(Color.LIGHT_GRAY);
        g2.fillRect(2, 2, logoRectWidth - 4, logoRectHeight - 4);
        g2.setComposite(AlphaComposite.SrcAtop);
        // 画 appendImage 图
        g2.drawImage(appendImage, 4, 4, logoRectWidth - 8, logoRectHeight - 8, null);
        appendImage.getGraphics().dispose();
        g2.dispose();
        // 将 logo 添加到 二维码上
        Graphics2D gc = (Graphics2D) qrCodeImage.getGraphics();
        gc.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        gc.setColor(qrCodeUtil.backGroundColor);
        gc.drawImage(logoRect, (baseWidth - logoRectWidth) / 2, (baseHeight - logoRectHeight) / 2, null);
        gc.dispose();
    }

    private static Color getColor(String hexString) {
        if (CharPools.HASH == hexString.charAt(0)) {
            return new Color(Long.decode(hexString).intValue());
        } else {
            return new Color(Long.decode("0xFF" + hexString).intValue());
        }
    }

    private static BitMatrix deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;
        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1])) {
                    resMatrix.set(i, j);
                }
            }
        }
        return resMatrix;
    }

    /**
     * 判断是否 http 地址
     *
     * @param text 文本
     * @return 是否 http 地址
     */
    private static boolean isHttpUrl(String text) {
        return text.startsWith("http://") || text.startsWith("https://");
    }

}

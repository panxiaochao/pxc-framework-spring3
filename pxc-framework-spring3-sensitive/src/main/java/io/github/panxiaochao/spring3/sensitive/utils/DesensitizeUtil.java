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
package io.github.panxiaochao.spring3.sensitive.utils;


import io.github.panxiaochao.spring3.core.utils.CharPools;
import io.github.panxiaochao.spring3.core.utils.CharSequenceUtil;
import io.github.panxiaochao.spring3.core.utils.StrUtil;

import java.util.function.Predicate;

/**
 * <p>
 * 脱敏工具类
 * </p>
 *
 * <ul>
 * <li>用户ID</li>
 * <li>中文名</li>
 * <li>身份证</li>
 * <li>座机号</li>
 * <li>手机号</li>
 * <li>地址</li>
 * <li>电子邮件</li>
 * <li>密码</li>
 * <li>车牌</li>
 * <li>银行卡号</li>
 * </ul>
 *
 * @author Lypxc
 * @since 2023-08-31
 */
public class DesensitizeUtil {

    public DesensitizeUtil() {
    }

    /**
     * 支持的脱敏类型枚举
     */
    public enum desensitizeType {

        /**
         * 用户id
         */
        USER_ID,
        /**
         * 中文名
         */
        CHINESE_NAME,
        /**
         * 身份证号
         */
        ID_CARD,
        /**
         * 座机号
         */
        FIXED_PHONE,
        /**
         * 手机号
         */
        MOBILE_PHONE,
        /**
         * 地址
         */
        ADDRESS,
        /**
         * 电子邮件
         */
        EMAIL,
        /**
         * 密码
         */
        PASSWORD,
        /**
         * 中国大陆车牌，包含普通车辆、新能源车辆
         */
        CAR_LICENSE,
        /**
         * 银行卡
         */
        BANK_CARD,
        /**
         * IPv4地址
         */
        IPV4,
        /**
         * IPv6地址
         */
        IPV6,
        /**
         * 定义了一个first_mask的规则，只显示第一个字符。
         */
        FIRST_MASK

    }

    /**
     * <p>
     * 脱敏，使用默认的脱敏策略
     * </p>
     *
     * <pre>
     * {@code
     *    DesensitizeUtil.desensitize("100", DesensitizeUtil.desensitizeType.USER_ID)) =  "0"
     * 	  DesensitizeUtil.desensitize("段正淳", DesensitizeUtil.desensitizeType.CHINESE_NAME)) = "段**"
     * 	  DesensitizeUtil.desensitize("51343620000320711X", DesensitizeUtil.desensitizeType.ID_CARD)) = "5***************1X"
     * 	  DesensitizeUtil.desensitize("09157518479", DesensitizeUtil.desensitizeType.FIXED_PHONE)) = "0915*****79"
     * 	  DesensitizeUtil.desensitize("18049531999", DesensitizeUtil.desensitizeType.MOBILE_PHONE)) = "180****1999"
     * 	  DesensitizeUtil.desensitize("浙江省杭州市西湖区武林银泰111号", DesensitizeUtil.desensitizeType.ADDRESS)) = "浙江省杭州市西湖********"
     * 	  DesensitizeUtil.desensitize("duandazhi-jack@gmail.com.cn", DesensitizeUtil.desensitizeType.EMAIL)) = "d*************@gmail.com.cn"
     * 	  DesensitizeUtil.desensitize("1234567890", DesensitizeUtil.desensitizeType.PASSWORD)) = "**********"
     * 	  DesensitizeUtil.desensitize("苏D40000", DesensitizeUtil.desensitizeType.CAR_LICENSE)) = "苏D4***0"
     * 	  DesensitizeUtil.desensitize("11011111222233333256", DesensitizeUtil.desensitizeType.BANK_CARD)) = "1101 **** **** **** 3256"
     * 	  DesensitizeUtil.desensitize("192.168.1.1", DesensitizeUtil.desensitizeType.IPV4)) = "192.*.*.*"
     * }
     * </pre>
     *
     * @param str             字符串
     * @param desensitizeType 脱敏类型;可以脱敏：用户id、中文名、身份证号、座机号、手机号、地址、电子邮件、密码
     * @return 脱敏之后的字符串
     */
    public static String desensitize(CharSequence str, DesensitizeUtil.desensitizeType desensitizeType) {
        if (StrUtil.isBlank(str)) {
            return StrUtil.EMPTY;
        }
        String newStr = String.valueOf(str);
        switch (desensitizeType) {
            case USER_ID:
                newStr = String.valueOf(userId());
                break;
            case CHINESE_NAME:
                newStr = chineseName(String.valueOf(str));
                break;
            case ID_CARD:
                newStr = idCardNum(String.valueOf(str), 1, 2);
                break;
            case FIXED_PHONE:
                newStr = fixedPhone(String.valueOf(str));
                break;
            case MOBILE_PHONE:
                newStr = mobilePhone(String.valueOf(str));
                break;
            case ADDRESS:
                newStr = address(String.valueOf(str), 8);
                break;
            case EMAIL:
                newStr = email(String.valueOf(str));
                break;
            case PASSWORD:
                newStr = password(String.valueOf(str));
                break;
            case CAR_LICENSE:
                newStr = carLicense(String.valueOf(str));
                break;
            case BANK_CARD:
                newStr = bankCard(String.valueOf(str));
                break;
            case IPV4:
                newStr = ipv4(String.valueOf(str));
                break;
            case IPV6:
                newStr = ipv6(String.valueOf(str));
                break;
            case FIRST_MASK:
                newStr = firstMask(String.valueOf(str));
                break;
            default:
        }
        return newStr;
    }

    /**
     * 【用户id】不对外提供userId
     *
     * @return 脱敏后的主键
     */
    public static Long userId() {
        return 0L;
    }

    /**
     * 定义了一个first_mask的规则，只显示第一个字符。<br>
     * 脱敏前：123456789；脱敏后：1********。
     *
     * @param str 字符串
     * @return 脱敏后的字符串
     */
    public static String firstMask(String str) {
        if (StrUtil.isBlank(str)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(str, 1, str.length());
    }

    /**
     * 【中文姓名】只显示第一个汉字，其他隐藏为2个星号，比如：李**
     *
     * @param fullName 姓名
     * @return 脱敏后的姓名
     */
    public static String chineseName(String fullName) {
        return firstMask(fullName);
    }

    /**
     * 【身份证号】前1位 和后2位
     *
     * @param idCardNum 身份证
     * @param front     保留：前面的front位数；从1开始
     * @param end       保留：后面的end位数；从1开始
     * @return 脱敏后的身份证
     */
    public static String idCardNum(String idCardNum, int front, int end) {
        // 身份证不能为空
        if (StrUtil.isBlank(idCardNum)) {
            return StrUtil.EMPTY;
        }
        // 需要截取的长度不能大于身份证号长度
        if ((front + end) > idCardNum.length()) {
            return StrUtil.EMPTY;
        }
        // 需要截取的不能小于0
        if (front < 0 || end < 0) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(idCardNum, front, idCardNum.length() - end);
    }

    /**
     * 【固定电话】前四位，后两位
     *
     * @param num 固定电话
     * @return 脱敏后的固定电话；
     */
    public static String fixedPhone(String num) {
        if (StrUtil.isBlank(num)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(num, 4, num.length() - 2);
    }

    /**
     * 【手机号码】前三位，后4位，其他隐藏，比如189****5550
     *
     * @param num 移动电话；
     * @return 脱敏后的移动电话；
     */
    public static String mobilePhone(String num) {
        if (StrUtil.isBlank(num)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(num, 3, num.length() - 4);
    }

    /**
     * 【地址】只显示到地区，不显示详细地址，比如：浙江省杭州市****
     *
     * @param address       家庭住址
     * @param sensitiveSize 敏感信息长度
     * @return 脱敏后的家庭地址
     */
    public static String address(String address, int sensitiveSize) {
        if (StrUtil.isBlank(address)) {
            return StrUtil.EMPTY;
        }
        int length = address.length();
        return StrUtil.hide(address, length - sensitiveSize, length);
    }

    /**
     * 【电子邮箱】邮箱前缀仅显示第一个字母，前缀其他隐藏，用星号代替，@及后面的地址显示，比如：d**@126.com
     *
     * @param email 邮箱
     * @return 脱敏后的邮箱
     */
    public static String email(String email) {
        if (StrUtil.isBlank(email)) {
            return StrUtil.EMPTY;
        }
        int index = email.indexOf('@');
        if (index <= 1) {
            return email;
        }
        return StrUtil.hide(email, 1, index);
    }

    /**
     * 【密码】密码的全部字符都用*代替，比如：******
     *
     * @param password 密码
     * @return 脱敏后的密码
     */
    public static String password(String password) {
        if (StrUtil.isBlank(password)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.repeat('*', password.length());
    }

    /**
     * <p>
     * 【中国车牌】车牌中间用*代替
     * </p>
     * <pre>
     *     eg1：null -》 ""
     *     eg1："" -》 ""
     *     eg3：苏D40000 -》 苏D4***0
     *     eg4：陕A12345D -》陕A1****D
     *     eg5：京A123 -》 京A123
     *     如果是错误的车牌，不处理
     * </pre>
     *
     * @param carLicense 完整的车牌号
     * @return 脱敏后的车牌
     */
    public static String carLicense(String carLicense) {
        if (StrUtil.isBlank(carLicense)) {
            return StrUtil.EMPTY;
        }
        // 普通车牌
        if (carLicense.length() == 7) {
            carLicense = StrUtil.hide(carLicense, 3, 6);
        } else if (carLicense.length() == 8) {
            // 新能源车牌
            carLicense = StrUtil.hide(carLicense, 3, 7);
        }
        return carLicense;
    }

    /**
     * <p>
     * 【银行卡号脱敏】由于银行卡号长度不定，所以只展示前4位，后面的位数根据卡号决定展示1-4位 例如：
     * </p>
     *
     * <pre>
     *     {@code
     *      1. "1234 2222 3333 4444 6789 9"    ->   "1234 **** **** **** **** 9"
     *      2. "1234 2222 3333 4444 6789 91"   ->   "1234 **** **** **** **** 91"
     *      3. "1234 2222 3333 4444 678"       ->    "1234 **** **** **** 678"
     *      4. "1234 2222 3333 4444 6789"      ->    "1234 **** **** **** 6789"
     *     }
     *  </pre>
     *
     * @param bankCardNo 银行卡号
     * @return 脱敏之后的银行卡号
     */
    public static String bankCard(String bankCardNo) {
        if (StrUtil.isBlank(bankCardNo)) {
            return bankCardNo;
        }
        bankCardNo = filter(bankCardNo, c -> false == CharSequenceUtil.isBlankChar(c));
        if (bankCardNo.length() < 9) {
            return bankCardNo;
        }

        final int length = bankCardNo.length();
        final int endLength = length % 4 == 0 ? 4 : length % 4;
        final int midLength = length - 4 - endLength;

        final StringBuilder buf = new StringBuilder();
        buf.append(bankCardNo, 0, 4);
        for (int i = 0; i < midLength; ++i) {
            if (i % 4 == 0) {
                buf.append(CharPools.SPACE);
            }
            buf.append('*');
        }
        buf.append(CharPools.SPACE).append(bankCardNo, length - endLength, length);
        return buf.toString();
    }

    /**
     * IPv4脱敏，如：脱敏前：192.0.2.1；脱敏后：192.*.*.*。
     *
     * @param ipv4 IPv4地址
     * @return 脱敏后的地址
     */
    public static String ipv4(String ipv4) {
        return subBefore(ipv4, '.', false) + ".*.*.*";
    }

    /**
     * IPv4脱敏，如：脱敏前：2001:0db8:86a3:08d3:1319:8a2e:0370:7344；脱敏后：2001:*:*:*:*:*:*:*
     *
     * @param ipv6 IPv4地址
     * @return 脱敏后的地址
     */
    public static String ipv6(String ipv6) {
        return subBefore(ipv6, ':', false) + ":*:*:*:*:*:*:*";
    }

    /**
     * 截取分隔字符串之前的字符串，不包括分隔字符串
     *
     * @param string          被查找的字符串
     * @param separator       分隔字符串（不包括）
     * @param isLastSeparator 是否查找最后一个分隔字符串（多次出现分隔字符串时选取最后一个），true为选取最后一个
     * @return 切割后的字符串
     */
    private static String subBefore(CharSequence string, char separator, boolean isLastSeparator) {
        if (StrUtil.isBlank(string)) {
            return StrUtil.EMPTY;
        }
        final String str = string.toString();
        final int pos = isLastSeparator ? str.lastIndexOf(separator) : str.indexOf(separator);
        if (-1 == pos) {
            return str;
        }
        if (0 == pos) {
            return StrUtil.EMPTY;
        }
        return str.substring(0, pos);
    }

    /**
     * 过滤字符串
     *
     * @param str    字符串
     * @param filter 过滤器
     * @return 过滤后的字符串
     */
    public static String filter(CharSequence str, final Predicate<Character> filter) {
        if (str == null || filter == null) {
            return StrUtil.EMPTY;
        }

        int len = str.length();
        final StringBuilder sb = new StringBuilder(len);
        char c;
        for (int i = 0; i < len; i++) {
            c = str.charAt(i);
            if (filter.test(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

}

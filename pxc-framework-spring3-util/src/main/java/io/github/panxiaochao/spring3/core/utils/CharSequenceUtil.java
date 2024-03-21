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
package io.github.panxiaochao.spring3.core.utils;

/**
 * <p>
 * 字符串工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-03-10
 */
public class CharSequenceUtil {

	/**
	 * Converts the given CharSequence to a char[].
	 * @param source the {@code CharSequence} to be processed.
	 * @return the resulting char array, never null.
	 */
	public static char[] toCharArray(final CharSequence source) {
		final int len = StrUtil.length(source);
		if (len == 0) {
			return ArrayUtil.EMPTY_CHAR_ARRAY;
		}
		if (source instanceof String) {
			return ((String) source).toCharArray();
		}
		final char[] array = new char[len];
		for (int i = 0; i < len; i++) {
			array[i] = source.charAt(i);
		}
		return array;
	}

    /**
     * <p>
     * 是否为ASCII字符，ASCII字符位于0-127之间.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAscii('a')  = true
     *   CharSequenceUtil.isAscii('A')  = true
     *   CharSequenceUtil.isAscii('3')  = true
     *   CharSequenceUtil.isAscii('-')  = true
     *   CharSequenceUtil.isAscii('\n') = true
     *   CharSequenceUtil.isAscii('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if less than 128
     */
    public static boolean isAscii(final char ch) {
        return ch < 128;
    }

    /**
     * <p>
     * 是否为可见ASCII字符，可见字符位于32-126之间.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAsciiPrintable('a')  = true
     *   CharSequenceUtil.isAsciiPrintable('A')  = true
     *   CharSequenceUtil.isAsciiPrintable('3')  = true
     *   CharSequenceUtil.isAsciiPrintable('-')  = true
     *   CharSequenceUtil.isAsciiPrintable('\n') = false
     *   CharSequenceUtil.isAsciiPrintable('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 32 and 126 inclusive
     */
    public static boolean isAsciiPrintable(final char ch) {
        return ch >= 32 && ch < 127;
    }

    /**
     * <p>
     * 是否为ASCII控制符（不可见字符），控制符位于0-31和127.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAsciiControl('a')  = false
     *   CharSequenceUtil.isAsciiControl('A')  = false
     *   CharSequenceUtil.isAsciiControl('3')  = false
     *   CharSequenceUtil.isAsciiControl('-')  = false
     *   CharSequenceUtil.isAsciiControl('\n') = true
     *   CharSequenceUtil.isAsciiControl('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if less than 32 or equals 127
     */
    public static boolean isAsciiControl(final char ch) {
        return ch < 32 || ch == 127;
    }

    /**
     * <p>
     * 判断是否为字母（包括大写字母和小写字母） 字母包括A-Z和a-z.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAsciiAlpha('a')  = true
     *   CharSequenceUtil.isAsciiAlpha('A')  = true
     *   CharSequenceUtil.isAsciiAlpha('3')  = false
     *   CharSequenceUtil.isAsciiAlpha('-')  = false
     *   CharSequenceUtil.isAsciiAlpha('\n') = false
     *   CharSequenceUtil.isAsciiAlpha('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 65 and 90 or 97 and 122 inclusive
     */
    public static boolean isAsciiAlpha(final char ch) {
        return isAsciiAlphaUpper(ch) || isAsciiAlphaLower(ch);
    }

    /**
     * <p>
     * 判断是否为大写字母，大写字母包括A-Z.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAsciiAlphaUpper('a')  = false
     *   CharSequenceUtil.isAsciiAlphaUpper('A')  = true
     *   CharSequenceUtil.isAsciiAlphaUpper('3')  = false
     *   CharSequenceUtil.isAsciiAlphaUpper('-')  = false
     *   CharSequenceUtil.isAsciiAlphaUpper('\n') = false
     *   CharSequenceUtil.isAsciiAlphaUpper('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 65 and 90 inclusive
     */
    public static boolean isAsciiAlphaUpper(final char ch) {
        return ch >= 'A' && ch <= 'Z';
    }

    /**
     * <p>
     * 检查字符是否为小写字母，小写字母指a-z.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAsciiAlphaLower('a')  = true
     *   CharSequenceUtil.isAsciiAlphaLower('A')  = false
     *   CharSequenceUtil.isAsciiAlphaLower('3')  = false
     *   CharSequenceUtil.isAsciiAlphaLower('-')  = false
     *   CharSequenceUtil.isAsciiAlphaLower('\n') = false
     *   CharSequenceUtil.isAsciiAlphaLower('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 97 and 122 inclusive
     */
    public static boolean isAsciiAlphaLower(final char ch) {
        return ch >= 'a' && ch <= 'z';
    }

    /**
     * <p>
     * 检查是否为数字字符，数字字符指0-9.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAsciiNumeric('a')  = false
     *   CharSequenceUtil.isAsciiNumeric('A')  = false
     *   CharSequenceUtil.isAsciiNumeric('3')  = true
     *   CharSequenceUtil.isAsciiNumeric('-')  = false
     *   CharSequenceUtil.isAsciiNumeric('\n') = false
     *   CharSequenceUtil.isAsciiNumeric('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 48 and 57 inclusive
     */
    public static boolean isAsciiNumeric(final char ch) {
        return ch >= '0' && ch <= '9';
    }

    /**
     * <p>
     * 是否为16进制规范的字符，判断是否为如下字符
     * </p>
     * <pre>
     * 1. 0~9
     * 2. a~f
     * 4. A~F
     * </pre>
     *
     * @param c 字符
     * @return 是否为16进制规范的字符
     */
    public static boolean isHexChar(char c) {
        return isAsciiNumeric(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F');
    }

    /**
     * <p>
     * 是否为字母或数字，包括A-Z、a-z、0-9.
     * </p>
     *
     * <pre>
     *   CharSequenceUtil.isAsciiAlphanumeric('a')  = true
     *   CharSequenceUtil.isAsciiAlphanumeric('A')  = true
     *   CharSequenceUtil.isAsciiAlphanumeric('3')  = true
     *   CharSequenceUtil.isAsciiAlphanumeric('-')  = false
     *   CharSequenceUtil.isAsciiAlphanumeric('\n') = false
     *   CharSequenceUtil.isAsciiAlphanumeric('&copy;') = false
     * </pre>
     *
     * @param ch the character to check
     * @return true if between 48 and 57 or 65 and 90 or 97 and 122 inclusive
     */
    public static boolean isAsciiAlphanumeric(final char ch) {
        return isAsciiAlpha(ch) || isAsciiNumeric(ch);
    }

    /**
     * <p>
     * 给定类名是否为字符类，字符类包括：
     * </p>
     *
     * <pre>
     * Character.class
     * char.class
     * </pre>
     *
     * @param clazz 被检查的类
     * @return true表示为字符类
     */
    public static boolean isCharClass(Class<?> clazz) {
        return clazz == Character.class || clazz == char.class;
    }

    /**
     * <p>
     * 给定对象对应的类是否为字符类，字符类包括：
     * </p>
     *
     * <pre>
     * Character.class
     * char.class
     * </pre>
     *
     * @param value 被检查的对象
     * @return true表示为字符类
     */
    public static boolean isChar(Object value) {
        return value instanceof Character || value.getClass() == char.class;
    }

    /**
     * 是否空白符、空白符包括空格、制表符、全角空格和不间断空格
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     */
    public static boolean isBlankChar(char c) {
        return isBlankChar((int) c);
    }

    /**
     * 是否空白符、空白符包括空格、制表符、全角空格和不间断空格
     *
     * @param c 字符
     * @return 是否空白符
     * @see Character#isWhitespace(int)
     * @see Character#isSpaceChar(int)
     */
    public static boolean isBlankChar(int c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c) || c == '\ufeff' || c == '\u202a' || c == '\u0000'
                || c == '\u3164' || c == '\u2800' || c == '\u180e';
    }

}

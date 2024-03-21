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

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * <p>
 * String tools
 * </p>
 *
 * @author Lypxc
 * @since 2023-01-28
 */
public class StrUtil {

	/**
	 * A String for a space character.
	 */
	public static final String SPACE = " ";

	/**
	 * The empty String {@code ""}.
	 */
	public static final String EMPTY = "";

	/**
	 * A String for linefeed LF ("\n").
	 */
	public static final String LF = "\n";

	/**
	 * A String for carriage return CR ("\r").
	 */
	public static final String CR = "\r";

	/**
	 * Represents a failed index search.
	 */
	public static final int INDEX_NOT_FOUND = -1;

	/**
	 * <p>
	 * Checks if a CharSequence is empty (""), null or whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isBlank(null)      = true
	 * StrUtil.isBlank("")        = true
	 * StrUtil.isBlank(" ")       = true
	 * StrUtil.isBlank("bob")     = false
	 * StrUtil.isBlank("  bob  ") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is null, empty or whitespace only
	 */
	public static boolean isBlank(final CharSequence cs) {
		final int strLen = length(cs);
		if (strLen == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if a CharSequence is empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isEmpty(null)      = true
	 * StrUtil.isEmpty("")        = true
	 * StrUtil.isEmpty(" ")       = false
	 * StrUtil.isEmpty("bob")     = false
	 * StrUtil.isEmpty("  bob  ") = false
	 * </pre>
	 *
	 * <p>
	 * NOTE: This method changed in Lang version 2.0. It no longer trims the CharSequence.
	 * That functionality is available in isBlank().
	 * </p>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is empty or null
	 */
	public static boolean isEmpty(final CharSequence cs) {
		return null == cs || cs.length() == 0;
	}

	/**
	 * Gets a CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 * @param cs a CharSequence or {@code null}
	 * @return CharSequence length or {@code 0} if the CharSequence is {@code null}.
	 */
	public static int length(final CharSequence cs) {
		return null == cs ? 0 : cs.length();
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty (""), not null and not whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isNotBlank(null)      = false
	 * StrUtil.isNotBlank("")        = false
	 * StrUtil.isNotBlank(" ")       = false
	 * StrUtil.isNotBlank("bob")     = true
	 * StrUtil.isNotBlank("  bob  ") = true
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null and not
	 * whitespace only
	 */
	public static boolean isNotBlank(final CharSequence cs) {
		return !isBlank(cs);
	}

	/**
	 * <p>
	 * Checks if a CharSequence is not empty ("") and not null.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isNotEmpty(null)      = false
	 * StrUtil.isNotEmpty("")        = false
	 * StrUtil.isNotEmpty(" ")       = true
	 * StrUtil.isNotEmpty("bob")     = true
	 * StrUtil.isNotEmpty("  bob  ") = true
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if the CharSequence is not empty and not null
	 */
	public static boolean isNotEmpty(final CharSequence cs) {
		return !isEmpty(cs);
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode digits. A decimal point is not a
	 * Unicode digit and returns false.
	 * </p>
	 *
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will
	 * return {@code false}.
	 * </p>
	 *
	 * <p>
	 * Note that the method does not allow for a leading sign, either positive or
	 * negative. Also, if a String passes the numeric test, it may still generate a
	 * NumberFormatException when parsed by Integer.parseInt or Long.parseLong, e.g. if
	 * the value is outside the range for int or long respectively.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isNumeric(null)   = false
	 * StrUtil.isNumeric("")     = false
	 * StrUtil.isNumeric("  ")   = false
	 * StrUtil.isNumeric("123")  = true
	 * StrUtil.isNumeric("\u0967\u0968\u0969")  = true
	 * StrUtil.isNumeric("12 3") = false
	 * StrUtil.isNumeric("ab2c") = false
	 * StrUtil.isNumeric("12-3") = false
	 * StrUtil.isNumeric("12.3") = false
	 * StrUtil.isNumeric("-123") = false
	 * StrUtil.isNumeric("+123") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains digits, and is non-null
	 */
	public static boolean isNumeric(final CharSequence cs) {
		if (isEmpty(cs)) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isDigit(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only Unicode digits or space ({@code ' '}). A
	 * decimal point is not a Unicode digit and returns false.
	 * </p>
	 *
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will
	 * return {@code true}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isNumericSpace(null)   = false
	 * StrUtil.isNumericSpace("")     = true
	 * StrUtil.isNumericSpace("  ")   = true
	 * StrUtil.isNumericSpace("123")  = true
	 * StrUtil.isNumericSpace("12 3") = true
	 * StrUtil.isNumericSpace("\u0967\u0968\u0969")  = true
	 * StrUtil.isNumericSpace("\u0967\u0968 \u0969")  = true
	 * StrUtil.isNumericSpace("ab2c") = false
	 * StrUtil.isNumericSpace("12-3") = false
	 * StrUtil.isNumericSpace("12.3") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains digits or space, and is non-null
	 */
	public static boolean isNumericSpace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			final char nowChar = cs.charAt(i);
			if (nowChar != ' ' && !Character.isDigit(nowChar)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains only whitespace.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <p>
	 * {@code null} will return {@code false}. An empty CharSequence (length()=0) will
	 * return {@code true}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isWhitespace(null)   = false
	 * StrUtil.isWhitespace("")     = true
	 * StrUtil.isWhitespace("  ")   = true
	 * StrUtil.isWhitespace("abc")  = false
	 * StrUtil.isWhitespace("ab2c") = false
	 * StrUtil.isWhitespace("ab-c") = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @return {@code true} if only contains whitespace, and is non-null
	 */
	public static boolean isWhitespace(final CharSequence cs) {
		if (cs == null) {
			return false;
		}
		final int sz = cs.length();
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(cs.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * <p>
	 * Checks if any of the CharSequences are empty ("") or null or whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isAnyBlank((String) null)    = true
	 * StrUtil.isAnyBlank((String[]) null)  = false
	 * StrUtil.isAnyBlank(null, "foo")      = true
	 * StrUtil.isAnyBlank(null, null)       = true
	 * StrUtil.isAnyBlank("", "bar")        = true
	 * StrUtil.isAnyBlank("bob", "")        = true
	 * StrUtil.isAnyBlank("  bob  ", null)  = true
	 * StrUtil.isAnyBlank(" ", "bar")       = true
	 * StrUtil.isAnyBlank(new String[] {})  = false
	 * StrUtil.isAnyBlank(new String[]{""}) = true
	 * StrUtil.isAnyBlank("foo", "bar")     = false
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if any of the CharSequences are empty or null or whitespace
	 * only
	 */
	public static boolean isAnyBlank(final CharSequence... css) {
		if (getArrayLength(css) == 0) {
			return false;
		}
		for (final CharSequence cs : css) {
			if (isBlank(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if any of the CharSequences are empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isAnyEmpty((String) null)    = true
	 * StrUtil.isAnyEmpty((String[]) null)  = false
	 * StrUtil.isAnyEmpty(null, "foo")      = true
	 * StrUtil.isAnyEmpty("", "bar")        = true
	 * StrUtil.isAnyEmpty("bob", "")        = true
	 * StrUtil.isAnyEmpty("  bob  ", null)  = true
	 * StrUtil.isAnyEmpty(" ", "bar")       = false
	 * StrUtil.isAnyEmpty("foo", "bar")     = false
	 * StrUtil.isAnyEmpty(new String[]{})   = false
	 * StrUtil.isAnyEmpty(new String[]{""}) = true
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if any of the CharSequences are empty or null
	 */
	public static boolean isAnyEmpty(final CharSequence... css) {
		if (getArrayLength(css) == 0) {
			return false;
		}
		for (final CharSequence cs : css) {
			if (isEmpty(cs)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if none of the CharSequences are empty (""), null or whitespace only.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isNoneBlank((String) null)    = false
	 * StrUtil.isNoneBlank((String[]) null)  = true
	 * StrUtil.isNoneBlank(null, "foo")      = false
	 * StrUtil.isNoneBlank(null, null)       = false
	 * StrUtil.isNoneBlank("", "bar")        = false
	 * StrUtil.isNoneBlank("bob", "")        = false
	 * StrUtil.isNoneBlank("  bob  ", null)  = false
	 * StrUtil.isNoneBlank(" ", "bar")       = false
	 * StrUtil.isNoneBlank(new String[] {})  = true
	 * StrUtil.isNoneBlank(new String[]{""}) = false
	 * StrUtil.isNoneBlank("foo", "bar")     = true
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if none of the CharSequences are empty or null or whitespace
	 * only
	 */
	public static boolean isNoneBlank(final CharSequence... css) {
		return !isAnyBlank(css);
	}

	/**
	 * <p>
	 * Checks if none of the CharSequences are empty ("") or null.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.isNoneEmpty((String) null)    = false
	 * StrUtil.isNoneEmpty((String[]) null)  = true
	 * StrUtil.isNoneEmpty(null, "foo")      = false
	 * StrUtil.isNoneEmpty("", "bar")        = false
	 * StrUtil.isNoneEmpty("bob", "")        = false
	 * StrUtil.isNoneEmpty("  bob  ", null)  = false
	 * StrUtil.isNoneEmpty(new String[] {})  = true
	 * StrUtil.isNoneEmpty(new String[]{""}) = false
	 * StrUtil.isNoneEmpty(" ", "bar")       = true
	 * StrUtil.isNoneEmpty("foo", "bar")     = true
	 * </pre>
	 * @param css the CharSequences to check, may be null or empty
	 * @return {@code true} if none of the CharSequences are empty or null
	 */
	public static boolean isNoneEmpty(final CharSequence... css) {
		return !isAnyEmpty(css);
	}

	/**
	 * @param array the array to retrieve the length from, may be null
	 * @return The length of the array, or {@code 0} if the array is {@code null}
	 */
	private static int getArrayLength(final Object array) {
		if (null == array) {
			return 0;
		}
		return Array.getLength(array);
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is whitespace,
	 * empty ("") or {@code null}, the value supplied by {@code defaultStrSupplier}.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <p>
	 * Caller responsible for thread-safety and exception handling of default value
	 * supplier
	 * </p>
	 *
	 * <pre>
	 * {@code
	 * StrUtil.getIfBlank(null, () -> "NULL")   = "NULL"
	 * StrUtil.getIfBlank("", () -> "NULL")     = "NULL"
	 * StrUtil.getIfBlank(" ", () -> "NULL")    = "NULL"
	 * StrUtil.getIfBlank("bat", () -> "NULL")  = "bat"
	 * StrUtil.getIfBlank("", () -> null)       = null
	 * StrUtil.getIfBlank("", null)             = null
	 * }</pre>
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultSupplier the supplier of default CharSequence to return if the input
	 * is whitespace, empty ("") or {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 */
	public static <T extends CharSequence> T getIfBlank(final T str, final Supplier<T> defaultSupplier) {
		return isBlank(str) ? defaultSupplier == null ? null : defaultSupplier.get() : str;
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is empty or
	 * {@code null}, the value supplied by {@code defaultStrSupplier}.
	 * </p>
	 *
	 * <p>
	 * Caller responsible for thread-safety and exception handling of default value
	 * supplier
	 * </p>
	 *
	 * <pre>
	 * {@code
	 * StrUtil.getIfEmpty(null, () -> "NULL")    = "NULL"
	 * StrUtil.getIfEmpty("", () -> "NULL")      = "NULL"
	 * StrUtil.getIfEmpty(" ", () -> "NULL")     = " "
	 * StrUtil.getIfEmpty("bat", () -> "NULL")   = "bat"
	 * StrUtil.getIfEmpty("", () -> null)        = null
	 * StrUtil.getIfEmpty("", null)              = null
	 * }
	 * </pre>
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultSupplier the supplier of default CharSequence to return if the input
	 * is empty ("") or {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 */
	public static <T extends CharSequence> T getIfEmpty(final T str, final Supplier<T> defaultSupplier) {
		return isEmpty(str) ? defaultSupplier == null ? null : defaultSupplier.get() : str;
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is whitespace,
	 * empty ("") or {@code null}, the value of {@code defaultStr}.
	 * </p>
	 *
	 * <p>
	 * Whitespace is defined by {@link Character#isWhitespace(char)}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.defaultIfBlank(null, "NULL")  = "NULL"
	 * StrUtil.defaultIfBlank("", "NULL")    = "NULL"
	 * StrUtil.defaultIfBlank(" ", "NULL")   = "NULL"
	 * StrUtil.defaultIfBlank("bat", "NULL") = "bat"
	 * StrUtil.defaultIfBlank("", null)      = null
	 * </pre>
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultStr the default CharSequence to return if the input is whitespace,
	 * empty ("") or {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 */
	public static <T extends CharSequence> T defaultIfBlank(final T str, final T defaultStr) {
		return isBlank(str) ? defaultStr : str;
	}

	/**
	 * <p>
	 * Returns either the passed in CharSequence, or if the CharSequence is empty or
	 * {@code null}, the value of {@code defaultStr}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.defaultIfEmpty(null, "NULL")  = "NULL"
	 * StrUtil.defaultIfEmpty("", "NULL")    = "NULL"
	 * StrUtil.defaultIfEmpty(" ", "NULL")   = " "
	 * StrUtil.defaultIfEmpty("bat", "NULL") = "bat"
	 * StrUtil.defaultIfEmpty("", null)      = null
	 * </pre>
	 * @param <T> the specific kind of CharSequence
	 * @param str the CharSequence to check, may be null
	 * @param defaultStr the default CharSequence to return if the input is empty ("") or
	 * {@code null}, may be null
	 * @return the passed in CharSequence, or the default
	 */
	public static <T extends CharSequence> T defaultIfEmpty(final T str, final T defaultStr) {
		return isEmpty(str) ? defaultStr : str;
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, an empty
	 * String ("").
	 * </p>
	 *
	 * <pre>
     * StrUtil.defaultIfNull(null)  = ""
     * StrUtil.defaultIfNull("")    = ""
     * StrUtil.defaultIfNull("bat") = "bat"
	 * </pre>
	 * @param str the String to check, may be null
	 * @return the passed in String, or the empty String if it was {@code null}
	 */
    public static String defaultIfNull(final String str) {
        return defaultIfNull(str, EMPTY);
	}

	/**
	 * <p>
	 * Returns either the passed in String, or if the String is {@code null}, the value of
	 * {@code defaultStr}.
	 * </p>
	 *
	 * <pre>
     * StrUtil.defaultIfNull(null, "NULL")  = "NULL"
     * StrUtil.defaultIfNull("", "NULL")    = ""
     * StrUtil.defaultIfNull("bat", "NULL") = "bat"
	 * </pre>
	 * @param str the String to check, may be null
	 * @param defaultStr the default String to return if the input is {@code null}, may be
	 * null
	 * @return the passed in String, or the default if it was {@code null}
	 */
    public static String defaultIfNull(final String str, final String defaultStr) {
        return null == str ? defaultStr : str;
	}

	/**
	 * Checks if CharSequence contains a search CharSequence irrespective of case,
	 * handling {@code null}. Case-insensitivity is defined as by
	 * {@link String#equalsIgnoreCase(String)}.
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}.
	 * </p>
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @return true if the CharSequence contains the search CharSequence irrespective of
	 * case or false if not or {@code null} string input
	 */
	public static boolean containsIgnoreCase(final CharSequence str, final CharSequence searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		String str1 = str.toString().toLowerCase();
		String str2 = searchStr.toString().toLowerCase();
		return str1.contains(str2);
	}

	/**
	 * Checks if CharSequence contains a search CharSequence.
	 * @param str the CharSequence to check, may be null
	 * @param searchStr the CharSequence to find, may be null
	 * @return true if the CharSequence contains the search CharSequence
	 */
	public static boolean contains(final CharSequence str, final CharSequence searchStr) {
		if (str == null || searchStr == null) {
			return false;
		}
		return str.toString().contains(searchStr);
	}

    /**
     * Checks if CharSequence contains a search CharSequence.
     *
     * @param str        the CharSequence to check, may be null
     * @param searchChar the Char to find, may be null
     * @return true if the CharSequence contains the search CharSequence
     */
    public static boolean contains(CharSequence str, char searchChar) {
        return containsAny(str, searchChar);
    }

	/**
	 * <p>
	 * Checks if the CharSequence contains any character in the given set of characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} or zero
	 * length search array will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.containsAny(null, *)                  = false
	 * StrUtil.containsAny("", *)                    = false
	 * StrUtil.containsAny(*, null)                  = false
	 * StrUtil.containsAny(*, [])                    = false
	 * StrUtil.containsAny("zzabyycdxx", ['z', 'a']) = true
	 * StrUtil.containsAny("zzabyycdxx", ['b', 'y']) = true
	 * StrUtil.containsAny("zzabyycdxx", ['z', 'y']) = true
	 * StrUtil.containsAny("aba", ['z'])             = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the {@code true} if any of the chars are found,
	 */
	public static boolean containsAny(final CharSequence cs, final char... searchChars) {
		if (isEmpty(cs) || ArrayUtil.isEmpty(searchChars)) {
			return false;
		}
		final int csLength = cs.length();
		final int searchLength = searchChars.length;
		final int csLast = csLength - 1;
		final int searchLast = searchLength - 1;
		for (int i = 0; i < csLength; i++) {
			final char ch = cs.charAt(i);
			for (int j = 0; j < searchLength; j++) {
				if (searchChars[j] == ch) {
					if (Character.isHighSurrogate(ch)) {
						if (j == searchLast) {
							// missing low surrogate, fine, like String.indexOf(String)
							return true;
						}
						if (i < csLast && searchChars[j + 1] == cs.charAt(i + 1)) {
							return true;
						}
					}
					else {
						// ch is in the Basic Multilingual Plane
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains any character in the given set of characters.
	 * </p>
	 *
	 * <p>
	 * A {@code null} CharSequence will return {@code false}. A {@code null} search
	 * CharSequence will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.containsAny(null, *)               = false
	 * StrUtil.containsAny("", *)                 = false
	 * StrUtil.containsAny(*, null)               = false
	 * StrUtil.containsAny(*, "")                 = false
	 * StrUtil.containsAny("zzabyycdxx", "za")    = true
	 * StrUtil.containsAny("zzabyycdxx", "by")    = true
	 * StrUtil.containsAny("zzabyycdxx", "zy")    = true
	 * StrUtil.containsAny("zzabyycdxx", "\tx")   = true
	 * StrUtil.containsAny("zzabyycdxx", "$.#yF") = true
	 * StrUtil.containsAny("aba", "z")            = false
	 * </pre>
	 * @param cs the CharSequence to check, may be null
	 * @param searchChars the chars to search for, may be null
	 * @return the {@code true} if any of the chars are found, {@code false} if no match
	 * or null input
	 */
	public static boolean containsAny(final CharSequence cs, final CharSequence searchChars) {
		if (searchChars == null) {
			return false;
		}
		return containsAny(cs, CharSequenceUtil.toCharArray(searchChars));
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains any of the CharSequences in the given array.
	 * </p>
	 *
	 * <p>
	 * A {@code null} {@code cs} CharSequence will return {@code false}. A {@code null} or
	 * zero length search array will return {@code false}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.containsAny(null, *)            = false
	 * StrUtil.containsAny("", *)              = false
	 * StrUtil.containsAny(*, null)            = false
	 * StrUtil.containsAny(*, [])              = false
	 * StrUtil.containsAny("abcd", "ab", null) = true
	 * StrUtil.containsAny("abcd", "ab", "cd") = true
	 * StrUtil.containsAny("abc", "d", "abc")  = true
	 * </pre>
	 * @param cs The CharSequence to check, may be null
	 * @param searchCharSequences The array of CharSequences to search for, may be null.
	 * Individual CharSequences may be null as well.
	 * @return {@code true} if any of the search CharSequences are found, {@code false}
	 * otherwise
	 */
	public static boolean containsAny(final CharSequence cs, final CharSequence... searchCharSequences) {
		return containsAny(StrUtil::contains, cs, searchCharSequences);
	}

	/**
	 * <p>
	 * Checks if the CharSequence contains any of the CharSequences in the given array.
	 * </p>
	 *
	 * <p>
	 * A {@code null} {@code cs} CharSequence will return {@code false}. A {@code null} or
	 * zero length search array will return {@code false}.
	 * </p>
	 * @param cs The CharSequence to check, may be null
	 * @param searchCharSequences The array of CharSequences to search for, may be null.
	 * Individual CharSequences may be null as well.
	 * @return {@code true} if any of the search CharSequences are found, {@code false}
	 * otherwise
	 */
	private static boolean containsAny(final ToBooleanBiFunction<CharSequence, CharSequence> test,
			final CharSequence cs, final CharSequence... searchCharSequences) {
		if (isEmpty(cs) || ArrayUtil.isEmpty(searchCharSequences)) {
			return false;
		}
		for (final CharSequence searchCharSequence : searchCharSequences) {
			if (test.applyAsBoolean(cs, searchCharSequence)) {
				return true;
			}
		}
		return false;
	}

	@FunctionalInterface
	interface ToBooleanBiFunction<T, U> {

		/**
		 * Applies this function to the given arguments.
		 * @param t the first function argument.
		 * @param u the second function argument.
		 * @return the function result.
		 */
		boolean applyAsBoolean(T t, U u);

	}

	/**
	 * <p>
	 * Deletes all whitespaces from a String as defined by
	 * {@link Character#isWhitespace(char)}.
	 * </p>
	 * @param str the String to delete whitespace from, may be null
	 * @return the String without whitespaces, <code>null</code> if null String input
	 */
	public static String deleteWhitespace(String str) {
		if (isEmpty(str)) {
			return str;
		}
		int sz = str.length();
		char[] chs = new char[sz];
		int count = 0;
		for (int i = 0; i < sz; i++) {
			if (!Character.isWhitespace(str.charAt(i))) {
				chs[count++] = str.charAt(i);
			}
		}
		if (count == sz) {
			return str;
		}
		return new String(chs, 0, count);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 *
	 * <p>
	 * A negative start position can be used to start {@code n} characters from the end of
	 * the String.
	 * </p>
	 *
	 * <p>
	 * A {@code null} String will return {@code null}. An empty ("") String will return
	 * "".
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substring(null, *)   = null
	 * StrUtil.substring("", *)     = ""
	 * StrUtil.substring("abc", 0)  = "abc"
	 * StrUtil.substring("abc", 2)  = "c"
	 * StrUtil.substring("abc", 4)  = ""
	 * StrUtil.substring("abc", -2) = "bc"
	 * StrUtil.substring("abc", -4) = "abc"
	 * </pre>
	 * @param str the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the end of
	 * the String by this many characters
	 * @return substring from start position, {@code null} if null String input
	 */
	public static String substring(final String str, int start) {
		if (str == null) {
			return null;
		}
		if (start < 0) {
			start = str.length() + start;
		}
		if (start < 0) {
			start = 0;
		}
		if (start > str.length()) {
			return EMPTY;
		}
		return str.substring(start);
	}

	/**
	 * <p>
	 * Gets a substring from the specified String avoiding exceptions.
	 * </p>
	 *
	 * <p>
	 * A negative start position can be used to start/end {@code n} characters from the
	 * end of the String.
	 * </p>
	 *
	 * <p>
	 * The returned substring starts with the character in the {@code start} position and
	 * ends before the {@code end} position. All position counting is zero-based -- i.e.,
	 * to start at the beginning of the string use {@code start = 0}. Negative start and
	 * end positions can be used to specify offsets relative to the end of the String.
	 * </p>
	 *
	 * <p>
	 * If {@code start} is not strictly to the left of {@code end}, "" is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substring(null, *, *)    = null
	 * StrUtil.substring("", * ,  *)    = "";
	 * StrUtil.substring("abc", 0, 2)   = "ab"
	 * StrUtil.substring("abc", 2, 0)   = ""
	 * StrUtil.substring("abc", 2, 4)   = "c"
	 * StrUtil.substring("abc", 4, 6)   = ""
	 * StrUtil.substring("abc", 2, 2)   = ""
	 * StrUtil.substring("abc", -2, -1) = "b"
	 * StrUtil.substring("abc", -4, 2)  = "ab"
	 * </pre>
	 * @param str the String to get the substring from, may be null
	 * @param start the position to start from, negative means count back from the end of
	 * the String by this many characters
	 * @param end the position to end at (exclusive), negative means count back from the
	 * end of the String by this many characters
	 * @return substring from start position to end position, {@code null} if null String
	 * input
	 */
	public static String substring(final String str, int start, int end) {
		if (str == null) {
			return null;
		}
		if (end < 0) {
			end = str.length() + end;
		}
		if (start < 0) {
			start = str.length() + start;
		}
		if (end > str.length()) {
			end = str.length();
		}
		if (start > end) {
			return EMPTY;
		}
		if (start < 0) {
			start = 0;
		}
		if (end < 0) {
			end = 0;
		}
		return str.substring(start, end);
	}

	/**
	 * <p>
	 * Gets the substring after the first occurrence of a separator. The separator is not
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input
	 * will return the empty string.
	 *
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringAfter(null, *)      = null
	 * StrUtil.substringAfter("", *)        = ""
	 * StrUtil.substringAfter("abc", 'a')   = "bc"
	 * StrUtil.substringAfter("abcba", 'b') = "cba"
	 * StrUtil.substringAfter("abc", 'c')   = ""
	 * StrUtil.substringAfter("abc", 'd')   = ""
	 * StrUtil.substringAfter(" abc", 32)   = "abc"
	 * </pre>
	 * @param str the String to get a substring from, may be null
	 * @param separator the character to search.
	 * @return the substring after the first occurrence of the separator, {@code null} if
	 * null String input
	 * 
	 */
	public static String substringAfter(final String str, final int separator) {
		if (isEmpty(str)) {
			return str;
		}
		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * <p>
	 * Gets the substring after the first occurrence of a separator. The separator is not
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input
	 * will return the empty string. A {@code null} separator will return the empty string
	 * if the input string is not {@code null}.
	 * </p>
	 *
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringAfter(null, *)      = null
	 * StrUtil.substringAfter("", *)        = ""
	 * StrUtil.substringAfter(*, null)      = ""
	 * StrUtil.substringAfter("abc", "a")   = "bc"
	 * StrUtil.substringAfter("abcba", "b") = "cba"
	 * StrUtil.substringAfter("abc", "c")   = ""
	 * StrUtil.substringAfter("abc", "d")   = ""
	 * StrUtil.substringAfter("abc", "")    = "abc"
	 * </pre>
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the first occurrence of the separator, {@code null} if
	 * null String input
	 * 
	 */
	public static String substringAfter(final String str, final String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (separator == null) {
			return EMPTY;
		}
		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * <p>
	 * Gets the substring after the last occurrence of a separator. The separator is not
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input
	 * will return the empty string.
	 *
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringAfterLast(null, *)      = null
	 * StrUtil.substringAfterLast("", *)        = ""
	 * StrUtil.substringAfterLast("abc", 'a')   = "bc"
	 * StrUtil.substringAfterLast(" bc", 32)    = "bc"
	 * StrUtil.substringAfterLast("abcba", 'b') = "a"
	 * StrUtil.substringAfterLast("abc", 'c')   = ""
	 * StrUtil.substringAfterLast("a", 'a')     = ""
	 * StrUtil.substringAfterLast("a", 'z')     = ""
	 * </pre>
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the last occurrence of the separator, {@code null} if
	 * null String input
	 * 
	 */
	public static String substringAfterLast(final String str, final int separator) {
		if (isEmpty(str)) {
			return str;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND || pos == str.length() - 1) {
			return EMPTY;
		}
		return str.substring(pos + 1);
	}

	/**
	 * <p>
	 * Gets the substring after the last occurrence of a separator. The separator is not
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input
	 * will return the empty string. An empty or {@code null} separator will return the
	 * empty string if the input string is not {@code null}.
	 * </p>
	 *
	 * <p>
	 * If nothing is found, the empty string is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringAfterLast(null, *)      = null
	 * StrUtil.substringAfterLast("", *)        = ""
	 * StrUtil.substringAfterLast(*, "")        = ""
	 * StrUtil.substringAfterLast(*, null)      = ""
	 * StrUtil.substringAfterLast("abc", "a")   = "bc"
	 * StrUtil.substringAfterLast("abcba", "b") = "a"
	 * StrUtil.substringAfterLast("abc", "c")   = ""
	 * StrUtil.substringAfterLast("a", "a")     = ""
	 * StrUtil.substringAfterLast("a", "z")     = ""
	 * </pre>
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring after the last occurrence of the separator, {@code null} if
	 * null String input
	 * 
	 */
	public static String substringAfterLast(final String str, final String separator) {
		if (isEmpty(str)) {
			return str;
		}
		if (isEmpty(separator)) {
			return EMPTY;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND || pos == str.length() - separator.length()) {
			return EMPTY;
		}
		return str.substring(pos + separator.length());
	}

	/**
	 * <p>
	 * Gets the substring before the first occurrence of a separator. The separator is not
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input
	 * will return the empty string.
	 * </p>
	 *
	 * <p>
	 * If nothing is found, the string input is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringBefore(null, *)      = null
	 * StrUtil.substringBefore("", *)        = ""
	 * StrUtil.substringBefore("abc", 'a')   = ""
	 * StrUtil.substringBefore("abcba", 'b') = "a"
	 * StrUtil.substringBefore("abc", 'c')   = "ab"
	 * StrUtil.substringBefore("abc", 'd')   = "abc"
	 * </pre>
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the first occurrence of the separator, {@code null} if
	 * null String input
	 * 
	 */
	public static String substringBefore(final String str, final int separator) {
		if (isEmpty(str)) {
			return str;
		}
		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Gets the substring before the first occurrence of a separator. The separator is not
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input
	 * will return the empty string. A {@code null} separator will return the input
	 * string.
	 * </p>
	 *
	 * <p>
	 * If nothing is found, the string input is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringBefore(null, *)      = null
	 * StrUtil.substringBefore("", *)        = ""
	 * StrUtil.substringBefore("abc", "a")   = ""
	 * StrUtil.substringBefore("abcba", "b") = "a"
	 * StrUtil.substringBefore("abc", "c")   = "ab"
	 * StrUtil.substringBefore("abc", "d")   = "abc"
	 * StrUtil.substringBefore("abc", "")    = ""
	 * StrUtil.substringBefore("abc", null)  = "abc"
	 * </pre>
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the first occurrence of the separator, {@code null} if
	 * null String input
	 * 
	 */
	public static String substringBefore(final String str, final String separator) {
		if (isEmpty(str) || separator == null) {
			return str;
		}
		if (separator.isEmpty()) {
			return EMPTY;
		}
		final int pos = str.indexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Gets the substring before the last occurrence of a separator. The separator is not
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} string input will return {@code null}. An empty ("") string input
	 * will return the empty string. An empty or {@code null} separator will return the
	 * input string.
	 * </p>
	 *
	 * <p>
	 * If nothing is found, the string input is returned.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringBeforeLast(null, *)      = null
	 * StrUtil.substringBeforeLast("", *)        = ""
	 * StrUtil.substringBeforeLast("abcba", "b") = "abc"
	 * StrUtil.substringBeforeLast("abc", "c")   = "ab"
	 * StrUtil.substringBeforeLast("a", "a")     = ""
	 * StrUtil.substringBeforeLast("a", "z")     = "a"
	 * StrUtil.substringBeforeLast("a", null)    = "a"
	 * StrUtil.substringBeforeLast("a", "")      = "a"
	 * </pre>
	 * @param str the String to get a substring from, may be null
	 * @param separator the String to search for, may be null
	 * @return the substring before the last occurrence of the separator, {@code null} if
	 * null String input
	 * 
	 */
	public static String substringBeforeLast(final String str, final String separator) {
		if (isEmpty(str) || isEmpty(separator)) {
			return str;
		}
		final int pos = str.lastIndexOf(separator);
		if (pos == INDEX_NOT_FOUND) {
			return str;
		}
		return str.substring(0, pos);
	}

	/**
	 * <p>
	 * Gets the String that is nested in between two instances of the same String.
	 * </p>
	 *
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} tag returns
	 * {@code null}.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringBetween(null, *)            = null
	 * StrUtil.substringBetween("", "")             = ""
	 * StrUtil.substringBetween("", "tag")          = null
	 * StrUtil.substringBetween("tagabctag", null)  = null
	 * StrUtil.substringBetween("tagabctag", "")    = ""
	 * StrUtil.substringBetween("tagabctag", "tag") = "abc"
	 * </pre>
	 * @param str the String containing the substring, may be null
	 * @param tag the String before and after the substring, may be null
	 * @return the substring, {@code null} if no match
	 * 
	 */
	public static String substringBetween(final String str, final String tag) {
		return substringBetween(str, tag, tag);
	}

	/**
	 * <p>
	 * Gets the String that is nested in between two Strings. Only the first match is
	 * returned.
	 * </p>
	 *
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} open/close returns
	 * {@code null} (no match). An empty ("") open and close returns an empty string.
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringBetween("wx[b]yz", "[", "]") = "b"
	 * StrUtil.substringBetween(null, *, *)          = null
	 * StrUtil.substringBetween(*, null, *)          = null
	 * StrUtil.substringBetween(*, *, null)          = null
	 * StrUtil.substringBetween("", "", "")          = ""
	 * StrUtil.substringBetween("", "", "]")         = null
	 * StrUtil.substringBetween("", "[", "]")        = null
	 * StrUtil.substringBetween("yabcz", "", "")     = ""
	 * StrUtil.substringBetween("yabcz", "y", "z")   = "abc"
	 * StrUtil.substringBetween("yabczyabcz", "y", "z")   = "abc"
	 * </pre>
	 * @param str the String containing the substring, may be null
	 * @param open the String before the substring, may be null
	 * @param close the String after the substring, may be null
	 * @return the substring, {@code null} if no match
	 * 
	 */
	public static String substringBetween(final String str, final String open, final String close) {
		if (!ObjectUtils.allNotNull(str, open, close)) {
			return null;
		}
		final int start = str.indexOf(open);
		if (start != INDEX_NOT_FOUND) {
			final int end = str.indexOf(close, start + open.length());
			if (end != INDEX_NOT_FOUND) {
				return str.substring(start + open.length(), end);
			}
		}
		return null;
	}

	/**
	 * <p>
	 * Searches a String for substrings delimited by a start and end tag, returning all
	 * matching substrings in an array.
	 * </p>
	 *
	 * <p>
	 * A {@code null} input String returns {@code null}. A {@code null} open/close returns
	 * {@code null} (no match). An empty ("") open/close returns {@code null} (no match).
	 * </p>
	 *
	 * <pre>
	 * StrUtil.substringsBetween("[a][b][c]", "[", "]") = ["a","b","c"]
	 * StrUtil.substringsBetween(null, *, *)            = null
	 * StrUtil.substringsBetween(*, null, *)            = null
	 * StrUtil.substringsBetween(*, *, null)            = null
	 * StrUtil.substringsBetween("", "[", "]")          = []
	 * </pre>
	 * @param str the String containing the substrings, null returns null, empty returns
	 * empty
	 * @param open the String identifying the start of the substring, empty returns null
	 * @param close the String identifying the end of the substring, empty returns null
	 * @return a String Array of substrings, or {@code null} if no match
	 *
	 */
	public static String[] substringsBetween(final String str, final String open, final String close) {
		if (str == null || isEmpty(open) || isEmpty(close)) {
			return null;
		}
		final int strLen = str.length();
		if (strLen == 0) {
			return ArrayUtils.EMPTY_STRING_ARRAY;
		}
		final int closeLen = close.length();
		final int openLen = open.length();
		final List<String> list = new ArrayList<>();
		int pos = 0;
		while (pos < strLen - closeLen) {
			int start = str.indexOf(open, pos);
			if (start < 0) {
				break;
			}
			start += openLen;
			final int end = str.indexOf(close, start);
			if (end < 0) {
				break;
			}
			list.add(str.substring(start, end));
			pos = end + closeLen;
		}
		if (list.isEmpty()) {
			return null;
		}
		return list.toArray(ArrayUtils.EMPTY_STRING_ARRAY);
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String, handling
	 * {@code null} by returning {@code null}.
     * </p>
     *
     * <pre>
     * StrUtil.trim(null)          = null
     * StrUtil.trim("")            = ""
     * StrUtil.trim("     ")       = ""
     * StrUtil.trim("abc")         = "abc"
     * StrUtil.trim("    abc    ") = "abc"
	 * </pre>
	 * @param str the String to be trimmed, may be null
	 * @return the trimmed string, {@code null} if null String input
	 */
	public static String trim(final String str) {
		return str == null ? null : str.trim();
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String returning
	 * an empty String ("") if the String is empty ("") after the trim or if it is
	 * {@code null}.
     *
     * <pre>
     * StrUtil.trimToEmpty(null)          = ""
     * StrUtil.trimToEmpty("")            = ""
     * StrUtil.trimToEmpty("     ")       = ""
     * StrUtil.trimToEmpty("abc")         = "abc"
     * StrUtil.trimToEmpty("    abc    ") = "abc"
	 * </pre>
	 * @param str the String to be trimmed, may be null
	 * @return the trimmed String, or an empty String if {@code null} input
	 */
	public static String trimToEmpty(final String str) {
		return str == null ? EMPTY : str.trim();
	}

	/**
	 * <p>
	 * Removes control characters (char &lt;= 32) from both ends of this String returning
	 * {@code null} if the String is empty ("") after the trim or if it is {@code null}.
     *
     * <pre>
     * StrUtil.trimToNull(null)          = null
     * StrUtil.trimToNull("")            = null
     * StrUtil.trimToNull("     ")       = null
     * StrUtil.trimToNull("abc")         = "abc"
     * StrUtil.trimToNull("    abc    ") = "abc"
	 * </pre>
	 * @param str the String to be trimmed, may be null
	 * @return the trimmed String, {@code null} if only chars &lt;= 32, empty or null
	 * String input
	 */
	public static String trimToNull(final String str) {
		final String ts = trim(str);
		return isEmpty(ts) ? null : ts;
    }

    /**
     * 替换指定字符串的指定区间内字符, 默认使用'*'替换
     *
     * <pre>
     * StrUtil.hide(null,*,*)=null
     * StrUtil.hide("",0,*)=""
     * StrUtil.hide("jackduan@163.com",-1,4)   ****duan@163.com
     * StrUtil.hide("jackduan@163.com",2,3)    ja*kduan@163.com
     * StrUtil.hide("jackduan@163.com",3,2)    jackduan@163.com
     * StrUtil.hide("jackduan@163.com",16,16)  jackduan@163.com
     * StrUtil.hide("jackduan@163.com",16,17)  jackduan@163.com
     * </pre>
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @return 替换后的字符串
     */
    public static String hide(CharSequence str, int startInclude, int endExclude) {
        return replace(str, startInclude, endExclude, '*');
    }

    /**
     * 替换指定字符串的指定区间内字符为固定字符<br>
     * 此方法使用{@link String#codePoints()}完成拆分替换
     *
     * @param str          字符串
     * @param startInclude 开始位置（包含）
     * @param endExclude   结束位置（不包含）
     * @param replacedChar 被替换的字符
     * @return 替换后的字符串
     */
    public static String replace(CharSequence str, int startInclude, int endExclude, char replacedChar) {
        if (isBlank(str)) {
            return EMPTY;
        }
        final String originalStr = str.toString();
        int[] strCodePoints = originalStr.codePoints().toArray();
        final int strLength = strCodePoints.length;
        if (startInclude > strLength) {
            return originalStr;
        }
        if (endExclude > strLength) {
            endExclude = strLength;
        }
        if (startInclude > endExclude) {
            // 如果起始位置大于结束位置，不替换
            return originalStr;
        }

        final StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < strLength; i++) {
            if (i >= startInclude && i < endExclude) {
                stringBuilder.append(replacedChar);
            } else {
                stringBuilder.append(new String(strCodePoints, i, 1));
            }
        }
        return stringBuilder.toString();
    }

    /**
     * 重复某个字符
     *
     * <pre>
     * StrUtil.repeat('e', 0)  = ""
     * StrUtil.repeat('e', 3)  = "eee"
     * StrUtil.repeat('e', -2) = ""
     * </pre>
     *
     * @param c     被重复的字符
     * @param count 重复的数目，如果小于等于0则返回""
     * @return 重复字符字符串
     */
    public static String repeat(char c, int count) {
        if (count <= 0) {
            return EMPTY;
        }
        char[] result = new char[count];
        Arrays.fill(result, c);
        return new String(result);
    }

    /**
     * <p>
     * Splits the provided text into an array, using whitespace as the separator.
     * Whitespace is defined by {@link Character#isWhitespace(char)}.
     * </p>
     *
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are
     * treated as one separator. For more control over the split use the StrTokenizer
     * class.
     * </p>
     *
     * <p>
     * A {@code null} input String returns {@code null}.
     * </p>
     *
     * <pre>
     * StrUtil.split(null)       = null
     * StrUtil.split("")         = []
     * StrUtil.split("abc def")  = ["abc", "def"]
     * StrUtil.split("abc  def") = ["abc", "def"]
     * StrUtil.split(" abc ")    = ["abc"]
     * </pre>
     *
     * @param str the String to parse, may be null
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str) {
        return split(str, null, -1);
    }

    /**
     * <p>
     * Splits the provided text into an array, separator specified. This is an alternative
     * to using StringTokenizer.
     * </p>
     *
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are
     * treated as one separator. For more control over the split use the StrTokenizer
     * class.
     * </p>
     *
     * <p>
     * A {@code null} input String returns {@code null}.
     * </p>
     *
     * <pre>
     * StrUtil.split(null, *)         = null
     * StrUtil.split("", *)           = []
     * StrUtil.split("a.b.c", '.')    = ["a", "b", "c"]
     * StrUtil.split("a..b.c", '.')   = ["a", "b", "c"]
     * StrUtil.split("a:b:c", '.')    = ["a:b:c"]
     * StrUtil.split("a b c", ' ')    = ["a", "b", "c"]
     * </pre>
     *
     * @param str           the String to parse, may be null
     * @param separatorChar the character used as the delimiter
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final char separatorChar) {
        return splitWorker(str, separatorChar, false);
    }

    /**
     * <p>
     * Splits the provided text into an array, separators specified. This is an
     * alternative to using StringTokenizer.
     * </p>
     *
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are
     * treated as one separator. For more control over the split use the StrTokenizer
     * class.
     * </p>
     *
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null} separatorChars
     * splits on whitespace.
     * </p>
     *
     * <pre>
     * StrUtil.split(null, *)         = null
     * StrUtil.split("", *)           = []
     * StrUtil.split("abc def", null) = ["abc", "def"]
     * StrUtil.split("abc def", " ")  = ["abc", "def"]
     * StrUtil.split("abc  def", " ") = ["abc", "def"]
     * StrUtil.split("ab:cd:ef", ":") = ["ab", "cd", "ef"]
     * </pre>
     *
     * @param str            the String to parse, may be null
     * @param separatorChars the characters used as the delimiters, {@code null} splits on
     *                       whitespace
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final String separatorChars) {
        return splitWorker(str, separatorChars, -1, false);
    }

    /**
     * <p>
     * Splits the provided text into an array with a maximum length, separators specified.
     * </p>
     *
     * <p>
     * The separator is not included in the returned String array. Adjacent separators are
     * treated as one separator.
     * </p>
     *
     * <p>
     * A {@code null} input String returns {@code null}. A {@code null} separatorChars
     * splits on whitespace.
     * </p>
     *
     * <p>
     * If more than {@code max} delimited substrings are found, the last returned string
     * includes all characters after the first {@code max - 1} returned strings (including
     * separator characters).
     * </p>
     *
     * <pre>
     * StrUtil.split(null, *, *)            = null
     * StrUtil.split("", *, *)              = []
     * StrUtil.split("ab cd ef", null, 0)   = ["ab", "cd", "ef"]
     * StrUtil.split("ab   cd ef", null, 0) = ["ab", "cd", "ef"]
     * StrUtil.split("ab:cd:ef", ":", 0)    = ["ab", "cd", "ef"]
     * StrUtil.split("ab:cd:ef", ":", 2)    = ["ab", "cd:ef"]
     * </pre>
     *
     * @param str            the String to parse, may be null
     * @param separatorChars the characters used as the delimiters, {@code null} splits on
     *                       whitespace
     * @param max            the maximum number of elements to include in the array. A zero or
     *                       negative value implies no limit
     * @return an array of parsed Strings, {@code null} if null String input
     */
    public static String[] split(final String str, final String separatorChars, final int max) {
        return splitWorker(str, separatorChars, max, false);
    }

    /**
     * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods
     * that do not return a maximum array length.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separatorChar     the separate character
     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty
     *                          token separators; if {@code false}, adjacent separators are treated as one
     *                          separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitWorker(final String str, final char separatorChar, final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return ArrayUtil.EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<>();
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        while (i < len) {
            if (str.charAt(i) == separatorChar) {
                if (match || preserveAllTokens) {
                    list.add(str.substring(start, i));
                    match = false;
                    lastMatch = true;
                }
                start = ++i;
                continue;
            }
            lastMatch = false;
            match = true;
            i++;
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(ArrayUtil.EMPTY_STRING_ARRAY);
    }

    /**
     * Performs the logic for the {@code split} and {@code splitPreserveAllTokens} methods
     * that return a maximum array length.
     *
     * @param str               the String to parse, may be {@code null}
     * @param separatorChars    the separate character
     * @param max               the maximum number of elements to include in the array. A zero or
     *                          negative value implies no limit.
     * @param preserveAllTokens if {@code true}, adjacent separators are treated as empty
     *                          token separators; if {@code false}, adjacent separators are treated as one
     *                          separator.
     * @return an array of parsed Strings, {@code null} if null String input
     */
    private static String[] splitWorker(final String str, final String separatorChars, final int max,
                                        final boolean preserveAllTokens) {
        // Performance tuned for 2.0 (JDK1.4)
        // Direct code is quicker than StringTokenizer.
        // Also, StringTokenizer uses isSpace() not isWhitespace()

        if (str == null) {
            return null;
        }
        final int len = str.length();
        if (len == 0) {
            return ArrayUtil.EMPTY_STRING_ARRAY;
        }
        final List<String> list = new ArrayList<>();
        int sizePlus1 = 1;
        int i = 0;
        int start = 0;
        boolean match = false;
        boolean lastMatch = false;
        if (separatorChars == null) {
            // Null separator means use whitespace
            while (i < len) {
                if (Character.isWhitespace(str.charAt(i))) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else if (separatorChars.length() == 1) {
            // Optimise 1 character case
            final char sep = separatorChars.charAt(0);
            while (i < len) {
                if (str.charAt(i) == sep) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        } else {
            // standard case
            while (i < len) {
                if (separatorChars.indexOf(str.charAt(i)) >= 0) {
                    if (match || preserveAllTokens) {
                        lastMatch = true;
                        if (sizePlus1++ == max) {
                            i = len;
                            lastMatch = false;
                        }
                        list.add(str.substring(start, i));
                        match = false;
                    }
                    start = ++i;
                    continue;
                }
                lastMatch = false;
                match = true;
                i++;
            }
        }
        if (match || preserveAllTokens && lastMatch) {
            list.add(str.substring(start, i));
        }
        return list.toArray(ArrayUtil.EMPTY_STRING_ARRAY);
	}

}

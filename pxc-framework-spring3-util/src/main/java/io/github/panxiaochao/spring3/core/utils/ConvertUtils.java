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

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * <p>
 * 值转换工具
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-09
 */
public class ConvertUtils {

	private static final String NULL_STR = "null";

	public static final Set<String> TRUE_SET = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("y", "yes", "on", "true", "t")));

	public static final Set<String> FALSE_SET = Collections
            .unmodifiableSet(new HashSet<>(Arrays.asList("n", "no", "off", "false", "f")));

	/**
	 * Convert String value to int value if parameter value is legal. And it automatically
	 * defaults to 0 if parameter value is null or blank str.
	 * @param val String value which need to be converted to int value.
	 * @return Converted int value and its default value is 0.
	 */
	public static int toInt(String val) {
		return toInt(val, 0);
	}

	/**
	 * Convert String value to int value if parameter value is legal. And return default
	 * value if parameter value is null or blank str.
	 * @param val value
	 * @param defaultValue default value
	 * @return int value if input value is legal, otherwise default value
	 */
	public static int toInt(String val, int defaultValue) {
		if (StringUtils.equalsIgnoreCase(val, NULL_STR)) {
			return defaultValue;
		}
		if (StringUtils.isBlank(val)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(val);
		}
		catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	/**
	 * Convert Object value to long value if parameter value is legal. And it
	 * automatically defaults to 0 if parameter value is null or other object.
	 * @param val object value
	 * @return Converted long value and its default value is 0.
	 */
	public static long toLong(Object val) {
		if (val instanceof Long) {
			return (Long) val;
		}
		return toLong(val.toString());
	}

	/**
	 * Convert String value to long value if parameter value is legal. And it
	 * automatically defaults to 0 if parameter value is null or blank str.
	 * @param val String value which need to be converted to int value.
	 * @return Converted long value and its default value is 0.
	 */
	public static long toLong(String val) {
		return toLong(val, 0L);
	}

	/**
	 * Convert String value to long value if parameter value is legal. And return default
	 * value if parameter value is null or blank str.
	 * @param val value
	 * @param defaultValue default value
	 * @return long value if input value is legal, otherwise default value
	 */
	public static long toLong(String val, long defaultValue) {
		if (StringUtils.isBlank(val)) {
			return defaultValue;
		}
		try {
			return Long.parseLong(val);
		}
		catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	/**
	 * <p>
	 * Convert String value to int value if parameter value is legal. And it automatically
	 * defaults to 0 if parameter value is null or blank str.
	 * </p>
	 * @param val String value which need to be converted to int value.
	 * @return Converted int value and its default value is 0.
	 */
	public static long toInteger(String val) {
		return toInteger(val, 0);
	}

	/**
	 * Convert String value to int value if parameter value is legal. And return default
	 * value if parameter value is null or blank str.
	 * @param val value
	 * @param defaultValue default value
	 * @return int value if input value is legal, otherwise default value
	 */
	public static long toInteger(String val, int defaultValue) {
		if (StringUtils.isBlank(val)) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(val);
		}
		catch (NumberFormatException exception) {
			return defaultValue;
		}
	}

	/**
	 * Convert String value to boolean value if parameter value is legal. And return
	 * default value if parameter value is null or blank str.
	 * @param val value
	 * @param defaultValue default value
	 * @return boolean value if input value is legal, otherwise default value
	 */
	public static boolean toBoolean(String val, boolean defaultValue) {
		if (StringUtils.isBlank(val)) {
			return defaultValue;
		}
		return Boolean.parseBoolean(val);
	}

	/**
	 * <p>
	 * Converts a String to a boolean (optimised for performance).
	 * </p>
	 *
	 * <p>
	 * {@code 'true'}, {@code 'on'}, {@code 'y'}, {@code 't'} or {@code 'yes'} (case
	 * insensitive) will return {@code true}. Otherwise, {@code false} is returned.
	 * </p>
	 *
	 * <p>
	 * This method performs 4 times faster (JDK1.4) than {@code Boolean.valueOf(String)}.
	 * However, this method accepts 'on' and 'yes', 't', 'y' as true values.
	 *
	 * <pre>
     *   ConvertUtils.toBoolean(null)    = false
     *   ConvertUtils.toBoolean("true")  = true
     *   ConvertUtils.toBoolean("TRUE")  = true
     *   ConvertUtils.toBoolean("tRUe")  = true
     *   ConvertUtils.toBoolean("on")    = true
     *   ConvertUtils.toBoolean("yes")   = true
     *   ConvertUtils.toBoolean("false") = false
     *   ConvertUtils.toBoolean("x gti") = false
     *   ConvertUtils.toBooleanObject("y") = true
     *   ConvertUtils.toBooleanObject("n") = false
     *   ConvertUtils.toBooleanObject("t") = true
     *   ConvertUtils.toBooleanObject("f") = false
	 * </pre>
	 * @param str the String to check
	 * @return the boolean value of the string, {@code false} if no match or the String is
	 * null
	 */
	public static boolean toBoolean(final String str) {
		return Boolean.TRUE.equals(toBooleanObject(str));
	}

	/**
	 * <p>
	 * Converts a String to a Boolean.
	 * </p>
	 *
	 * <p>
	 * {@code 'true'}, {@code 'on'}, {@code 'y'}, {@code 't'} or {@code 'yes'} (case
	 * insensitive) will return {@code true}. {@code 'false'}, {@code 'off'}, {@code 'n'},
	 * {@code 'f'} or {@code
	 * 'no'} (case insensitive) will return {@code false}. Otherwise, {@code null} is
	 * returned.
	 * </p>
	 *
	 * <p>
	 * NOTE: This returns null and will throw a NullPointerException if autoboxed to a
	 * boolean.
	 * </p>
	 *
	 * <pre>
	 *   // N.B. case is not significant
     *   ConvertUtils.toBooleanObject(null)    = null
     *   ConvertUtils.toBooleanObject("true")  = Boolean.TRUE
     *   ConvertUtils.toBooleanObject("T")     = Boolean.TRUE // i.e. T[RUE]
     *   ConvertUtils.toBooleanObject("false") = Boolean.FALSE
     *   ConvertUtils.toBooleanObject("f")     = Boolean.FALSE // i.e. f[alse]
     *   ConvertUtils.toBooleanObject("No")    = Boolean.FALSE
     *   ConvertUtils.toBooleanObject("n")     = Boolean.FALSE // i.e. n[o]
     *   ConvertUtils.toBooleanObject("on")    = Boolean.TRUE
     *   ConvertUtils.toBooleanObject("ON")    = Boolean.TRUE
     *   ConvertUtils.toBooleanObject("off")   = Boolean.FALSE
     *   ConvertUtils.toBooleanObject("oFf")   = Boolean.FALSE
     *   ConvertUtils.toBooleanObject("yes")   = Boolean.TRUE
     *   ConvertUtils.toBooleanObject("Y")     = Boolean.TRUE // i.e. Y[ES]
     *   ConvertUtils.toBooleanObject("blue")  = null
     *   ConvertUtils.toBooleanObject("true ") = null // trailing space (too long)
     *   ConvertUtils.toBooleanObject("ono")   = null // does not match on or no
	 * </pre>
	 * @param str the String to check; upper and lower case are treated as the same
	 * @return the Boolean value of the string, {@code null} if no match or {@code null}
	 * input
	 */
	@SuppressWarnings("all")
	public static Boolean toBooleanObject(String str) {
		String formatStr = (str == null ? StrUtil.EMPTY : str).toLowerCase();

		if (TRUE_SET.contains(formatStr)) {
			return true;
		}
		else if (FALSE_SET.contains(formatStr)) {
			return false;
		}
		else {
			return null;
		}
	}

}

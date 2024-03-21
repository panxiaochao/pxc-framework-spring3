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

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * <p>
 * 数组工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-03-10
 */
public class ArrayUtil {

	/**
	 * An empty immutable {@code boolean} array.
	 */
	public static final boolean[] EMPTY_BOOLEAN_ARRAY = new boolean[0];

	/**
	 * An empty immutable {@code Boolean} array.
	 */
	public static final Boolean[] EMPTY_BOOLEAN_OBJECT_ARRAY = new Boolean[0];

	/**
	 * An empty immutable {@code byte} array.
	 */
	public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];

	/**
	 * An empty immutable {@code Byte} array.
	 */
	public static final Byte[] EMPTY_BYTE_OBJECT_ARRAY = new Byte[0];

	/**
	 * An empty immutable {@code char} array.
	 */
	public static final char[] EMPTY_CHAR_ARRAY = new char[0];

	/**
	 * An empty immutable {@code Character} array.
	 */
	public static final Character[] EMPTY_CHARACTER_OBJECT_ARRAY = new Character[0];

	/**
	 * An empty immutable {@code Class} array.
	 */
	public static final Class<?>[] EMPTY_CLASS_ARRAY = new Class[0];

	/**
	 * An empty immutable {@code double} array.
	 */
	public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

	/**
	 * An empty immutable {@code Double} array.
	 */
	public static final Double[] EMPTY_DOUBLE_OBJECT_ARRAY = new Double[0];

	/**
	 * An empty immutable {@code Field} array.
	 *
	 * @since 3.10
	 */
	public static final Field[] EMPTY_FIELD_ARRAY = new Field[0];

	/**
	 * An empty immutable {@code float} array.
	 */
	public static final float[] EMPTY_FLOAT_ARRAY = new float[0];

	/**
	 * An empty immutable {@code Float} array.
	 */
	public static final Float[] EMPTY_FLOAT_OBJECT_ARRAY = new Float[0];

	/**
	 * An empty immutable {@code int} array.
	 */
	public static final int[] EMPTY_INT_ARRAY = new int[0];

	/**
	 * An empty immutable {@code Integer} array.
	 */
	public static final Integer[] EMPTY_INTEGER_OBJECT_ARRAY = new Integer[0];

	/**
	 * An empty immutable {@code long} array.
	 */
	public static final long[] EMPTY_LONG_ARRAY = new long[0];

	/**
	 * An empty immutable {@code Long} array.
	 */
	public static final Long[] EMPTY_LONG_OBJECT_ARRAY = new Long[0];

	/**
	 * An empty immutable {@code Method} array.
	 *
	 * @since 3.10
	 */
	public static final Method[] EMPTY_METHOD_ARRAY = new Method[0];

	/**
	 * An empty immutable {@code Object} array.
	 */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

	/**
	 * An empty immutable {@code short} array.
	 */
	public static final short[] EMPTY_SHORT_ARRAY = new short[0];

	/**
	 * An empty immutable {@code Short} array.
	 */
	public static final Short[] EMPTY_SHORT_OBJECT_ARRAY = new Short[0];

	/**
	 * An empty immutable {@code String} array.
	 */
	public static final String[] EMPTY_STRING_ARRAY = new String[0];

	/**
	 * <p>
	 * Checks if an array of primitive chars is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final char[] array) {
		return getLength(array) == 0;
	}

	/**
	 * <p>
	 * Checks if an array of Objects is empty or {@code null}.
	 * @param array the array to test
	 * @return {@code true} if the array is empty or {@code null}
	 */
	public static boolean isEmpty(final Object[] array) {
		return getLength(array) == 0;
	}

	/**
	 * 数组是否为空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回true<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回false<br>
	 * 如果此对象为数组对象，数组长度大于0情况下返回false，否则返回true
	 * @param array 数组
	 * @return 是否为空
	 */
	public static boolean isEmpty(Object array) {
		if (array != null) {
			if (isArray(array)) {
				return 0 == Array.getLength(array);
			}
			return false;
		}
		return true;
	}

	/**
	 * 对象是否为数组对象
	 * @param obj 对象
	 * @return 是否为数组对象，如果为{@code null} 返回false
	 */
	public static boolean isArray(Object obj) {
		return null != obj && obj.getClass().isArray();
	}

	/**
	 * <p>
	 * Returns the length of the specified array. This method can deal with {@code Object}
	 * arrays and with primitive arrays.
	 *
	 * <p>
	 * If the input array is {@code null}, {@code 0} is returned.
	 *
	 * <pre>
	 * ArrayUtil.getLength(null)            = 0
	 * ArrayUtil.getLength([])              = 0
	 * ArrayUtil.getLength([null])          = 1
	 * ArrayUtil.getLength([true, false])   = 2
	 * ArrayUtil.getLength([1, 2, 3])       = 3
	 * ArrayUtil.getLength(["a", "b", "c"]) = 3
	 * </pre>
	 * @param array the array to retrieve the length from, may be null
	 * @return The length of the array, or {@code 0} if the array is {@code null}
	 */
	public static int getLength(final Object array) {
		if (array == null) {
			return 0;
		}
		return Array.getLength(array);
	}

	/**
	 * 数组是否为非空
	 * @param <T> 数组元素类型
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static <T> boolean isNotEmpty(T[] array) {
		return (null != array && array.length != 0);
	}

	/**
	 * 数组是否为非空<br>
	 * 此方法会匹配单一对象，如果此对象为{@code null}则返回false<br>
	 * 如果此对象为非数组，理解为此对象为数组的第一个元素，则返回true<br>
	 * 如果此对象为数组对象，数组长度大于0情况下返回true，否则返回false
	 * @param array 数组
	 * @return 是否为非空
	 */
	public static boolean isNotEmpty(Object array) {
		return !isEmpty(array);
	}

	/**
	 * <p>
	 * Shallow clones an array returning a typecast result and handling
	 * @param <T> the component type of the array
	 * @param array the array to shallow clone, may be {@code null}
	 * @return the cloned array, {@code null} if {@code null} input
	 */
	public static <T> T[] clone(final T[] array) {
		if (array == null) {
			return null;
		}
		return array.clone();
	}

	/**
	 * <p>
	 * Adds all the elements of the given arrays into a new array.
	 * <p>
	 * The new array contains all of the element of {@code array1} followed by all of the
	 * elements {@code array2}. When an array is returned, it is always a new array.
	 *
	 * <pre>
	 * ArrayUtils.addAll(null, null)     = null
	 * ArrayUtils.addAll(array1, null)   = cloned copy of array1
	 * ArrayUtils.addAll(null, array2)   = cloned copy of array2
	 * ArrayUtils.addAll([], [])         = []
	 * ArrayUtils.addAll([null], [null]) = [null, null]
	 * ArrayUtils.addAll(["a", "b", "c"], ["1", "2", "3"]) = ["a", "b", "c", "1", "2", "3"]
	 * </pre>
	 * @param <T> the component type of the array
	 * @param array1 the first array whose elements are added to the new array, may be
	 * {@code null}
	 * @param array2 the second array whose elements are added to the new array, may be
	 * {@code null}
	 * @return The new array, {@code null} if both arrays are {@code null}. The type of
	 * the new array is the type of the first array, unless the first array is null, in
	 * which case the type is the same as the second array.
	 */
	@SafeVarargs
	public static <T> T[] addAll(final T[] array1, final T... array2) {
		if (array1 == null) {
			return clone(array2);
		}
		else if (array2 == null) {
			return clone(array1);
		}
		final Class<?> type1 = array1.getClass().getComponentType();
		@SuppressWarnings("unchecked") // OK, because array is of type T
		final T[] joinedArray = (T[]) Array.newInstance(type1, array1.length + array2.length);
		System.arraycopy(array1, 0, joinedArray, 0, array1.length);
		try {
			System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
		}
		catch (final ArrayStoreException ase) {
			// Check if problem was due to incompatible types
			/*
			 * We do this here, rather than before the copy because: - it would be a
			 * wasted check most of the time - safer, in case check turns out to be too
			 * strict
			 */
			final Class<?> type2 = array2.getClass().getComponentType();
			if (!type1.isAssignableFrom(type2)) {
				throw new IllegalArgumentException(
						"Cannot store " + type2.getName() + " in an array of " + type1.getName(), ase);
			}
			throw ase; // No, so rethrow original
		}
		return joinedArray;
	}

}

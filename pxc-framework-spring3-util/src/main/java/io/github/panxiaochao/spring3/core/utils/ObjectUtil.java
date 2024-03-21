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

import org.apache.commons.lang3.exception.CloneFailedException;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * <p>
 * This class tries to handle {@code null} input gracefully
 * </p>
 *
 * @author Lypxc
 * @since 2023-01-28
 */
public class ObjectUtil {

	/**
	 * <p>
	 * Checks if an Object is empty or null.
	 * </p>
	 * <p>
	 * The following types are supported:
	 * <ul>
	 * <li>{@link CharSequence}: Considered empty if its length is zero.</li>
	 * <li>{@code Array}: Considered empty if its length is zero.</li>
	 * <li>{@link Collection}: Considered empty if it has zero elements.</li>
	 * <li>{@link Map}: Considered empty if it has zero key-value mappings.</li>
	 * </ul>
	 *
	 * <pre>
	 * ObjectUtil.isEmpty(null)             = true
	 * ObjectUtil.isEmpty("")               = true
	 * ObjectUtil.isEmpty("ab")             = false
	 * ObjectUtil.isEmpty(new int[]{})      = true
	 * ObjectUtil.isEmpty(new int[]{1,2,3}) = false
	 * ObjectUtil.isEmpty(1234)             = false
	 * </pre>
	 * @param object the {@code Object} to test, may be {@code null}
	 * @return {@code true} if the object has a supported type and is empty or null,
	 * {@code false} otherwise
	 */
	public static boolean isEmpty(final Object object) {
		if (object == null) {
			return true;
		}
        if (object instanceof Optional) {
            return !((Optional<?>) object).isPresent();
        }
		if (object instanceof CharSequence) {
			return ((CharSequence) object).length() == 0;
		}
		if (object.getClass().isArray()) {
			return Array.getLength(object) == 0;
		}
		if (object instanceof Collection<?>) {
			return ((Collection<?>) object).isEmpty();
		}
		if (object instanceof Map<?, ?>) {
			return ((Map<?, ?>) object).isEmpty();
		}
		if (object instanceof Iterable) {
			return !((Iterable<?>) object).iterator().hasNext();
		}
		if (object instanceof Iterator) {
			return !((Iterator<?>) object).hasNext();
		}
		return false;
	}

    /**
     * Determine whether the given array is empty: i.e. {@code null} or of zero length.
     *
     * @param array the array to check
     * @see #isEmpty(Object)
     */
    public static boolean isEmpty(Object[] array) {
        return (array == null || array.length == 0);
    }

	/**
	 * <p>
	 * Checks if an Object is not empty and not null.
	 * </p>
	 * <p>
	 * The following types are supported:
	 * <ul>
	 * <li>{@link CharSequence}: Considered empty if its length is zero.</li>
	 * <li>{@code Array}: Considered empty if its length is zero.</li>
	 * <li>{@link Collection}: Considered empty if it has zero elements.</li>
	 * <li>{@link Map}: Considered empty if it has zero key-value mappings.</li>
	 * </ul>
	 *
	 * <pre>
	 * ObjectUtil.isNotEmpty(null)             = false
	 * ObjectUtil.isNotEmpty("")               = false
	 * ObjectUtil.isNotEmpty("ab")             = true
	 * ObjectUtil.isNotEmpty(new int[]{})      = false
	 * ObjectUtil.isNotEmpty(new int[]{1,2,3}) = true
	 * ObjectUtil.isNotEmpty(1234)             = true
	 * </pre>
	 * @param object the {@code Object} to test, may be {@code null}
	 * @return {@code true} if the object has an unsupported type or is not empty and not
	 * null, {@code false} otherwise
	 */
	public static boolean isNotEmpty(final Object object) {
		return !isEmpty(object);
	}

	/**
	 * <p>
	 * Returns a default value if the object passed is {@code null}.
	 * </p>
	 *
	 * <pre>
	 * ObjectUtil.defaultIfNull(null, null)      = null
	 * ObjectUtil.defaultIfNull(null, "")        = ""
	 * ObjectUtil.defaultIfNull(null, "zz")      = "zz"
	 * ObjectUtil.defaultIfNull("abc", *)        = "abc"
	 * ObjectUtil.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
	 * </pre>
	 * @param <T> the type of the object
	 * @param object the {@code Object} to test, may be {@code null}
	 * @param defaultValue the default value to return, may be {@code null}
	 * @return {@code object} if it is not {@code null}, defaultValue otherwise
	 */
	public static <T> T getIfNull(final T object, final T defaultValue) {
        return isNotEmpty(object) ? object : defaultValue;
    }

    /**
     * Append the given object to the given array, returning a new array consisting of the
     * input array contents plus the given object.
     *
     * @param array the array to append to (can be {@code null})
     * @param obj   the object to append
     * @return the new array (of the same component type; never {@code null})
     */
    public static <A, O extends A> A[] addObjectToArray(A[] array, O obj) {
        Class<?> compType = Object.class;
        if (array != null) {
            compType = array.getClass().getComponentType();
        } else if (obj != null) {
            compType = obj.getClass();
        }
        int newArrLength = (array != null ? array.length + 1 : 1);
        @SuppressWarnings("unchecked")
        A[] newArr = (A[]) Array.newInstance(compType, newArrLength);
        if (array != null) {
            System.arraycopy(array, 0, newArr, 0, array.length);
        }
        newArr[newArr.length - 1] = obj;
        return newArr;
    }

    /**
     * <p>
     * Clone an object.
     * </p>
     *
     * @param <T> the type of the object
     * @param obj the object to clone, null returns null
     * @return the clone if the object implements {@link Cloneable} otherwise {@code null}
     * @throws CloneFailedException if the object is cloneable and the clone operation
     *                              fails
     */
    public static <T> T clone(final T obj) {
        if (obj instanceof Cloneable) {
            final Object result;
            if (obj.getClass().isArray()) {
                final Class<?> componentType = obj.getClass().getComponentType();
                if (componentType.isPrimitive()) {
                    int length = Array.getLength(obj);
                    result = Array.newInstance(componentType, length);
                    while (length-- > 0) {
                        Array.set(result, length, Array.get(obj, length));
                    }
                } else {
                    result = ((Object[]) obj).clone();
                }
            } else {
                try {
                    final Method clone = obj.getClass().getMethod("clone");
                    result = clone.invoke(obj);
                } catch (final NoSuchMethodException e) {
                    throw new CloneFailedException(
                            "Cloneable type " + obj.getClass().getName() + " has no clone method", e);
                } catch (final IllegalAccessException e) {
                    throw new CloneFailedException("Cannot clone Cloneable type " + obj.getClass().getName(), e);
                } catch (final InvocationTargetException e) {
                    throw new CloneFailedException("Exception cloning Cloneable type " + obj.getClass().getName(),
                            e.getCause());
                }
            }
            @SuppressWarnings("unchecked") // OK because input is of type T
            final T checked = (T) result;
            return checked;
        } else {
            if (obj instanceof Serializable) {
                return SerializationUtil.clone(obj);
            }
        }
        return null;
    }

    /**
     * 比较两个对象是否相等，此方法是 {@link #equal(Object, Object)}的别名方法。<br>
     * 相同的条件有两个，满足其一即可：<br>
     * <ol>
     * <li>obj1 == null &amp;&amp; obj2 == null</li>
     * <li>obj1.equals(obj2)</li>
     * <li>如果是BigDecimal比较，0 == obj1.compareTo(obj2)</li>
     * </ol>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     * @see #equal(Object, Object)
     */
    public static boolean equals(Object obj1, Object obj2) {
        return equal(obj1, obj2);
    }

    /**
     * 比较两个对象是否相等。<br>
     * 相同的条件有两个，满足其一即可：<br>
     * <ol>
     * <li>obj1 == null &amp;&amp; obj2 == null</li>
     * <li>obj1.equals(obj2)</li>
     * <li>如果是BigDecimal比较，0 == obj1.compareTo(obj2)</li>
     * </ol>
     *
     * @param obj1 对象1
     * @param obj2 对象2
     * @return 是否相等
     * @see Objects#equals(Object, Object)
     */
    public static boolean equal(Object obj1, Object obj2) {
        if (obj1 instanceof Number && obj2 instanceof Number) {
            if (obj1 instanceof BigDecimal && obj2 instanceof BigDecimal) {
                // BigDecimal使用compareTo方式判断，因为使用equals方法也判断小数位数，如2.0和2.00就不相等
                return equals((BigDecimal) obj1, (BigDecimal) obj2);
            }
        }
        return Objects.equals(obj1, obj2);
    }

    /**
     * 比较大小，值相等 返回true<br>
     * 此方法通过调用{@link BigDecimal#compareTo(BigDecimal)}方法来判断是否相等<br>
     * 此方法判断值相等时忽略精度的，即0.00 == 0
     *
     * @param bigNum1 数字1
     * @param bigNum2 数字2
     * @return 是否相等
     */
    public static boolean equals(BigDecimal bigNum1, BigDecimal bigNum2) {
        //noinspection NumberEquality
        if (bigNum1 == bigNum2) {
            // 如果用户传入同一对象，省略compareTo以提高性能。
            return true;
        }
        if (bigNum1 == null || bigNum2 == null) {
            return false;
        }
        return 0 == bigNum1.compareTo(bigNum2);
	}

}

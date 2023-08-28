/*
 * Copyright © 2023-2024 Lypxc (545685602@qq.com)
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
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

/**
 * <p>
 * This class tries to handle {@code null} input gracefully.
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
		return object != null ? object : defaultValue;
	}

}

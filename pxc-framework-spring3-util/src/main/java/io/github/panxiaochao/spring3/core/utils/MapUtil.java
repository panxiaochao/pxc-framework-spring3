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

import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.function.Function;

/**
 * <p>
 * Map工具类
 * </p>
 *
 * @author Lypxc
 * @since 2022-11-28
 */
public class MapUtil {

	/**
	 * 默认初始大小
	 */
	public static final int DEFAULT_INITIAL_CAPACITY = 16;

	/**
	 * 默认增长因子，当Map的size达到 容量*增长因子时，开始扩充Map
	 */
	public static final float DEFAULT_LOAD_FACTOR = 0.75f;

	/**
	 * 新建一个HashMap
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap() {
		return new HashMap<>();
	}

	/**
	 * 新建一个HashMap
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size) {
		return newHashMap(size, false);
	}

	/**
	 * 新建一个HashMap
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(boolean isLinked) {
		return newHashMap(DEFAULT_INITIAL_CAPACITY, isLinked);
	}

	/**
	 * 新建一个HashMap
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param size 初始大小，由于默认负载因子0.75，传入的size会实际初始大小为size / 0.75 + 1
	 * @param isLinked Map的Key是否有序，有序返回 {@link LinkedHashMap}，否则返回 {@link HashMap}
	 * @return HashMap对象
	 */
	public static <K, V> HashMap<K, V> newHashMap(int size, boolean isLinked) {
		final int initialCapacity = (int) (size / DEFAULT_LOAD_FACTOR) + 1;
		return isLinked ? new LinkedHashMap<>(initialCapacity) : new HashMap<>(initialCapacity);
	}

	/**
	 * Null-safe check if the specified Dictionary is empty.
	 *
	 * <p>
	 * Null returns true.
	 * @param map the collection to check, may be null
	 * @return true if empty or null
	 */
	public static boolean isEmpty(Map<?, ?> map) {
		return (null == map || map.isEmpty());
	}

	/**
	 * Null-safe check if the specified Dictionary is not empty.
	 *
	 * <p>
	 * Null returns false.
	 * @param map the collection to check, may be null
	 * @return true if non-null and non-empty
	 */
	public static boolean isNotEmpty(Map<?, ?> map) {
		return !isEmpty(map);
	}

	/**
	 * 如果提供的集合为{@code null}，返回一个不可变的默认空集合，否则返回原集合<br>
	 * 空集合使用{@link Collections#emptyMap()}
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param set 提供的集合，可能为null
	 * @return 原集合，若为null返回空集合
	 */
	public static <K, V> Map<K, V> emptyIfNull(Map<K, V> set) {
		return (null == set) ? Collections.emptyMap() : set;
	}

	/**
	 * 如果给定Map为空，返回默认Map
	 * @param <T> 集合类型
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map Map
	 * @param defaultMap 默认Map
	 * @return 非空（empty）的原Map或默认Map
	 */
	public static <T extends Map<K, V>, K, V> T defaultIfEmpty(T map, T defaultMap) {
		return isEmpty(map) ? defaultMap : map;
	}

	/**
	 * Put into map if value is not null.
	 * @param target target map
	 * @param key key
	 * @param value value
	 */
	public static void putIfValNoNull(Map target, Object key, Object value) {
		Objects.requireNonNull(key, "key");
		if (value != null) {
			target.put(key, value);
		}
	}

	/**
	 * Put into map if value is not empty.
	 * @param target target map
	 * @param key key
	 * @param value value
	 */
	public static void putIfValNoEmpty(Map target, Object key, Object value) {
		Objects.requireNonNull(key, "key");
		if (value instanceof String) {
			if (!StringUtils.hasText((String) value)) {
				target.put(key, value);
			}
			return;
		}
		if (value instanceof Collection) {
			if (!CollectionUtils.isEmpty((Collection) value)) {
				target.put(key, value);
			}
			return;
		}
		if (value instanceof Map) {
			if (isNotEmpty((Map) value)) {
				target.put(key, value);
			}
			return;
		}
	}

	/**
	 * 如果 key 对应的 value 不存在，则使用获取 mappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回
	 * value。<br>
	 * 方法来自Dubbo，解决使用ConcurrentHashMap.computeIfAbsent导致的死循环问题。（issues#2349）<br>
	 * A temporary workaround for Java 8 specific performance issue JDK-8161372 .<br>
	 * This class should be removed once we drop Java 8 support.<br>
	 * 参考：https://github.com/apache/dubbo/blob/3.2/dubbo-common/src/main/java/org/apache/dubbo/common/utils/ConcurrentHashMapUtils.java
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map Map
	 * @param key 键
	 * @param mappingFunction 值不存在时值的生成函数
	 * @return 值
	 * @see <a href=
	 * "https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
	 */
	public static <K, V> V computeIfAbsent(Map<K, V> map, K key, Function<? super K, ? extends V> mappingFunction) {
		if (JdkUtil.IS_JDK8) {
			return computeIfAbsentForJdk8(map, key, mappingFunction);
		}
		else {
			return map.computeIfAbsent(key, mappingFunction);
		}
	}

	/**
	 * 如果 key 对应的 value 不存在，则使用获取 mappingFunction 重新计算后的值，并保存为该 key 的 value，否则返回
	 * value。<br>
	 * 解决使用ConcurrentHashMap.computeIfAbsent导致的死循环问题。（issues#2349）<br>
	 * A temporary workaround for Java 8 specific performance issue JDK-8161372 .<br>
	 * This class should be removed once we drop Java 8 support.
	 *
	 * <p>
	 * 注意此方法只能用于JDK8
	 * </p>
	 * @param <K> 键类型
	 * @param <V> 值类型
	 * @param map Map，一般用于线程安全的Map
	 * @param key 键
	 * @param mappingFunction 值计算函数
	 * @return 值
	 * @see <a href=
	 * "https://bugs.openjdk.java.net/browse/JDK-8161372">https://bugs.openjdk.java.net/browse/JDK-8161372</a>
	 */
	public static <K, V> V computeIfAbsentForJdk8(final Map<K, V> map, final K key,
			final Function<? super K, ? extends V> mappingFunction) {
		V value = map.get(key);
		if (null == value) {
			value = mappingFunction.apply(key);
			final V res = map.putIfAbsent(key, value);
			if (null != res) {
				// issues#I6RVMY
				// 如果旧值存在，说明其他线程已经赋值成功，putIfAbsent没有执行，返回旧值
				return res;
			}
			// 如果旧值不存在，说明赋值成功，返回当前值

			// Dubbo的解决方式，判空后调用依旧无法解决死循环问题
			// 见：Issue2349Test
			// value = map.computeIfAbsent(key, mappingFunction);
		}
		return value;
	}

	/**
	 * 去掉Map中指定key的键值对，修改原Map
	 * @param <K> Key类型
	 * @param <V> Value类型
	 * @param map Map
	 * @param keys 键列表
	 */
	@SafeVarargs
	public static <K, V> void removeAny(Map<K, V> map, final K... keys) {
		if (isEmpty(map)) {
			return;
		}

		if (ArrayUtil.isEmpty(keys)) {
			return;
		}

		List<K> keysArray = Arrays.asList(keys);
		final Iterator<Map.Entry<K, V>> iter = map.entrySet().iterator();
		Map.Entry<K, V> entry;
		while (iter.hasNext()) {
			entry = iter.next();
			if (keysArray.contains(entry.getKey())) {
				iter.remove();
			}
		}
	}

    /**
     * 按照值排序，可选是否倒序
     *
     * @param map    需要对值排序的map
     * @param <K>    键类型
     * @param <V>    值类型
     * @param isDesc 是否倒序
     * @return 排序后新的Map
     */
    public static <K, V extends Comparable<? super V>> Map<K, V> comparingByValue(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = new LinkedHashMap<>();
        Comparator<Map.Entry<K, V>> entryComparator = Map.Entry.comparingByValue();
        if (isDesc) {
            entryComparator = entryComparator.reversed();
        }
        map.entrySet().stream().sorted(entryComparator).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

    /**
     * 按照键值排序，可选是否倒序
     *
     * @param map    需要对值排序的map
     * @param <K>    键类型
     * @param <V>    值类型
     * @param isDesc 是否倒序
     * @return 排序后新的Map
     */
    public static <K extends Comparable<? super K>, V> Map<K, V> comparingByKey(Map<K, V> map, boolean isDesc) {
        Map<K, V> result = new LinkedHashMap<>();
        Comparator<Map.Entry<K, V>> entryComparator = Map.Entry.comparingByKey();
        if (isDesc) {
            entryComparator = entryComparator.reversed();
        }
        map.entrySet().stream().sorted(entryComparator).forEachOrdered(e -> result.put(e.getKey(), e.getValue()));
        return result;
    }

}

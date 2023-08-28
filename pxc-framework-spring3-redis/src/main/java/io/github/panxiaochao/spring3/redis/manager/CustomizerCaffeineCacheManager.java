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
package io.github.panxiaochao.spring3.redis.manager;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import io.github.panxiaochao.spring3.core.utils.StringPools;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * <p>
 * 支持 cacheName 多参数，用#隔开.
 * </p>
 * <p>
 * 重写源码：org.springframework.cache.caffeine.CaffeineCacheManager
 * </p>
 * <pre>
 * key格式为: cacheNames#ttl
 * ttl 过期时间 如果设置为0则不过期 默认为0
 * 例子: test、test#60s
 *</pre>
 *
 * @author Lypxc
 * @since 2023-08-01
 */
public class CustomizerCaffeineCacheManager implements CacheManager {

	private Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();

	private boolean allowNullValues = true;

	private boolean dynamic = true;

	private final Map<String, Cache> cacheMap = new ConcurrentHashMap<>(16);

	private final Collection<String> customCacheNames = new CopyOnWriteArrayList<>();

	/**
	 * Construct a dynamic CaffeineCacheManager, lazily creating cache instances as they
	 * are being requested.
	 */
	public CustomizerCaffeineCacheManager() {
	}

	/**
	 * Specify the set of cache names for this CacheManager's 'static' mode.
	 * <p>
	 * The number of caches and their names will be fixed after a call to this method,
	 * with no creation of further cache regions at runtime.
	 * <p>
	 * Calling this with a {@code null} collection argument resets the mode to 'dynamic',
	 * allowing for further creation of caches again.
	 */
	public void setCacheNames(@Nullable Collection<String> cacheNames) {
		if (cacheNames != null) {
			for (String name : cacheNames) {
				this.cacheMap.put(name, createCaffeineCache(name));
			}
			this.dynamic = false;
		}
		else {
			this.dynamic = true;
		}
	}

	/**
	 * Set the Caffeine to use for building each individual {@link CaffeineCache}
	 * instance.
	 * @see #createNativeCaffeineCache
	 * @see Caffeine#build()
	 */
	public void setCaffeine(Caffeine<Object, Object> caffeine) {
		Assert.notNull(caffeine, "Caffeine must not be null");
		doSetCaffeine(caffeine);
	}

	/**
	 * Set the {@link CaffeineSpec} to use for building each individual
	 * {@link CaffeineCache} instance.
	 * @see #createNativeCaffeineCache
	 * @see Caffeine#from(CaffeineSpec)
	 */
	public void setCaffeineSpec(CaffeineSpec caffeineSpec) {
		doSetCaffeine(Caffeine.from(caffeineSpec));
	}

	/**
	 * Set the Caffeine cache specification String to use for building each individual
	 * {@link CaffeineCache} instance. The given value needs to comply with Caffeine's
	 * {@link CaffeineSpec} (see its javadoc).
	 * @see #createNativeCaffeineCache
	 * @see Caffeine#from(String)
	 */
	public void setCacheSpecification(String cacheSpecification) {
		doSetCaffeine(Caffeine.from(cacheSpecification));
	}

	private void doSetCaffeine(Caffeine<Object, Object> cacheBuilder) {
		if (!ObjectUtils.nullSafeEquals(this.cacheBuilder, cacheBuilder)) {
			this.cacheBuilder = cacheBuilder;
			refreshCommonCaches();
		}
	}

	/**
	 * Specify whether to accept and convert {@code null} values for all caches in this
	 * cache manager.
	 * <p>
	 * Default is "true", despite Caffeine itself not supporting {@code null} values. An
	 * internal holder object will be used to store user-level {@code null}s.
	 */
	public void setAllowNullValues(boolean allowNullValues) {
		if (this.allowNullValues != allowNullValues) {
			this.allowNullValues = allowNullValues;
			refreshCommonCaches();
		}
	}

	/**
	 * Return whether this cache manager accepts and converts {@code null} values for all
	 * of its caches.
	 */
	public boolean isAllowNullValues() {
		return this.allowNullValues;
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(this.cacheMap.keySet());
	}

	@Override
	@Nullable
	public Cache getCache(String name) {
		// 重写 name 分割 name
		String[] array = StringUtils.delimitedListToStringArray(name, StringPools.HASH);
		name = array[0];
		Cache cache = this.cacheMap.get(name);
		if (cache == null && this.dynamic) {
			if (array.length > 1) {
				long mills = DurationStyle.detectAndParse(array[1]).toMillis();
				cache = this.cacheMap.computeIfAbsent(name,
						str -> this.adaptCaffeineCache(str,
								Caffeine.newBuilder()
									.expireAfterWrite(Duration.ofMillis(mills))
									.initialCapacity(100)
									.maximumSize(200)
									.build()));
			}
			else {
				cache = this.cacheMap.computeIfAbsent(name, this::createCaffeineCache);
			}
		}
		return cache;
	}

	/**
	 * Register the given native Caffeine Cache instance with this cache manager, adapting
	 * it to Spring's cache API for exposure through {@link #getCache}. Any number of such
	 * custom caches may be registered side by side.
	 * <p>
	 * This allows for custom settings per cache (as opposed to all caches sharing the
	 * common settings in the cache manager's configuration) and is typically used with
	 * the Caffeine builder API:
	 * {@code registerCustomCache("myCache", Caffeine.newBuilder().maximumSize(10).build())}
	 * <p>
	 * Note that any other caches, whether statically specified through
	 * {@link #setCacheNames} or dynamically built on demand, still operate with the
	 * common settings in the cache manager's configuration.
	 * @param name the name of the cache
	 * @param cache the custom Caffeine Cache instance to register
	 * @since 5.2.8
	 * @see #adaptCaffeineCache
	 */
	public void registerCustomCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
		this.customCacheNames.add(name);
		this.cacheMap.put(name, adaptCaffeineCache(name, cache));
	}

	/**
	 * Adapt the given new native Caffeine Cache instance to Spring's {@link Cache}
	 * abstraction for the specified cache name.
	 * @param name the name of the cache
	 * @param cache the native Caffeine Cache instance
	 * @return the Spring CaffeineCache adapter (or a decorator thereof)
	 * @since 5.2.8
	 * @see CaffeineCache
	 * @see #isAllowNullValues()
	 */
	protected Cache adaptCaffeineCache(String name, com.github.benmanes.caffeine.cache.Cache<Object, Object> cache) {
		return new CaffeineCache(name, cache, isAllowNullValues());
	}

	/**
	 * Build a common {@link CaffeineCache} instance for the specified cache name, using
	 * the common Caffeine configuration specified on this cache manager.
	 * <p>
	 * Delegates to {@link #adaptCaffeineCache} as the adaptation method to Spring's cache
	 * abstraction (allowing for centralized decoration etc), passing in a freshly built
	 * native Caffeine Cache instance.
	 * @param name the name of the cache
	 * @return the Spring CaffeineCache adapter (or a decorator thereof)
	 * @see #adaptCaffeineCache
	 * @see #createNativeCaffeineCache
	 */
	protected Cache createCaffeineCache(String name) {
		return adaptCaffeineCache(name, createNativeCaffeineCache(name));
	}

	/**
	 * Build a common Caffeine Cache instance for the specified cache name, using the
	 * common Caffeine configuration specified on this cache manager.
	 * @param name the name of the cache
	 * @return the native Caffeine Cache instance
	 * @see #createCaffeineCache
	 */
	protected com.github.benmanes.caffeine.cache.Cache<Object, Object> createNativeCaffeineCache(String name) {
		return this.cacheBuilder.build();
	}

	/**
	 * Recreate the common caches with the current state of this manager.
	 */
	private void refreshCommonCaches() {
		for (Map.Entry<String, Cache> entry : this.cacheMap.entrySet()) {
			if (!this.customCacheNames.contains(entry.getKey())) {
				entry.setValue(createCaffeineCache(entry.getKey()));
			}
		}
	}

}

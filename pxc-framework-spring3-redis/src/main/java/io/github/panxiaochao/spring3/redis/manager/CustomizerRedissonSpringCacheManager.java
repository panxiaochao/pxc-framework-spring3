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
package io.github.panxiaochao.spring3.redis.manager;

import io.github.panxiaochao.spring3.core.utils.StringPools;
import io.github.panxiaochao.spring3.redis.utils.RedissonUtil;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.beans.factory.BeanDefinitionStoreException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.convert.DurationStyle;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.TransactionAwareCacheDecorator;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * <p>
 * 支持 cacheName 多参数，用#隔开.
 * </p>
 * <p>
 * 重写源码：org.redisson.spring.cache.RedissonSpringCacheManager
 * </p>
 * <pre>
 * key格式为: cacheNames#ttl#maxIdleTime#maxSize
 * ttl 过期时间 如果设置为0则不过期 默认为0
 * maxIdleTime 最大空闲时间 根据LRU算法清理空闲数据 如果设置为0则不检测 默认为0
 * maxSize 组最大长度 根据LRU算法清理溢出数据 如果设置为0则无限长 默认为0
 * 例子: test、test#60s、test#0#60s、test#0#1m#1000、test#1h#0#500
 *</pre>
 *
 * @author Lypxc
 * @since 2023-08-01
 */
public class CustomizerRedissonSpringCacheManager implements CacheManager, ResourceLoaderAware, InitializingBean {

	ResourceLoader resourceLoader;

	private boolean dynamic = true;

	private boolean allowNullValues = true;

	private boolean transactionAware = false;

	RedissonClient redisson;

	Map<String, CacheConfig> configMap = new ConcurrentHashMap<String, CacheConfig>();

	ConcurrentMap<String, Cache> instanceMap = new ConcurrentHashMap<String, Cache>();

	String configLocation;

	/**
	 * Creates CacheManager
	 */
	public CustomizerRedissonSpringCacheManager() {
	}

	/**
	 * Defines possibility of storing {@code null} values.
	 * <p>
	 * Default is <code>true</code>
	 * @param allowNullValues stores if <code>true</code>
	 */
	public void setAllowNullValues(boolean allowNullValues) {
		this.allowNullValues = allowNullValues;
	}

	/**
	 * Defines if cache aware of Spring-managed transactions. If {@code true} put/evict
	 * operations are executed only for successful transaction in after-commit phase.
	 * <p>
	 * Default is <code>false</code>
	 * @param transactionAware cache is transaction aware if <code>true</code>
	 */
	public void setTransactionAware(boolean transactionAware) {
		this.transactionAware = transactionAware;
	}

	/**
	 * Defines 'fixed' cache names. A new cache instance will not be created in dynamic
	 * for non-defined names.
	 * <p>
	 * `null` parameter setups dynamic mode
	 * @param names of caches
	 */
	public void setCacheNames(Collection<String> names) {
		if (names != null) {
			for (String name : names) {
				getCache(name);
			}
			dynamic = false;
		}
		else {
			dynamic = true;
		}
	}

	/**
	 * Set cache config location
	 * @param configLocation object
	 */
	public void setConfigLocation(String configLocation) {
		this.configLocation = configLocation;
	}

	/**
	 * Set cache config mapped by cache name
	 * @param config object
	 */
	public void setConfig(Map<String, ? extends CacheConfig> config) {
		this.configMap = (Map<String, CacheConfig>) config;
	}

	/**
	 * Set Redisson instance
	 * @param redisson instance
	 */
	public void setRedisson(RedissonClient redisson) {
		this.redisson = redisson;
	}

	/**
	 * Obtain RedissonClient instance
	 * @return RedissonClient
	 */
	public RedissonClient getRedisson() {
		if (this.redisson == null) {
			this.redisson = RedissonUtil.INSTANCE().ofRedissonClient();
		}
		return this.redisson;
	}

	protected CacheConfig createDefaultConfig() {
		return new CacheConfig();
	}

	@Override
	public Cache getCache(@Nullable String name) {
		// 重写 name 分割name
		String[] array = StringUtils.delimitedListToStringArray(name, StringPools.HASH);
		name = array[0];

		Cache cache = instanceMap.get(name);
		if (cache != null) {
			return cache;
		}
		if (!dynamic) {
			return cache;
		}

		CacheConfig config = configMap.get(name);
		if (config == null) {
			config = createDefaultConfig();
			configMap.put(name, config);
		}

		// 重写参数
		if (array.length > 1) {
			config.setTTL(DurationStyle.detectAndParse(array[1]).toMillis());
		}
		if (array.length > 2) {
			config.setMaxIdleTime(DurationStyle.detectAndParse(array[2]).toMillis());
		}
		if (array.length > 3) {
			config.setMaxSize(Integer.parseInt(array[3]));
		}

		if (config.getMaxIdleTime() == 0 && config.getTTL() == 0 && config.getMaxSize() == 0) {
			return createMap(name);
		}

		return createMapCache(name, config);
	}

	private Cache createMap(String name) {
		RMap<Object, Object> map = getRedisson().getMap(name);
		Cache cache = new RedissonCache(map, allowNullValues);
		if (transactionAware) {
			cache = new TransactionAwareCacheDecorator(cache);
		}
		Cache oldCache = instanceMap.putIfAbsent(name, cache);
		if (oldCache != null) {
			cache = oldCache;
		}
		return cache;
	}

	private Cache createMapCache(String name, CacheConfig config) {
		RMapCache<Object, Object> map = getRedisson().getMapCache(name);
		Cache cache = new RedissonCache(map, config, allowNullValues);
		if (transactionAware) {
			cache = new TransactionAwareCacheDecorator(cache);
		}
		Cache oldCache = instanceMap.putIfAbsent(name, cache);
		if (oldCache != null) {
			cache = oldCache;
		}
		else {
			map.setMaxSize(config.getMaxSize());
		}
		return cache;
	}

	@Override
	public Collection<String> getCacheNames() {
		return Collections.unmodifiableSet(configMap.keySet());
	}

	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (configLocation == null) {
			return;
		}

		Resource resource = resourceLoader.getResource(configLocation);
		try {
			this.configMap = (Map<String, CacheConfig>) CacheConfig.fromYAML(resource.getInputStream());
		}
		catch (IOException e) {
			// try to read yaml
			try {
				this.configMap = (Map<String, CacheConfig>) CacheConfig.fromJSON(resource.getInputStream());
			}
			catch (IOException e1) {
				e1.addSuppressed(e);
				throw new BeanDefinitionStoreException(
						"Could not parse cache configuration at [" + configLocation + "]", e1);
			}
		}
	}

}

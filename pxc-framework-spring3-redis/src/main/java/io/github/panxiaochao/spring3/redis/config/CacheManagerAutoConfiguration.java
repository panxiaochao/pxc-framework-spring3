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
package io.github.panxiaochao.spring3.redis.config;

import io.github.panxiaochao.spring3.redis.constants.CacheManagerType;
import io.github.panxiaochao.spring3.redis.manager.CustomizerCaffeineCacheManager;
import io.github.panxiaochao.spring3.redis.manager.CustomizerRedissonSpringCacheManager;
import io.github.panxiaochao.spring3.redis.properties.Redisson3Properties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * 缓存自动配置类
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-01
 */
@AutoConfiguration
@EnableConfigurationProperties({ Redisson3Properties.class })
public class CacheManagerAutoConfiguration {

	/**
	 * 自定义 CacheManager 缓存管理器
	 * @return CacheManager
	 */
	@Bean
	public CacheManager cacheManager(final Redisson3Properties redisson3Properties) {
		if (CacheManagerType.redis.equals(redisson3Properties.getCacheType())) {
			// 使用自定义 RedissonSpringCacheManager 缓存管理器
			return new CustomizerRedissonSpringCacheManager();
		}
		else if (CacheManagerType.caffeine.equals(redisson3Properties.getCacheType())) {
			// 使用自定义 CaffeineCacheManager 缓存管理器
            return new CustomizerCaffeineCacheManager();
		}
		return new ConcurrentMapCacheManager();
	}

}

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
package io.github.panxiaochao.spring3.redis.properties;

import io.github.panxiaochao.spring3.redis.constants.CacheManagerType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * <p>
 * Redisson 自定义属性
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-27
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.redis", ignoreInvalidFields = true)
public class Redisson3Properties {

	/**
	 * redis 缓存 key 前缀
	 */
	private String keyPrefix;

	/**
	 * 缓存类型: redis（默认）、caffeine
	 */
	private CacheManagerType cacheType = CacheManagerType.redis;

}

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
package io.github.panxiaochao.spring3.redis.mapper;

import io.github.panxiaochao.spring3.core.utils.StringPools;
import org.redisson.api.NameMapper;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Redis 缓存 key 自定义前缀
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-27
 */
public class KeyPrefixNameMapper implements NameMapper {

	private final String keyPrefix;

	/**
	 * 前缀 默认为空字符串
	 */
	public KeyPrefixNameMapper() {
		this.keyPrefix = StringPools.EMPTY;
	}

	/**
	 * 前缀 判断
	 * @param keyPrefix 前缀字符串
	 */
	public KeyPrefixNameMapper(String keyPrefix) {
		this.keyPrefix = StringUtils.hasText(keyPrefix) ? keyPrefix + StringPools.COLON : StringPools.EMPTY;
	}

	/**
	 * Applies map function to input <code>name</code>
	 * @param name - original Redisson object name
	 * @return mapped name
	 */
	@Override
	public String map(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		if (StringUtils.hasText(keyPrefix) && !name.startsWith(keyPrefix)) {
			return keyPrefix + name;
		}
		return name;
	}

	/**
	 * Applies unmap function to input mapped <code>name</code> to get original name.
	 * @param name - mapped name
	 * @return original Redisson object name
	 */
	@Override
	public String unmap(String name) {
		if (!StringUtils.hasText(name)) {
			return null;
		}
		if (StringUtils.hasText(keyPrefix) && name.startsWith(keyPrefix)) {
			return name.substring(keyPrefix.length());
		}
		return name;
	}

}

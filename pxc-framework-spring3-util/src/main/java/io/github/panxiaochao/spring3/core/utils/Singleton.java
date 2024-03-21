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

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 * 静态工具类
 * </p>
 *
 * @author Lypxc
 * @since 2022/8/25
 */
public enum Singleton {

	/**
	 * Inst singleton.
	 */
	INST;

	/**
	 * The Singles.
	 */
	private static final Map<String, Object> SINGLES = new ConcurrentHashMap<>();

	/**
	 * Single.
	 * @param key the String key
	 * @param o the o
	 */
	public void single(final String key, final Object o) {
		SINGLES.put(key, o);
	}

	/**
	 * Get t.
	 * @param <T> the type parameter
	 * @param key String key
	 * @return the t
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(final String key) {
		return (T) SINGLES.get(key);
	}

    /**
     * 存储数量
     *
     * @return size
     */
    public int count() {
        return SINGLES.size();
    }

}

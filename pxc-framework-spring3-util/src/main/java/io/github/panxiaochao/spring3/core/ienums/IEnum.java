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
package io.github.panxiaochao.spring3.core.ienums;

/**
 * <p>
 * 枚举基类
 * </p>
 *
 * @author LyPxc
 * @since 2023-03-13
 */
public interface IEnum<T> {

	/**
	 * 码值
	 * @return T类型
	 */
	T getCode();

	/**
	 * 码值对应描述
	 * @return 返回信息
	 */
	String getMessage();

	/**
	 * 根据code获取描述
	 * @param code 码值
	 * @return 返回信息
	 */
	default String ofCode(T code) {
        return "";
	}

}

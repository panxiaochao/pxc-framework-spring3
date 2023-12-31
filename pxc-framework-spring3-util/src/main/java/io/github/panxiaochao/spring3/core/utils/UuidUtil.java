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

import java.util.UUID;

/**
 * <p>
 * UUID 生成.
 * </p>
 *
 * @author Lypxc
 * @since 2023-03-16
 */
public class UuidUtil {

	/**
	 * 获取原生UUID
	 * @return return UUID
	 */
	public static String getUUID() {
		return UUID.randomUUID().toString();
	}

	/**
	 * 获取原生UUID，去除-的简化UUID
	 * @return return simple UUID
	 */
	public static String getSimpleUUID() {
		return getUUID().replaceAll("-", "");
	}

}

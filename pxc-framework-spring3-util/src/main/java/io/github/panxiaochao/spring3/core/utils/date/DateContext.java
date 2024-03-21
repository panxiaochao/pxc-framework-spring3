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
package io.github.panxiaochao.spring3.core.utils.date;

import java.text.SimpleDateFormat;

/**
 * <p>
 * new Date() 上下文，解决SimpleDateFormat多线程安全问题.
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/12
 */
public class DateContext {

	private static final ThreadLocal<SimpleDateFormat> THREAD_LOCAL = new ThreadLocal<>();

	/**
	 * 设置数据
	 * @param simpleDateFormat 参数
	 */
	public static void set(SimpleDateFormat simpleDateFormat) {
		// set之前先remove，以免内存泄露，重复数据
		remove();
		THREAD_LOCAL.set(simpleDateFormat);
	}

	/**
	 * 获取当前数据
	 * @return String
	 */
	public static SimpleDateFormat get() {
		return THREAD_LOCAL.get();
	}

	/**
	 * 移除当前线程数据
	 */
	public static void remove() {
		THREAD_LOCAL.remove();
	}

}

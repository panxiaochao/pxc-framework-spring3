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
package io.github.panxiaochao.spring3.operate.log.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * <p>
 * 方法时间耗时上下文，使用 alibaba ttl 工具
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
public final class MethodCostContext {

	/**
	 * 存储毫秒
	 */
	private static final TransmittableThreadLocal<Long> METHOD_COST_TIME_LOCAL = new TransmittableThreadLocal<>();

	public static void setMethodCostTime(long costTime) {
		METHOD_COST_TIME_LOCAL.set(costTime);
	}

	/**
	 * 获取时间毫秒数
	 */
	public static long getMethodCostTime() {
		return METHOD_COST_TIME_LOCAL.get();
	}

	public static void removeMethodCostTime() {
		METHOD_COST_TIME_LOCAL.remove();
	}

}

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
package io.github.panxiaochao.spring3.core.utils.sysinfo;

import io.github.panxiaochao.spring3.core.utils.ArithmeticUtil;
import lombok.Setter;

/**
 * <p>
 * Mem Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Setter
public class Mem {

	/**
	 * 内存总量
	 */
	private double total;

	/**
	 * 已用内存
	 */
	private double used;

	/**
	 * 剩余内存
	 */
	private double free;

	public double getTotal() {
		return ArithmeticUtil.div(total, (1024 * 1024 * 1024), 2);
	}

	public double getUsed() {
		return ArithmeticUtil.div(used, (1024 * 1024 * 1024), 2);
	}

	public double getFree() {
		return ArithmeticUtil.div(free, (1024 * 1024 * 1024), 2);
	}

	public double getUsage() {
		return ArithmeticUtil.mul(ArithmeticUtil.div(used, total, 4), 100);
	}

}

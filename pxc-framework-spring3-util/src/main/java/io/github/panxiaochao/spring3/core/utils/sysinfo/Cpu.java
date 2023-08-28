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
import lombok.ToString;

/**
 * <p>
 * Cpu Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Setter
@ToString
public class Cpu {

	/**
	 * 核心数
	 */
	private int cpuNum;

	/**
	 * CPU总的使用率
	 */
	private double total;

	/**
	 * CPU系统使用率
	 */
	private double sys;

	/**
	 * CPU用户使用率
	 */
	private double used;

	/**
	 * CPU当前等待率
	 */
	private double wait;

	/**
	 * CPU当前空闲率
	 */
	private double free;

	public int getCpuNum() {
		return cpuNum;
	}

	public double getTotal() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(total, 100), 2);
	}

	public double getSys() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(sys / total, 100), 2);
	}

	public double getUsed() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(used / total, 100), 2);
	}

	public double getWait() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(wait / total, 100), 2);
	}

	public double getFree() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(free / total, 100), 2);
	}

}

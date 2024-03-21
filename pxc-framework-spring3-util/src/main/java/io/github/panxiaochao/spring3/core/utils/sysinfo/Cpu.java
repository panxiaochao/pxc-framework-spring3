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
package io.github.panxiaochao.spring3.core.utils.sysinfo;

import io.github.panxiaochao.spring3.core.utils.ArithmeticUtil;
import lombok.Getter;
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
@Getter
@ToString
public class Cpu {

	/**
     * CPU名称
     */
    private String cpuName;

    /**
     * 物理CPU数
	 */
    private int physicalPackageCount;

    /**
     * 物理CPU核心数
     */
    private int physicalProcessorCount;

    /**
     * 能效核心数
     */
    private int efficiencyCount;

    /**
     * 逻辑处理核心数
     */
    private int logicalProcessorCount;

    /**
     * 供应商
     */
    private String vendor;

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
	private double user;

	/**
	 * CPU当前等待率
	 */
	private double wait;

	/**
	 * CPU当前空闲率
	 */
    private double free;

    /**
     * CPU使用率：100 - 当前空闲
     */
    private double usage;

    // public double getTotal() {
    // return ArithmeticUtil.round(ArithmeticUtil.mul(total, 100), 2);
	// }

	public double getSys() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(sys / total, 100), 2);
    }

    public double getUser() {
        return ArithmeticUtil.round(ArithmeticUtil.mul(user / total, 100), 2);
	}

	public double getWait() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(wait / total, 100), 2);
	}

	public double getFree() {
		return ArithmeticUtil.round(ArithmeticUtil.mul(free / total, 100), 2);
    }

    public double getUsage() {
        return ArithmeticUtil.sub(100D, this.getFree());
	}

}

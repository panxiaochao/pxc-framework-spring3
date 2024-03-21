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
import io.github.panxiaochao.spring3.core.utils.unit.DataOfSize;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * Mem Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Setter
@ToString
public class Mem {

	/**
     * 内存总量, 单位MB
	 */
    private long total;

	/**
     * 已用内存, 单位MB
	 */
    private long used;

	/**
     * 剩余内存, 单位MB
	 */
    private long free;

    public long getTotal() {
        return DataOfSize.ofBytes(total).toMegabytes();
	}

    public long getUsed() {
        return DataOfSize.ofBytes(used).toMegabytes();
	}

    public long getFree() {
        return DataOfSize.ofBytes(free).toMegabytes();
	}

	public double getUsage() {
        return ArithmeticUtil.mul(ArithmeticUtil.div(String.valueOf(used), String.valueOf(total), 4), "100")
                .doubleValue();
	}

}

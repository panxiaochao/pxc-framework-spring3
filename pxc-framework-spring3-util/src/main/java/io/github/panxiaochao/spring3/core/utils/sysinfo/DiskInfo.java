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

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * <p>
 * DiskInfo Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Getter
@Setter
@ToString
public class DiskInfo {

	/**
	 * 盘符路径
	 */
	private String dirName;

	/**
	 * 盘符类型
	 */
	private String sysTypeName;

	/**
	 * 文件类型
	 */
	private String typeName;

	/**
	 * 总大小
	 */
    private double total;

	/**
	 * 剩余大小
	 */
    private double free;

	/**
	 * 已经使用量
	 */
    private double used;

	/**
	 * 资源的使用率
	 */
	private double usage;

}

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

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * ServerInfo Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Getter
@Setter
public class ServerInfo {

	/**
	 * CPU 相关信息
	 */
	private Cpu cpu = new Cpu();

	/**
	 * 內存 相关信息
	 */
	private Mem mem = new Mem();

	/**
	 * JVM 相关信息
	 */
	private Jvm jvm = new Jvm();

	/**
	 * 服务器 相关信息
	 */
	private SysInfo sys = new SysInfo();

	/**
     * 磁盘存储 相关信息
     */
    private DiskInfo diskInfo = new DiskInfo();

    /**
     * 磁盘文件 相关信息
	 */
	private List<DiskInfo> diskInfos = new LinkedList<>();

}

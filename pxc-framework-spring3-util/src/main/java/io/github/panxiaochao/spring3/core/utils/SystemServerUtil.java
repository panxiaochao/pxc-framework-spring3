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

import io.github.panxiaochao.spring3.core.utils.sysinfo.*;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.NetworkParams;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * <p>
 * 系统服务器相关信息(CPU, 内存, JVM, 硬盘等) 工具类.
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
public class SystemServerUtil {

	/**
	 * 单例
	 */
	private final static SystemServerUtil SYSTEM_SERVER_UTIL = new SystemServerUtil();

	/**
	 * SystemInfo 初始化
	 */
	private final SystemInfo systemInfo = new SystemInfo();

	private SystemServerUtil() {
	}

	/**
	 * @return SystemServerUtil
	 */
	public static SystemServerUtil INSTANCE() {
		return SYSTEM_SERVER_UTIL;
	}

	/**
	 * 获取系统服务器信息，包括CPU, 内存, JVM, 硬盘等等信息
	 * @return ServerInfo
	 */
	public ServerInfo getServerInfo() {
		ServerInfo serverInfo = new ServerInfo();
		serverInfo.setCpu(ofCpuInfo());
		serverInfo.setMem(ofMemInfo());
		serverInfo.setJvm(ofJvmInfo());
		serverInfo.setSys(ofSysInfo());
		serverInfo.setDiskInfos(ofDiskInfosInfo());
		return serverInfo;
	}

	/**
	 * 获取CPU信息
	 * @return Cpu
	 */
	public Cpu ofCpuInfo() {
		HardwareAbstractionLayer hal = systemInfo.getHardware();
		CentralProcessor processor = hal.getProcessor();
		// CPU信息
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		try {
			Thread.sleep(1000);
		}
		catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		long[] ticks = processor.getSystemCpuLoadTicks();
		long user = ticks[CentralProcessor.TickType.USER.getIndex()]
				- prevTicks[CentralProcessor.TickType.USER.getIndex()];
		long nice = ticks[CentralProcessor.TickType.NICE.getIndex()]
				- prevTicks[CentralProcessor.TickType.NICE.getIndex()];
		long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()]
				- prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
		long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()]
				- prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
		long ioWait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()]
				- prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
		long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()]
				- prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
		long softIrq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()]
				- prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
		long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()]
				- prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
		long totalCpu = user + nice + cSys + idle + ioWait + irq + softIrq + steal;
		Cpu cpu = new Cpu();
		cpu.setCpuNum(processor.getLogicalProcessorCount());
		cpu.setTotal(totalCpu);
		cpu.setSys(cSys);
		cpu.setUsed(user);
		cpu.setWait(ioWait);
		cpu.setFree(idle);
		return cpu;
	}

	/**
	 * 获取内存信息
	 * @return Mem
	 */
	public Mem ofMemInfo() {
		GlobalMemory memory = systemInfo.getHardware().getMemory();
		memory.getVirtualMemory();
		// 内存信息
		Mem mem = new Mem();
		mem.setTotal(memory.getTotal());
		mem.setUsed(memory.getTotal() - memory.getAvailable());
		mem.setFree(memory.getAvailable());
		return mem;
	}

	/**
	 * 获取JVM信息
	 * @return Jvm
	 */
	public Jvm ofJvmInfo() {
		Jvm jvm = new Jvm();
		Properties props = System.getProperties();
		jvm.setTotal(Runtime.getRuntime().totalMemory());
		jvm.setMax(Runtime.getRuntime().maxMemory());
		jvm.setFree(Runtime.getRuntime().freeMemory());
		jvm.setVersion(props.getProperty("java.version"));
		jvm.setHome(props.getProperty("java.home"));
		return jvm;
	}

	/**
	 * 获取系统信息
	 * @return SysInfo
	 */
	public SysInfo ofSysInfo() {
		OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
		NetworkParams networkParams = operatingSystem.getNetworkParams();
		SysInfo sys = new SysInfo();
		Properties props = System.getProperties();
		sys.setComputerName(networkParams.getHostName());
		sys.setComputerIp(IpUtil.getHostIp());
		sys.setDns(Arrays.toString(networkParams.getDnsServers()));
		sys.setGateway(networkParams.getIpv4DefaultGateway());
		sys.setOsName(props.getProperty("os.name"));
		sys.setOsArch(props.getProperty("os.arch"));
		sys.setUserDir(props.getProperty("user.dir"));
		return sys;
	}

	/**
	 * 获取文件存储信息
	 * @return DiskInfo
	 */
	public List<DiskInfo> ofDiskInfosInfo() {
		OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
		FileSystem fileSystem = operatingSystem.getFileSystem();
		List<OSFileStore> fsArray = fileSystem.getFileStores();
		List<DiskInfo> diskInfos = new ArrayList<>();
		for (OSFileStore fs : fsArray) {
			long free = fs.getUsableSpace();
			long total = fs.getTotalSpace();
			long used = total - free;
			DiskInfo diskInfo = new DiskInfo();
			diskInfo.setDirName(fs.getMount());
			diskInfo.setSysTypeName(fs.getType());
			diskInfo.setTypeName(fs.getName());
			diskInfo.setTotal(convertFileSize(total));
			diskInfo.setFree(convertFileSize(free));
			diskInfo.setUsed(convertFileSize(used));
			diskInfo.setUsage(ArithmeticUtil.mul(ArithmeticUtil.div(used, total, 4), 100));
			diskInfos.add(diskInfo);
		}
		return diskInfos;
	}

	/**
	 * 字节转换
	 * @param size 字节大小
	 * @return 转换后值
	 */
	private String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb) {
			return String.format("%.1f GB", (float) size / gb);
		}
		else if (size >= mb) {
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		}
		else if (size >= kb) {
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		}
		else {
			return String.format("%d B", size);
		}
	}

	// public static void main(String[] args) {
	// Properties props = System.getProperties();
	// //遍历所有的属性
	// for (String key : props.stringPropertyNames()) {
	// //输出对应的键和值
	// System.out.println(key + " = " + props.getProperty(key));
	// }
	// }

}

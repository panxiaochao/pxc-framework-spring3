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
import io.github.panxiaochao.spring3.core.utils.date.DateUtil;
import lombok.Setter;

import java.lang.management.ManagementFactory;
import java.util.Date;

/**
 * <p>
 * Jvm Entity
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-07
 */
@Setter
public class Jvm {

	/**
	 * 当前JVM占用的内存总数(M)
	 */
	private double total;

	/**
	 * JVM最大可用内存总数(M)
	 */
	private double max;

	/**
	 * JVM空闲内存(M)
	 */
	private double free;

	/**
	 * JDK版本
	 */
	private String version;

	/**
	 * JDK路径
	 */
	private String home;

	public double getTotal() {
		return ArithmeticUtil.div(total, (1024 * 1024), 2);
	}

	public double getMax() {
		return ArithmeticUtil.div(max, (1024 * 1024), 2);
	}

	public double getFree() {
		return ArithmeticUtil.div(free, (1024 * 1024), 2);
	}

	public double getUsed() {
		return ArithmeticUtil.div(total - free, (1024 * 1024), 2);
	}

	public double getUsage() {
		return ArithmeticUtil.mul(ArithmeticUtil.div(total - free, total, 4), 100);
	}

	/**
	 * 获取JDK名称
	 */
	public String getName() {
		return ManagementFactory.getRuntimeMXBean().getVmName();
	}

	public String getVersion() {
		return version;
	}

	public String getHome() {
		return home;
	}

	/**
	 * JDK启动时间
	 */
	public String getStartTime() {
		long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		return DateUtil.dateToString(new Date(startTime));
	}

	/**
	 * JDK运行时间
	 */
	public String getRunTime() {
		long startTime = ManagementFactory.getRuntimeMXBean().getStartTime();
		// 格式化输出
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		long nm = 1000 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = System.currentTimeMillis() - startTime;
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		// 计算差多少分钟
		long min = diff % nd % nh / nm;
		// 计算差多少秒//输出结果
		// long sec = diff % nd % nh % nm / ns;
		return day + "天" + hour + "小时" + min + "分钟";
	}

	/**
	 * 运行参数
	 */
	public String getInputArgs() {
		return ManagementFactory.getRuntimeMXBean().getInputArguments().toString();
	}

}

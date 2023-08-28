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

import java.io.Serializable;

/**
 * <p>
 * Twitter_Snowflake算法.
 * </p>
 * <p>
 * 分布式系统中，有一些需要使用全局唯一ID的场景，这种时候为了防止ID冲突可以使用36位的UUID，但是UUID有一些缺点，首先他相对比较长，另外UUID一般是无序的。
 * 有些时候我们希望能使用一种简单一些的ID，并且希望ID能够按照时间有序生成。
 * </p>
 * <p>
 * 而twitter的snowflake解决了这种需求，最初Twitter把存储系统从MySQL迁移到Cassandra，因为Cassandra没有顺序ID生成机制，所以开发了这样一套全局唯一ID生成服务。
 * </p>
 * <p>
 * SnowFlake的结构如下(每部分用-分开): <pre>
 * 0 - 0000000000 0000000000 0000000000 0000000000 0 - 00000 - 00000 - 000000000000
 * </pre> 1位标识，由于long基本类型在Java中是带符号的，最高位是符号位，正数是0，负数是1，所以id一般是正数，最高位是0
 * 41位时间截(毫秒级)，注意，41位时间截不是存储当前时间的时间截，而是存储时间截的差值（当前时间截 - 开始时间截)
 * 得到的值），这里的的开始时间截，一般是我们的id生成器开始使用的时间，由我们程序来指定的（如下下面程序IdWorker类的startTime属性）。41位的时间截，可以使用69年，年T
 * = (1L << 41) / (1000L * 60 * 60 * 24 * 365) = 69<br>
 * 10位的数据机器位，可以部署在1024个节点，包括5位datacenterId和5位workerId<br>
 * 12位序列，毫秒内的计数，12位的计数顺序号支持每个节点每毫秒(同一机器，同一时间截)产生4096个ID序号<br>
 * 加起来刚好64位，为一个Long型。<br>
 * </p>
 * <p>
 * datacenterId（分布式）（服务ID 1，2，3.....） 每个服务中写死 machineId（用于集群） 机器ID 读取机器的环境变量MACHINEID
 * 部署时每台服务器ID不一样
 * </p>
 *
 * @author Lypxc
 * @since 2022-12-22
 */
public class SnowFlakeUtil implements Serializable {

	private static final long serialVersionUID = -225070455103042973L;

	/**
	 * 开始时间截 (1990-01-01)
	 */
	private final long startTimestamp = 631123200000L;

	/**
	 * 机器id所占的位数
	 */
	private final long workerIdBits = 5L;

	/**
	 * 数据标识id所占的位数
	 */
	private final long datacenterIdBits = 5L;

	/**
	 * 序列在id中占的位数
	 */
	private final long sequenceBits = 12L;

	/**
	 * 支持的最大机器id，结果是0~31 (这个移位算法可以很快的计算出几位二进制数所能表示的最大十进制数)
	 */
	private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

	/**
	 * 支持的最大数据标识id，结果是0~31
	 */
	private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);

	/**
	 * 机器ID向左移12位
	 */
	private final long workerIdShift = sequenceBits;

	/**
	 * 数据标识id向左移17位(12+5)
	 */
	private final long dataCenterIdShift = sequenceBits + workerIdBits;

	/**
	 * 时间截向左移22位(5+5+12)
	 */
	private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;

	/**
	 * 生成序列的掩码，这里为4095 (0b111111111111=0xfff=4095)
	 */
	private final long sequenceMask = -1L ^ (-1L << sequenceBits);

	/**
	 * 工作机器ID(0~31)
	 */
	private long workerId;

	/**
	 * 数据中心ID(0~31)
	 */
	private long dataCenterId;

	/**
	 * 毫秒内序列(0~4095)
	 */
	private long sequence = 0L;

	/**
	 * 上次生成ID的时间截
	 */
	private long lastTimestamp = -1L;

	/**
	 * 单例
	 */
	private final static SnowFlakeUtil SNOW_FLAKE_UTILS = new SnowFlakeUtil();

	/**
	 * 不能私有构造
	 */
	private SnowFlakeUtil() {
		this.workerId = IpUtil.ipv4ToLong(IpUtil.getLocalhostStr()) & 31;
		this.dataCenterId = workerId > 30 ? 0 : workerId + 1;
	}

	/**
	 * @return SnowFlakeUtils
	 */
	public static SnowFlakeUtil INSTANCE() {
		return SNOW_FLAKE_UTILS;
	}

	/**
	 * 构造函数
	 * @param workerId 工作ID (0~31)
	 * @param datacenterId 数据中心ID (0~31)
	 */
	public SnowFlakeUtil(long workerId, long datacenterId) {
		if (workerId > maxWorkerId || workerId < 0) {
			throw new IllegalArgumentException(
					String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
		}
		if (datacenterId > maxDatacenterId || datacenterId < 0) {
			throw new IllegalArgumentException(
					String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
		}
		this.workerId = workerId;
		this.dataCenterId = datacenterId;
	}

	/**
	 * 根据Snowflake的ID，获取机器id
	 * @param id snowflake算法生成的id
	 * @return 所属机器的id
	 */
	public long getWorkerId(long id) {
		return id >> workerIdShift & ~(-1L << workerIdBits);
	}

	/**
	 * 根据Snowflake的ID，获取数据中心id
	 * @param id snowflake算法生成的id
	 * @return 所属数据中心
	 */
	public long getDataCenterId(long id) {
		return id >> dataCenterIdShift & ~(-1L << dataCenterIdShift);
	}

	/**
	 * 根据Snowflake的ID，获取生成时间
	 * @param id snowflake算法生成的id
	 * @return 生成的时间
	 */
	public long getGenerateDateTime(long id) {
		return (id >> timestampLeftShift & ~(-1L << 41L)) + startTimestamp;
	}

	/**
	 * 获得下一个ID (该方法是线程安全的)
	 * @return SnowflakeId
	 */
	public synchronized long nextId() {
		long timestamp = timeGen();

		// 如果当前时间小于上一次ID生成的时间戳，说明系统时钟回退过这个时候应当抛出异常
		if (timestamp < lastTimestamp) {
			throw new RuntimeException(String.format(
					"Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
		}

		// 如果是同一时间生成的，则进行毫秒内序列
		if (lastTimestamp == timestamp) {
			sequence = (sequence + 1) & sequenceMask;
			// 毫秒内序列溢出
			if (sequence == 0) {
				// 阻塞到下一个毫秒,获得新的时间戳
				timestamp = tilNextMillis(lastTimestamp);
			}
		}
		// 时间戳改变，毫秒内序列重置
		else {
			sequence = 0L;
		}

		// 上次生成ID的时间截
		lastTimestamp = timestamp;

		// 移位并通过或运算拼到一起组成64位的ID
		return ((timestamp - startTimestamp) << timestampLeftShift) | (dataCenterId << dataCenterIdShift)
				| (workerId << workerIdShift) | sequence;
	}

	/**
	 * 获得下一个ID（字符串形式）
	 * @return ID 字符串形式
	 */
	public String nextIdStr() {
		return Long.toString(nextId());
	}

	/**
	 * 阻塞到下一个毫秒，直到获得新的时间戳
	 * @param lastTimestamp 上次生成ID的时间截
	 * @return 当前时间戳
	 */
	protected long tilNextMillis(long lastTimestamp) {
		long timestamp = timeGen();
		while (timestamp == lastTimestamp) {
			timestamp = timeGen();
		}
		if (timestamp < lastTimestamp) {
			// 如果发现新的时间戳比上次记录的时间戳数值小，说明操作系统时间发生了倒退，报错
			throw new IllegalStateException(String.format("Clock moved backwards. Refusing to generate id for %dms",
					lastTimestamp - timestamp));
		}
		return timestamp;
	}

	/**
	 * 返回以毫秒为单位的当前时间
	 * @return 当前时间(毫秒)
	 */
	protected long timeGen() {
		return System.currentTimeMillis();
	}

}

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

import io.github.panxiaochao.spring3.core.utils.ipregion.Ip2RegionLoader;
import io.github.panxiaochao.spring3.core.utils.ipregion.IpInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

/**
 * <p>
 * Ip2region 工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-10
 */
public class Ip2regionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(Ip2regionUtil.class);

	/**
	 * ip 位置 搜索
	 * @param ip ip
	 * @return 位置
	 */
	public static IpInfo memorySearch(String ip) {
		try {
			// 1.ipv4
			String[] ipV4Part = IpInfo.getIpv4Part(ip);
			if (ipV4Part.length == 4) {
				IpInfo ipInfo = IpInfo.toIpInfo(Ip2RegionLoader.INSTANCE().getSearcher().search(ip));
				ipInfo.setIp(ip);
				return ipInfo;
			}
			else if (!ip.contains(":")) {
				// 2.非 ipv6
				LOGGER.error("invalid ipv6 address {}", ip);
				return null;
			}
			else {
				// 不是ipv4的情况下返回 null
				return null;
			}
		}
		catch (Exception e) {
			LOGGER.error("memorySearch ip {} is error", ip, e);
			return null;
		}
	}

	/**
	 * 读取 ipInfo 中的信息
	 * @param ip ip
	 * @param function Function
	 * @return 地址
	 */
	public static String getInfo(String ip, Function<IpInfo, String> function) {
		return IpInfo.readInfo(memorySearch(ip), function);
	}

}

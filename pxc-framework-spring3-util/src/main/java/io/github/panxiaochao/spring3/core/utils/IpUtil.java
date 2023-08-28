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

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.math.BigInteger;
import java.net.*;
import java.util.*;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * Ip获取工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-01-17
 */
public class IpUtil {

	private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);

	private final static String UNKNOWN = "unknown";

	private static String hostIp;

	/**
	 * Ipv4 和 Ipv6 本地地址
	 */
	private static final List<String> LOCAL_HOST_ARR = Arrays.asList("127.0.0.1", "0:0:0:0:0:0:0:1");

	private static String localhostName;

	/**
	 *
	 * <p>
	 * 获取客户端IP
	 * </p>
	 * <pre>
	 *     1、x-Original-Forwarded-For（k8s）
	 *     2、X-Forwarded-For
	 *     3、X-Real-IP
	 *     4、Proxy-Client-IP
	 *     5、WL-Proxy-Client-IP
	 *     6、HTTP_CLIENT_IP
	 *     7、HTTP_X_FORWARDED_FOR
	 * </pre>
	 * @return IP
	 */
	public static String ofRequestIp() {
		HttpServletRequest request = RequestUtil.getRequest();
		return null == request ? "" : ofRequestIp(request, ArrayUtil.EMPTY_STRING_ARRAY);
	}

	/**
	 *
	 * <p>
	 * 获取客户端IP
	 * </p>
	 * <pre>
	 *     1、x-Original-Forwarded-For（k8s）
	 *     2、X-Forwarded-For
	 *     3、X-Real-IP
	 *     4、Proxy-Client-IP
	 *     5、WL-Proxy-Client-IP
	 *     6、HTTP_CLIENT_IP
	 *     7、HTTP_X_FORWARDED_FOR
	 * </pre>
	 * @param request 请求对象
	 * @return IP
	 */
	public static String ofRequestIp(HttpServletRequest request) {
		return ofRequestIp(request, ArrayUtil.EMPTY_STRING_ARRAY);
	}

	/**
	 * <p>
	 * 获取客户端IP
	 * </p>
	 * @param request 请求对象
	 * @param headerNames 自定义头，通常在Http服务器（例如Nginx）中配置
	 * @return IP
	 */
	public static String ofRequestIp(HttpServletRequest request, String... headerNames) {
		String[] headers = { "x-Original-Forwarded-For", "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
				"WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(headerNames)) {
			headers = ArrayUtil.addAll(headers, headerNames);
		}
		String ip;
		for (String header : headers) {
			ip = request.getHeader(header);
			if (Boolean.FALSE.equals(isUnknown(ip))) {
				return checkLocalHost(getMultistageReverseProxyIp(ip));
			}
		}
		ip = request.getRemoteAddr();
		return checkLocalHost(getMultistageReverseProxyIp(ip));
	}

	/**
	 * 检查是否是本地地址
	 * @param ip ip
	 * @return ip
	 */
	private static String checkLocalHost(String ip) {
		if (LOCAL_HOST_ARR.contains(ip)) {
			// 根据网卡取本机配置的IP
			InetAddress iNet;
			try {
				iNet = InetAddress.getLocalHost();
				ip = iNet.getHostAddress();
			}
			catch (Exception e) {
				logger.error("checkLocalHost error", e);
			}
		}
		return ip;
	}

	/**
	 * 从多级反向代理中获得第一个非unknown IP地址
	 * @param ip 获得的IP地址
	 * @return 第一个非unknown IP地址
	 */
	private static String getMultistageReverseProxyIp(String ip) {
		// 多级反向代理检测
		if (ip != null && org.apache.commons.lang3.StringUtils.indexOf(ip, ',') > 0) {
			final String[] ips = org.apache.commons.lang3.StringUtils.split(ip, ',');
			for (final String subIp : ips) {
				if (Boolean.FALSE.equals(isUnknown(subIp))) {
					ip = subIp;
					break;
				}
			}
		}
		return ip;
	}

	/**
	 * 检测给定字符串是否为 unknown，多用于检测HTTP请求相关
	 * @param checkString 被检测的字符串
	 * @return 是否未知
	 */
	private static Boolean isUnknown(String checkString) {
		return StrUtil.isBlank(checkString) || UNKNOWN.equalsIgnoreCase(checkString);
	}

	public static String getHostIp() {
		if (StrUtil.isNotBlank(hostIp)) {
			return hostIp;
		}
		final InetAddress localhost = getLocalhost();
		if (null != localhost) {
			hostIp = localhost.getHostAddress();
			return hostIp;
		}
		return hostIp;
	}

	public static String getHostName() {
		if (StrUtil.isNotBlank(localhostName)) {
			return localhostName;
		}
		final InetAddress localhost = getLocalhost();
		if (null != localhost) {
			String name = localhost.getHostName();
			if (StrUtil.isEmpty(name)) {
				name = localhost.getHostAddress();
			}
			localhostName = name;
		}
		return localhostName;
	}

	public static String getLocalhostStr() {
		InetAddress localhost = getLocalhost();
		if (null != localhost) {
			return localhost.getHostAddress();
		}
		return null;
	}

	public static String getIpByHost(String hostName) {
		try {
			return InetAddress.getByName(hostName).getHostAddress();
		}
		catch (UnknownHostException e) {
			return hostName;
		}
	}

	public static LinkedHashSet<String> localIpv4s() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(t -> t instanceof Inet4Address);
		return toIpList(localAddressList);
	}

	public static LinkedHashSet<String> localIpv6s() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(t -> t instanceof Inet6Address);
		return toIpList(localAddressList);
	}

	public static LinkedHashSet<String> toIpList(Set<InetAddress> addressList) {
		final LinkedHashSet<String> ipSet = new LinkedHashSet<>();
		for (InetAddress address : addressList) {
			ipSet.add(address.getHostAddress());
		}
		return ipSet;
	}

	public static LinkedHashSet<String> localIps() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(null);
		return toIpList(localAddressList);
	}

	/**
	 * Ipv4 转换 十进制数
	 * @param ip ipv4
	 * @return 十进制Ip
	 */
	public static long ipv4ToLong(String ip) {
		return Ipv4Util.ipv4ToLong(ip);
	}

	/**
	 * 十进制 转换 String Ipv4
	 * @param longIp 十进制Ip
	 * @return String Ipv4
	 */
	public static String longToIpv4(long longIp) {
		return Ipv4Util.longToIpv4(longIp);
	}

	/**
	 * 将IPv6地址字符串转为大整数
	 * @param ipv6Str 字符串
	 * @return 大整数, 如发生异常返回 null
	 */
	private static BigInteger ipv6ToBigInteger(String ipv6Str) {
		try {
			InetAddress address = InetAddress.getByName(ipv6Str);
			if (address instanceof Inet6Address) {
				return new BigInteger(1, address.getAddress());
			}
		}
		catch (UnknownHostException ignore) {
			// skip
		}
		return null;
	}

	/**
	 * 将大整数转换成ipv6字符串
	 * @param bigInteger 大整数
	 * @return IPv6字符串, 如发生异常返回 null
	 */
	private static String bigIntegerToIpv6(BigInteger bigInteger) {
		try {
			return InetAddress.getByAddress(bigInteger.toByteArray()).toString().substring(1);
		}
		catch (UnknownHostException ignore) {
			// skip
		}
		return null;
	}

	/**
	 * 获取本机网卡IP地址，规则如下：
	 *
	 * <pre>
	 * 1. 查找所有网卡地址，必须非回路（loopback）地址、非局域网地址（siteLocal）、IPv4地址
	 * 2. 如果无满足要求的地址，调用 {@link InetAddress#getLocalHost()} 获取地址
	 * </pre>
	 * <p>
	 * 此方法不会抛出异常，获取失败将返回<code>null</code><br>
	 * <p>
	 * @return 本机网卡IP地址，获取失败返回<code>null</code>
	 */
	private static InetAddress getLocalhost() {
		final LinkedHashSet<InetAddress> localAddressList = localAddressList(address -> {
			// 非loopback地址，指127.*.*.*的地址
			return false == address.isLoopbackAddress()
					// 需为IPV4地址
					&& address instanceof Inet4Address;
		});
		if (localAddressList != null && localAddressList.size() > 0) {
			InetAddress address2 = null;
			for (InetAddress inetAddress : localAddressList) {
				if (false == inetAddress.isSiteLocalAddress()) {
					// 非地区本地地址，指10.0.0.0 ~ 10.255.255.255、172.16.0.0 ~
					// 172.31.255.255、192.168.0.0 ~ 192.168.255.255
					return inetAddress;
				}
				else if (null == address2) {
					address2 = inetAddress;
				}
			}

			if (null != address2) {
				return address2;
			}
		}

		try {
			return InetAddress.getLocalHost();
		}
		catch (UnknownHostException e) {
			// ignore
		}

		return null;
	}

	public static LinkedHashSet<InetAddress> localAddressList(Predicate<InetAddress> addressFilter) {
		return localAddressList(null, addressFilter);
	}

	private static LinkedHashSet<InetAddress> localAddressList(Predicate<NetworkInterface> networkInterfaceFilter,
			Predicate<InetAddress> addressFilter) {
		Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		}
		catch (SocketException e) {
			throw new RuntimeException(e);
		}

		if (networkInterfaces == null) {
			throw new RuntimeException("Get network interface error!");
		}

		final LinkedHashSet<InetAddress> ipSet = new LinkedHashSet<>();

		while (networkInterfaces.hasMoreElements()) {
			final NetworkInterface networkInterface = networkInterfaces.nextElement();
			if (networkInterfaceFilter != null && false == networkInterfaceFilter.test(networkInterface)) {
				continue;
			}
			final Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
			while (inetAddresses.hasMoreElements()) {
				final InetAddress inetAddress = inetAddresses.nextElement();
				if (inetAddress != null && (null == addressFilter || addressFilter.test(inetAddress))) {
					ipSet.add(inetAddress);
				}
			}
		}

		return ipSet;
	}

	/**
	 * 获得本机MAC地址
	 * @return 本机MAC地址
	 */
	private String getLocalMacAddress() {
		return getMacAddress(getLocalhost());
	}

	/**
	 * 获取本机所有网卡
	 * @return 所有网卡，异常返回{@code null}
	 */
	public static Collection<NetworkInterface> getNetworkInterfaces() {
		Enumeration<NetworkInterface> networkInterfaces;
		try {
			networkInterfaces = NetworkInterface.getNetworkInterfaces();
		}
		catch (SocketException e) {
			return null;
		}
		// 加入集合
		Collection<NetworkInterface> collection = new ArrayList<>();
		if (null != networkInterfaces) {
			while (networkInterfaces.hasMoreElements()) {
				collection.add(networkInterfaces.nextElement());
			}
		}
		return collection;
	}

	/**
	 * 获得指定地址信息中的MAC地址，使用分隔符“-”
	 * @param inetAddress {@link InetAddress}
	 * @return MAC地址，用-分隔
	 */
	private String getMacAddress(InetAddress inetAddress) {
		return getMacAddress(inetAddress, "-");
	}

	/**
	 * 获得指定地址信息中的MAC地址
	 * @param inetAddress {@link InetAddress}
	 * @param separator 分隔符，推荐使用“-”或者“:”
	 * @return MAC地址，用-分隔
	 */
	private String getMacAddress(InetAddress inetAddress, String separator) {
		if (null == inetAddress) {
			return null;
		}
		byte[] mac;
		try {
			mac = NetworkInterface.getByInetAddress(inetAddress).getHardwareAddress();
		}
		catch (SocketException e) {
			throw new RuntimeException(e);
		}
		if (null != mac) {
			final StringBuilder sb = new StringBuilder();
			String s;
			for (int i = 0; i < mac.length; i++) {
				if (i != 0) {
					sb.append(separator);
				}
				// 字节转换为整数
				s = Integer.toHexString(mac[i] & 0xFF);
				sb.append(s.length() == 1 ? 0 + s : s);
			}
			return sb.toString();
		}
		return null;
	}

	/**
	 * 检查远程端口是否开启
	 * @param address 远程地址
	 * @param timeout 检测超时
	 * @return 远程端口是否开启
	 * @since 5.3.2
	 */
	public static boolean isOpen(InetSocketAddress address, int timeout) {
		try (Socket sc = new Socket()) {
			sc.connect(address, timeout);
			return true;
		}
		catch (Exception e) {
			return false;
		}
	}

	private static String getLocalHostName() {
		if (StringUtils.hasText(localhostName)) {
			return localhostName;
		}
		final InetAddress localhost = getLocalhost();
		if (null != localhost) {
			String name = localhost.getHostName();
			if (!StringUtils.hasText(name)) {
				name = localhost.getHostAddress();
			}
			localhostName = name;
		}
		return localhostName;
	}

	/**
	 * 将 ip 转成 InetAddress
	 * @param ip ip
	 * @return InetAddress
	 */
	public static InetAddress getInetAddress(String ip) {
		try {
			return InetAddress.getByName(ip);
		}
		catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断是否内网 ip
	 * @param ip ip
	 * @return boolean
	 */
	public static boolean isInternalIp(String ip) {
		return isInternalIp(getInetAddress(ip));
	}

	/**
	 * 判断是否内网 ip
	 * @param address InetAddress
	 * @return boolean
	 */
	public static boolean isInternalIp(InetAddress address) {
		if (isLocalIp(address)) {
			return true;
		}
		return isInternalIp(address.getAddress());
	}

	/**
	 * 判断是否本地 ip
	 * @param address InetAddress
	 * @return boolean
	 */
	private static boolean isLocalIp(InetAddress address) {
		return address.isAnyLocalAddress() || address.isLoopbackAddress() || address.isSiteLocalAddress();
	}

	/**
	 * 判断是否内网 ip
	 * @param addr ip
	 * @return boolean
	 */
	private static boolean isInternalIp(byte[] addr) {
		final byte b0 = addr[0];
		final byte b1 = addr[1];
		// 10.x.x.x/8
		final byte section1 = 0x0A;
		// 172.16.x.x/12
		final byte section2 = (byte) 0xAC;
		final byte section3 = (byte) 0x10;
		final byte section4 = (byte) 0x1F;
		// 192.168.x.x/16
		final byte section5 = (byte) 0xC0;
		final byte section6 = (byte) 0xA8;
		switch (b0) {
			case section1:
				return true;
			case section2:
				if (b1 >= section3 && b1 <= section4) {
					return true;
				}
			case section5:
				if (b1 == section6) {
					return true;
				}
			default:
				return false;
		}
	}

	/**
	 * Ipv4 工具类
	 */
	static class Ipv4Util {

		/**
		 * 用来分组IPV4验证
		 */
		private static final String IPV4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)$";

		/**
		 * Pattern Ipv4
		 */
		private static final Pattern PATTERN_IPV4 = Pattern.compile(IPV4);

		/**
		 * 根据long值获取ipv4地址：xx.xx.xx.xx
		 * @param longIP IP的long表示形式
		 * @return IP V4 地址
		 */
		private static String longToIpv4(long longIP) {
			final StringBuilder sb = new StringBuilder();
			// 直接右移24位
			sb.append(longIP >> 24 & 0xFF);
			sb.append(StringPools.DOT);
			// 将高8位置0，然后右移16位
			sb.append(longIP >> 16 & 0xFF);
			sb.append(StringPools.DOT);
			sb.append(longIP >> 8 & 0xFF);
			sb.append(StringPools.DOT);
			sb.append(longIP & 0xFF);
			return sb.toString();
		}

		/**
		 * 根据ip地址计算出long型的数据
		 * @param strIp IP V4 地址
		 * @return long值
		 */
		private static long ipv4ToLong(String strIp) {
			final Matcher matcher = PATTERN_IPV4.matcher(strIp);
			if (matcher.matches()) {
				return matchIp(matcher);
			}
			throw new IllegalArgumentException("IPv4 is not invalid!");
		}

		/**
		 * 将匹配到的Ipv4地址的4个分组分别处理
		 * @param matcher 匹配到的Ipv4正则
		 * @return ipv4对应long
		 */
		private static long matchIp(Matcher matcher) {
			long addr = 0;
			for (int i = 1; i <= 4; ++i) {
				addr |= Long.parseLong(matcher.group(i)) << 8 * (4 - i);
			}
			return addr;
		}

	}

}

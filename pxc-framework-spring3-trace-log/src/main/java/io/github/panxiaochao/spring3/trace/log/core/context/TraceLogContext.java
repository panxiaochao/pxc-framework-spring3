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
package io.github.panxiaochao.spring3.trace.log.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 日志追踪链路上下文
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-14
 */
public class TraceLogContext {

	private static final TransmittableThreadLocal<String> TRACE_ID_LOCAL = new TransmittableThreadLocal<>();

	private static final TransmittableThreadLocal<String> SPAN_ID_LOCAL = new TransmittableThreadLocal<>();

	private static final TransmittableThreadLocal<AtomicInteger> SPAN_INDEX = new TransmittableThreadLocal<>();

	private static final TransmittableThreadLocal<String> HOST_IP = new TransmittableThreadLocal<>();

	private static final TransmittableThreadLocal<String> HOST_NAME = new TransmittableThreadLocal<>();

	private static final TransmittableThreadLocal<Map<String, String>> EXT_DATA = new TransmittableThreadLocal<>();

	public static void setTraceId(String traceId) {
		TRACE_ID_LOCAL.set(traceId);
	}

	public static String getTraceId() {
		return TRACE_ID_LOCAL.get();
	}

	public static void removeTraceId() {
		TRACE_ID_LOCAL.remove();
	}

	public static void setSpanId(String spanId) {
		if (!StringUtils.hasText(spanId)) {
			spanId = "0";
		}
		SPAN_ID_LOCAL.set(spanId);
		SPAN_INDEX.set(new AtomicInteger(TraceLogConstant.INITIAL_VALUE));
	}

	public static String getSpanId() {
		return SPAN_ID_LOCAL.get();
	}

	public static String generateNextSpanId() {
		String curSpanId = getSpanId();
		int curSpanIndex = SPAN_INDEX.get().incrementAndGet();
		return String.format("%s.%s", curSpanId, curSpanIndex);
	}

	public static void removeSpanId() {
		SPAN_ID_LOCAL.remove();
	}

	public static void removeSpanIndex() {
		SPAN_INDEX.remove();
	}

	public static void setHostIp(String hostIp) {
		HOST_IP.set(hostIp);
	}

	public static String getHostIp() {
		return HOST_IP.get();
	}

	public static void removeHostIp() {
		HOST_IP.remove();
	}

	public static void setHostName(String hostName) {
		HOST_NAME.set(hostName);
	}

	public static String getHostName() {
		return HOST_NAME.get();
	}

	public static void removeHostName() {
		HOST_NAME.remove();
	}

	public static void setExtData(Map<String, String> extData) {
		EXT_DATA.set(extData);
	}

	public static Map<String, String> getExtData() {
		return EXT_DATA.get();
	}

	public static void removeExtData() {
		EXT_DATA.remove();
	}

	/**
	 * 清除所有ThreadLocal数据
	 */
	public static void removeAll() {
		removeTraceId();
		removeSpanId();
		removeSpanIndex();
		removeHostIp();
		removeHostName();
		removeExtData();
	}

}

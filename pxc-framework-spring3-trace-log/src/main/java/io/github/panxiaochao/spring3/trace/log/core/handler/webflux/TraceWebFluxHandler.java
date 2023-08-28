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
package io.github.panxiaochao.spring3.trace.log.core.handler.webflux;

import io.github.panxiaochao.spring3.core.utils.IpUtil;
import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import io.github.panxiaochao.spring3.trace.log.core.context.TraceLogContext;
import io.github.panxiaochao.spring3.trace.log.core.domain.TraceLogDomain;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.server.reactive.ServerHttpRequest;

/**
 * <p>
 * WebFlux处理类
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-24
 */
public class TraceWebFluxHandler {

	/**
	 * volatile是为了保证内存可见性，防止编译器过度优化（指令重排序）
	 */
	private static volatile TraceWebFluxHandler traceWebFluxHandler = null;

	/**
	 * 饿汉模式，多线程安全
	 * @return 初始化实例
	 */
	public static TraceWebFluxHandler instance() {
		if (null == traceWebFluxHandler) {
			synchronized (TraceWebFluxHandler.class) {
				if (null == traceWebFluxHandler) {
					traceWebFluxHandler = new TraceWebFluxHandler();
				}
			}
		}
		return traceWebFluxHandler;
	}

	/**
	 * 处理前置追踪日志
	 * @param request request
	 */
	public ServerHttpRequest processBeforeTraceLog(ServerHttpRequest request) {
		// 日志标签语句
		// @formatter:off
		String labelLogLabel = TraceLogDomain
				.withServerHttpRequest(request).build()
				.formatTraceLogLabel();
		// @formatter:on
		// 置入MDC
		MDC.put(TraceLogConstant.MDC_KEY, labelLogLabel);
		// 添加Header
		if (StringUtils.isNotBlank(TraceLogContext.getTraceId())) {
			return request.mutate()
				.headers(httpHeader -> httpHeader.set(TraceLogConstant.TRACE_ID, TraceLogContext.getTraceId()))
				.headers(httpHeader -> httpHeader.set(TraceLogConstant.SPAN_ID, TraceLogContext.generateNextSpanId()))
				.headers(httpHeader -> httpHeader.set(TraceLogConstant.HOST_IP, TraceLogContext.getHostIp()))
				.headers(httpHeader -> httpHeader.set(TraceLogConstant.HOST_NAME, TraceLogContext.getHostName()))
				// 当前微服务属性，也是下游的上一节点属性
				.headers(httpHeader -> httpHeader.set(TraceLogConstant.PRE_APP, SpringContextUtil.getApplicationName()))
				.headers(httpHeader -> httpHeader.set(TraceLogConstant.PRE_HOST_IP, IpUtil.getHostIp()))
				.headers(httpHeader -> httpHeader.set(TraceLogConstant.PRE_HOST_NAME, IpUtil.getHostName()))
				.build();
		}
		return request;
	}

	/**
	 * 清除日志记录
	 */
	public void cleanTraceLogAll() {
		TraceLogContext.removeAll();
		MDC.remove(TraceLogConstant.MDC_KEY);
	}

}

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
package io.github.panxiaochao.spring3.trace.log.core.handler.mvc;

import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import io.github.panxiaochao.spring3.trace.log.core.context.TraceLogContext;
import io.github.panxiaochao.spring3.trace.log.core.domain.TraceLogDomain;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;

/**
 * <p>
 * WebMvc处理类
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-14
 */
public class TraceWebMvcHandler {

	/**
	 * volatile是为了保证内存可见性，防止编译器过度优化（指令重排序）
	 */
	private static volatile TraceWebMvcHandler traceWebMvcHandler = null;

	/**
	 * 饿汉模式，多线程安全
	 * @return 初始化实例
	 */
	public static TraceWebMvcHandler instance() {
		if (null == traceWebMvcHandler) {
			synchronized (TraceWebMvcHandler.class) {
				if (null == traceWebMvcHandler) {
					traceWebMvcHandler = new TraceWebMvcHandler();
				}
			}
		}
		return traceWebMvcHandler;
	}

	/**
	 * 处理前置追踪日志
	 * @param request request
	 */
	public void processBeforeTraceLog(HttpServletRequest request) {
		// 日志标签语句
		// @formatter:off
		String labelLogLabel = TraceLogDomain
				.withServletRequest(request).build()
				.formatTraceLogLabel();
		// @formatter:on
		// 置入MDC
		MDC.put(TraceLogConstant.MDC_KEY, labelLogLabel);
	}

	/**
	 * 清除日志记录
	 */
	public void cleanTraceLogAll() {
		TraceLogContext.removeAll();
		MDC.remove(TraceLogConstant.MDC_KEY);
	}

}

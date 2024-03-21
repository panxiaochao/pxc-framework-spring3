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
package io.github.panxiaochao.spring3.trace.log.core.interceptor.mvc;

import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import io.github.panxiaochao.spring3.trace.log.core.context.TraceLogContext;
import io.github.panxiaochao.spring3.trace.log.core.handler.mvc.TraceWebMvcHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.Nullable;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


/**
 * <p>
 * WebMvc拦截类
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-14
 */
public class TraceWebMvcInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if (handler instanceof HandlerMethod) {
			TraceWebMvcHandler.instance().processBeforeTraceLog(request);
			response.addHeader(TraceLogConstant.TRACE_ID, TraceLogContext.getTraceId());
		}
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
			@Nullable Exception ex) {
		TraceWebMvcHandler.instance().cleanTraceLogAll();
	}

}

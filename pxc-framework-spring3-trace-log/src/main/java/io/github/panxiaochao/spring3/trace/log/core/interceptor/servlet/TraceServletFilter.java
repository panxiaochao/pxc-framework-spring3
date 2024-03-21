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
package io.github.panxiaochao.spring3.trace.log.core.interceptor.servlet;

import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import io.github.panxiaochao.spring3.trace.log.core.context.TraceLogContext;
import io.github.panxiaochao.spring3.trace.log.core.handler.mvc.TraceWebMvcHandler;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * <p>
 * Servlet 拦截器
 * </p>
 *
 * @author Lypxc
 * @since 2024-01-03
 */
public class TraceServletFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {
            try {
                HttpServletRequest request = (HttpServletRequest) servletRequest;
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                TraceWebMvcHandler.instance().processBeforeTraceLog(request);
                response.addHeader(TraceLogConstant.TRACE_ID, TraceLogContext.getTraceId());
                chain.doFilter(request, response);
                return;
            } finally {
                TraceWebMvcHandler.instance().cleanTraceLogAll();
            }
        }
        chain.doFilter(servletRequest, servletResponse);
    }

}

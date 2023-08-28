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
package io.github.panxiaochao.spring3.web.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * <p>
 * RequestWrapper过滤器
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
public class RequestWrapperFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws ServletException, IOException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		String contentType = request.getContentType();
		// 判断请求类型
		if (!StringUtils.hasText(contentType)) {
			filterChain.doFilter(request, response);
		}
		else if (StringUtils.hasText(contentType) || contentType.contains("multipart/form-data")) {
			filterChain.doFilter(request, response);
		}
		else {
			// 重新包装 Request Wrapper
			request = new RequestWrapper(request);
			if (null == request) {
				filterChain.doFilter(servletRequest, response);
			}
			else {
				filterChain.doFilter(request, response);
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		Filter.super.init(filterConfig);
	}

	@Override
	public void destroy() {
		Filter.super.destroy();
	}

}

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
package io.github.panxiaochao.spring3.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * CorsFilter过滤器
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
public class CorsFilter extends OncePerRequestFilter {

	/**
	 * 当前跨域请求最大有效时长，同一个域名不会再进行检查，默认3600
	 */
	private static final String MAX_AGE = "3600";

	/**
	 * 允许请求的方法
	 */
	private static final List<String> ALLOWED_METHODS = Arrays.asList("OPTIONS", "HEAD", "GET", "PUT", "POST", "DELETE",
			"PATCH");

	@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
		// 解决跨域的问题
		cors(request, response);
		// 放行
		filterChain.doFilter(request, response);
	}

	private void cors(HttpServletRequest request, HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Methods", String.join(",", ALLOWED_METHODS));
        response.setHeader("Access-Control-Allow-Headers", "*");
		response.setHeader("Access-Control-Max-Age", MAX_AGE);
	}

}

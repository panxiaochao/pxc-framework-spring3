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

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * <p>
 * Request相关请求工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-05-06
 */
public class RequestUtil {

	/**
	 * obtain HttpServletRequest
	 * @return HttpServletRequest
	 */
	public static HttpServletRequest getRequest() {
		return null == getRequestAttributes() ? null : getRequestAttributes().getRequest();
	}

	/**
	 * obtain HttpServletResponse
	 * @return HttpServletResponse
	 */
	public static HttpServletResponse getResponse() {
		return null == getRequestAttributes() ? null : getRequestAttributes().getResponse();
	}

	/**
	 * obtain ServerRequestAttributes
	 * @return ServletRequestAttributes
	 */
	public static ServletRequestAttributes getRequestAttributes() {
		RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
		return null == attributes ? null : (ServletRequestAttributes) attributes;
	}

	/**
	 * 获得所有请求参数
	 * @return Map
	 */
	public static Map<String, String[]> getParams() {
		if (null == getRequest()) {
			return MapUtil.newHashMap();
		}
		final Map<String, String[]> map = getRequest().getParameterMap();
		return Collections.unmodifiableMap(map);

	}

	/**
	 * 获得所有请求参数
	 * @param request 请求对象{@link HttpServletRequest}
	 * @return Map
	 */
	public static Map<String, String[]> getParams(HttpServletRequest request) {
		final Map<String, String[]> map = request.getParameterMap();
		return Collections.unmodifiableMap(map);
	}

	/**
	 * 获得所有请求参数
	 * @return Map
	 */
	public static Map<String, String> getParamMap() {
		Map<String, String> params = MapUtil.newHashMap();
		for (Map.Entry<String, String[]> entry : getParams().entrySet()) {
			params.put(entry.getKey(), String.join(StringPools.COMMA, entry.getValue()));
		}
		return params;
	}

	/**
	 * 获得所有请求参数
	 * @param request 请求对象{@link HttpServletRequest}
	 * @return Map
	 */
	public static Map<String, String> getParamMap(HttpServletRequest request) {
		Map<String, String> params = MapUtil.newHashMap();
		for (Map.Entry<String, String[]> entry : getParams(request).entrySet()) {
			params.put(entry.getKey(), String.join(StringPools.COMMA, entry.getValue()));
		}
		return params;
	}

	/**
	 * 一次性获取请求体String - 注意：调用该方法后，getParam方法将失效
	 * @param request request
	 * @return byte[]
	 */
	public static String getBodyString(ServletRequest request) {
		try {
			return IOUtils.toString(request.getInputStream(), StandardCharsets.UTF_8);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 一次性获取请求体byte[] - 注意：调用该方法后，getParam方法将失效
	 * @param request request
	 * @return byte[]
	 */
	public static byte[] getBodyBytes(ServletRequest request) {
		try {
			return IOUtils.toByteArray(request.getInputStream());
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}

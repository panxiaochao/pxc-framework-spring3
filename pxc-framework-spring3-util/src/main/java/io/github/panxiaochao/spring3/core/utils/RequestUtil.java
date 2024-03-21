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
package io.github.panxiaochao.spring3.core.utils;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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

    /**
     * 获取请求体 - 调用该方法后，getParam方法将失效
     *
     * @param request {@link ServletRequest}
     * @return 获得请求体
     */
    public static String getBody(ServletRequest request) {
        try (final BufferedReader reader = request.getReader()) {
            return IOUtils.toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取请求所有的头（header）信息
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return header值
     */
    public static Map<String, String> getHeaderMap(HttpServletRequest request) {
        final Map<String, String> headerMap = new HashMap<>();
        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            headerMap.put(name, request.getHeader(name));
        }
        return headerMap;
    }

    /**
     * 获取请求所有的头（header）信息
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return header值
     */
    public static Map<String, List<String>> getHeadersMap(final HttpServletRequest request) {
        final Map<String, List<String>> headerMap = new LinkedHashMap<>();
        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            headerMap.put(name, list(request.getHeaders(name)));
        }
        return headerMap;
    }

    /**
     * 新建一个List
     *
     * @param <T>        集合元素类型
     * @param enumration {@link Enumeration}
     * @return ArrayList对象
     */
    private static <T> List<T> list(Enumeration<T> enumration) {
        final List<T> list = new ArrayList<>();
        if (null != enumration) {
            while (enumration.hasMoreElements()) {
                list.add(enumration.nextElement());
            }
        }
        return list;
    }

    /**
     * 获取响应所有的头（header）信息
     *
     * @param response 响应对象{@link HttpServletResponse}
     * @return header值
     */
    public static Map<String, Collection<String>> getHeadersMap(final HttpServletResponse response) {
        final Map<String, Collection<String>> headerMap = new HashMap<>();
        final Collection<String> names = response.getHeaderNames();
        for (final String name : names) {
            headerMap.put(name, response.getHeaders(name));
        }
        return headerMap;
    }

    /**
     * 忽略大小写获得请求header中的信息
     *
     * @param request        请求对象{@link HttpServletRequest}
     * @param nameIgnoreCase 忽略大小写头信息的KEY
     * @return header值
     */
    public static String getHeaderIgnoreCase(HttpServletRequest request, String nameIgnoreCase) {
        final Enumeration<String> names = request.getHeaderNames();
        String name;
        while (names.hasMoreElements()) {
            name = names.nextElement();
            if (name != null && name.equalsIgnoreCase(nameIgnoreCase)) {
                return request.getHeader(name);
            }
        }
        return null;
    }

    /**
     * 是否为Multipart类型表单，此类型表单用于文件上传
     *
     * @param request 请求对象{@link HttpServletRequest}
     * @return 是否为Multipart类型表单，此类型表单用于文件上传
     */
    public static boolean isMultipart(HttpServletRequest request) {
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            return false;
        }
        String contentType = request.getContentType();
        if (StrUtil.isBlank(contentType)) {
            return false;
        }
        return contentType.toLowerCase().startsWith("multipart/");
    }

}

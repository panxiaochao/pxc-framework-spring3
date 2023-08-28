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
package io.github.panxiaochao.spring3.trace.log.core.domain;

import io.github.panxiaochao.spring3.core.utils.IpUtil;
import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import io.github.panxiaochao.spring3.core.utils.StringPools;
import io.github.panxiaochao.spring3.core.utils.UuidUtil;
import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import io.github.panxiaochao.spring3.trace.log.core.context.TraceLogContext;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;

/**
 * <p>
 * 日志追踪Builder类
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-15
 */
@Getter
public class TraceLogDomain implements Serializable {

	private static final long serialVersionUID = 5728436343648917967L;

	private Map<String, String> attributes = new HashMap<>();

	private TraceLogDomain() {
	}

	public TraceLogDomain(TraceLogDomainBuilder builder) {
		// traceId 如果没有取到TraceId，就重新生成一个
		if (StringUtils.isBlank(builder.getTraceId())) {
			builder.setTraceId(UuidUtil.getSimpleUUID());
		}
		TraceLogContext.setTraceId(builder.getTraceId());
		// spanId 如果为空，会放入初始值
		TraceLogContext.setSpanId(builder.getSpanId());
		// 本机IP
		TraceLogContext.setHostIp(builder.getHostIp());
		// 本机名
		TraceLogContext.setHostName(builder.getHostName());
		// 额外属性
		// TraceLogContext.setExtData(this.attributes);
	}

	/**
	 * 格式化生成打印日志标签
	 * @return 日志标签
	 */
	public String formatTraceLogLabel() {
		StringJoiner traceLogLabel = new StringJoiner(StringPools.COMMA, "[", "]");
		// 额外属性添加
		traceLogLabel.add(attributes.get(TraceLogConstant.PRE_APP));
		traceLogLabel.add(attributes.get(TraceLogConstant.PRE_HOST_IP));
		traceLogLabel.add(attributes.get(TraceLogConstant.PRE_HOST_NAME));
		traceLogLabel.add(SpringContextUtil.getApplicationName());
		traceLogLabel.add(TraceLogContext.getSpanId());
		traceLogLabel.add(TraceLogContext.getTraceId());
		traceLogLabel.add(TraceLogContext.getHostIp());
		traceLogLabel.add(TraceLogContext.getHostName());
		return traceLogLabel.toString();
	}

	/**
	 * WebMvc Builder构造
	 * @param request HttpServletRequest
	 * @return TraceLogDomainBuilder
	 */
	public static TraceLogDomainBuilder withServletRequest(HttpServletRequest request) {
		Assert.notNull(request, "request cannot be null");
		return new TraceLogDomainBuilder(request);
	}

	/**
	 * WebFlux Builder构造
	 * @param request ServerHttpRequest
	 * @return TraceLogDomainBuilder
	 */
	public static TraceLogDomainBuilder withServerHttpRequest(ServerHttpRequest request) {
		Assert.notNull(request, "request cannot be null");
		return new TraceLogDomainBuilder(request);
	}

	@Getter
	@Setter
	public static final class TraceLogDomainBuilder implements Serializable {

		private static final long serialVersionUID = 5728436343648917967L;

		/**
		 * 链路唯一ID
		 */
		private String traceId;

		/**
		 * 链路节点
		 */
		private String spanId;

		/**
		 * 当前ip
		 */
		private String hostIp;

		/**
		 * 当前HostName
		 */
		private String hostName;

		private final Map<String, String> attributes = new HashMap<>();

		/**
		 * Servlet Http Request
		 */
		private TraceLogDomainBuilder(HttpServletRequest request) {
			// 获取 RequestHead 信息
			this.traceId = request.getHeader(TraceLogConstant.TRACE_ID);
			this.spanId = request.getHeader(TraceLogConstant.SPAN_ID);
			this.hostIp = IpUtil.getHostIp();
			this.hostName = IpUtil.getHostName();
			// 额外属性
			attributes.put(TraceLogConstant.PRE_APP,
					getNotBlankHeaderName(request.getHeader(TraceLogConstant.PRE_APP)));
			attributes.put(TraceLogConstant.PRE_HOST_IP,
					getNotBlankHeaderName(request.getHeader(TraceLogConstant.PRE_HOST_IP)));
			attributes.put(TraceLogConstant.PRE_HOST_NAME,
					getNotBlankHeaderName(request.getHeader(TraceLogConstant.PRE_HOST_NAME)));
		}

		/**
		 * WebFlux ServerHttpRequest
		 */
		private TraceLogDomainBuilder(ServerHttpRequest request) {
			// 获取 RequestHead 信息
			HttpHeaders headers = request.getHeaders();
			this.traceId = getWebFluxHeaderName(headers, TraceLogConstant.TRACE_ID);
			this.spanId = getWebFluxHeaderName(headers, TraceLogConstant.SPAN_ID);
			this.hostIp = IpUtil.getHostIp();
			this.hostName = IpUtil.getHostName();
			// 额外属性
			attributes.put(TraceLogConstant.PRE_APP,
					getNotBlankHeaderName(getWebFluxHeaderName(headers, TraceLogConstant.PRE_APP)));
			attributes.put(TraceLogConstant.PRE_HOST_IP,
					getNotBlankHeaderName(getWebFluxHeaderName(headers, TraceLogConstant.PRE_HOST_IP)));
			attributes.put(TraceLogConstant.PRE_HOST_NAME,
					getNotBlankHeaderName(getWebFluxHeaderName(headers, TraceLogConstant.PRE_HOST_NAME)));
		}

		private String getWebFluxHeaderName(HttpHeaders headers, String headerName) {
			List<String> traceIds = headers.get(headerName);
			if (!CollectionUtils.isEmpty(traceIds)) {
				return traceIds.get(0);
			}
			return null;
		}

		private String getNotBlankHeaderName(String headerName) {
			if (StringUtils.isBlank(headerName)) {
				return TraceLogConstant.UNKNOWN;
			}
			return headerName;
		}

		public TraceLogDomain build() {
			TraceLogDomain traceLogDomain = new TraceLogDomain(this);
			traceLogDomain.attributes = Collections.unmodifiableMap(this.attributes);
			return traceLogDomain;
		}

	}

}

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
package io.github.panxiaochao.spring3.trace.log.core.interceptor.feign;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import io.github.panxiaochao.spring3.core.utils.IpUtil;
import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import io.github.panxiaochao.spring3.trace.log.core.context.TraceLogContext;
import org.springframework.util.StringUtils;

/**
 * <p>
 * OpenFeign 拦截器
 * </p>
 *
 * @author Lypxc
 * @since 2023-09-11
 */
public class TraceFeignInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        String traceId = TraceLogContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            requestTemplate.header(TraceLogConstant.TRACE_ID, traceId);
            requestTemplate.header(TraceLogConstant.SPAN_ID, TraceLogContext.generateNextSpanId());
            requestTemplate.header(TraceLogConstant.PRE_APP, SpringContextUtil.getApplicationName());
            requestTemplate.header(TraceLogConstant.PRE_HOST_IP, IpUtil.getHostIp());
            requestTemplate.header(TraceLogConstant.PRE_HOST_NAME, IpUtil.getHostName());
        }
    }

}

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
package io.github.panxiaochao.spring3.trace.log.core.interceptor.resttemplate;

import io.github.panxiaochao.spring3.core.utils.IpUtil;
import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import io.github.panxiaochao.spring3.trace.log.constants.TraceLogConstant;
import io.github.panxiaochao.spring3.trace.log.core.context.TraceLogContext;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.util.StringUtils;

import java.io.IOException;

/**
 * <p>
 * RestTemplate 拦截器
 * </p>
 * <p>
 * 可以通过代码注入：
 * </p>
 * <pre>restTemplate.getInterceptors().add(new TraceRestTemplateInterceptor());</pre>
 *
 * @author Lypxc
 * @since 2023-09-11
 */
public class TraceRestTemplateInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
            throws IOException {
        String traceId = TraceLogContext.getTraceId();
        if (StringUtils.hasText(traceId)) {
            request.getHeaders().add(TraceLogConstant.TRACE_ID, traceId);
            request.getHeaders().add(TraceLogConstant.SPAN_ID, TraceLogContext.generateNextSpanId());
            request.getHeaders().add(TraceLogConstant.PRE_APP, SpringContextUtil.getApplicationName());
            request.getHeaders().add(TraceLogConstant.PRE_HOST_NAME, IpUtil.getHostName());
            request.getHeaders().add(TraceLogConstant.PRE_HOST_IP, IpUtil.getHostIp());
        }
        return execution.execute(request, body);
    }

}

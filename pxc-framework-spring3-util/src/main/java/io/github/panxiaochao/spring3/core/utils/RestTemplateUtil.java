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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * RestTemplateUtil 静态工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-05-06
 */
public class RestTemplateUtil {

    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    static {
        List<HttpMessageConverter<?>> httpMessageConverters = REST_TEMPLATE.getMessageConverters();
        httpMessageConverters.forEach(httpMessageConverter -> {
            if (httpMessageConverter instanceof StringHttpMessageConverter messageConverter) {
                // 解决乱码问题
                messageConverter.setDefaultCharset(StandardCharsets.UTF_8);
            }
        });
        // 加入拦截器
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(new LoggingClientHttpRequestInterceptor());
        REST_TEMPLATE.setInterceptors(interceptors);
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(10 * 1000);
        factory.setConnectTimeout(10 * 1000);
        // 是否使用缓存流, 使用大文件传输影响性能
        factory.setBufferRequestBody(false);
        REST_TEMPLATE.setRequestFactory(new BufferingClientHttpRequestFactory(factory));
    }

    /**
     * 静态获取RestTemplate实例对象，可自由调用其方法
     *
     * @return RestTemplate实例对象
     */
    public static RestTemplate getRestTemplate() {
        return REST_TEMPLATE;
    }

    /**
     * 请求日志拦截
     */
    static class LoggingClientHttpRequestInterceptor implements ClientHttpRequestInterceptor {

        private final Logger log = LoggerFactory.getLogger(getClass());

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
                throws IOException {
            // 跟踪请求
            traceRequest(request, body);
            ClientHttpResponse response = execution.execute(request, body);
            // 跟踪返回结果
            traceResponse(response);
            return response;
        }

        private void traceRequest(HttpRequest request, byte[] body) throws IOException {
            log.info("===========================request begin================================================");
            log.info("URI         : {}", request.getURI());
            log.info("Method      : {}", request.getMethod());
            log.info("Headers     : {}", request.getHeaders());
            log.info("Request body: {}", new String(body, StandardCharsets.UTF_8));
            log.info("==========================request end================================================");
        }

        private void traceResponse(ClientHttpResponse response) throws IOException {
            String responseBody = FileCopyUtils
                    .copyToString(new InputStreamReader(response.getBody(), StandardCharsets.UTF_8));
            // StringBuilder inputStringBuilder = new StringBuilder();
            // try (BufferedReader bufferedReader = new BufferedReader(new
            // InputStreamReader(response.getBody(), StandardCharsets.UTF_8))) {
            // String line = bufferedReader.readLine();
            // while (line != null) {
            // inputStringBuilder.append(line);
            // inputStringBuilder.append('\n');
            // line = bufferedReader.readLine();
            // }
            // }
            log.info("============================ response begin ============================");
            log.info("Status code  : {}", response.getStatusCode());
            log.info("Status text  : {}", response.getStatusText());
            log.info("Headers      : {}", response.getHeaders());
            log.info("Response body: {}", responseBody);
            // WARNING:
            // comment
            // out
            // in
            // production
            // to
            // improve
            // performance
            log.info("============================ response end ============================");
        }

    }

    public static void main(String[] args) throws IOException {
        String url = "https://wx.hzwindow.com.cn/hzwechat/hzwx/applet/page/img/image";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        // 接口参数
        MultiValueMap<String, Object> multiValueMap = new LinkedMultiValueMap<>();
        multiValueMap.add("token", "b5b3hc8e008dba62a449fdc1977");
        // 处理文件
        FileSystemResource fileSystemResource = new FileSystemResource(new File("/Users/Lypxc/Desktop/1.jpg"));
        multiValueMap.add("file", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(multiValueMap, headers);
        REST_TEMPLATE.postForEntity(url, httpEntity, String.class);
    }

}

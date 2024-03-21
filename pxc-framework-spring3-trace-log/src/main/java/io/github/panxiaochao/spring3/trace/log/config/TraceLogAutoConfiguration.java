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
package io.github.panxiaochao.spring3.trace.log.config;

import io.github.panxiaochao.spring3.trace.log.core.interceptor.feign.TraceFeignInterceptor;
import io.github.panxiaochao.spring3.trace.log.core.interceptor.mvc.TraceWebMvcInterceptor;
import io.github.panxiaochao.spring3.trace.log.core.interceptor.scg.TraceGatewayGlobalFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * <p>
 * 日志追踪链路 配置类
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-11
 */
@AutoConfiguration
public class TraceLogAutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(TraceLogAutoConfiguration.class);

	/**
	 * WebMvc Trace Log
	 */
	@Configuration
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
	@ConditionalOnClass(name = {
			//
			"org.springframework.web.servlet.config.annotation.WebMvcConfigurer",
			"org.springframework.boot.web.servlet.FilterRegistrationBean" //
	})
	static class TraceLogWebMvcConfigurer implements WebMvcConfigurer {

		@Override
		public void addInterceptors(InterceptorRegistry registry) {
			LOGGER.info("配置[TraceLog-WebMvc]成功！");
			registry.addInterceptor(new TraceWebMvcInterceptor())
				.addPathPatterns("/**")
				.order(Ordered.HIGHEST_PRECEDENCE);
		}

	}

	/**
	 * GateWay Trace Log
	 */
	@Configuration
	@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.REACTIVE)
	@ConditionalOnClass(name = { "org.springframework.cloud.gateway.filter.GlobalFilter" })
	static class TraceLogGatewayConfiguration {

		@Bean
		@Order(Ordered.HIGHEST_PRECEDENCE)
		public TraceGatewayGlobalFilter tracerScgGlobalFilter() {
			LOGGER.info("配置[TraceLog-Gateway]成功！");
			return new TraceGatewayGlobalFilter();
		}

	}

    /**
     * Feign Trace Log
     */
    @Configuration
    @ConditionalOnClass(name = {"feign.RequestInterceptor"})
    static class TraceLogFeignConfiguration {

        @Bean
        @Order(Ordered.HIGHEST_PRECEDENCE)
        public TraceFeignInterceptor traceFeignInterceptor() {
            LOGGER.info("配置[TraceLog-Feign]成功！");
            return new TraceFeignInterceptor();
        }

    }

}

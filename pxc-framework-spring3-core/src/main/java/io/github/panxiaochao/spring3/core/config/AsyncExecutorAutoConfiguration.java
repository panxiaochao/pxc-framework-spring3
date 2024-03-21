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
package io.github.panxiaochao.spring3.core.config;

import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.lang.Nullable;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.Executor;

/**
 * <p>
 * 异步线程池 自动配置
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-06
 */
@AutoConfiguration
@EnableAsync(proxyTargetClass = true)
@ConditionalOnProperty(name = "spring.pxc-framework.async", havingValue = "true")
public class AsyncExecutorAutoConfiguration implements AsyncConfigurer {

	private final static Logger LOGGER = LoggerFactory.getLogger(AsyncExecutorAutoConfiguration.class);

	@Nullable
	@Override
	public Executor getAsyncExecutor() {
		LOGGER.info("配置[AsyncExecutor]成功！");
		return SpringContextUtil.getBean("threadPoolTaskExecutor");
	}

	@Nullable
	@Override
	public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
		return (throwable, method, objects) -> {
			throwable.printStackTrace();
			StringBuilder sb = new StringBuilder();
			sb.append("Exception message - ")
				.append(throwable.getMessage())
				.append(", Method name - ")
				.append(method.getName());
			if (objects.getClass().isArray() && Objects.nonNull(objects)) {
				sb.append(", Parameter value - ").append(Arrays.toString(objects));
			}
			LOGGER.error(sb.toString());
		};
	}

}

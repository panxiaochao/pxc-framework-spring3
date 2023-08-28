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
package io.github.panxiaochao.spring3.repeatsubmit.config;

import io.github.panxiaochao.spring3.repeatsubmit.aspect.RepeatSubmitLimiterAspect;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConfiguration;

/**
 * <p>
 * RepeatSubmitLimiter 自动配置类
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-28
 */
@AutoConfiguration(after = RedisConfiguration.class)
public class RepeatSubmitLimiterAutoConfiguration {

	@Bean
	public RepeatSubmitLimiterAspect repeatSubmitLimiterAspect() {
		return new RepeatSubmitLimiterAspect();
	}

}
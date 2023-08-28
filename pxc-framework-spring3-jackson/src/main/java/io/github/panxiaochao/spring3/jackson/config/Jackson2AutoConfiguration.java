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
package io.github.panxiaochao.spring3.jackson.config;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.panxiaochao.spring3.core.utils.date.DatePattern;
import io.github.panxiaochao.spring3.core.utils.jackson.CustomizeJavaTimeModule;
import io.github.panxiaochao.spring3.core.utils.jackson.jsonserializer.NullValueJsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

/**
 * <p>
 * Jackson2 自动化配置.
 * </p>
 * <pre>
 *     注册顺序：
 *     JacksonAutoConfiguration
 *     ObjectMapper
 *     Jackson2ObjectMapperBuilder
 *     Jackson2ObjectMapperBuilderCustomizer
 * </pre>
 *
 * <p>
 * 参考：<a href=
 * "https://codingnconcepts.com/spring-boot/customize-jackson-json-mapper/">customize-jackson-json-mapper</a><a></a>
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@AutoConfiguration(before = JacksonAutoConfiguration.class)
public class Jackson2AutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(Jackson2AutoConfiguration.class);

	/**
	 * <p>
	 * To override the default ObjectMapper (and XmlMapper).
	 * </p>
	 * <pre>
	 *     1.Jackson2ObjectMapperBuilderCustomizer 注册Bean
	 *     2.生成Bean Jackson2ObjectMapperBuilder
	 *     3.通过 Jackson2ObjectMapperBuilder 生成 ObjectMapper
	 * </pre>
	 * @return custom Jackson2ObjectMapperBuilderCustomizer
	 */
	@Bean
	public Jackson2ObjectMapperBuilderCustomizer jsonCustomizer() {
		LOGGER.info("配置[Jackson2ObjectMapper]成功！");
		return builder -> builder.locale(Locale.CHINA)
			// 所有字段全部展现
			.serializationInclusion(JsonInclude.Include.ALWAYS)
			.timeZone(TimeZone.getDefault())
			.dateFormat(new SimpleDateFormat(DatePattern.NORMAL_DATE_TIME_PATTERN))
			// 忽略空Bean转json的错误
			.failOnEmptyBeans(false)
			// 忽略未知属性
			.failOnUnknownProperties(false)
			// 自定义 NUll 处理
			.postConfigurer(objectMapper -> objectMapper.getSerializerProvider()
				.setNullValueSerializer(NullValueJsonSerializer.INSTANCE))
			// 时间格式化处理
			.modules(new CustomizeJavaTimeModule());
	}

}

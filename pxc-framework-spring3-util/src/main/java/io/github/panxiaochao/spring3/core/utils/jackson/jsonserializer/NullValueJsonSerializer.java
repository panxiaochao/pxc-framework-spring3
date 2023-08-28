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
package io.github.panxiaochao.spring3.core.utils.jackson.jsonserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import org.springframework.util.ReflectionUtils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * 针对空对象的处理.
 * </p>
 *
 * @author Lypxc
 * @since 2022/8/30
 */
@JacksonStdImpl
public class NullValueJsonSerializer extends JsonSerializer<Object> {

	private static final String EMPTY_STRING = "";

	public static final NullValueJsonSerializer INSTANCE = new NullValueJsonSerializer();

	@Override
	public void serialize(Object value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
		String fieldName = gen.getOutputContext().getCurrentName();
		// 反射获取字段
		Field field = ReflectionUtils.findField(gen.getCurrentValue().getClass(), fieldName);
		if (Objects.nonNull(field)) {
			// 数字类型Integer、Double、Long等返回""
			if (Number.class.isAssignableFrom(field.getType())) {
				gen.writeString(EMPTY_STRING);
				return;
			}
			// String类型返回""
			if (Objects.equals(field.getType(), String.class)) {
				gen.writeString(EMPTY_STRING);
				return;
			}
			// Boolean类型返回false
			if (Objects.equals(field.getType(), Boolean.class)) {
				gen.writeBoolean(false);
				return;
			}
			// List类型返回[]
			if (isArrayType(field.getType())) {
				gen.writeStartArray();
				gen.writeEndArray();
				return;
			}
			// Map类型返回{}
			if (isMapType(field.getType())) {
				gen.writeStartObject();
				gen.writeEndObject();
				return;
			}
		}
		// 其他Object默认返回""
		gen.writeString(EMPTY_STRING);
	}

	/**
	 * 是否是数组
	 * @param rawClass rawClass
	 * @return boolean
	 */
	private boolean isArrayType(Class<?> rawClass) {
		return rawClass.isArray() || Collection.class.isAssignableFrom(rawClass);
	}

	/**
	 * 是否是map
	 * @param rawClass rawClass
	 * @return boolean
	 */
	private boolean isMapType(Class<?> rawClass) {
		return Map.class.isAssignableFrom(rawClass);

	}

}

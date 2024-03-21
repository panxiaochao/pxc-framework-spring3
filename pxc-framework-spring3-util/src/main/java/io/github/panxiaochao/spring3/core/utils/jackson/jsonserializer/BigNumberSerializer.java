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
package io.github.panxiaochao.spring3.core.utils.jackson.jsonserializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.ser.std.NumberSerializer;

import java.io.IOException;

/**
 * <p>
 * 大精度转换String, 根据前端 JS Number.MAX_SAFE_INTEGER 与 Number.MIN_SAFE_INTEGER 百度得来
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@JacksonStdImpl
public class BigNumberSerializer extends NumberSerializer {

	private static final long serialVersionUID = 3143649332656435290L;

	/**
	 * 最大范围
	 */
	private static final long MAX_SAFE_INTEGER = 9007199254740991L;

	/**
	 * 最小范围
	 */
	private static final long MIN_SAFE_INTEGER = -9007199254740991L;

	/**
	 * 提供实例
	 */
	public static final BigNumberSerializer INSTANCE = new BigNumberSerializer(Number.class);

	public BigNumberSerializer(Class<? extends Number> rawType) {
		super(rawType);
	}

	@Override
	public void serialize(Number value, JsonGenerator gen, SerializerProvider provider) throws IOException {
		// MIN_SAFE_INTEGER < value < MAX_SAFE_INTEGER
		if (value.longValue() > MIN_SAFE_INTEGER && value.longValue() < MAX_SAFE_INTEGER) {
			super.serialize(value, gen, provider);
		}
		else {
			gen.writeString(value.toString());
		}
	}

}

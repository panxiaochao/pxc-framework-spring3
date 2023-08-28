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
package io.github.panxiaochao.spring3.core.enums;

import io.github.panxiaochao.spring3.core.ienums.IResponseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 通用枚举异常
 * </p>
 *
 * @author Lypxc
 * @since 2022/11/27
 */
@Getter
@AllArgsConstructor
public enum CommonResponseEnum implements IResponseEnum<Integer> {

	/**
	 * 成功, 兼容 {code: 0} 的情况
	 */
	OK_0(0, "成功！"),

	/**
	 * 成功
	 */
	OK(200, "成功！"),

	/**
	 * 错误请求
	 */
	BAD_REQUEST(400, "错误请求！"),

	/**
	 * 未授权
	 */
	UNAUTHORIZED(401, "未授权！"),

	/**
	 * 资源不存在
	 */
	NOT_FOUND(404, "资源不存在！"),

	/**
	 * 请求方式错误
	 */
	METHOD_NOT_ALLOWED(405, "请求方式错误！"),

	/**
	 * 服务器忙，请稍候重试
	 */
	INTERNAL_SERVER_ERROR(500, "服务器异常，请联系管理员！");

	private final Integer code;

	private final String message;

	public static final Map<Integer, String> MAP_VALUES = Arrays.stream(CommonResponseEnum.values())
		.collect(Collectors.toMap(CommonResponseEnum::getCode, CommonResponseEnum::getMessage));

	@Override
	public String ofCode(Integer code) {
		for (CommonResponseEnum value : CommonResponseEnum.values()) {
			if (value.getCode().equals(code)) {
				return value.getMessage();
			}
		}
		return "";
	}

}

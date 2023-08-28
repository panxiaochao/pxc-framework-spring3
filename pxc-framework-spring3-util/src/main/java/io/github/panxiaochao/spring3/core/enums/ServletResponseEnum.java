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

/**
 * <p>
 * 服务异常枚举, code以5000开始
 * </p>
 *
 * @author Lypxc
 * @since 2022/11/27
 */
@Getter
@AllArgsConstructor
public enum ServletResponseEnum implements IResponseEnum<Integer> {

	/**
	 * 5001 请求地址无效
	 */
	NoHandlerFoundException(5001, "请求地址无效！"),
	/**
	 * 5002 请求方法不支持
	 */
	HttpRequestMethodNotSupportedException(5002, "请求方法不支持！"),
	/**
	 * 5003 请求头不支持
	 */
	HttpMediaTypeNotSupportedException(5003, "请求头不支持！"),
	/**
	 * 5004 未检测到参数路径
	 */
	MissingPathVariableException(5004, "未检测到参数路径！"),
	/**
	 * 5005 请求方法不支持
	 */
	MissingServletRequestParameterException(5005, "请求方法不支持！"),
	/**
	 * 5006 参数类型匹配失败
	 */
	TypeMismatchException(5006, "参数类型匹配失败！"),
	/**
	 * 5007 无效接收参数@RequestBody, 序列化失败
	 */
	HttpMessageNotReadableException(5007, "无效接收参数@RequestBody, 序列化失败！"),
	/**
	 * 5008 返回序列化失败
	 */
	HttpMessageNotWritableException(5008, "返回序列化失败！"),
	/**
	 * 5009 找不到可接收MediaType
	 */
	HttpMediaTypeNotAcceptableException(5009, "找不到可接收MediaType！"),
	/**
	 * 5010 输入参数有误
	 */
	ServletRequestBindingException(5010, "输入参数有误！"),
	/**
	 * 5011 请求方法不支持
	 */
	ConversionNotSupportedException(5011, "请求方法不支持！"),
	/**
	 * 5012 输入参数有误
	 */
	MissingServletRequestPartException(5012, "输入参数有误！"),
	/**
	 * 5013 异步请求超时
	 */
	AsyncRequestTimeoutException(5013, "异步请求超时！"),
	/**
	 * 5014 无效参数
	 */
	InvalidArgumentException(5014, "无效参数！");

	private final Integer code;

	private final String message;

	@Override
	public String ofCode(Integer code) {
		for (ServletResponseEnum value : ServletResponseEnum.values()) {
			if (value.getCode().equals(code)) {
				return value.getMessage();
			}
		}
		return "";
	}

}

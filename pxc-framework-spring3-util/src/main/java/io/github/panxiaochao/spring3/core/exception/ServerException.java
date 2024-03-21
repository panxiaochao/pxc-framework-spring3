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
package io.github.panxiaochao.spring3.core.exception;

import io.github.panxiaochao.spring3.core.ienums.IEnum;
import lombok.Getter;

/**
 * <p>
 * 基础错误异常类
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/19
 */
@Getter
public class ServerException extends Exception {

	private static final long serialVersionUID = 9012390889969142663L;

	/**
	 * 错误码
	 */
	private final int code;

	public ServerException(IEnum<Integer> responseEnum) {
		super(responseEnum.getMessage());
		this.code = responseEnum.getCode();
	}

	public ServerException(IEnum<Integer> responseEnum, String message) {
		super(message);
		this.code = responseEnum.getCode();
	}

	public ServerException(IEnum<Integer> responseEnum, Throwable cause) {
		super(responseEnum.getMessage(), cause);
		this.code = responseEnum.getCode();
	}

	public ServerException(IEnum<Integer> responseEnum, String message, Throwable cause) {
		super(message, cause);
		this.code = responseEnum.getCode();
	}

}

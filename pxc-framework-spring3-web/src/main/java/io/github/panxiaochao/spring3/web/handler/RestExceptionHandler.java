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
package io.github.panxiaochao.spring3.web.handler;

import io.github.panxiaochao.spring3.core.enums.CommonResponseEnum;
import io.github.panxiaochao.spring3.core.exception.ServerException;
import io.github.panxiaochao.spring3.core.exception.ServerRuntimeException;
import io.github.panxiaochao.spring3.core.exception.ext.ApiServerException;
import io.github.panxiaochao.spring3.core.response.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>
 * 统一异常处理器类增强, 默认不拦截4xx错误, 比如(400,405,404)等
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@RestControllerAdvice
public class RestExceptionHandler {

	private static final Logger LOG = LoggerFactory.getLogger(RestExceptionHandler.class);

	/**
	 * 生产环境
	 */
	private final static String ENV_PROD = "prod";

	/**
	 * 当前环境
	 */
	@Value("${spring.profiles.active:}")
	private String profile;

	/**
	 * 常规兜底报错
	 * @param e Exception
	 * @return R
	 */
	@ExceptionHandler(value = Exception.class)
	public R<String> exception(Exception e) {
		LOG.error(e.getMessage(), e);
		if (ENV_PROD.equals(profile)) {
			return R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getMessage());
		}
		return R.fail(e.getMessage());
	}

	/**
	 * IllegalArgumentException 报错拦截
	 * @param e Exception
	 * @return R
	 */
	@ExceptionHandler(value = IllegalArgumentException.class)
	public R<String> illegalArgumentException(IllegalArgumentException e) {
		LOG.error(e.getMessage(), e);
		if (ENV_PROD.equals(profile)) {
			return R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getMessage());
		}
		return R.fail(e.getMessage());
	}

	/**
	 * 常规业务异常
	 * @param e 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = ApiServerException.class)
	public R<String> handleBusinessException(ServerException e) {
		LOG.error(e.getMessage(), e);
		return R.fail(e.getMessage());
	}

	/**
	 * 自定义异常 BaseException
	 * @param e 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = ServerException.class)
	public R<String> handleBaseException(ServerException e) {
		LOG.error(e.getMessage(), e);
		return R.fail(e.getCode(), e.getMessage());
	}

	/**
	 * 自定义异常 BaseRuntimeException
	 * @param e 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = ServerRuntimeException.class)
	public R<String> handleBaseException(ServerRuntimeException e) {
		LOG.error(e.getMessage(), e);
		return R.fail(e.getCode(), e.getMessage());
	}

	/**
	 * 参数绑定异常
	 * @param e 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = BindException.class)
	public R<String> handleBindException(BindException e) {
		LOG.error(e.getMessage(), e);
		return wrapperBindingResult(e.getBindingResult());
	}

	/**
	 * 参数校验异常，将校验失败的所有异常组合成一条错误信息
	 * @param e 异常
	 * @return 异常结果
	 */
	@ExceptionHandler(value = MethodArgumentNotValidException.class)
	public R<String> handleValidException(MethodArgumentNotValidException e) {
		LOG.error(e.getMessage(), e);
		return wrapperBindingResult(e.getBindingResult());
	}

	/**
	 * 包装绑定异常结果
	 * @param bindingResult 绑定结果
	 * @return 异常结果
	 */
	private R<String> wrapperBindingResult(BindingResult bindingResult) {
		StringBuilder msg = new StringBuilder();
		for (ObjectError error : bindingResult.getAllErrors()) {
			msg.append(", ");
			if (error instanceof FieldError) {
				msg.append(((FieldError) error).getField()).append(": ");
			}
			msg.append(error.getDefaultMessage() == null ? "" : error.getDefaultMessage());
		}
		return R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getCode(), msg.substring(2));
	}

}

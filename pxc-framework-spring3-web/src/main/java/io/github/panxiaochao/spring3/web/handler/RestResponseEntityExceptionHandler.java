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
package io.github.panxiaochao.spring3.web.handler;

import io.github.panxiaochao.spring3.core.enums.CommonResponseEnum;
import io.github.panxiaochao.spring3.core.enums.ServletResponseEnum;
import io.github.panxiaochao.spring3.core.response.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <p>
 * 默认情况下，标准的 Spring MVC 异常会通过 DefaultHandlerExceptionResolver 来处理 继承自定义处理一些特殊错误.
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-26
 */
@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

	protected ResponseEntity<Object> handleExceptionInternal(Exception e, @Nullable Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		LOGGER.error(e.getMessage(), e);
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute("javax.servlet.error.exception", e, 0);
		}
		ServletResponseEnum servletExceptionEnum;
		try {
			servletExceptionEnum = ServletResponseEnum.valueOf(e.getClass().getSimpleName());
		}
		catch (IllegalArgumentException e1) {
			LOGGER.error("class [{}] not defined in enum {}", e.getClass().getName(),
					ServletResponseEnum.class.getName());
			return new ResponseEntity<>(R.fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getCode(),
					CommonResponseEnum.INTERNAL_SERVER_ERROR.getMessage(), body), headers, status);
		}
		return new ResponseEntity<>(R.fail(servletExceptionEnum.getCode(), servletExceptionEnum.getMessage(), body),
				headers, status);
	}

}

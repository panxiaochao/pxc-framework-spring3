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
package io.github.panxiaochao.spring3.core.response;

import io.github.panxiaochao.spring3.core.enums.CommonResponseEnum;
import io.github.panxiaochao.spring3.core.response.page.PageObjectResponse;
import io.github.panxiaochao.spring3.core.response.page.Pagination;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>
 * Api分页对象返回响应体
 * </p>
 *
 * @param <T> 泛型参数
 * @author Lypxc
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "数据对象返回响应类", description = "数据对象返回响应类")
public class RPageObject<T> {

	/**
	 * 响应码
	 */
	@Schema(description = "响应码")
	private int code;

	/**
	 * 响应消息
	 */
	@Schema(description = "响应消息")
	private String message;

	/**
	 * 响应数据
	 */
	@Schema(description = "响应数据")
	private PageObjectResponse<T> data;

	/**
	 * 成功
	 * @param <T> 数据类型
	 * @return 成功的响应
	 */
	public static <T> RPageObject<T> ok() {
		return ok(null);
	}

	/**
	 * 成功
	 * @param pageResponse 数据
	 * @param <T> 数据类型
	 * @return 成功的响应
	 */
	public static <T> RPageObject<T> ok(PageObjectResponse<T> pageResponse) {
		return new RPageObject<>(CommonResponseEnum.OK.getCode(), CommonResponseEnum.OK.getMessage(), pageResponse);
	}

	/**
	 * 成功
	 * @param pagination 分页
	 * @param data 数据
	 * @param <T> 数据类型
	 * @return 成功的响应
	 */
	public static <T> RPageObject<T> ok(Pagination pagination, T data) {
		return new RPageObject<>(CommonResponseEnum.OK.getCode(), CommonResponseEnum.OK.getMessage(),
				new PageObjectResponse<>(pagination, data));
	}

    /**
     * 成功
     *
     * @param code       响应码
     * @param message    自定义消息
     * @param pagination 分页
     * @param data       数据
     * @param <T>        数据类型
     * @return 成功的响应
     */
    public static <T> RPageObject<T> ok(int code, String message, Pagination pagination, T data) {
        return new RPageObject<>(code, message, new PageObjectResponse<>(pagination, data));
    }

	/**
	 * 失败
	 * @param <T> 数据类型
	 * @return 失败的响应
	 */
	public static <T> RPageObject<T> fail() {
		return fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getCode(),
				CommonResponseEnum.INTERNAL_SERVER_ERROR.getMessage(), null);
	}

	/**
	 * 失败
	 * @param message 消息
	 * @param <T> 数据类型
	 * @return 失败的响应
	 */
	public static <T> RPageObject<T> fail(String message) {
		return fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getCode(), message, null);
	}

	/**
	 * 失败
	 * @param message 消息
	 * @param data 数据
	 * @param <T> 数据类型
	 * @return 失败的响应
	 */
	public static <T> RPageObject<T> fail(String message, PageObjectResponse<T> data) {
		return fail(CommonResponseEnum.INTERNAL_SERVER_ERROR.getCode(), message, data);
	}

	/**
	 * 失败
	 * @param code 响应码
	 * @param message 响应消息
	 * @param data 数据
	 * @param <T> 数据类型
	 * @return 失败的响应
	 */
	public static <T> RPageObject<T> fail(int code, String message, PageObjectResponse<T> data) {
		return new RPageObject<T>(code, message, data);
	}

}

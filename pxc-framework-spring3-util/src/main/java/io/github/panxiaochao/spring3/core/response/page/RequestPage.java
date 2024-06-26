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
package io.github.panxiaochao.spring3.core.response.page;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 请求分页参数
 * </p>
 *
 * @author Lypxc
 * @since 2021/12/3 17:51
 */
@Getter
@Setter
@Schema(description = "请求分页参数")
public class RequestPage {

	/**
	 * 页号
	 */
	@Schema(description = "页码，不小于1")
	private long pageNo = 1;

	/**
	 * 页数
	 */
	@Schema(description = "页数")
	private long pageSize = 10;

	/**
	 * 是否查询总数
	 */
	@Schema(description = "页码")
	private boolean searchCount = true;

}

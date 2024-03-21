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

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 请求排序
 * </p>
 *
 * @author Lypxc
 * @since 2022/4/7
 */
@Getter
@Setter
@Schema(description = "请求排序")
public class OrderItems implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 排序字段
	 */
	@Schema(description = "排序字段")
	private String column;

	/**
	 * 是否正序排列，默认 true
	 */
	@Schema(description = "是否正序排列，默认true")
	private boolean asc = true;

	private OrderItems() {
	}

	public OrderItems(String column, boolean asc) {
		this.column = column;
		this.asc = asc;
	}

	/**
	 * @param column 字段
	 * @return OrderItems
	 */
	public static OrderItems asc(String column) {
		return build(column, true);
	}

	/**
	 * @param column 字段
	 * @return OrderItems
	 */
	public static OrderItems desc(String column) {
		return build(column, false);
	}

	/**
	 * @param columns 字段
	 * @return OrderItems
	 */
	public static List<OrderItems> ascList(String... columns) {
		return Arrays.stream(columns).map(OrderItems::asc).collect(Collectors.toList());
	}

	/**
	 * @param columns 字段
	 * @return OrderItems
	 */
	public static List<OrderItems> descList(String... columns) {
		return Arrays.stream(columns).map(OrderItems::desc).collect(Collectors.toList());
	}

	/**
	 * @param column 字段
	 * @param asc 排序
	 * @return OrderItems
	 */
	private static OrderItems build(String column, boolean asc) {
		return new OrderItems(column, asc);
	}

}

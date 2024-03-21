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
 * 分页对象属性.
 * </p>
 *
 * @author Lypxc
 * @since 2023-01-03
 */
@Getter
@Setter
@Schema(description = "分页对象属性")
public class Pagination {

	/**
	 * 页码.
	 */
	@Schema(description = "页码，不小于1")
	private long pageNo;

	/**
	 * 页数.
	 */
	@Schema(description = "页数")
	private long pageSize;

	/**
	 * 总数.
	 */
	@Schema(description = "总数")
	private long total;

	/**
	 * 总页码数.
	 */
	@Schema(description = "总页码数")
	private long totalPages;

	/**
	 * Construct.
	 */
	public Pagination() {
        this.pageNo = 1;
        this.pageSize = 10;
		this.total = 0;
		this.totalPages = getTotalPages(0, pageSize);
	}

	/**
	 * Construct.
	 * @param pageNo 页码
	 * @param pageSize 页数
	 */
	public Pagination(final long pageNo, final long pageSize) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = 0;
		this.totalPages = getTotalPages(getTotal(), pageSize);
	}

	/**
	 * Construct.
	 * @param pageNo 页码
	 * @param pageSize 页数
	 * @param total 总数
	 */
	public Pagination(final long pageNo, final long pageSize, final long total) {
		this.pageNo = pageNo;
		this.pageSize = pageSize;
		this.total = total;
		this.totalPages = getTotalPages(total, pageSize);
	}

	/**
	 * 是否有上一页.
	 * @return boolean
	 */
	public boolean getHasPrevious() {
		return (getPageNo() > 1 && getPageNo() <= this.getTotalPages());
	}

	/**
	 * 是否有下一页.
	 * @return boolean
	 */
	public boolean getHasNext() {
		return getPageNo() < getTotalPages();
	}

	/**
	 * 获取总页数.
     * @param total 总条数
	 * @param pageSize 分页
	 * @return ResponsePageBuilder
	 */
    private long getTotalPages(final long total, final long pageSize) {
        if (total == 0) {
			return 0L;
		}
        long pages = total / pageSize;
        if (total % pageSize != 0) {
			pages++;
		}
		return pages;
	}

}

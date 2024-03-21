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
package io.github.panxiaochao.spring3.mybatis.plus.po;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 基础持久化实体
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-17
 */
@Getter
@Setter
public abstract class BasePo implements Serializable {

	private static final long serialVersionUID = 846044951522308141L;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	@TableField(value = "CREATE_TIME", fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@Schema(description = "更新时间")
	@TableField(value = "UPDATE_TIME", fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

    /**
     * 创建人
     */
    // @Schema(description = "创建人")
    // @TableField(value = "CREATE_ID", fill = FieldFill.INSERT)
    // private Long createId;

	/**
	 * 更新人
	 */
	// @Schema(description = "更新人")
	// @TableField(value = "UPDATE_ID", fill = FieldFill.INSERT_UPDATE)
	// private Long updateId;

}

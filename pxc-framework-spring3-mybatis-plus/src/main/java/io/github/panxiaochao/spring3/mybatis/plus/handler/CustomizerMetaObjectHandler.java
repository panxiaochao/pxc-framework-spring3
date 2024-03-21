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
package io.github.panxiaochao.spring3.mybatis.plus.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

/**
 * <p>
 * Mybatis Plus 自动填充配置
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-17
 */
public class CustomizerMetaObjectHandler implements MetaObjectHandler {

	private static final String FIELD_ID = "createId";

	private static final String FIELD_CREATE_TIME = "createTime";

	private static final String FIELD_UPDATE_TIME = "updateTime";

	@Override
	public void insertFill(MetaObject metaObject) {
		// Long
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, System.currentTimeMillis(), Long.class, false);
		strictFillValByName(metaObject, FIELD_CREATE_TIME, System.currentTimeMillis(), Long.class, false);
		// LocalDateTime
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, LocalDateTime.now(), LocalDateTime.class, false);
		strictFillValByName(metaObject, FIELD_CREATE_TIME, LocalDateTime.now(), LocalDateTime.class, false);
		// LocalDate
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, LocalDate.now(), LocalDate.class, false);
		strictFillValByName(metaObject, FIELD_CREATE_TIME, LocalDate.now(), LocalDate.class, false);
		// Date
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, new Date(), Date.class, false);
		strictFillValByName(metaObject, FIELD_CREATE_TIME, new Date(), Date.class, false);
	}

	@Override
	public void updateFill(MetaObject metaObject) {
		// Long
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, System.currentTimeMillis(), Long.class, true);
		// LocalDateTime
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, LocalDateTime.now(), LocalDateTime.class, true);
		// LocalDate
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, LocalDate.now(), LocalDate.class, true);
		// Date
		strictFillValByName(metaObject, FIELD_UPDATE_TIME, new Date(), Date.class, true);
	}

	/**
	 * 填充值，判断是是否是insert还是update，例如：job必须手动设置, 多线程必须手动设置
	 * @param metaObject 元数据对象
	 * @param fieldName 属性名
	 * @param fieldVal 属性值
	 * @param updateFill 是否更新
	 */
	private static void strictFillValByName(MetaObject metaObject, String fieldName, Object fieldVal,
			Class<?> fieldType, boolean updateFill) {
		// 0. 如果填充值为空
		if (fieldVal == null) {
			return;
		}
		// 1. 没有 get 方法
		if (!metaObject.hasSetter(fieldName)) {
			return;
		}
		// 2. 当是insert和值为null的时候才会置值
		if (!updateFill) {
			Object setValueObj = metaObject.getValue(fieldName);
			String setValueStr = Objects.isNull(setValueObj) ? "" : String.valueOf(setValueObj);
			if (StringUtils.hasText(setValueStr)) {
				return;
			}
		}
		// 3. 判断 fieldType 和 getterType 是否相同
		Class<?> getterType = metaObject.getGetterType(fieldName);
		if (Objects.equals(getterType, fieldType)) {
			if (ClassUtils.isAssignableValue(getterType, fieldVal)) {
				metaObject.setValue(fieldName, fieldVal);
			}
		}
	}

}

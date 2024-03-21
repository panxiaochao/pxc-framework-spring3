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
package io.github.panxiaochao.spring3.operate.log.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * 操作日志注解
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface OperateLog {

    /**
     * 获取请求参数key, 支持 Spring EL 表达式, 例如 #id, #user.id
     */
    String key() default "";

	/**
	 * 模块名
	 */
	String title() default "";

	/**
	 * 描述操作日志
	 **/
	String description() default "";

	/**
	 * 业务类型
	 */
	BusinessType businessType() default BusinessType.OTHER;

	/**
	 * 操作人类型
	 */
	OperatorUserType operatorUserType() default OperatorUserType.WEB;

	/**
	 * 排除指定的请求参数名
	 */
	String[] excludeParamNames() default {};

	/**
	 * 是否保存请求的参数
	 */
	boolean saveReqParams() default true;

	/**
	 * 是否保存响应的参数
	 */
	boolean saveResData() default true;

	/**
	 * 业务类型
	 */
	enum BusinessType {

		/**
		 * 新增
		 */
		INSERT,
		/**
		 * 修改
		 */
		UPDATE,
		/**
		 * 删除
		 */
		DELETE,
        /**
         * 查询
         */
        QUERY,
		/**
		 * 授权
		 */
		GRANT,
		/**
		 * 导出
		 */
		EXPORT,
		/**
		 * 导入
		 */
        IMPORT,
        /**
         * 登录
         */
        LOGIN,
        /**
         * 登出
		 */
		LOGOUT,
		/**
		 * 生成代码
		 */
		GENERATE_CODE,
		/**
		 * 清空数据
		 */
		CLEAN,
		/**
		 * 其它
		 */
		OTHER

	}

	/**
	 * 操作人类型
	 */
	enum OperatorUserType {

		/**
		 * PC 用户
		 */
		WEB,
		/**
		 * PHONE 用户
		 */
		MOBILE,
		/**
		 * 其它
		 */
		OTHER

	}

}

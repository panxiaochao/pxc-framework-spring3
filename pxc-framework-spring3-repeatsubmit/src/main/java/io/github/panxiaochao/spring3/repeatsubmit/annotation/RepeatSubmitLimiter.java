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
package io.github.panxiaochao.spring3.repeatsubmit.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 限制重复提交注解
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-29
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RepeatSubmitLimiter {

	/**
	 * 提交间隔时间, 小于此时间间隔属于重复提交, 默认毫秒
	 */
	long interval() default 5000;

	/**
	 * 时间单位格式, 默认毫秒
	 */
	TimeUnit timeUnit() default TimeUnit.MILLISECONDS;

	/**
	 * 自定义提示消息
	 */
	String message() default "";

}

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
package io.github.panxiaochao.spring3.ratelimiter.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * 防刷、限流注解, 应用于字段、类、方法
 * </p>
 * <pre>
 *  ElementType.TYPE：能修饰类、接口或枚举类型
 *  ElementType.FIELD：能修饰成员变量
 *  ElementType.METHOD：能修饰方法
 * </pre>
 * <p>
 * 限流的思路:
 * </p>
 * <pre>
 * 1、 通过[请求路径或者方法名:访问ip]的作为key，[访问次数]为value的方式对某一请求进行唯一标识
 * 2、 每次访问的时候判断key是否存在，是否count超过了限制的访问次数
 * 3、 若访问超出限制，则返回限制报错信息
 * </pre>
 *
 * @author Lypxc
 * @since 2023-06-28
 */
@Documented
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface RateLimiter {

	/**
	 * 限流Key, 支持 Spring EL 表达式, 例如 #id, #user.id
	 */
	String key() default "";

	/**
	 * 指定second时间内, API最大请求次数
	 */
	int maxCount() default 10;

	/**
	 * 限定时间范围, 单位秒
	 */
	int limitSecond() default 60;

	/**
	 * 限流类型
	 */
	RateLimiterType rateLimiterType() default RateLimiterType.METHOD;

	/**
	 * 限流类型
	 */
	enum RateLimiterType {

		/**
		 * 根据 IP 进行限流
		 */
		IP,

		/**
		 * 根据 METHOD 进行限流
		 */
		METHOD,

		/**
		 * 根据 IP+METHOD 进行限流
		 */
		IP_METHOD,

		/**
		 * 单机/单实例限流
		 */
		SINGLE

	}

}

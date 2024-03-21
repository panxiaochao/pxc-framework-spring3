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
package io.github.panxiaochao.spring3.operate.log.core.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 操作日志 domain
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
@Getter
@Setter
@ToString
public class OperateLogDomain implements Serializable {

	private static final long serialVersionUID = -8831737354114961499L;

	/**
	 * 名称
	 */
	private String title;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 业务类型
	 */
	private Integer businessType;

	/**
	 * 操作人员类型
	 */
	private Integer operateUsertype;

	/**
	 * 请求url
	 */
	private String requestUrl;

	/**
	 * 请求方式
	 */
	private String requestMethod;

	/**
	 * 请求类型
	 */
	private String requestContentType;

    /**
     * 请求浏览器
     */
    private String browser;

    /**
     * 请求操作系统
     */
    private String os;

	/**
	 * 请求Ip
	 */
	private String ip;

	/**
	 * 请求Ip地址
	 */
	private String address;

	/**
	 * 请求类名
	 */
	private String className;

	/**
	 * 请求类方法
	 */
	private String classMethod;

	/**
	 * GET - 请求参数
	 */
	private String requestParam;

	/**
	 * POST - 请求参数
	 */
	private String requestBody;

	/**
	 * 返回内容
	 */
	private Object responseData;

    /**
     * 自定义参数值
     */
    private Object value;

    /**
     * 执行耗时, 单位毫秒
	 */
	private long costTime;

	/**
	 * 请求时间
	 */
	private LocalDateTime requestDateTime;

	/**
	 * 是否成功 1=成功, 0=失败
	 */
	private Integer code;

	/**
	 * 错误原因
	 */
	private String errorMessage;

}

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
package io.github.panxiaochao.spring3.trace.log.constants;

/**
 * <p>
 * 日志链路追踪常量类
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-14
 */
public interface TraceLogConstant {

	String APP = "APP";

	String PRE_APP = "PRE_APP";

	String TRACE_ID = "TRACE_ID";

	String SPAN_ID = "SPAN_ID";

	/**
	 * 本机IP
	 */
	String HOST_IP = "HOST_IP";

	/**
	 * 本机 Host Name
	 */
	String HOST_NAME = "HOST_NAME";

	/**
	 * 上游 IP
	 */
	String PRE_HOST_IP = "PRE_HOST_IP";

	/**
	 * 上游 Host Name
	 */
	String PRE_HOST_NAME = "PRE_HOST_NAME";

	String UNKNOWN = "unknown";

	String MDC_KEY = "tl";

	int INITIAL_VALUE = 0;

}

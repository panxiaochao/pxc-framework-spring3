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
package io.github.panxiaochao.spring3.operate.log.core.handler;


import io.github.panxiaochao.spring3.operate.log.core.domain.OperateLogDomain;

/**
 * <p>
 * 操作日志接口
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
public interface IOperateLogHandler {

	/**
	 * 日志存储数据库
	 * @param operateLogDomain 存储对象
	 */
	void saveOperateLog(OperateLogDomain operateLogDomain);

}

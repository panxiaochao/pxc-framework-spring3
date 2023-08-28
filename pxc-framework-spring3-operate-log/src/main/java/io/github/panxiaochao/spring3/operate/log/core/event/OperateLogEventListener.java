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
package io.github.panxiaochao.spring3.operate.log.core.event;

import io.github.panxiaochao.spring3.core.utils.Ip2regionUtil;
import io.github.panxiaochao.spring3.core.utils.ipregion.IpInfo;
import io.github.panxiaochao.spring3.operate.log.core.OperateLogDao;
import io.github.panxiaochao.spring3.operate.log.core.domain.OperateLogDomain;
import io.github.panxiaochao.spring3.operate.log.core.enums.OperateLogType;
import io.github.panxiaochao.spring3.operate.log.properties.OperateLogProperties;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.StringUtils;

/**
 * <p>
 * 操作日志监听类
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
@RequiredArgsConstructor
@AutoConfiguration
@EnableConfigurationProperties(OperateLogProperties.class)
public class OperateLogEventListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperateLogEventListener.class);

	private final OperateLogProperties operateLogProperties;

	private final OperateLogDao operateLogDao;

	/**
	 * <pre> 1、可以支持使用异步存储操作 2、自定义存储(数据库、大数据等都可以)或者打印日志 </pre
	 * @param operateLogDomain 操作日志领域
	 */
	@Async
	@EventListener
	public void operateLog(OperateLogDomain operateLogDomain) {
		if (StringUtils.hasText(operateLogDomain.getIp())) {
			IpInfo info = Ip2regionUtil.memorySearch(operateLogDomain.getIp());
			if (info != null) {
				operateLogDomain.setAddress(info.getAddressAndIsp());
			}
		}
		LOGGER.info("[ip]: {}, [address]: {}, [classMethod]: {}, [requestDateTime]: {}, [costTime]: {}ms",
				operateLogDomain.getIp(), operateLogDomain.getAddress(), operateLogDomain.getClassMethod(),
				operateLogDomain.getRequestDateTime(), operateLogDomain.getCostTime());
		// 如果是数据库操作
		if (operateLogProperties.logType.equals(OperateLogType.OTHER)) {
			operateLogDao.handle(operateLogDomain);
		}
	}

}

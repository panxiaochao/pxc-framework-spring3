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
package io.github.panxiaochao.spring3.operate.log.config;

import io.github.panxiaochao.spring3.core.exception.ServerRuntimeException;
import io.github.panxiaochao.spring3.core.utils.SpringContextUtil;
import io.github.panxiaochao.spring3.operate.log.core.OperateLogDao;
import io.github.panxiaochao.spring3.operate.log.core.aspect.OperateLogAspect;
import io.github.panxiaochao.spring3.operate.log.core.enums.OperateLogErrorEnum;
import io.github.panxiaochao.spring3.operate.log.core.enums.OperateLogType;
import io.github.panxiaochao.spring3.operate.log.core.handler.AbstractOperateLogHandler;
import io.github.panxiaochao.spring3.operate.log.properties.OperateLogProperties;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * <p>
 * OperateLog 自动配置类
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
@AutoConfiguration
@EnableConfigurationProperties(OperateLogProperties.class)
public class OperateLogAutoConfiguration {

	@Bean
	public OperateLogAspect operateLogAspect() {
		return new OperateLogAspect();
	}

	@Bean
	public OperateLogDao operateLogDao(OperateLogProperties operateLogProperties) {
		if (operateLogProperties.logType.equals(OperateLogType.OTHER)) {
			if (!Objects.isNull(operateLogProperties.getHandler())) {
				AbstractOperateLogHandler handler;
				Component cpt = AnnotationUtils.findAnnotation(operateLogProperties.getHandler(), Component.class);
				if (cpt != null) {
					handler = SpringContextUtil.getBean(operateLogProperties.getHandler());
				}
				else {
					GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
					beanDefinition.setBeanClass(operateLogProperties.getHandler());
					beanDefinition.setAutowireCandidate(true);
					beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
					String beanName = SpringContextUtil.registerBeanDefinition(beanDefinition,
							operateLogProperties.getHandler().getClassLoader());
					handler = SpringContextUtil.getBeanByClassName(beanName);
				}
				return new OperateLogDao(handler);
			}
			else {
				throw new ServerRuntimeException(OperateLogErrorEnum.OPERATE_LOG_HANDLER_ERROR);
			}
		}
		return null;
	}

}

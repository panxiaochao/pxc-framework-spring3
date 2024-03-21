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
package io.github.panxiaochao.spring3.operate.log.core.aspect;

import io.github.panxiaochao.spring3.operate.log.core.annotation.OperateLog;
import io.github.panxiaochao.spring3.operate.log.core.context.MethodCostContext;
import io.github.panxiaochao.spring3.operate.log.utils.OperateLogUtil;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

/**
 * <p>
 * 操作日志 Aspect处理
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
@Aspect
@Order(1)
public class OperateLogAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(OperateLogAspect.class);

	public OperateLogAspect() {
		LOGGER.info("配置[OperateLogAspect]成功！");
	}

	/**
	 * 前置拦截
	 */
	@Before("@annotation(operatorLog)")
	public void before(JoinPoint joinPoint, OperateLog operatorLog) {
		// 设置请求方法执行开始时间
		MethodCostContext.setMethodCostTime(System.currentTimeMillis());
	}

	/**
	 * 返回拦截
	 */
	@AfterReturning(pointcut = "@annotation(operatorLog)", returning = "returnValue")
	public void afterReturning(JoinPoint joinPoint, OperateLog operatorLog, Object returnValue) {
		OperateLogUtil.handleOperateLog(joinPoint, operatorLog, returnValue, null);
	}

	/**
	 * 错误异常拦截
	 */
	@AfterThrowing(pointcut = "@annotation(operatorLog)", throwing = "ex")
	public void afterThrowing(JoinPoint joinPoint, OperateLog operatorLog, Exception ex) {
		OperateLogUtil.handleOperateLog(joinPoint, operatorLog, null, ex);
	}

}

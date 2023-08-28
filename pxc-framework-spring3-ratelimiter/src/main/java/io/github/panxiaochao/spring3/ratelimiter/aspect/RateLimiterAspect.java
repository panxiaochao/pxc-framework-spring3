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
package io.github.panxiaochao.spring3.ratelimiter.aspect;

import io.github.panxiaochao.spring3.core.exception.ServerRuntimeException;
import io.github.panxiaochao.spring3.core.ienums.IEnum;
import io.github.panxiaochao.spring3.core.utils.IpUtil;
import io.github.panxiaochao.spring3.core.utils.StrUtil;
import io.github.panxiaochao.spring3.core.utils.StringPools;
import io.github.panxiaochao.spring3.ratelimiter.annotation.RateLimiter;
import io.github.panxiaochao.spring3.redis.utils.RedissonUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RateType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * <p>
 * 限流 Aspect 处理
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-28
 */
@Aspect
@Order(2)
public class RateLimiterAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterAspect.class);

	public RateLimiterAspect() {
		LOGGER.info("配置[RateLimiterAspect]成功！");
	}

	/**
	 * 限流 redis key
	 */
	private static final String RATE_LIMITER_KEY = "rate_limiter:";

	/**
	 * 定义EL表达式解析器
	 */
	private final ExpressionParser expressionParser = new SpelExpressionParser();

	/**
	 * 定义EL解析模版
	 */
	private final ParserContext parserContext = new TemplateParserContext();

	/**
	 * 定义EL上下文对象进行解析
	 */
	private final EvaluationContext evaluationContext = new StandardEvaluationContext();

	/**
	 * 方法参数解析器
	 */
	private final ParameterNameDiscoverer parameterNameDiscoverer = new DefaultParameterNameDiscoverer();

	@Before("@annotation(rateLimiter)")
	public void before(JoinPoint joinPoint, RateLimiter rateLimiter) {
		int maxCount = rateLimiter.maxCount();
		int limitSecond = rateLimiter.limitSecond();
		// 获取限流 KEY
		String rateLimiterKey = getRateLimiterKey(joinPoint, rateLimiter);
		// RateType.OVERALL 全局限流
		// RateType.PER_CLIENT 客户端单独计算限流
		long availableCount = RedissonUtil.INSTANCE()
			.setRateLimiter(rateLimiterKey, RateType.OVERALL, maxCount, limitSecond);
		if (availableCount == -1) {
			throw new ServerRuntimeException(RateLimiterErrorEnum.RATE_LIMITER_FREQUENT_ERROR);
		}
		LOGGER.info("缓存key: {}, 限制数: {}, 剩余数: {}", rateLimiterKey, maxCount, availableCount);
	}

	/**
	 * 获取限流 key
	 * @param joinPoint joinPoint
	 * @param rateLimiter rateLimiter
	 * @return obtain the key
	 */
	private String getRateLimiterKey(JoinPoint joinPoint, RateLimiter rateLimiter) {
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		Method method = methodSignature.getMethod();
		String key = rateLimiter.key();
		String classMethodName = method.getDeclaringClass().getName() + "." + method.getName();
		if (StrUtil.containsAny(key, StringPools.HASH)) {
			// 参数
			Object[] args = joinPoint.getArgs();
			// 获取方法上参数的名称
			String[] parameterNames = parameterNameDiscoverer.getParameterNames(method);
			Objects.requireNonNull(parameterNames, "限流Key解析异常, 请确认方法体是否存在定义参数！");
			for (int i = 0; i < parameterNames.length; i++) {
				evaluationContext.setVariable(parameterNames[i], args[i]);
			}
			try {
				Expression expression;
				if (StringUtils.startsWithIgnoreCase(key, parserContext.getExpressionPrefix())
						&& StringUtils.endsWithIgnoreCase(key, parserContext.getExpressionSuffix())) {
					expression = expressionParser.parseExpression(key, parserContext);
				}
				else {
					expression = expressionParser.parseExpression(key);
				}
				String value = expression.getValue(evaluationContext, String.class);
				if (StringUtils.hasText(value)) {
					key = value + ":";
				}
				else {
					key = StringPools.EMPTY;
				}
			}
			catch (Exception e) {
				throw new ServerRuntimeException(RateLimiterErrorEnum.RATE_LIMITER_PARSE_EXPRESSION_ERROR);
			}
		}
		StringBuilder stringBuilder = new StringBuilder(RATE_LIMITER_KEY);
		stringBuilder.append(key);
		if (rateLimiter.rateLimiterType() == RateLimiter.RateLimiterType.IP) {
			// 根据IP限流
			stringBuilder.append(IpUtil.ofRequestIp());
		}
		else if (rateLimiter.rateLimiterType() == RateLimiter.RateLimiterType.METHOD) {
			// 根据METHOD限流
			stringBuilder.append(DigestUtils.md5DigestAsHex(classMethodName.getBytes(StandardCharsets.UTF_8)));
		}
		else if (rateLimiter.rateLimiterType() == RateLimiter.RateLimiterType.IP_METHOD) {
			// 根据IP+METHOD限流
			stringBuilder.append(IpUtil.ofRequestIp())
				.append(":")
				.append(DigestUtils.md5DigestAsHex(classMethodName.getBytes(StandardCharsets.UTF_8)));
		}
		else if (rateLimiter.rateLimiterType() == RateLimiter.RateLimiterType.SINGLE) {
			// 获取客户端实例id
			stringBuilder.append(RedissonUtil.INSTANCE().getRedissonId());
		}
		return stringBuilder.toString();
	}

	/**
	 * 限流错误码
	 */
	@Getter
	@AllArgsConstructor
	enum RateLimiterErrorEnum implements IEnum<Integer> {

		/**
		 * 请求频繁，请过会儿再试
		 */
		RATE_LIMITER_FREQUENT_ERROR(6001, "访问过于频繁，请稍后再试!"),
		/**
		 * 限流KEY解析异常
		 */
		RATE_LIMITER_PARSE_EXPRESSION_ERROR(6002, "限流KEY解析异常!");

		private final Integer code;

		private final String message;

	}

}

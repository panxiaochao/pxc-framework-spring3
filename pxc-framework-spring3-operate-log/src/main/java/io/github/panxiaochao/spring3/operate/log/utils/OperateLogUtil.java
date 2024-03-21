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
package io.github.panxiaochao.spring3.operate.log.utils;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.github.panxiaochao.spring3.core.utils.*;
import io.github.panxiaochao.spring3.operate.log.core.annotation.OperateLog;
import io.github.panxiaochao.spring3.operate.log.core.context.MethodCostContext;
import io.github.panxiaochao.spring3.operate.log.core.domain.OperateLogDomain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParserContext;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpMethod;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Method;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;

/**
 * <p>
 * 操作日志工具类
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-03
 */
public class OperateLogUtil {

    /**
     * LOGGER OperateLogUtil.class
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OperateLogUtil.class);

    /**
     * 定义EL表达式解析器
     */
    private static final ExpressionParser EXPRESSIONPARSER = new SpelExpressionParser();

    /**
     * 定义EL解析模版
     */
    private static final ParserContext PARSERCONTEXT = new TemplateParserContext();

    /**
     * 定义EL上下文对象进行解析
     */
    private static final EvaluationContext EVALUATIONCONTEXT = new StandardEvaluationContext();

    /**
     * 方法参数解析器
     */
    private static final ParameterNameDiscoverer PARAMETERNAMEDISCOVERER = new DefaultParameterNameDiscoverer();

	/**
	 * 处理日志方式
	 * @param joinPoint joinPoint
	 * @param returnValue 返回值
	 * @param ex 报错信息
	 */
	public static void handleOperateLog(final JoinPoint joinPoint, OperateLog operateLog, Object returnValue,
										Exception ex) {
		Object target = joinPoint.getTarget();
		MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
		// 参数
		Object[] args = joinPoint.getArgs();
		// Method
		Method method = methodSignature.getMethod();
		// 设置方法名称
		String className = target.getClass().getName();
		String methodName = method.getName();
		OperateLogDomain operateLogDomain = new OperateLogDomain();
		operateLogDomain.setClassName(target.getClass().getSimpleName());
		operateLogDomain.setClassMethod(className + "." + methodName + "()");
		operateLogDomain.setTitle(operateLog.title());
		operateLogDomain.setDescription(operateLog.description());
		operateLogDomain.setBusinessType(operateLog.businessType().ordinal());
		operateLogDomain.setOperateUsertype(operateLog.operatorUserType().ordinal());
		if (RequestUtil.getRequest() != null) {
			operateLogDomain.setRequestUrl(RequestUtil.getRequest().getRequestURI());
			operateLogDomain.setRequestMethod(RequestUtil.getRequest().getMethod());
			operateLogDomain.setRequestContentType(RequestUtil.getRequest().getContentType());
			operateLogDomain.setIp(IpUtil.ofRequestIp());
		}
		operateLogDomain.setRequestDateTime(LocalDateTime.now());
		if (ex != null) {
			operateLogDomain.setCode(0);
			operateLogDomain.setErrorMessage(StrUtil.substring(ExceptionUtil.getMessage(ex), 0, 2000));
		}
		else {
			operateLogDomain.setCode(1);
        }
        // 设置请求浏览器和操作系统
        String uaString = RequestUtil.getRequest().getHeader("User-Agent").toLowerCase();
        UserAgent userAgent = UserAgentUtil.parse(uaString);
        operateLogDomain.setBrowser(userAgent.getBrowser().toString() + " " + userAgent.getVersion());
        operateLogDomain.setOs(userAgent.getPlatform().toString() + " " + userAgent.getOs().toString());
		// 设置请求参数
		if (operateLog.saveReqParams()) {
			setRequestParam(args, operateLogDomain, operateLog.excludeParamNames());
		}
		// 设置返回值
		if (operateLog.saveResData() && ObjectUtil.isNotEmpty(returnValue)) {
            JsonNode jsonNode = JacksonUtil.transferToJsonNode(returnValue);
            if (ObjectUtil.isNotEmpty(jsonNode)) {
                // 兼容返回是 0 或者 200 的情况
                if (jsonNode.get("code") != null && jsonNode.get("code").asInt() != 0
                        && jsonNode.get("code").asInt() != 200) {
                    operateLogDomain.setCode(0);
                    operateLogDomain.setErrorMessage(jsonNode.get("message").asText());
				}
			}
			operateLogDomain.setResponseData(StrUtil.substring(JacksonUtil.toString(returnValue), 0, 2000));
        }
        // 设置参数值
        String key = operateLog.key();
        if (StringUtils.hasText(key)) {
            operateLogDomain.setValue(parseExpression(method, joinPoint, key));
		}
		// 设置消耗时间
		operateLogDomain.setCostTime(System.currentTimeMillis() - MethodCostContext.getMethodCostTime());
		// 使用完清除，以免内存泄漏
		MethodCostContext.removeMethodCostTime();
		// 发布事件保存数据库
		SpringContextUtil.publishEvent(operateLogDomain);
    }

    /**
     * 解析参数
     *
     * @param method    Method
     * @param joinPoint JoinPoint
     * @param key       参数key
     * @return 解析值
     */
    private static String parseExpression(Method method, JoinPoint joinPoint, String key) {
        if (StrUtil.containsAny(key, StringPools.HASH)) {
            // 参数
            Object[] args = joinPoint.getArgs();
            // 获取方法上参数的名称
            String[] parameterNames = PARAMETERNAMEDISCOVERER.getParameterNames(method);
            Objects.requireNonNull(parameterNames, "限流Key解析异常, 请确认方法体是否存在定义参数！");
            for (int i = 0; i < parameterNames.length; i++) {
                EVALUATIONCONTEXT.setVariable(parameterNames[i], args[i]);
            }
            try {
                Expression expression;
                if (StringUtils.startsWithIgnoreCase(key, PARSERCONTEXT.getExpressionPrefix())
                        && StringUtils.endsWithIgnoreCase(key, PARSERCONTEXT.getExpressionSuffix())) {
                    expression = EXPRESSIONPARSER.parseExpression(key, PARSERCONTEXT);
                } else {
                    expression = EXPRESSIONPARSER.parseExpression(key);
                }
                return expression.getValue(EVALUATIONCONTEXT, String.class);
            } catch (Exception e) {
                LOGGER.error("解析参数出错", e);
            }
        }
		return StrUtil.EMPTY;
	}

	/**
	 * 设置参数
	 */
	private static void setRequestParam(Object[] args, OperateLogDomain operateLogDomain, String[] excludeProperties) {
		String requestMethod = operateLogDomain.getRequestMethod();
		Map<String, String> paramsMap = RequestUtil.getParamMap();
		if (HttpMethod.POST.name().equals(requestMethod) || HttpMethod.PUT.name().equals(requestMethod)) {
			String params = argsArrayToString(args, excludeProperties);
			if (StringUtils.hasText(params)) {
				operateLogDomain.setRequestBody(StrUtil.substring(params, 0, 2000));
			}
		}
		// 会出现混合模式，POST 中用跟参数的情况
		if (MapUtil.isNotEmpty(paramsMap)) {
			if (MapUtil.isNotEmpty(paramsMap)) {
				MapUtil.removeAny(paramsMap, excludeProperties);
			}
			operateLogDomain.setRequestParam(StrUtil.substring(JacksonUtil.toString(paramsMap), 0, 2000));
		}
	}

	/**
	 * 参数拼装
	 */
	private static String argsArrayToString(Object[] args, String[] excludeProperties) {
		StringJoiner params = new StringJoiner(" ");
		if (ArrayUtil.isEmpty(args)) {
			return params.toString();
		}
		for (Object object : args) {
			if (ObjectUtil.isNotEmpty(object) && !isFilterObject(object)) {
				String jsonObj = JacksonUtil.toString(object);
				// 排除自定义属性
				if (!ArrayUtil.isEmpty(excludeProperties) && StrUtil.isNotBlank(jsonObj)) {
					Map<String, Object> objectMap = JacksonUtil.toMap(jsonObj);
					if (MapUtil.isNotEmpty(objectMap)) {
						MapUtil.removeAny(objectMap, excludeProperties);
						jsonObj = JacksonUtil.toString(objectMap);
					}
				}
				params.add(jsonObj);
			}
		}
		return params.toString();
	}

	/**
	 * 判断是否需要过滤的对象。
	 * @param o 对象信息。
	 * @return 如果是需要过滤的对象，则返回true；否则返回false。
	 */
	@SuppressWarnings("rawtypes")
	private static boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return MultipartFile.class.isAssignableFrom(clazz.getComponentType());
		}
		else if (Collection.class.isAssignableFrom(clazz)) {
			Collection<?> collection = (Collection<?>) o;
			for (Object value : collection) {
				return value instanceof MultipartFile;
			}
		}
		else if (Map.class.isAssignableFrom(clazz)) {
			Map map = (Map) o;
			for (Object value : map.values()) {
				return value instanceof MultipartFile;
			}
		}
		return (o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
				|| o instanceof BindingResult);
	}

}

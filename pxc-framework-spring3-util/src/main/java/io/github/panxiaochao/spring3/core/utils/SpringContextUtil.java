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
package io.github.panxiaochao.spring3.core.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopContext;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 获取Spring容器工具类
 * <p>
 * 这种方式让 ApplicationContext 更早的初始化
 *
 * @author Lypxc
 * @since 2022-05-05
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {

	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	private static ApplicationContext applicationContext;

	/**
	 * get ApplicationContext instance
	 * @return ApplicationContext
	 */
	public static ApplicationContext getApplicationContext() {
		return applicationContext;
	}

	/**
	 * obtain BeanFactory instance
	 * @return DefaultListableBeanFactory
	 */
	public static DefaultListableBeanFactory getBeanFactory() {
		return (DefaultListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
	}

	/**
	 * @param beanName bean name
	 * @param <T> type
	 * @return bean class
	 */
	@SuppressWarnings("all")
	public static <T> T getBean(String beanName) {
		try {
			return (T) applicationContext.getBean(beanName);
		}
		catch (BeansException e) {
			return null;
		}
	}

	/**
	 * @param type type
	 * @param <T> class
	 * @return bean class
	 */
	public static <T> T getBean(Class<T> type) {
		try {
			return applicationContext.getBean(type);
		}
		catch (BeansException e) {
			return null;
		}
	}

	/**
	 * Get bean by class name.
	 * @param className the class name
	 * @param <T> type
	 * @return bean by class name
	 */
	@SuppressWarnings("all")
	public static <T> T getBeanByClassName(final String className) {
		String beanName = getBeanName(className);
		try {
			return getBean(beanName);
		}
		catch (BeansException e) {
			return null;
		}
	}

	/**
	 * Exist spring bean boolean.
	 * @param beanName bean name
	 * @return the boolean
	 */
	public static boolean existBean(final String beanName) {
		return applicationContext.containsBean(beanName);
	}

	/**
	 * Exist spring bean boolean.
	 * @param className class name
	 * @return the boolean
	 */
	public static boolean existBeanByClassName(final String className) {
		String beanName = getBeanName(className);
		return applicationContext.containsBean(beanName);
	}

	/**
	 * get bean name
	 * @param className class name
	 * @return class name
	 */
	public static String getBeanName(final String className) {
		String name = className.substring(className.lastIndexOf(".") + 1);
		String start = name.substring(0, 1);
		String end = name.substring(1);
		return start.toLowerCase() + end;
	}

	/**
	 * Register bean.
	 * @param beanDefinition the bean definition
	 * @param classLoader the class loader
	 * @return the beanName
	 */
	public static String registerBeanDefinition(final GenericBeanDefinition beanDefinition,
			final ClassLoader classLoader) {
		String beanClassName = beanDefinition.getBeanClassName();
		if (StringUtils.isBlank(beanClassName)) {
			throw new NullPointerException("beanDefinition.beanClassName is null");
		}
		String beanName = getBeanName(beanClassName);
		// 创建Bean工厂
		DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) applicationContext
			.getAutowireCapableBeanFactory();
		beanFactory.setBeanClassLoader(classLoader);
		beanFactory.registerBeanDefinition(beanName, beanDefinition);
		return beanName;
	}

	/**
	 * find properties by key
	 * @param key key
	 * @return properties
	 */
	public static String getProperty(String key) {
		return applicationContext.getEnvironment().getProperty(key);
	}

	/**
	 * get application name
	 * @return application name
	 */
	public static String getApplicationName() {
		return getProperty("spring.application.name");
	}

	/**
	 * get Active profile
	 * @return the array of active profiles
	 */
	public static String[] getActiveProfiles() {
		return applicationContext.getEnvironment().getActiveProfiles();
	}

	/**
	 * get current Active profile
	 * @return the current active profile
	 */
	public static String getActiveProfile() {
		final String[] activeProfiles = getActiveProfiles();
		return ArrayUtil.isNotEmpty(activeProfiles) ? activeProfiles[0] : null;
	}

	/**
	 * 获取aop代理对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getAopProxy(T invoker) {
		return (T) AopContext.currentProxy();
	}

	/**
	 * 发布事件
	 * @param event 待发布的事件，事件必须是{@link ApplicationEvent}的子类
	 */
	public static void publishEvent(ApplicationEvent event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

	/**
	 * 发布事件 Spring 4.2+ 版本事件可以不再是{@link ApplicationEvent}的子类
	 * @param event 待发布的事件
	 */
	public static void publishEvent(Object event) {
		if (null != applicationContext) {
			applicationContext.publishEvent(event);
		}
	}

	@Override
	public void setApplicationContext(@NonNull final ApplicationContext applicationContext) {
		SpringContextUtil.applicationContext = applicationContext;
		LOGGER.info("配置[ApplicationContext]成功！");
	}

}

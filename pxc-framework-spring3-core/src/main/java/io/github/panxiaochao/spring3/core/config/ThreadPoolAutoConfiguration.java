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
package io.github.panxiaochao.spring3.core.config;

import io.github.panxiaochao.spring3.core.properties.PxcFrameWorkProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * <p>
 * 线程池 自动配置类
 * </p>
 * <pre>
 *     CPU密集型： 核心线程数 = CPU核心数 (或 核心线程数 = CPU核心数 + 1)
 *     I/O密集型： 核心线程数 = 2 * CPU核心数 (或 核心线程数 = CPU核心数 / (1 - 阻塞系数))
 *     混合型：    核心线程数 = (线程等待时间 / 线程CPU时间 + 1) * CPU核心数
 * </pre>
 *
 * @author Lypxc
 * @since 2023-07-06
 */
@AutoConfiguration
@EnableConfigurationProperties(PxcFrameWorkProperties.class)
public class ThreadPoolAutoConfiguration {

	private final static Logger LOGGER = LoggerFactory.getLogger(ThreadPoolAutoConfiguration.class);

	/**
	 * 核心线程数, 根据规则生成
	 */
	private final int core = Runtime.getRuntime().availableProcessors() + 1;

	/**
	 * 构造 ThreadPoolTaskExecutor 多线程配置： <pre>
	 * 1.corePoolSize：线程池维护线程最小的数量，默认为1
	 * 2.maxPoolSize：线程池维护线程最大数量，默认为Integer.MAX_VALUE
	 * 3.keepAliveSeconds：(maxPoolSize-corePoolSize)部分线程空闲最大存活时间，默认存活时间是60s
	 * 4.queueCapacity：阻塞任务队列的大小，默认为Integer.MAX_VALUE，默认使用LinkedBlockingQueue
	 * 5.allowCoreThreadTimeOut：设置为true的话，keepAliveSeconds参数设置的有效时间对corePoolSize线程也有效，默认是flase
	 * 6.threadFactory：：用于设置创建线程的工厂，可以通过线程工厂给每个创建出来的线程设置更有意义的名字。使用开源框架guava提供的ThreadFactoryBuilder可以快速给线程池里的线程设置有意义的名字
	 * 7.rejectedExecutionHandler：拒绝策略，当队列workQueue和线程池maxPoolSize都满了，说明线程池处于饱和状态，那么必须采取一种策略处理提交的新任务。这个策略默认情况下是AbortPolicy，表示无法处理新任务时抛出异常，有以下四种策略，当然也可以根据实际业务需求类实现RejectedExecutionHandler接口实现自己的处理策略
	 *     7.1.AbortPolicy：丢弃任务，并且抛出RejectedExecutionException异常
	 *     7.2.DiscardPolicy：丢弃任务，不处理，不抛出异常
	 *     7.3.CallerRunsPolicy：直接在execute方法的调用线程中运行被拒绝的任务
	 *     7.4.DiscardOldestPolicy：丢弃队列中最前面的任务，然后重新尝试执行任务
	 * </pre>
	 */
	@Bean(name = "threadPoolTaskExecutor")
	// @ConditionalOnProperty(prefix = "spring.pxc-framework.thread-pool", name =
	// "enabled", havingValue = "true")
	public ThreadPoolTaskExecutor threadPoolTaskExecutor(PxcFrameWorkProperties pxcFrameWorkProperties) {
		PxcFrameWorkProperties.ThreadPoolConfig threadPoolConfig = pxcFrameWorkProperties.getThreadPool();
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		// 核心线程数
		executor.setCorePoolSize(core);
		// 最大线程数
		executor.setMaxPoolSize(core * 2);
		// 队列大小
		executor.setQueueCapacity(threadPoolConfig.getQueueCapacity());
		// 线程活跃时间(秒)
		executor.setKeepAliveSeconds(threadPoolConfig.getKeepAliveSeconds());
		// 线程前缀
		executor.setThreadNamePrefix(threadPoolConfig.getThreadNamePrefix());
		// 线程分组名称
		executor.setThreadGroupName(threadPoolConfig.getThreadGroupName());
		// 所有任务结束后关闭线程池
		executor.setWaitForTasksToCompleteOnShutdown(threadPoolConfig.isWaitForJobsToCompleteOnShutdown());
		// 拒绝策略 CallerRunsPolicy
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 添加装饰器，上文传递
		executor.setTaskDecorator(new TraceLogCopyContextTaskDecorator());
		// 初始化
		executor.initialize();
		LOGGER.info("配置[ThreadPoolTaskExecutor]成功！");
		return executor;
	}

	/**
	 * 多线程自定义装饰器
	 */
	static class TraceLogCopyContextTaskDecorator implements TaskDecorator {

		/**
		 * Decorate the given {@code Runnable}, returning a potentially wrapped
		 * {@code Runnable} for actual execution, internally delegating to the original
		 * {@link Runnable#run()} implementation.
		 * @param runnable the original {@code Runnable}
		 * @return the decorated {@code Runnable}
		 */
		@Override
		@NonNull
		public Runnable decorate(@NonNull Runnable runnable) {
			RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
			Map<String, String> map = MDC.getCopyOfContextMap();
			return () -> {
				try {
					if (Objects.nonNull(requestAttributes)) {
						RequestContextHolder.setRequestAttributes(requestAttributes);
					}
					if (Objects.nonNull(map)) {
						MDC.setContextMap(map);
					}
					runnable.run();
				}
				finally {
					RequestContextHolder.resetRequestAttributes();
					MDC.clear();
				}
			};
		}

	}

	/**
	 * 定时任务线程池配置
	 */
	@Bean
	public TaskScheduler taskScheduler(PxcFrameWorkProperties pxcFrameWorkProperties) {
		PxcFrameWorkProperties.TaskSchedulerConfig taskSchedulerConfig = pxcFrameWorkProperties.getTaskScheduler();
		ThreadPoolTaskScheduler executor = new ThreadPoolTaskScheduler();
		// 核心线程数目
		executor.setPoolSize(core);
		// 调度器shutdown被调用时等待当前被调度的任务完成
		executor.setAwaitTerminationSeconds(taskSchedulerConfig.getAwaitTerminationSeconds());
		// 线程名称前缀
		executor.setThreadNamePrefix(taskSchedulerConfig.getThreadNamePrefix());
		// 线程名称前缀
		executor.setWaitForTasksToCompleteOnShutdown(taskSchedulerConfig.isWaitForTasksToCompleteOnShutdown());
		// 线程拒绝策略
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
		// 初始化
		executor.initialize();
		LOGGER.info("配置[TaskScheduler]成功！");
		return executor;
	}

}

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
package io.github.panxiaochao.spring3.mybatis.plus.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.incrementer.DefaultIdentifierGenerator;
import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.ParameterUtils;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import io.github.panxiaochao.spring3.core.utils.IpUtil;
import io.github.panxiaochao.spring3.mybatis.plus.handler.CustomizerMetaObjectHandler;
import io.github.panxiaochao.spring3.mybatis.plus.properties.MpProperties;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.sql.SQLException;

/**
 * <p>
 * MyBatis plus 自动配置类
 * </p>
 *
 * @author Lypxc
 * @since 2023-07-17
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(MpProperties.class)
public class CustomizerMybatisPlusAutoConfiguration {

	private final MpProperties mpProperties;

	/**
	 * 配置 mybatis plus 插件
	 * @return MybatisPlusInterceptor
	 */
	@Bean
	public MybatisPlusInterceptor mybatisPlusInterceptor() {
		MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
		// 分页插件
		interceptor.addInnerInterceptor(paginationInnerInterceptor());
		// 乐观锁插件
		// interceptor.addInnerInterceptor(optimisticLockerInnerInterceptor());
		return interceptor;
	}

	/**
	 * 分页插件
	 */
	private CustomizerPaginationInnerInterceptor paginationInnerInterceptor() {
		CustomizerPaginationInnerInterceptor paginationInnerInterceptor = new CustomizerPaginationInnerInterceptor();
		// 设置数据库类型
		paginationInnerInterceptor.setDbType(mpProperties.getDbType());
		paginationInnerInterceptor.setOptimizeJoin(false);
		// 设置最大单页限制数量，默认 500 条，-1 不受限制
		paginationInnerInterceptor.setMaxLimit(-1L);
		// 分页合理化
		paginationInnerInterceptor.setOverflow(true);
		return paginationInnerInterceptor;
	}

	/**
	 * 自定义分页插件, 解决当 size 小于 0 时, 直接设置为 0, 防止错误查询全表
	 */
	static class CustomizerPaginationInnerInterceptor extends PaginationInnerInterceptor {

		@Override
		public void beforeQuery(Executor executor, MappedStatement ms, Object parameter, RowBounds rowBounds,
				ResultHandler resultHandler, BoundSql boundSql) throws SQLException {
			IPage<?> page = ParameterUtils.findPage(parameter).orElse(null);
			// size 小于 0 直接设置为 0
			if (null != page && page.getSize() < 0) {
				page.setSize(0);
			}
			super.beforeQuery(executor, ms, page, rowBounds, resultHandler, boundSql);
		}

	}

	/**
	 * 乐观锁插件
	 */
	public OptimisticLockerInnerInterceptor optimisticLockerInnerInterceptor() {
		return new OptimisticLockerInnerInterceptor();
	}

	/**
	 * Mybatis Plus 自动填充配置
	 * @return MetaObjectHandler
	 */
	@Bean
	public MetaObjectHandler metaObjectHandler() {
		return new CustomizerMetaObjectHandler();
	}

	/**
	 * 使用网卡信息绑定雪花生成器, 防止集群雪花ID重复
	 */
	@Bean
	public IdentifierGenerator idGenerator() {
		long workerId = IpUtil.ipv4ToLong(IpUtil.getLocalhostStr()) & 31;
		long dataCenterId = workerId > 30 ? 0 : workerId + 1;
		return new DefaultIdentifierGenerator(workerId, dataCenterId);
	}

}

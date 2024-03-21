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
package io.github.panxiaochao.spring3.redis.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import io.github.panxiaochao.spring3.core.utils.JacksonUtil;
import io.github.panxiaochao.spring3.redis.mapper.KeyPrefixNameMapper;
import io.github.panxiaochao.spring3.redis.properties.Redisson3Properties;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SentinelServersConfig;
import org.redisson.config.SingleServerConfig;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p>
 * Redisson 自动配置类 3表示大版本号
 * </p>
 *
 * @author Lypxc
 * @since 2023-06-27
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties({ Redisson3Properties.class })
@ConditionalOnWebApplication
public class Redisson3AutoConfiguration {

	private static final Logger LOGGER = LoggerFactory.getLogger(Redisson3AutoConfiguration.class);

	private final Redisson3Properties redisson3Properties;

	/**
	 * 自定义 Redisson 配置
	 * @return RedissonAutoConfigurationCustomizer
	 */
	@Bean
	public RedissonAutoConfigurationCustomizer redissonAutoConfigurationCustomizers() {
		return config -> {
			// 序列化模式
			config.setCodec(new JsonJacksonCodec(JacksonUtil.objectMapper()));
			// 获取方法
			Method singleServerMethod = ReflectionUtils.findMethod(Config.class, "getSingleServerConfig");
			Method sentinelServersMethod = ReflectionUtils.findMethod(Config.class, "getSentinelServersConfig");
			Method clusterServersMethod = ReflectionUtils.findMethod(Config.class, "getClusterServersConfig");
			// 自定义前缀
			KeyPrefixNameMapper keyPrefixNameMapper = new KeyPrefixNameMapper(redisson3Properties.getKeyPrefix());
			// 使用单机模式, 使用自定义前缀
			if (singleServerMethod != null) {
				ReflectionUtils.makeAccessible(singleServerMethod);
				Object singleServerObject = ReflectionUtils.invokeMethod(singleServerMethod, config);
				if (Objects.nonNull(singleServerObject)) {
					((SingleServerConfig) singleServerObject).setNameMapper(keyPrefixNameMapper);
				}
			}
			// 哨兵模式
			if (sentinelServersMethod != null) {
				ReflectionUtils.makeAccessible(sentinelServersMethod);
				Object sentinelServersObject = ReflectionUtils.invokeMethod(sentinelServersMethod, config);
				if (Objects.nonNull(sentinelServersObject)) {
					((SentinelServersConfig) sentinelServersObject).setNameMapper(keyPrefixNameMapper);
				}
			}
			// 集群配置方式
			// 哨兵模式
			if (clusterServersMethod != null) {
				ReflectionUtils.makeAccessible(clusterServersMethod);
				Object clusterServersObject = ReflectionUtils.invokeMethod(clusterServersMethod, config);
				if (Objects.nonNull(clusterServersObject)) {
					((ClusterServersConfig) clusterServersObject).setNameMapper(keyPrefixNameMapper);
				}
			}
			LOGGER.info("配置[Redis -> Redisson]成功！");
		};
	}

    /**
     * RedissonConnectionFactory工厂
     *
     * @param redissonClient 实例
     * @return RedissonConnectionFactory工厂
     */
    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redissonClient) {
        return new RedissonConnectionFactory(redissonClient);
    }

	/**
	 * Redis 序列化配置 采用 RedissonConnectionFactory 工厂
	 * @return RedisTemplate
	 */
	@Bean(name = "redisTemplate")
	public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, T> template = new RedisTemplate<>();
		template.setConnectionFactory(redisConnectionFactory);
		// 使用Jackson2JsonRedisSerialize 替换默认序列化(默认采用的是JDK序列化)
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL,
				JsonTypeInfo.As.PROPERTY);
        jackson2JsonRedisSerializer.setObjectMapper(om);
		// 使用 StringRedisSerializer 来序列化和反序列化redis的key值
		template.setKeySerializer(RedisSerializer.string());
		template.setHashKeySerializer(RedisSerializer.string());
		// 使用 Jackson2JsonRedisSerializer 序列化VALUE
		template.setValueSerializer(jackson2JsonRedisSerializer);
		template.setHashValueSerializer(jackson2JsonRedisSerializer);
		// afterPropertiesSet
		template.afterPropertiesSet();
		LOGGER.info("配置[Redis -> RedisTemplate]成功！");
		return template;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		return new StringRedisTemplate(redisConnectionFactory);
	}

}

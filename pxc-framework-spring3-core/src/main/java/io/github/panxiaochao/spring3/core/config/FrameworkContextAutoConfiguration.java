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
package io.github.panxiaochao.spring3.core.config;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * <p>
 * 框架容器 自定义皮配置
 * </p>
 * <pre>
 *     1. @EnableAspectJAutoProxy(exposeProxy = true) 暴露该代理对象, AopContext 能够访问
 * </pre>
 *
 * @author Lypxc
 * @since 2023-07-06
 */
@AutoConfiguration
@EnableAspectJAutoProxy(exposeProxy = true)
public class FrameworkContextAutoConfiguration {

}

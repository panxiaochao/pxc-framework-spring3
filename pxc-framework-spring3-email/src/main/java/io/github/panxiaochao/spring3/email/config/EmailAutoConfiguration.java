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
package io.github.panxiaochao.spring3.email.config;

import cn.hutool.extra.mail.MailAccount;
import io.github.panxiaochao.spring3.email.config.properties.EmailProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * Email 自动配置类
 * </p>
 *
 * @author Lypxc
 * @since 2023-09-07
 */
@AutoConfiguration
@EnableConfigurationProperties(EmailProperties.class)
@ConditionalOnProperty(name = "spring.email.enable", havingValue = "true")
public class EmailAutoConfiguration {

    @Bean
    public MailAccount mailAccount(EmailProperties emailProperties) {
        MailAccount account = new MailAccount();
        account.setHost(emailProperties.getHost());
        account.setPort(emailProperties.getPort());
        account.setAuth(emailProperties.getAuth());
        account.setFrom(emailProperties.getFrom());
        account.setUser(emailProperties.getLoginName());
        account.setPass(emailProperties.getPassword());
        account.setSocketFactoryPort(emailProperties.getPort());
        account.setStarttlsEnable(emailProperties.getStarttlsEnable());
        account.setSslEnable(emailProperties.getSslEnable());
        account.setTimeout(emailProperties.getTimeout());
        account.setConnectionTimeout(emailProperties.getConnectionTimeout());
        return account;
    }

}

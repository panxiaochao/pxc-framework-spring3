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
package io.github.panxiaochao.spring3.sensitive.enums;

import io.github.panxiaochao.spring3.sensitive.utils.DesensitizeUtil;

import java.util.function.Function;

/**
 * <p>
 * 脱敏策略
 * </p>
 *
 * @author Lypxc
 * @since 2023-08-31
 */
public enum FSensitiveStrategy {

    /**
     * 身份证脱敏
     */
    ID_CARD(s -> DesensitizeUtil.idCardNum(s, 3, 4)),
    /**
     * 姓名
     */
    FULL_NAME(DesensitizeUtil::chineseName),
    /**
     * 手机号脱敏
     */
    PHONE(DesensitizeUtil::mobilePhone),
    /**
     * 电话号码
     */
    MOBILE(DesensitizeUtil::fixedPhone),
    /**
     * 地址脱敏
     */
    ADDRESS(s -> DesensitizeUtil.address(s, 8)),
    /**
     * 邮箱脱敏
     */
    EMAIL(DesensitizeUtil::email),
    /**
     * 银行卡
     */
    BANK_CARD(DesensitizeUtil::bankCard),
    /**
     * 密码
     */
    PASSWORD(DesensitizeUtil::password),
    /**
     * 车牌
     */
    CAR_NUMBER(DesensitizeUtil::carLicense),
    /**
     * 默认, 原值返回
     */
    DEFAULT(s -> s);

    private final Function<String, String> desensitize;

    public Function<String, String> desensitize() {
        return this.desensitize;
    }

    FSensitiveStrategy(Function<String, String> desensitize) {
        this.desensitize = desensitize;
    }

}

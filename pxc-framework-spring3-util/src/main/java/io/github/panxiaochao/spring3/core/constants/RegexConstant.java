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
package io.github.panxiaochao.spring3.core.constants;

/**
 * <p>
 * 常用正则表达式字符串, 更多正则见: <a href=https://any86.github.io/any-rule/>规则表达式</a>
 * </p>
 *
 * @author Lypxc
 * @since 2024-01-17
 */
public interface RegexConstant {

    /**
     * 数字
     */
    String NUMBERS = "\\d+";

    /**
     * 字母
     */
    String CHARS = "[a-zA-Z]+";

    /**
     * QQ号码
     */
    String QQ_NUMBER = "^[1-9][0-9]\\d{4,9}$";

    /**
     * 邮政编码
     */
    String POSTAL_CODE = "^[1-9]\\d{5}$";

    /**
     * 18位身份证号码
     */
    String CITIZEN_ID = "[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([012]\\d)|3[0-1])\\d{3}(\\d|X|x)";

    /**
     * 注册账号, 格式：大小写字母、数字、下划线、4-15位
     */
    String ACCOUNT = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";

    /**
     * 密码：包含至少8个字符，包括大写字母、小写字母、数字和特殊字符
     */
    String PASSWORD = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

}

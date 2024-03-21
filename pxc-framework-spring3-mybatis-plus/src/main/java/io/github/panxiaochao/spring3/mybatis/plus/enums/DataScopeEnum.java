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
package io.github.panxiaochao.spring3.mybatis.plus.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * <p>
 * 数据权限枚举，法支持<b>SPEL</b>模板表达式
 * </p>
 *
 * @author Lypxc
 * @since 2024-02-05
 */
@Getter
@AllArgsConstructor
public enum DataScopeEnum {

    // 数据权限（1.全部数据 2.自定义数据 3.本部门数据 4.本部门及以下数据 5.仅本人数据）
    /**
     * 全部数据
     */
    ALL("1", "", "", "全部数据"),

    /**
     * 自定义数据
     */
    CUSTOM("2", " #{#orgId} IN ( #{@sdss.getRoleCustom( #user.roleId )} ) ", " 1 = 0 ", "自定义数据"),

    /**
     * 本部门数据
     */
    ORG("3", " #{#orgId} = #{#user.orgId} ", " 1 = 0 ", "本部门数据"),

    /**
     * 本部门及以下数据
     */
    ORG_AND_CHILD("4", " #{#orgId} IN ( #{@sdss.getOrgAndChild( #user.orgId )} )", " 1 = 0 ", "本部门及以下数据"),

    /**
     * 仅本人数据
     */
    SELF("5", " #{#createId} = #{#user.userId} ", " 1 = 0 ", "仅本人数据");

    /**
     * code
     */
    private final String code;

    /**
     * SQL语法，采用 spel 模板表达式
     */
    private final String sqlTemplate;

    /**
     * 不满足 sqlTemplate 则填充
     */
    private final String elseSql;

    /**
     * 备注
     */
    private final String message;

    /**
     * 根据code获取数据权限
     *
     * @param code 权限值
     * @return 返回数据权限
     */
    public DataScopeEnum ofCode(String code) {
        for (DataScopeEnum dataScopeEnum : values()) {
            if (dataScopeEnum.getCode().equals(code)) {
                return dataScopeEnum;
            }
        }
        return null;
    }

}

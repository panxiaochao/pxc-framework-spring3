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
package io.github.panxiaochao.spring3.core.component.select;

import lombok.Getter;

import java.io.Serializable;

/**
 * <p>
 * 下拉节点属性
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-20
 */
@Getter
public class SelectOptionProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private String disabled = "disabled";

    private String key = "key";

    private String title = "title";

    private String value = "value";

    private String weight = "weight";

    /**
     * 静态构造
     */
    public static SelectOptionProperties builder() {
        return new SelectOptionProperties();
    }

    /**
     * 是否禁用
     *
     * @param disabled 别名 disabled 的key
     * @return this
     */
    public SelectOptionProperties disabled(String disabled) {
        this.disabled = disabled;
        return this;
    }

    /**
     * 和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     *
     * @param key 别名 key 的key
     * @return this
     */
    public SelectOptionProperties key(String key) {
        this.key = key;
        return this;
    }

    /**
     * 选中该 Option 后，Select 的 title
     *
     * @param title 别名 title 的key
     * @return this
     */
    public SelectOptionProperties title(String title) {
        this.title = title;
        return this;
    }

    /**
     * 默认根据此属性值进行筛选
     *
     * @param value 别名 value 的key
     * @return this
     */
    public SelectOptionProperties value(String value) {
        this.value = value;
        return this;
    }

    /**
     * 设置weightKey别名
     *
     * @param weight 别名 weight 的key
     * @return this
     */
    public SelectOptionProperties weight(String weight) {
        this.weight = weight;
        return this;
    }

}

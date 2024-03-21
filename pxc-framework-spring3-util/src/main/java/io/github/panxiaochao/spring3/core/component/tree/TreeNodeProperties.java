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
package io.github.panxiaochao.spring3.core.component.tree;

import lombok.Getter;

import java.io.Serializable;

/**
 * <p>
 * 树节点属性
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-06
 */
@Getter
public class TreeNodeProperties implements Serializable {

    private static final long serialVersionUID = 1L;

    private String idKey = "id";

    private String parentIdKey = "parentId";

    private String childrenKey = "children";

    private String weightKey = "weight";

    private String labelKey = "name";

    /**
     * 静态构造
     */
    public static TreeNodeProperties builder() {
        return new TreeNodeProperties();
    }

    /**
     * 设置输出idKey别名
     *
     * @param idKey 别名id的key
     * @return this
     */
    public TreeNodeProperties idKey(String idKey) {
        this.idKey = idKey;
        return this;
    }

    /**
     * 设置输出parentIdKey别名
     *
     * @param parentIdKey 别名parentId的key
     * @return this
     */
    public TreeNodeProperties parentIdKey(String parentIdKey) {
        this.parentIdKey = parentIdKey;
        return this;
    }

    /**
     * 设置输出childrenKey别名
     *
     * @param childrenKey 别名children的key
     * @return this
     */
    public TreeNodeProperties childrenKey(String childrenKey) {
        this.childrenKey = childrenKey;
        return this;
    }

    /**
     * 设置weightKey别名
     *
     * @param weightKey 别名weight的key
     * @return this
     */
    public TreeNodeProperties weightKey(String weightKey) {
        this.weightKey = weightKey;
        return this;
    }

    /**
     * 设置labelKey别名
     *
     * @param labelKey 别名label的key
     * @return this
     */
    public TreeNodeProperties labelKey(String labelKey) {
        this.labelKey = labelKey;
        return this;
    }

}

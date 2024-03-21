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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * <p>
 * 树节点类，用来存储每条节点信息
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-06
 */
@Getter
public class TreeNode<T> implements Serializable {

    private static final long serialVersionUID = 6308056513889157607L;

    /**
     * 节点 ID
     */
    private T id;

    /**
     * 父节点 ID
     */
    private T parentId;

    /**
     * 节点值
     */
    private CharSequence labelValue;

    /**
     * 顺序 越小优先级越高
     */
    private Comparable<?> weight;

    /**
     * 扩展属性字段
     */
    private Map<String, Object> extra;

    /**
     * 构造方法
     *
     * @param id         节点ID
     * @param parentId   父节点ID
     * @param labelValue 节点值
     * @param <T>        节点参数类型
     * @return 树节点
     */
    public static <T> TreeNode<T> of(T id, T parentId, CharSequence labelValue) {
        return of(id, parentId, labelValue, 0, null);
    }

    /**
     * 构造方法
     *
     * @param id         节点ID
     * @param parentId   父节点ID
     * @param labelValue 节点值
     * @param extraMap   扩展属性
     * @param <T>        节点参数类型
     * @return 树节点
     */
    public static <T> TreeNode<T> of(T id, T parentId, CharSequence labelValue,
                                     Consumer<Map<String, Object>> extraMap) {
        return of(id, parentId, labelValue, 0, extraMap);
    }

    /**
     * 构造方法
     *
     * @param id         节点ID
     * @param parentId   父节点ID
     * @param labelValue 节点值
     * @param weight     权重值
     * @param extraMap   扩展属性
     * @param <T>        节点参数类型
     * @return 树节点
     */
    public static <T> TreeNode<T> of(T id, T parentId, CharSequence labelValue, Comparable<?> weight,
                                     Consumer<Map<String, Object>> extraMap) {
        return new TreeNode<T>(id, parentId, labelValue, weight, extraMap);
    }

    /**
     * 构造方法
     *
     * @param id         父 ID
     * @param parentId   父节点 ID
     * @param labelValue 节点值
     * @param weight     权重
     * @param extraMap   扩展属性
     */
    private TreeNode(T id, T parentId, CharSequence labelValue, Comparable<?> weight,
                     Consumer<Map<String, Object>> extraMap) {
        this.id = id;
        this.parentId = parentId;
        this.labelValue = labelValue;
        this.weight = weight;
        this.extra = new LinkedHashMap<>();
        if (extraMap != null) {
            extraMap.accept(this.extra);
        }
    }

    public TreeNode<T> setId(T id) {
        this.id = id;
        return this;
    }

    public TreeNode<T> setParentId(T parentId) {
        this.parentId = parentId;
        return this;
    }

    public TreeNode<T> setLabelValue(CharSequence labelValue) {
        this.labelValue = labelValue;
        return this;
    }

    public TreeNode<T> setWeight(Comparable<?> weight) {
        this.weight = weight;
        return this;
    }

    /**
     * 设置扩展字段
     *
     * @param extra 扩展字段
     * @return this
     */
    public TreeNode<T> setExtra(Map<String, Object> extra) {
        this.extra = extra;
        return this;
    }

    /**
     * 设置扩展字段
     *
     * @param extra 扩展字段
     * @return this
     */
    public TreeNode<T> setExtra(Consumer<Map<String, Object>> extra) {
        extra.accept(this.extra);
        return this;
    }

    /**
     * 设置所有扩展字段
     *
     * @param extraAll 扩展字段
     * @return this
     */
    public TreeNode<T> setExtraAll(Map<String, Object> extraAll) {
        this.extra.putAll(extraAll);
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeNode<?> treeNode = (TreeNode<?>) o;
        return Objects.equals(id, treeNode.getId());
    }

    public int hashCode() {
        return Objects.hash(id);
    }

}

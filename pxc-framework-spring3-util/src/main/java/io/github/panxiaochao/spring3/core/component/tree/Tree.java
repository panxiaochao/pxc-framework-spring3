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

import io.github.panxiaochao.spring3.core.utils.ArrayUtil;
import io.github.panxiaochao.spring3.core.utils.ObjectUtil;
import lombok.Getter;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * <p>
 * 树形结果结构类
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-06
 */
@Getter
public class Tree<T> extends LinkedHashMap<String, Object> implements Comparable<Tree<T>> {

    private static final long serialVersionUID = 1L;

    private final TreeNodeProperties treeNodeProperties;

    private Tree<T> parent;

    /**
     * 无参构造
     */
    public Tree(TreeNodeProperties properties) {
        this.treeNodeProperties = ObjectUtil.getIfNull(properties, TreeNodeProperties.builder());
    }

    /**
     * 设置父节点
     *
     * @param parent 父节点
     * @return this
     */
    public Tree<T> setParent(Tree<T> parent) {
        this.parent = parent;
        if (null != parent) {
            this.setParentId(parent.getId());
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    public T getId() {
        return (T) this.get(treeNodeProperties.getIdKey());
    }

    public Tree<T> setId(T id) {
        this.put(treeNodeProperties.getIdKey(), id);
        return this;
    }

    @SuppressWarnings("unchecked")
    public T getParentId() {
        return (T) this.get(treeNodeProperties.getParentIdKey());
    }

    public Tree<T> setParentId(T parentId) {
        this.put(treeNodeProperties.getParentIdKey(), parentId);
        return this;
    }

    public CharSequence getLabelValue() {
        return (CharSequence) this.get(treeNodeProperties.getLabelKey());
    }

    public Tree<T> setLabelValue(CharSequence labelValue) {
        this.put(treeNodeProperties.getLabelKey(), labelValue);
        return this;
    }

    public Comparable<?> getWeight() {
        return (Comparable<?>) this.get(treeNodeProperties.getWeightKey());
    }

    public Tree<T> setWeight(Comparable<?> weight) {
        this.put(treeNodeProperties.getWeightKey(), weight);
        return this;
    }

    /**
     * 获取所有子节点
     *
     * @return 所有子节点
     */
    @SuppressWarnings("unchecked")
    public List<Tree<T>> getChildren() {
        return (List<Tree<T>>) this.get(treeNodeProperties.getChildrenKey());
    }

    /**
     * 是否有子节点，无子节点则此为叶子节点
     *
     * @return 是否有子节点
     */
    public boolean hasChildren() {
        return !CollectionUtils.isEmpty(getChildren());
    }

    /**
     * 递归树并处理子树下的节点：
     *
     * @param consumer 节点处理器
     */
    public void walk(Consumer<Tree<T>> consumer) {
        consumer.accept(this);
        final List<Tree<T>> children = getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            children.forEach((tree) -> tree.walk(consumer));
        }
    }

    /**
     * 递归过滤并生成新的树<br>
     * 通过{@link Predicate}指定的过滤规则，本节点或子节点满足过滤条件，则保留当前节点，否则抛弃节点及其子节点
     *
     * @param predicate 节点过滤规则函数，只需处理本级节点本身即可
     * @return 过滤后的节点，{@code null} 表示不满足过滤要求，丢弃之
     * @see #filter(Predicate)
     */
    public Tree<T> filterNew(Predicate<Tree<T>> predicate) {
        return cloneTree().filter(predicate);
    }

    /**
     * 递归过滤当前树，注意此方法会修改当前树<br>
     * 通过{@link Consumer}指定的过滤规则，本节点或子节点满足过滤条件，则保留当前节点及其所有子节点，否则抛弃节点及其子节点
     *
     * @param predicate 节点过滤规则函数，只需处理本级节点本身即可
     * @return 过滤后的节点，{@code null} 表示不满足过滤要求，丢弃之
     * @see #filterNew(Predicate)
     */
    public Tree<T> filter(Predicate<Tree<T>> predicate) {
        if (predicate.test(this)) {
            // 本节点满足，则包括所有子节点都保留
            return this;
        }

        final List<Tree<T>> children = getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            // 递归过滤子节点
            final List<Tree<T>> filteredChildren = new ArrayList<>(children.size());
            Tree<T> filteredChild;
            for (Tree<T> child : children) {
                filteredChild = child.filter(predicate);
                if (null != filteredChild) {
                    filteredChildren.add(filteredChild);
                }
            }
            if (!CollectionUtils.isEmpty(filteredChildren)) {
                // 子节点有符合过滤条件的节点，则本节点保留
                return this.setChildren(filteredChildren);
            } else {
                this.setChildren(null);
            }
        }

        // 子节点都不符合过滤条件，检查本节点
        return null;
    }

    /**
     * 设置子节点，设置后会覆盖所有原有子节点
     *
     * @param children 子节点列表，如果为{@code null}表示移除子节点
     * @return this
     */
    public Tree<T> setChildren(List<Tree<T>> children) {
        if (null == children) {
            this.remove(treeNodeProperties.getChildrenKey());
        }
        this.put(treeNodeProperties.getChildrenKey(), children);
        return this;
    }

    /**
     * 增加子节点，同时关联子节点的父节点为当前节点
     *
     * @param children 子节点列表
     * @return this
     */
    @SafeVarargs
    public final Tree<T> addChildren(Tree<T>... children) {
        if (ArrayUtil.isNotEmpty(children)) {
            List<Tree<T>> childrenList = this.getChildren();
            if (null == childrenList) {
                childrenList = new ArrayList<>();
                setChildren(childrenList);
            }
            for (Tree<T> child : children) {
                child.setParent(this);
                childrenList.add(child);
            }
        }
        return this;
    }

    /**
     * 扩展属性
     *
     * @param key   键
     * @param value 扩展值
     */
    public void putExtra(String key, Object value) {
        Assert.notNull(key, "Key must be not empty !");
        this.put(key, value);
    }

    /**
     * 递归克隆当前节点（即克隆整个树，保留字段值）<br>
     * 注意，此方法只会克隆节点，节点属性如果是引用类型，不会克隆
     *
     * @return 新的节点
     */
    public Tree<T> cloneTree() {
        final Tree<T> result = ObjectUtil.clone(this);
        result.setChildren(cloneChildren());
        return result;
    }

    /**
     * 递归复制子节点
     *
     * @return 新的子节点列表
     */
    private List<Tree<T>> cloneChildren() {
        final List<Tree<T>> children = getChildren();
        if (null == children) {
            return null;
        }
        final List<Tree<T>> newChildren = new ArrayList<>(children.size());
        children.forEach((t) -> newChildren.add(t.cloneTree()));
        return newChildren;
    }

    /**
     * @param o the object to be compared.
     * @return 比较结果，如果c1 < c2，返回数小于0，c1==c2返回0，c1 > c2 大于0
     */
    @Override
    public int compareTo(Tree<T> o) {
        if (null == o) {
            return 1;
        }
        final Comparable weight = this.getWeight();
        final Comparable weightOther = o.getWeight();

        if (weight == weightOther) {
            return 0;
        } else if (weight == null) {
            return -1;
        } else if (weightOther == null) {
            return 1;
        }
        return weight.compareTo(weightOther);
    }

}

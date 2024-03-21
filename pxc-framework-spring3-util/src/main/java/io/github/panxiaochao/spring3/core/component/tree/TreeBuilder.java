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

import io.github.panxiaochao.spring3.core.component.select.Select;
import io.github.panxiaochao.spring3.core.component.select.SelectBuilder;
import io.github.panxiaochao.spring3.core.component.select.SelectOption;
import io.github.panxiaochao.spring3.core.utils.JacksonUtil;
import io.github.panxiaochao.spring3.core.utils.MapUtil;
import io.github.panxiaochao.spring3.core.utils.ObjectUtil;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * <p>
 * 树形构建器
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-06
 */
public class TreeBuilder<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Tree<E> root;

    private final Map<E, Tree<E>> treeMap;

    private boolean isBuild;

    private int deep;

    private final boolean isNullChildrenAsEmpty;

    private boolean isDesc;

    /**
     * 创建Tree构建器
     *
     * @param rootId 根节点ID
     * @param <E>    ID类型
     * @return TreeBuilder
     */
    public static <E> TreeBuilder<E> of(E rootId) {
        return new TreeBuilder<>(rootId, false, null);
    }

    /**
     * 创建Tree构建器
     *
     * @param rootId                根节点ID
     * @param isNullChildrenAsEmpty 是否子节点没有数据的情况下，给一个默认空集，默认 false
     * @param <E>                   ID类型
     * @return TreeBuilder
     */
    public static <E> TreeBuilder<E> of(E rootId, boolean isNullChildrenAsEmpty) {
        return of(rootId, isNullChildrenAsEmpty, null);
    }

    /**
     * 创建Tree构建器
     *
     * @param rootId                根节点ID
     * @param isNullChildrenAsEmpty 是否子节点没有数据的情况下，给一个默认空集，默认 false
     * @param properties            树节点属性配置
     * @param <E>                   ID类型
     * @return TreeBuilder
     */
    public static <E> TreeBuilder<E> of(E rootId, boolean isNullChildrenAsEmpty, TreeNodeProperties properties) {
        return new TreeBuilder<>(rootId, isNullChildrenAsEmpty, properties);
    }

    /**
     * 私有参数构造
     *
     * @param rootId                根节点ID
     * @param isNullChildrenAsEmpty 是否子节点没有数据的情况下，给一个默认空集，默认 false
     * @param properties            树节点属性配置
     */
    private TreeBuilder(E rootId, boolean isNullChildrenAsEmpty, TreeNodeProperties properties) {
        this.root = new Tree<>(properties);
        this.root.setId(rootId);
        this.treeMap = new LinkedHashMap<>();
        this.deep = -1;
        this.isNullChildrenAsEmpty = isNullChildrenAsEmpty;
        this.isDesc = false;
    }

    /**
     * 循环深度层次, 默认-1, 不受限制
     *
     * @param deep 深度
     * @return this
     */
    public TreeBuilder<E> deep(int deep) {
        this.deep = deep;
        return this;
    }

    /**
     * 是否倒序，默认升序
     *
     * @return this
     */
    public TreeBuilder<E> desc() {
        this.isDesc = true;
        return this;
    }

    /**
     * 增加节点列表，增加的节点是不带子节点的
     *
     * @param map 节点列表
     * @return this
     */
    public TreeBuilder<E> append(Map<E, Tree<E>> map) {
        Assert.isTrue(!isBuild, "Current tree has not been built.");
        this.treeMap.putAll(map);
        return this;
    }

    /**
     * 增加节点列表，增加的节点是不带子节点的
     *
     * @param <T>  Bean类型
     * @param list Bean列表
     * @return this
     */
    public <T> TreeBuilder<E> append(List<TreeNode<T>> list) {
        Assert.isTrue(!isBuild, "Current tree has been built.");
        return append(list, null);
    }

    public <T> TreeBuilder<E> append(List<TreeNode<T>> list, BiConsumer<TreeNode<T>, Tree<E>> consumer) {
        Assert.isTrue(!isBuild, "Current tree has been built.");
        final Map<E, Tree<E>> map = new LinkedHashMap<>(list.size(), 1);
        E rootId = this.root.getId();
        Tree<E> tree;
        for (TreeNode<T> treeNode : list) {
            tree = new Tree<>(this.root.getTreeNodeProperties());
            // 是否需要置入空数组
            if (this.isNullChildrenAsEmpty) {
                tree.setChildren(new ArrayList<>());
            }
            // 自定义解析为空，执行默认的方法
            if (null == consumer) {
                parseTo(treeNode, tree);
            } else {
                consumer.accept(treeNode, tree);
            }
            if (null != rootId && !rootId.getClass().equals(tree.getId().getClass())) {
                throw new IllegalArgumentException("rootId class type is not equals tree id class type!");
            }
            map.put(tree.getId(), tree);
        }
        this.treeMap.putAll(map);
        return this;
    }

    /**
     * 来源数据-》目标数据
     *
     * @param source 来源数据实体
     * @param target 目标节点实体
     */
    private <T> void parseTo(TreeNode<T> source, Tree<E> target) {
        target.setId((E) source.getId());
        target.setParentId((E) source.getParentId());
        target.setWeight(source.getWeight());
        target.setLabelValue(source.getLabelValue());
        // 扩展属性字段
        final Map<String, Object> extra = source.getExtra();
        if (MapUtil.isNotEmpty(extra)) {
            extra.forEach(target::putExtra);
        }
    }

    /**
     * 开始构建树
     */
    private void buildTreeMap() {
        if (MapUtil.isEmpty(this.treeMap)) {
            return;
        }
        final Map<E, Tree<E>> eTreeMap = MapUtil.comparingByValue(this.treeMap, isDesc);
        E parentId;
        for (Tree<E> node : eTreeMap.values()) {
            if (null == node) {
                continue;
            }
            parentId = node.getParentId();
            if (ObjectUtil.equals(this.root.getId(), parentId)) {
                this.root.addChildren(node);
                continue;
            }

            final Tree<E> parentNode = eTreeMap.get(parentId);
            if (null != parentNode) {
                parentNode.addChildren(node);
            }
        }
    }

    /**
     * <p>
     * 快速构建树
     * </p>
     *
     * @return this
     */
    public TreeBuilder<E> fastBuild() {
        buildTreeMap();
        // -1 默认不剪切
        if (this.deep > -1) {
            cutTree();
        }
        this.isBuild = true;
        this.treeMap.clear();
        return this;
    }

    private void cutTree() {
        cutTree(this.root, 0, this.deep);
    }

    /**
     * 剪切层级
     *
     * @param tree    树节点
     * @param curDepp 当前层级
     * @param maxDeep 最大层级
     */
    private void cutTree(Tree<E> tree, int curDepp, int maxDeep) {
        if (null == tree) {
            return;
        }
        if (curDepp == maxDeep) {
            tree.setChildren(new ArrayList<>());
            return;
        }

        final List<Tree<E>> children = tree.getChildren();
        if (!CollectionUtils.isEmpty(children)) {
            for (Tree<E> child : children) {
                cutTree(child, curDepp + 1, maxDeep);
            }
        }
    }

    /**
     * 构建单实体树
     *
     * @return 构建树实体
     */
    public Tree<E> toTree() {
        Assert.isTrue(isBuild, "Current tree has not been built.");
        return this.root;
    }

    /**
     * 构建树列表，没有顶层节点
     *
     * @return 构建树列表
     */
    public List<Tree<E>> toTreeList() {
        Assert.isTrue(isBuild, "Current tree has not been built.");
        return this.root.getChildren();
    }

    public static void main(String[] args) {
        // 构建 TreeNode 列表
        List<TreeNode<String>> nodeList = new ArrayList<>();
        nodeList.add(TreeNode.of("1", "0", "系统管理", 5, (extraMap) -> {
            extraMap.put("a", "1");
            extraMap.put("b", new Object());
            extraMap.put("c", new ArrayList<>());
        }));
        nodeList.add(TreeNode.of("11", "1", "用户管理", 222222, (extraMap) -> {
            extraMap.put("a", "1");
            extraMap.put("b", new Object());
            extraMap.put("c", new ArrayList<>());
        }));
        nodeList.add(TreeNode.of("111", "11", "用户添加", 0, (extraMap) -> {
            extraMap.put("a", "1");
            extraMap.put("b", new Object());
            extraMap.put("c", new ArrayList<>());
        }));

        nodeList.add(TreeNode.of("2", "0", "店铺管理", 1, (extraMap) -> {
            extraMap.put("a", "1");
            extraMap.put("b", new Object());
            extraMap.put("c", new ArrayList<>());
        }));
        nodeList.add(TreeNode.of("21", "2", "商品管理", 44, (extraMap) -> {
            extraMap.put("a", "1");
            extraMap.put("b", new Object());
            extraMap.put("c", new ArrayList<>());
        }));
        nodeList.add(TreeNode.of("221", "21", "商品添加", 2, (extraMap) -> {
            extraMap.put("a", "1");
            extraMap.put("b", new Object());
            extraMap.put("c", new ArrayList<>());
        }));

        List<Tree<String>> treeSingle = TreeBuilder.of("0").append(nodeList).deep(1).fastBuild().toTreeList();

        // System.out.println(JacksonUtil.toString(treeSingle));

        // 构建 SelectOption 列表
        List<SelectOption<String>> selectOptionsList = new ArrayList<>();
        selectOptionsList.add(SelectOption.of("0", "张三0", 5));
        selectOptionsList.add(SelectOption.of("1", "张三1", 4));
        selectOptionsList.add(SelectOption.of("2", "张三2", 3));
        selectOptionsList.add(SelectOption.of("3", "张三3", 2));
        selectOptionsList.add(SelectOption.of("4", "张三4", 7));
        selectOptionsList.add(SelectOption.of("5", "张三5", 8));
        selectOptionsList.add(SelectOption.of("6", "张三6", 1));

        List<Select<String>> select = SelectBuilder.of(selectOptionsList).desc().fastBuild().toSelectList();

        System.out.println(JacksonUtil.toString(select));

    }

}

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

import io.github.panxiaochao.spring3.core.utils.MapUtil;
import org.springframework.util.Assert;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

/**
 * <p>
 * 下拉菜单构建器, 默认升序
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-20
 */
public class SelectBuilder<E> implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Select<E> root;

    private final List<Select<E>> selectList;

    private final Map<E, Select<E>> selectMap;

    private boolean isBuild;

    private boolean isDesc;

    /**
     * 创建Select构建器
     *
     * @param selectList 加载数据
     * @param <E>        类型
     * @return SelectBuilder
     */
    public static <E> SelectBuilder<E> of(List<SelectOption<E>> selectList) {
        return new SelectBuilder<>(selectList, null);
    }

    /**
     * 创建Select构建器
     *
     * @param properties 下拉选项属性配置
     * @param <E>        ID类型
     * @return SelectBuilder
     */
    public static <E> SelectBuilder<E> of(List<SelectOption<E>> selectList, SelectOptionProperties properties) {
        return new SelectBuilder<>(selectList, properties);
    }

    /**
     * 私有参数构造
     *
     * @param properties 下拉选项属性配置
     */
    private SelectBuilder(List<SelectOption<E>> selectList, SelectOptionProperties properties) {
        this.root = new Select<>(properties);
        this.selectMap = MapUtil.newHashMap();
        this.selectList = new ArrayList<>();
        this.isDesc = false;
        this.append(selectList);
    }

    /**
     * 是否倒叙，默认升序
     *
     * @return this
     */
    public SelectBuilder<E> desc() {
        this.isDesc = true;
        return this;
    }

    /**
     * 增加下拉节点列表
     *
     * @param map 下拉节点列表
     * @return this
     */
    public SelectBuilder<E> append(Map<E, Select<E>> map) {
        Assert.isTrue(!isBuild, "Current select has not been built.");
        this.selectMap.putAll(map);
        return this;
    }

    /**
     * 增加节点列表
     *
     * @param <T>  Bean类型
     * @param list Bean列表
     * @return this
     */
    public <T> SelectBuilder<E> append(List<SelectOption<T>> list) {
        Assert.isTrue(!isBuild, "Current select has been built.");
        return append(list, null);
    }

    public <T> SelectBuilder<E> append(List<SelectOption<T>> list, BiConsumer<SelectOption<T>, Select<E>> consumer) {
        Assert.isTrue(!isBuild, "Current select has been built.");
        final Map<E, Select<E>> map = new LinkedHashMap<>(list.size(), 1);
        Select<E> select;
        for (SelectOption<T> selectOption : list) {
            select = new Select<>(this.root.getSelectOptionProperties());
            // 自定义解析为空，执行默认的方法
            if (null == consumer) {
                parseTo(selectOption, select);
            } else {
                consumer.accept(selectOption, select);
            }
            map.put(select.getKey(), select);
        }
        this.selectMap.putAll(map);
        return this;
    }

    /**
     * 来源数据-》目标数据
     *
     * @param source 来源数据实体
     * @param target 目标节点实体
     */
    private <T> void parseTo(SelectOption<T> source, Select<E> target) {
        target.setKey((E) source.getKey());
        target.setValue((E) source.getValue());
        target.setDisabled(source.isDisabled());
        target.setWeight(source.getWeight());
        target.setTitle(source.getTitle());
        // 扩展属性字段
        final Map<String, Object> extra = source.getExtra();
        if (MapUtil.isNotEmpty(extra)) {
            extra.forEach(target::putExtra);
        }
    }

    /**
     * 开始构建下拉菜单
     */
    private void buildSelectMap() {
        if (MapUtil.isEmpty(this.selectMap)) {
            return;
        }
        List<Select<E>> selectList = new ArrayList<>();
        for (Select<E> node : this.selectMap.values()) {
            if (null == node) {
                continue;
            }
            selectList.add(node);
        }
        final Comparator<Select<E>> comparator = getSelectComparator();
        selectList = selectList.stream().sorted(comparator).collect(Collectors.toList());
        // 置入
        this.selectList.addAll(selectList);
    }

    /**
     * 排序 比较结果，如果c1 < c2，返回数小于0，c1==c2返回0，c1 > c2 大于0
     *
     * @return Comparator 比较器
     */
    private Comparator<Select<E>> getSelectComparator() {
        Comparator<Select<E>> comparator = (o1, o2) -> {
            if (null == o1) {
                return 1;
            }
            final Comparable weight = o1.getWeight();
            final Comparable weightOther = o2.getWeight();

            if (weight == weightOther) {
                return 0;
            } else if (weight == null) {
                return -1;
            } else if (weightOther == null) {
                return 1;
            }
            return weight.compareTo(weightOther);
        };
        // 降序
        if (isDesc) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

    /**
     * 快速构建下拉菜单
     *
     * @return this
     */
    public SelectBuilder<E> fastBuild() {
        buildSelectMap();
        this.isBuild = true;
        this.selectMap.clear();
        return this;
    }

    /**
     * 构建下拉菜单列表
     *
     * @return 构建下拉菜单列表
     */
    public List<Select<E>> toSelectList() {
        Assert.isTrue(isBuild, "Current select has not been built.");
        return this.selectList;
    }

}

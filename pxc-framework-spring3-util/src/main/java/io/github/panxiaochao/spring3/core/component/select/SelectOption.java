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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * <p>
 * 下拉菜单选项类，用来存储每条节点信息
 * </p>
 *
 * @author Lypxc
 * @since 2023-12-20
 */
@Getter
public class SelectOption<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private boolean disabled;

    private T key;

    private CharSequence title;

    private T value;

    private Comparable<?> weight;

    /**
     * 扩展属性字段
     */
    private Map<String, Object> extra;

    /**
     * 构造方法
     *
     * @param key   和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title 选中该 Option 后，显示文本
     * @param <T>   节点参数类型
     * @return 下拉菜单节点
     */
    public static <T> SelectOption<T> of(T key, CharSequence title) {
        return of(false, key, title, key, null, null);
    }

    /**
     * 构造方法
     *
     * @param key    和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title  选中该 Option 后，显示文本
     * @param weight 权重值
     * @param <T>    节点参数类型
     * @return 下拉菜单节点
     */
    public static <T> SelectOption<T> of(T key, CharSequence title, Comparable<?> weight) {
        return of(false, key, title, key, weight, null);
    }

    /**
     * 构造方法
     *
     * @param key      和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title    选中该 Option 后，显示文本
     * @param extraMap 扩展属性
     * @param <T>      节点参数类型
     * @return 下拉菜单节点
     */
    public static <T> SelectOption<T> of(T key, CharSequence title, Consumer<Map<String, Object>> extraMap) {
        return of(false, key, title, key, null, extraMap);
    }

    /**
     * 构造方法
     *
     * @param key      和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title    选中该 Option 后，显示文本
     * @param weight   权重值
     * @param extraMap 扩展属性
     * @param <T>      节点参数类型
     * @return 下拉菜单节点
     */
    public static <T> SelectOption<T> of(T key, CharSequence title, Comparable<?> weight,
                                         Consumer<Map<String, Object>> extraMap) {
        return of(false, key, title, key, weight, extraMap);
    }

    /**
     * 构造方法
     *
     * @param disabled 是否禁用
     * @param key      和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title    选中该 Option 后，显示文本
     * @param extraMap 扩展属性
     * @param <T>      节点参数类型
     * @return 下拉菜单节点
     */
    public static <T> SelectOption<T> of(boolean disabled, T key, CharSequence title,
                                         Consumer<Map<String, Object>> extraMap) {
        return of(disabled, key, title, key, null, extraMap);
    }

    /**
     * 构造方法
     *
     * @param disabled 是否禁用
     * @param key      和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title    选中该 Option 后，显示文本
     * @param weight   权重值
     * @param extraMap 扩展属性
     * @param <T>      节点参数类型
     * @return 下拉菜单节点
     */
    public static <T> SelectOption<T> of(boolean disabled, T key, CharSequence title, Comparable<?> weight,
                                         Consumer<Map<String, Object>> extraMap) {
        return of(disabled, key, title, key, weight, extraMap);
    }

    /**
     * 静态构造方法
     *
     * @param disabled 是否禁用
     * @param key      和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title    选中该 Option 后，显示文本
     * @param value    默认根据此属性值进行筛选
     * @param weight   权重值
     * @param extraMap 扩展属性
     * @param <T>      节点参数类型
     * @return 下拉菜单选项
     */
    public static <T> SelectOption<T> of(boolean disabled, T key, CharSequence title, T value, Comparable<?> weight,
                                         Consumer<Map<String, Object>> extraMap) {
        return new SelectOption<T>(disabled, key, title, value, weight, extraMap);
    }

    /**
     * 构造方法
     *
     * @param disabled 是否禁用
     * @param key      和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     * @param title    选中该 Option 后，显示文本
     * @param value    默认根据此属性值进行筛选
     * @param weight   权重值
     * @param extraMap 扩展属性
     */
    private SelectOption(boolean disabled, T key, CharSequence title, T value, Comparable<?> weight,
                         Consumer<Map<String, Object>> extraMap) {
        this.disabled = disabled;
        this.key = key;
        this.title = title;
        this.weight = weight;
        this.value = value;
        this.extra = new LinkedHashMap<>();
        if (extraMap != null) {
            extraMap.accept(this.extra);
        }
    }

    /**
     * 是否禁用
     */
    public SelectOption<T> setDisabled(boolean disabled) {
        this.disabled = disabled;
        return this;
    }

    /**
     * 和 value 含义一致。如果 Vue 需要你设置此项，此项值与 value 的值相同，然后可以省略 value 设置
     */
    public SelectOption<T> setKey(T key) {
        this.key = key;
        return this;
    }

    /**
     * 默认根据此属性值进行筛选
     */
    public SelectOption<T> setValue(T value) {
        this.value = value;
        return this;
    }

    /**
     * 选中该 Option 后，Select 的 title
     */
    public SelectOption<T> setTitle(CharSequence title) {
        this.title = title;
        return this;
    }

    /**
     * 顺序 越小优先级越高
     */
    public SelectOption<T> setWeight(Comparable<?> weight) {
        this.weight = weight;
        return this;
    }

    /**
     * 设置扩展字段
     *
     * @param extra 扩展字段
     * @return this
     */
    public SelectOption<T> setExtra(Map<String, Object> extra) {
        this.extra = extra;
        return this;
    }

    /**
     * 设置扩展字段
     *
     * @param extra 扩展字段
     * @return this
     */
    public SelectOption<T> setExtra(Consumer<Map<String, Object>> extra) {
        extra.accept(this.extra);
        return this;
    }

    /**
     * 设置所有扩展字段
     *
     * @param extraAll 扩展字段
     * @return this
     */
    public SelectOption<T> setExtraAll(Map<String, Object> extraAll) {
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
        SelectOption<?> selectOption = (SelectOption<?>) o;
        return Objects.equals(key, selectOption.getKey());
    }

    public int hashCode() {
        return Objects.hash(key);
    }

}

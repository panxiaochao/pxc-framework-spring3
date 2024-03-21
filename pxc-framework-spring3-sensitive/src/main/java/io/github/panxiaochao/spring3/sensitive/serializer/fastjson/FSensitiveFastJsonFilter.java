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
package io.github.panxiaochao.spring3.sensitive.serializer.fastjson;

import com.alibaba.fastjson.serializer.ValueFilter;
import io.github.panxiaochao.spring3.sensitive.annotation.FSensitive;
import io.github.panxiaochao.spring3.sensitive.enums.FSensitiveStrategy;
import io.github.panxiaochao.spring3.sensitive.strategy.AbstractFSensitiveStrategy;
import io.github.panxiaochao.spring3.sensitive.utils.InvokeMethodSensitiveUtil;

import java.lang.reflect.Field;
import java.util.Objects;

/**
 * <p>
 * FastJson 1.X 版本过滤器
 * </p>
 *
 * @author Lypxc
 * @since 2023-09-01
 */
public class FSensitiveFastJsonFilter implements ValueFilter {

    @Override
    public Object process(Object object, String name, Object value) {
        if (Objects.isNull(value) || !(value instanceof String)) {
            return value;
        }
        // 获取字段上注解
        try {
            Field field = object.getClass().getDeclaredField(name);
            FSensitive fSensitive = field.getAnnotation(FSensitive.class);
            if (Objects.isNull(fSensitive) || field.getType() != String.class) {
                return value;
            }
            // 获取属性
            FSensitiveStrategy strategy = fSensitive.strategy();
            String customStrategyClassName = fSensitive.customStrategy().getName();
            // 相同的class，使用自带策略
            if (customStrategyClassName.equals(AbstractFSensitiveStrategy.class.getName())) {
                return strategy.desensitize().apply(value.toString());
            } else {
                return InvokeMethodSensitiveUtil.invokeSensitiveMethod(customStrategyClassName, value);
            }
        } catch (Exception e) {
            // 使用默认转换
            return value;
        }
    }

}

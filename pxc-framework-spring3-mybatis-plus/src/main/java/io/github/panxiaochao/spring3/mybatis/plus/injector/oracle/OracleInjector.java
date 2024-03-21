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
package io.github.panxiaochao.spring3.mybatis.plus.injector.oracle;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.injector.DefaultSqlInjector;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import io.github.panxiaochao.spring3.mybatis.plus.injector.mysql.MySqlUpdateBatchSomeColumn;

import java.util.List;

/**
 * <p>
 * Oracle 注入器，注意：支持Oracle的注入器！！！
 * </p>
 *
 * @author Lypxc
 * @since 2023-11-03
 */
public class OracleInjector extends DefaultSqlInjector {

    @Override
    public List<AbstractMethod> getMethodList(Class<?> mapperClass, TableInfo tableInfo) {
        // 获取 MyBatis-Plus 自带方法
        List<AbstractMethod> methodList = super.getMethodList(mapperClass, tableInfo);
        // 增加自定义方法，字段注解上不等于 FieldFill.UPDATE 的字段才会插入
        methodList.add(new OracleInsertBatchSomeColumn(i -> i.getFieldFill() != FieldFill.UPDATE));
        methodList.add(new MySqlUpdateBatchSomeColumn());
        return methodList;
    }

}

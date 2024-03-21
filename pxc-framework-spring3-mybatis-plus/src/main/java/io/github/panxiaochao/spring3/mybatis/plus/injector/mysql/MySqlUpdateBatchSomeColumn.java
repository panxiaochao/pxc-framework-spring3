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
package io.github.panxiaochao.spring3.mybatis.plus.injector.mysql;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import io.github.panxiaochao.spring3.core.utils.StringPools;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * <p>
 * 批量更新注入器
 * </p>
 *
 * <b> 注意：这种实现方式要 特别注意数据库SQL语句的长度限制，在进行数据合并在同一SQL中务必不能超过SQL长度限制，通过max_allowed_packet
 * 配置可以修改，默认是1M，测试时修改为8M。 </b>
 *
 * @author Lypxc
 * @since 2023-09-08
 */
public class MySqlUpdateBatchSomeColumn extends AbstractMethod {

    private static final long serialVersionUID = 794731765508860279L;

    private static final String METHOD_NAME = "updateBatchSomeColumn";

    /**
     * 默认方法名
     */
    public MySqlUpdateBatchSomeColumn() {
        super(METHOD_NAME);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        String sql = "<script>\n<foreach collection=\"list\" item=\"item\" separator=\";\">\nupdate %s %s where %s=#{%s} %s\n</foreach>\n</script>";
        String additional = tableInfo.isWithVersion() ? tableInfo.getVersionFieldInfo().getVersionOli("item", "item.")
                : StringPools.EMPTY + tableInfo.getLogicDeleteSql(true, true);
        String setSql = sqlSet(tableInfo.isWithLogicDelete(), false, tableInfo, false, "item", "item.");
        String sqlResult = String.format(sql, tableInfo.getTableName(), setSql, tableInfo.getKeyColumn(),
                "item." + tableInfo.getKeyProperty(), additional);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sqlResult, modelClass);
        return this.addUpdateMappedStatement(mapperClass, modelClass, METHOD_NAME, sqlSource);
    }

}

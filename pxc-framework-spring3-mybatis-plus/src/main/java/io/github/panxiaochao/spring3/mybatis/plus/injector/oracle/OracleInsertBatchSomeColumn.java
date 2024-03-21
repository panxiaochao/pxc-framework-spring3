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

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableFieldInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.metadata.TableInfoHelper;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.executor.keygen.Jdbc3KeyGenerator;
import org.apache.ibatis.executor.keygen.KeyGenerator;
import org.apache.ibatis.executor.keygen.NoKeyGenerator;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * <p>
 * Oracle批量新增，自定义.
 * </p>
 *
 * @author Lypxc
 * @since 2023-11-03
 */
public class OracleInsertBatchSomeColumn extends AbstractMethod {

    private static final long serialVersionUID = 2784412040297613815L;

    /**
     * 字段筛选条件
     */
    @Setter
    @Accessors(chain = true)
    private Predicate<TableFieldInfo> predicate;

    private static final String INSERT_BATCH_SQL = "<script>\nINSERT ALL \n  %s\n</script>";

    /**
     * 默认方法名
     *
     * @param predicate 字段筛选条件
     */
    public OracleInsertBatchSomeColumn(Predicate<TableFieldInfo> predicate) {
        super("insertBatchSomeColumn");
        this.predicate = predicate;
    }

    @SuppressWarnings("Duplicates")
    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        // pojo类型为Map时禁用
        if (tableInfo.getEntityType().equals(Map.class)) {
            return null;
        }
        // return super.injectMappedStatement(mapperClass,modelClass,tableInfo);
        KeyGenerator keyGenerator = new NoKeyGenerator();
        SqlMethod sqlMethod = SqlMethod.INSERT_ONE;
        List<TableFieldInfo> fieldList = tableInfo.getFieldList();
        String insertSqlColumn = tableInfo.getKeyInsertSqlColumn(true, null, false)
                + this.filterTableFieldInfo(fieldList, predicate, TableFieldInfo::getInsertSqlColumn, EMPTY);
        String columns = insertSqlColumn.substring(0, insertSqlColumn.length() - 1);
        String insertSqlProperty = tableInfo.getKeyInsertSqlProperty(true, ENTITY_DOT, false)
                + this.filterTableFieldInfo(fieldList, predicate, i -> i.getInsertSqlProperty(ENTITY_DOT), EMPTY);
        insertSqlProperty = LEFT_BRACKET + insertSqlProperty.substring(0, insertSqlProperty.length() - 1)
                + RIGHT_BRACKET;
        String valuesScript = convertForeach(insertSqlProperty, "list", tableInfo.getTableName(), columns, ENTITY,
                NEWLINE);
        String keyProperty = null;
        String keyColumn = null;
        // 表包含主键处理逻辑,如果不包含主键当普通字段处理
        if (tableInfo.havePK()) {
            if (tableInfo.getIdType() == IdType.AUTO) {
                /* 自增主键 */
                keyGenerator = new Jdbc3KeyGenerator();
                keyProperty = tableInfo.getKeyProperty();
                keyColumn = tableInfo.getKeyColumn();
            } else {
                if (null != tableInfo.getKeySequence()) {
                    keyGenerator = TableInfoHelper.genKeyGenerator(this.methodName, tableInfo, builderAssistant);
                    keyProperty = tableInfo.getKeyProperty();
                    keyColumn = tableInfo.getKeyColumn();
                }
            }
        }
        String sql = String.format(INSERT_BATCH_SQL, valuesScript);
        SqlSource sqlSource = languageDriver.createSqlSource(configuration, sql, modelClass);
        return this.addInsertMappedStatement(mapperClass, modelClass, this.methodName, sqlSource, keyGenerator,
                keyProperty, keyColumn);
    }

    public static String convertForeach(final String sqlScript, final String collection, final String tableName,
                                        final String columns, final String item, final String separator) {
        StringBuilder sb = new StringBuilder("<foreach");

        if (StringUtils.isNotBlank(collection)) {
            sb.append(" collection=\"").append(collection).append("\"");
        }

        if (StringUtils.isNotBlank(item)) {
            sb.append(" item=\"").append(item).append("\"");
        }

        if (StringUtils.isNotBlank(separator)) {
            sb.append(" separator=\"").append(separator).append("\"");
        }

        sb.append(">").append("\n");

        if (StringUtils.isNotBlank(tableName)) {
            sb.append(" INTO ").append(tableName).append(" ");
        }

        if (StringUtils.isNotBlank(columns)) {
            sb.append(LEFT_BRACKET).append(columns).append(RIGHT_BRACKET).append(" VALUES ");
        }

        return sb.append(sqlScript)
                .append("\n")
                .append("</foreach>\n")
                .append(" SELECT ")
                .append("*")
                .append(" FROM dual")
                .toString();
    }

}

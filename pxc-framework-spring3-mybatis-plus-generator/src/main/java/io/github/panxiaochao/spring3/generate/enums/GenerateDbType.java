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
package io.github.panxiaochao.spring3.generate.enums;

import com.baomidou.mybatisplus.generator.config.IKeyWordsHandler;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.DB2TypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.OracleTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.SqlServerTypeConvert;
import com.baomidou.mybatisplus.generator.config.converts.SqliteTypeConvert;
import com.baomidou.mybatisplus.generator.config.querys.AbstractDbQuery;
import com.baomidou.mybatisplus.generator.config.querys.DB2Query;
import com.baomidou.mybatisplus.generator.config.querys.H2Query;
import com.baomidou.mybatisplus.generator.config.querys.MySqlQuery;
import com.baomidou.mybatisplus.generator.config.querys.OracleQuery;
import com.baomidou.mybatisplus.generator.config.querys.SqlServerQuery;
import com.baomidou.mybatisplus.generator.config.querys.SqliteQuery;
import com.baomidou.mybatisplus.generator.keywords.H2KeyWordsHandler;
import com.baomidou.mybatisplus.generator.keywords.MySqlKeyWordsHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <p>
 * 数据库类型枚举.
 * </p>
 *
 * @author Lypxc
 * @since 2023-02-14
 */
@Getter
@AllArgsConstructor
public enum GenerateDbType {

    /**
     * MYSQL
     */
    MYSQL("mysql", new MySqlQuery(), new MySqlTypeConvert(), new MySqlKeyWordsHandler(), "MySql数据库"),
    /**
     * ORACLE
     */
    ORACLE("oracle", new OracleQuery(), new OracleTypeConvert(), new MySqlKeyWordsHandler(),
            "Oracle11g及以下数据库(高版本推荐使用ORACLE_NEW)"),
    /**
     * DB2
     */
    DB2("db2", new DB2Query(), new DB2TypeConvert(), new MySqlKeyWordsHandler(), "DB2数据库"),
    /**
     * H2
     */
    H2("h2", new H2Query(), new MySqlTypeConvert(), new H2KeyWordsHandler(), "H2数据库"),
    /**
     * SQLITE
     */
    SQLITE("sqlite", new SqliteQuery(), new SqliteTypeConvert(), new MySqlKeyWordsHandler(), "SQLite数据库"),
    /**
     * SQLSERVER2005
     */
    SQL_SERVER2005("sqlserver2005", new SqlServerQuery(), new SqlServerTypeConvert(), new MySqlKeyWordsHandler(),
            "SQLServer2005数据库"),
    /**
     * SQLSERVER
     */
    SQL_SERVER("sqlserver", new SqlServerQuery(), new SqlServerTypeConvert(), new MySqlKeyWordsHandler(),
            "SQLServer数据库");

    private final String dbName;

    private final AbstractDbQuery dbQuery;

    private final ITypeConvert typeConvert;

    private final IKeyWordsHandler keyWordsHandler;

    private final String desc;

    public static final Map<String, GenerateDbType> GENERATE_DB_TYPE_MAP = Arrays.stream(GenerateDbType.values())
            .collect(Collectors.toMap(GenerateDbType::getDbName, Function.identity()));

}

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
package io.github.panxiaochao.spring3.generate.tool;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.IFill;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.TemplateConfig;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.fill.Column;
import com.baomidou.mybatisplus.generator.fill.Property;
import io.github.panxiaochao.spring3.generate.enhance.EnhanceFreemarkerTemplateEngine;
import io.github.panxiaochao.spring3.generate.enums.GenerateDbType;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 代码生成器工具类.
 * </p>
 *
 * @author Lypxc
 * @since 2023-02-14
 */
public class PxcMybatisPlusGeneratorTools {

    private static final Builder INSTANCE = new Builder();

    private PxcMybatisPlusGeneratorTools() {
    }

    /**
     * 获取实例
     *
     * @return PxcMybatisPlusGeneratorTools
     */
    public static Builder builder() {
        return INSTANCE;
    }

    /**
     * 代码生成
     */
    private void build(Builder builder) {
        FastAutoGenerator.create(createDataSourceConfig(builder))
                .globalConfig(globalConfigBuilder -> this.createGlobalConfig(globalConfigBuilder, builder))
                .packageConfig(packageConfigBuilder -> this.createPackageConfig(packageConfigBuilder, builder))
                .strategyConfig(strategyConfigBuilder -> this.createStrategyConfig(strategyConfigBuilder, builder))
                .templateConfig(templateConfigBuilder -> this.createTemplateConfig(templateConfigBuilder, builder))
                // 使用Freemarker引擎，默认使用Velocity引擎
                .templateEngine(new EnhanceFreemarkerTemplateEngine())
                .execute();
    }

    /**
     * 数据库配置
     *
     * @return DataSourceConfig
     */
    private DataSourceConfig.Builder createDataSourceConfig(Builder builder) {
        GenerateDbType currentDbType = GenerateDbType.GENERATE_DB_TYPE_MAP.get(builder.dbType);
        return new DataSourceConfig.Builder(builder.jdbcUrl, builder.username, builder.password)
                .dbQuery(currentDbType.getDbQuery())
                .typeConvert(currentDbType.getTypeConvert())
                .keyWordsHandler(currentDbType.getKeyWordsHandler());
    }

    /**
     * 全局配置
     *
     * @param builder GlobalBuilder
     */
    private void createGlobalConfig(GlobalConfig.Builder globalConfigBuilder, Builder builder) {
        final String pattern = "yyyy-MM-dd";
        if (builder.springdoc) {
            globalConfigBuilder.enableSpringdoc();
        }
        globalConfigBuilder.author(builder.author)
                .dateType(DateType.ONLY_DATE)
                .commentDate(pattern)
                .outputDir(builder.outputDir + "/src/main/java")
                .disableOpenDir();
    }

    /**
     * 包配置
     *
     * @param builder PackageBuilder
     */
    private void createPackageConfig(PackageConfig.Builder packageConfigBuilder, Builder builder) {
        packageConfigBuilder.parent(builder.parent)
                .moduleName(builder.moduleName)
                .entity(builder.entity)
                .pathInfo(Collections.singletonMap(OutputFile.xml, builder.outputDir + "/src/main/resources/mapper"));
    }

    /**
     * 策略配置
     *
     * @param builder StrategyBuilder
     */
    private void createStrategyConfig(StrategyConfig.Builder strategyBuilder, Builder builder) {
        // Entity 策略配置
        strategyBuilder.entityBuilder()
                .disableSerialVersionUID()
                .enableFileOverride()
                .enableLombok()
                .enableChainModel()
                .enableRemoveIsPrefix()
                .enableTableFieldAnnotation()
                .naming(NamingStrategy.underline_to_camel)
                .columnNaming(NamingStrategy.underline_to_camel)
                .idType(IdType.AUTO);
//			.formatFileName("%sPO");
        if (!CollectionUtils.isEmpty(builder.insertFields)) {
            strategyBuilder.entityBuilder().addTableFills(builder.insertFields);
        }
        if (!CollectionUtils.isEmpty(builder.updateFields)) {
            strategyBuilder.entityBuilder().addTableFills(builder.updateFields);
        }

        // Controller 策略配置
        strategyBuilder.controllerBuilder()
                .enableFileOverride()
                .enableHyphenStyle()
                .enableRestStyle()
                .formatFileName("%sApi");

        // Service 策略配置
        strategyBuilder.serviceBuilder()
                .enableFileOverride()
                .formatServiceFileName("%sService")
                .formatServiceImplFileName("%sServiceImpl");

        // Mapper 策略配置
        strategyBuilder.mapperBuilder()
                .enableFileOverride()
                .mapperAnnotation(org.apache.ibatis.annotations.Mapper.class)
                .enableBaseResultMap()
                .enableBaseColumnList()
                .formatMapperFileName("%sMapper")
                .formatXmlFileName("%sMapper");

        // 需要生成的表名
        if (!CollectionUtils.isEmpty(builder.includes)) {
            strategyBuilder.addInclude(builder.includes);
        }

        // 需要排除的表名
        if (!CollectionUtils.isEmpty(builder.excludes)) {
            strategyBuilder.addExclude(builder.excludes);
        }
    }

    /**
     * 模版引擎配置
     *
     * @param templateConfigBuilder 模版引擎生成器
     * @param builder               自定义配置
     */
    private void createTemplateConfig(TemplateConfig.Builder templateConfigBuilder, Builder builder) {
        templateConfigBuilder.controller("/templates/controller.java")
                .entity("/templates/entity.java")
                .service("/templates/service.java")
                .serviceImpl("/templates/serviceImpl.java")
                .mapper("/templates/mapper.java")
                .xml("/templates/mapper.xml")
                .controller("/templates/controller.java")
                .build();
    }

    /**
     * 自定义Builder
     */
    public static class Builder {

        /**
         * 数据库链接
         */
        private String jdbcUrl;

        /**
         * 数据库用户名
         */
        private String username;

        /**
         * 数据库密码
         */
        private String password;

        /**
         * 数据库类型
         */
        private String dbType;

        /**
         * 作者
         */
        private String author = "Lypxc";

        /**
         * 开启 swagger 3.0 springdoc
         */
        private boolean springdoc;

        /**
         * 生成文件的输出目录
         */
        private String outputDir;

        /**
         * 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
         */
        private String parent = "io.github.panxiaochao";

        /**
         * 父包模块名
         */
        private String moduleName = "";

        /**
         * Entity包名
         */
        private String entity = "entity";

        /**
         * 新增填充字段
         */
        private final List<IFill> insertFields = new ArrayList<>();

        /**
         * 更新填充字段
         */
        private final List<IFill> updateFields = new ArrayList<>();

        /**
         * 包含的表
         */
        private final List<String> includes = new ArrayList<>();

        /**
         * 排除的表
         */
        private final List<String> excludes = new ArrayList<>();

        /**
         * Instantiates a new Builder.
         */
        private Builder() {
            // use static factory method in parent class
        }

        public Builder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder dbType(GenerateDbType dbType) {
            GenerateDbType generateDbType = GenerateDbType.GENERATE_DB_TYPE_MAP.get(dbType.getDbName());
            Objects.requireNonNull(generateDbType, () -> generateDbType.getDbName() + " not supported now");
            this.dbType = generateDbType.getDbName();
            return this;
        }

        public Builder author(String author) {
            this.author = author;
            return this;
        }

        public Builder enableSpringdoc() {
            this.springdoc = true;
            return this;
        }

        public Builder outputDir(String outputDir) {
            this.outputDir = outputDir;
            return this;
        }

        public Builder parent(String parent) {
            this.parent = parent;
            return this;
        }

        public Builder moduleName(String moduleName) {
            this.moduleName = moduleName;
            return this;
        }

        public Builder entityName(String entityName) {
            this.entity = entityName;
            return this;
        }

        public Builder insertFields(String... insertFields) {
            return insertFields(Arrays.asList(insertFields));
        }

        public Builder insertFields(List<String> insertFields) {
            List<IFill> tableFills = new ArrayList<>();
            insertFields.forEach(insertField -> tableFills.add(new Column(insertField, FieldFill.INSERT)));
            this.insertFields.addAll(tableFills);
            return this;
        }

        public Builder updateFields(String... updateFields) {
            return updateFields(Arrays.asList(updateFields));
        }

        public Builder updateFields(List<String> updateFields) {
            List<IFill> tableFills = new ArrayList<>();
            updateFields.forEach(updateField -> tableFills.add(new Property(updateField, FieldFill.INSERT_UPDATE)));
            this.updateFields.addAll(tableFills);
            return this;
        }

        public Builder includes(String... includes) {
            return includes(Arrays.asList(includes));
        }

        public Builder includes(List<String> includes) {
            this.includes.addAll(includes);
            return this;
        }

        public Builder excludes(String... excludes) {
            return excludes(Arrays.asList(excludes));
        }

        public Builder excludes(List<String> excludes) {
            this.excludes.addAll(excludes);
            return this;
        }

        /**
         * Build MybatisPlusGenerator Code.
         */
        public void build() {
            Objects.requireNonNull(jdbcUrl, () -> "jdbcUrl is required");
            Objects.requireNonNull(username, () -> "username is required");
            Objects.requireNonNull(password, () -> "password is required");
            Objects.requireNonNull(dbType, () -> "dbType is required");
            Objects.requireNonNull(outputDir, () -> "outputDir is required");
            Objects.requireNonNull(parent, () -> "parent is required");
            new PxcMybatisPlusGeneratorTools().build(this);
        }

    }

}

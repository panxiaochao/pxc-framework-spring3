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
package io.github.panxiaochao.spring3.generate.enhance;

import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.builder.ConfigBuilder;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.engine.AbstractTemplateEngine;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import io.github.panxiaochao.spring3.core.utils.StringPools;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 * Freemarker 模版引擎增强.
 * </p>
 *
 * @author Lypxc
 * @since 2023-02-17
 */
public class EnhanceFreemarkerTemplateEngine extends FreemarkerTemplateEngine {

    private static final String SRC_MAIN_JAVA = "src/main/java";

    private static final String SRC_MAIN_RESOURCES = "src/main/resources";

    private static final String APPLICATION_JAVA_FILE = "Application.java";

    private static final String APPLICATION_YML_FILE = "application.yml";

    private static final String TEMPLATE_APPLICATION_JAVA_FILE_PATH = "/templates/application/";

    private static final String POM_XML_FILE = "pom.xml";

    private static final String TEMPLATE_POM_XML_FILE_PATH = "/templates/pom/";

    /**
     * 渲染对象 MAP 信息
     *
     * @param configBuilder 配置信息
     * @param tableInfo     表信息对象
     * @return ignore
     */
    @Override
    public Map<String, Object> getObjectMap(ConfigBuilder configBuilder, TableInfo tableInfo) {
        // 继承原先渲对象的逻辑
        Map<String, Object> objectMap = super.getObjectMap(configBuilder, tableInfo);
        // 以下加入自定义
        // do something
        return objectMap;
    }

    /**
     * 批量输出 java xml 文件
     */
    @Override
    public AbstractTemplateEngine batchOutput() {
        // 首先使用父功能
        super.batchOutput();
        // 以下加入自定义
        try {
            ConfigBuilder config = this.getConfigBuilder();
            // Application.java
            outputApplication(config);
            // application.yml
            outputApplicationYml(config);
            // pom.xml
            outputPom(config);
        } catch (Exception e) {
            throw new RuntimeException("无法创建文件，请检查配置信息！", e);
        }

        return this;
    }

    /**
     * 输出 Application.java
     *
     * @param config 配置汇总
     */
    private void outputApplication(ConfigBuilder config) {
        String parentPackage = config.getPackageConfig().getParent();
        String parentPath = config.getPathInfo().get(OutputFile.parent);
        if (StringUtils.hasText(config.getPackageConfig().getModuleName())) {
            parentPackage = parentPackage.replaceAll(StringPools.DOT + config.getPackageConfig().getModuleName(),
                    StringPools.EMPTY);
            parentPath = parentPath.replace(config.getPackageConfig().getModuleName(), StringPools.EMPTY);
        }
        Map<String, Object> objectMap = new HashMap<>(3);
        objectMap.put("parentPackage", parentPackage);
        objectMap.put("author", config.getGlobalConfig().getAuthor());
        objectMap.put("date", config.getGlobalConfig().getCommentDate());

        String applicationFilePath = parentPath + APPLICATION_JAVA_FILE;
        String templateApplicationName = templateFilePath(APPLICATION_JAVA_FILE).toLowerCase();
        String templateApplicationPath = TEMPLATE_APPLICATION_JAVA_FILE_PATH + templateApplicationName;
        try {
            super.writer(objectMap, templateApplicationPath, new File(applicationFilePath));
        } catch (Exception exception) {
            throw new RuntimeException("创建 Application.java 文件失败！", exception);
        }
    }

    /**
     * 输出 application.yml
     *
     * @param config 配置汇总
     */
    private void outputApplicationYml(ConfigBuilder config) {
        String outputDir = config.getGlobalConfig().getOutputDir();
        outputDir = outputDir.replace(SRC_MAIN_JAVA, SRC_MAIN_RESOURCES);
        Map<String, Object> objectMap = new HashMap<>(3);

        String applicationYmlFilePath = outputDir + File.separator + APPLICATION_YML_FILE;
        String templateApplicationYmlName = templateFilePath(APPLICATION_YML_FILE);
        String templateApplicationYmlPath = TEMPLATE_APPLICATION_JAVA_FILE_PATH + templateApplicationYmlName;
        try {
            super.writer(objectMap, templateApplicationYmlPath, new File(applicationYmlFilePath));
        } catch (Exception exception) {
            throw new RuntimeException("创建 application.yml 文件失败！", exception);
        }
    }

    /**
     * 输出 pom.xml
     *
     * @param config 配置汇总
     */
    private void outputPom(ConfigBuilder config) {
        String outputDir = config.getGlobalConfig().getOutputDir();
        outputDir = outputDir.replace(SRC_MAIN_JAVA, StringPools.EMPTY);
        Map<String, Object> objectMap = new HashMap<>(3);

        String pomFilePath = outputDir + POM_XML_FILE;
        String templatePomName = templateFilePath(POM_XML_FILE);
        String templatePomPath = TEMPLATE_POM_XML_FILE_PATH + templatePomName;
        try {
            super.writer(objectMap, templatePomPath, new File(pomFilePath));
        } catch (Exception exception) {
            throw new RuntimeException("创建 pom.xml 文件失败！", exception);
        }
    }

}

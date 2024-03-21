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
package io.github.panxiaochao.spring3.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.github.panxiaochao.spring3.core.utils.jackson.CustomizeJavaTimeModule;
import io.github.panxiaochao.spring3.core.utils.jackson.jsonserializer.NullValueJsonSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

/**
 * <p>
 * JackSon 工具类
 * </p>
 *
 * @author Lypxc
 * @since 2024-03-20
 */
public class JacksonUtil {

    /**
     * not init
     */
    private JacksonUtil() {
        throw new RuntimeException("can't be construct");
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(JacksonUtil.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 默认日期时间格式
     */
    private static final String LOCAL_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 默认日期格式
     */
    private static final String LOCAL_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * 默认时间格式
     */
    private static final String DATE_TIME_FORMAT = "HH:mm:ss";

    static {
        OBJECT_MAPPER.setLocale(Locale.CHINA);
        // 对象的所有字段全部列入，还是其他的选项，可以忽略null等
        OBJECT_MAPPER.setDefaultPropertyInclusion(Include.ALWAYS);
        // 设置时区
        OBJECT_MAPPER.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        // 设置Date类型的序列化及反序列化格式
        OBJECT_MAPPER.setDateFormat(new SimpleDateFormat(LOCAL_DATE_TIME_FORMAT));
        // 忽略空Bean转json的错误
        OBJECT_MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略未知属性，防止json字符串中存在，java对象中不存在对应属性的情况出现错误
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        // 注册一个时间序列化及反序列化的处理模块，用于解决jdk8中localDateTime等的序列化问题
        OBJECT_MAPPER.registerModule(new CustomizeJavaTimeModule());
        // 空值处理
        OBJECT_MAPPER.getSerializerProvider().setNullValueSerializer(NullValueJsonSerializer.INSTANCE);
    }

    /**
     * @return ObjectMapper
     */
    public static ObjectMapper objectMapper() {
        return OBJECT_MAPPER;
    }

    /**
     * Object to json string.
     *
     * @param obj obj
     * @return Json String
     */
    public static String toString(Object obj) {
        if (obj != null) {
            if (obj.getClass() == String.class) {
                return (String) obj;
            }
            try {
                return OBJECT_MAPPER.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                LOGGER.error("json序列化出错：{}", obj, e);
                return null;
            }
        }
        return null;
    }

    /**
     * @param json   json
     * @param tClass class
     * @param <T>    T类型
     * @return T类型
     */
    public static <T> T toBean(String json, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(json, tClass);
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * @param inputStream 流
     * @param tClass      class
     * @param <T>         T类型
     * @return T类型
     */
    public static <T> T toBean(InputStream inputStream, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(inputStream, tClass);
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", inputStream.toString(), e);
            return null;
        }
    }

    /**
     * Object to json string byte array.
     *
     * @param obj obj
     * @return json string byte array
     */
    public static byte[] toJsonBytes(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            LOGGER.error("json解析出错：{}", obj, e);
            return null;
        }
    }

    /**
     * Json string deserialize to Object.
     *
     * @param bytes  json string
     * @param tClass class of obj
     * @param <T>    General type
     * @return T类型
     */
    public static <T> T toBean(byte[] bytes, Class<T> tClass) {
        try {
            return OBJECT_MAPPER.readValue(bytes, tClass);
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", new String(bytes), e);
            return null;
        }
    }

    /**
     * Json string deserialize to Object.
     *
     * @param json          json string byte array
     * @param typeReference {@link TypeReference} of object
     * @param <T>           General type
     * @return object
     */
    public static <T> T toBean(byte[] json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (Exception e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * Json string deserialize to Object.
     *
     * @param json          json string
     * @param typeReference {@link TypeReference} of object
     * @param <T>           General type
     * @return object
     */
    public static <T> T toObj(String json, TypeReference<T> typeReference) {
        try {
            return toObj(OBJECT_MAPPER.readTree(json), typeReference);
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * Json node deserialize to Object.
     *
     * @param jsonNode      json node
     * @param typeReference {@link TypeReference} of object
     * @param <T>           General type
     * @return object
     */
    public static <T> T toObj(JsonNode jsonNode, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(jsonNode.traverse(OBJECT_MAPPER), typeReference);
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", jsonNode, e);
            return null;
        }
    }

    /**
     * Json string deserialize to Object.
     *
     * @param json json string
     * @param cls  {@link Type} of object
     * @param <T>  General type
     * @return object
     */
    public static <T> T toBean(byte[] json, Type cls) {
        try {
            return OBJECT_MAPPER.readValue(json, OBJECT_MAPPER.constructType(cls));
        } catch (Exception e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * @param json   json
     * @param eClass class
     * @param <E>    E
     * @return E
     */
    public static <E> List<E> toList(String json, Class<E> eClass) {
        try {
            return OBJECT_MAPPER.readValue(json,
                    OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, eClass));
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * @param json json
     * @param <T>  T类型
     * @return T类型
     */
    public static <T> T toMap(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<T>() {
            });
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * @param json   json
     * @param kClass class
     * @param vClass class
     * @param <K>    K
     * @param <V>    V
     * @return <K, V>
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> kClass, Class<V> vClass) {
        try {
            return OBJECT_MAPPER.readValue(json,
                    OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, kClass, vClass));
        } catch (IOException e) {
            LOGGER.error("json解析出错：{}", json, e);
            return null;
        }
    }

    /**
     * Register sub type for child class.
     *
     * @param clz  child class
     * @param type type name of child class
     */
    public static void registerSubtype(Class<?> clz, String type) {
        OBJECT_MAPPER.registerSubtypes(new NamedType(clz, type));
    }

    /**
     * Create a new empty Jackson {@link ObjectNode}.
     *
     * @return {@link ObjectNode}
     */
    public static ObjectNode createEmptyJsonNode() {
        return new ObjectNode(OBJECT_MAPPER.getNodeFactory());
    }

    /**
     * Create a new empty Jackson {@link ArrayNode}.
     *
     * @return {@link ArrayNode}
     */
    public static ArrayNode createEmptyArrayNode() {
        return new ArrayNode(OBJECT_MAPPER.getNodeFactory());
    }

    /**
     * Parse object to Jackson {@link JsonNode}.
     *
     * @param obj object
     * @return {@link JsonNode}
     */
    public static JsonNode transferToJsonNode(Object obj) {
        return OBJECT_MAPPER.valueToTree(obj);
    }

    /**
     * construct java type -> Jackson Java Type.
     *
     * @param type java type
     * @return JavaType {@link JavaType}
     */
    public static JavaType constructJavaType(Type type) {
        return OBJECT_MAPPER.constructType(type);
    }

    public static ObjectWriter pretty() {
        return OBJECT_MAPPER.writer(new DefaultPrettyPrinter());
    }

    public static String pretty(Object o) {
        try {
            return pretty().writeValueAsString(o);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void prettyPrint(Object o) {
        try {
            System.out.println(pretty().writeValueAsString(o).replace("\r", ""));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}

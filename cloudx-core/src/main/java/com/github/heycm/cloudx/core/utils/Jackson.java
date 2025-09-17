package com.github.heycm.cloudx.core.utils;

import com.github.heycm.cloudx.core.contract.exception.JsonException;
import com.github.heycm.cloudx.core.utils.date.DateUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import com.fasterxml.jackson.module.afterburner.AfterburnerModule;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import lombok.extern.slf4j.Slf4j;

/**
 * Json 工具
 * @author heycm
 * @version 1.0
 * @since 2025-3-22 20:40
 */
@Slf4j
public final class Jackson {

    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private static final String TIME_PATTERN = "HH:mm:ss";
    private static final String TIME_ZONE = "Asia/Shanghai";

    private Jackson() {
        throw new UnsupportedOperationException("Utility class");
    }

    private static final ObjectMapper MAPPER = defaultObjectMapper();

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper om = new ObjectMapper();
        om.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); // 忽略json存在但Java对象中不存在的字段
        om.disable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);  // 关闭默认转换timestamps格式
        om.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);  // 关闭默认转换timestamps格式
        om.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);  // 忽略空bean转json错误
        om.enable(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS);  // Map转换按key排序

        // 值为null字段不转换
        om.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 统一日期格式：java.util.Date
        om.setDateFormat(new SimpleDateFormat(DATETIME_PATTERN));
        om.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
        // Java8时间支持：LocalDate、LocalDateTime、LocalTime
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(DateUtil.getDateTimeFormatter(DATETIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateUtil.getDateTimeFormatter(DATETIME_PATTERN)));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(DateUtil.getDateTimeFormatter(TIME_PATTERN)));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(DateUtil.getDateTimeFormatter(TIME_PATTERN)));
        om.registerModule(javaTimeModule);

        // 字节码增强模块
        om.registerModule(new AfterburnerModule());
        return om;
    }

    /**
     * 对象转 Json 字符串
     * @param obj 对象
     * @return json
     */
    public static <T> String toJson(T obj) {
        if (obj != null) {
            if (obj instanceof String) {
                return (String) obj;
            }
            try {
                return MAPPER.writeValueAsString(obj);
            } catch (JsonProcessingException e) {
                throw new JsonException(e);
            }
        }
        return null;
    }


    /**
     * 对象转 Json 带格式的字符串
     * @param obj 对象
     * @return json
     */
    public static <T> String toPrettyJson(T obj) {
        try {
            return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new JsonException(e);
        }
    }

    /**
     * 对象转字节数组
     * @param obj 对象
     * @return bytes
     */
    public static <T> byte[] toBytes(T obj) {
        if (obj != null) {
            try {
                return MAPPER.writeValueAsBytes(obj);
            } catch (JsonProcessingException e) {
                throw new JsonException(e);
            }
        }
        return null;
    }

    /**
     * Json字符串转对象
     * @param json  json 字符串
     * @param clazz 对象类型
     * @return 对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T toObject(String json, Class<T> clazz) {
        if (json != null && !json.isBlank() && clazz != null) {
            try {
                return clazz.equals(String.class) ? (T) json : MAPPER.readValue(json, clazz);
            } catch (Exception e) {
                throw new JsonException(e);
            }
        }
        return null;
    }

    /**
     * 字节数组转对象
     * @param bytes 字节数组
     * @param clazz 对象类型
     * @return 对象
     */
    public static <T> T toObject(byte[] bytes, Class<T> clazz) {
        if (bytes != null && bytes.length > 0 && clazz != null) {
            try {
                return MAPPER.readValue(bytes, clazz);
            } catch (Exception e) {
                throw new JsonException(e);
            }
        }
        return null;
    }

    /**
     * Json字符串转 List 集合
     * @param json         json 字符串
     * @param elementClazz 集合元素类型
     * @return List
     */
    public static <T> List<T> toList(String json, Class<T> elementClazz) {
        if (json != null && !json.isBlank() && elementClazz != null) {
            try {
                return MAPPER.readValue(json, listOf(elementClazz));
            } catch (JsonProcessingException e) {
                throw new JsonException(e);
            }
        }
        return null;
    }

    /**
     * Json字符串转 Set 集合
     * @param json         json 字符串
     * @param elementClazz 集合元素类型
     * @return Set
     */
    public static <T> Set<T> toSet(String json, Class<T> elementClazz) {
        if (json != null && !json.isBlank() && elementClazz != null) {
            try {
                return MAPPER.readValue(json, setOf(elementClazz));
            } catch (JsonProcessingException e) {
                throw new JsonException(e);
            }
        }
        return null;
    }

    /**
     * Json字符串转 Map 集合
     * @param json       json 字符串
     * @param keyClazz   集合键类型
     * @param valueClazz 集合值类型
     * @return Map
     */
    public static <K, V> Map<K, V> toMap(String json, Class<K> keyClazz, Class<V> valueClazz) {
        if (json != null && !json.isBlank() && keyClazz != null && valueClazz != null) {
            try {
                return MAPPER.readValue(json, mapOf(keyClazz, valueClazz));
            } catch (JsonProcessingException e) {
                throw new JsonException(e);
            }
        }
        return null;
    }

    private static <T> JavaType listOf(Class<T> elementClazz) {
        return MAPPER.getTypeFactory().constructCollectionType(List.class, elementClazz);
    }

    private static <T> JavaType setOf(Class<T> elementClazz) {
        return MAPPER.getTypeFactory().constructCollectionType(Set.class, elementClazz);
    }

    private static <K, V> JavaType mapOf(Class<K> keyClazz, Class<V> valueClazz) {
        return MAPPER.getTypeFactory().constructMapType(Map.class, keyClazz, valueClazz);
    }
}

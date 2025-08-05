package cn.heycm.d3framework.core.utils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;

/**
 * Json 工具
 * @author heycm
 * @version 1.0
 * @since 2025-3-22 20:40
 */
@Slf4j
public class Jackson {

    private static final String TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    private Jackson() {
    }

    private static final ObjectMapper OBJECT_MAPPER = defaultObjectMapper();

    public static ObjectMapper defaultObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        // 值为null字段不转换
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 统一日期格式
        objectMapper.setDateFormat(new SimpleDateFormat(TIME_PATTERN));
        // 关闭默认转换timestamps格式
        objectMapper.configure(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 忽略空bean转json错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 忽略json存在但Java对象中不存在的字段
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return objectMapper;
    }

    /**
     * 对象转 Json 字符串
     * @param obj 对象
     * @return json
     */
    public static <T> String toJson(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? obj.toString() : OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.error("Parse Object to String error : {}", e.getMessage(), e);
        }
        return null;
    }

    /**
     * 对象转字节数组
     * @param obj 对象
     * @return bytes
     */
    public static <T> byte[] toBytes(T obj) {
        if (obj != null) {
            try {
                return OBJECT_MAPPER.writeValueAsBytes(obj);
            } catch (JsonProcessingException e) {
                log.error("Parse Object to byte[] error : {}", e.getMessage(), e);
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
                return clazz.equals(String.class) ? (T) json : OBJECT_MAPPER.readValue(json, clazz);
            } catch (Exception e) {
                log.error("Parse String to Object error : {}", e.getMessage(), e);
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
                return OBJECT_MAPPER.readValue(bytes, clazz);
            } catch (Exception e) {
                log.error("Parse byte[] to Object error : {}", e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * Json字符串转 List 集合
     * @param json         json 字符串
     * @param elementClazz 集合元素类型
     * @return 集合
     */
    @SuppressWarnings("unchecked")
    public static <T> List<T> toList(String json, Class<T> elementClazz) {
        return toCollection(json, List.class, elementClazz);
    }

    /**
     * Json字符串转 Set 集合
     * @param json         json 字符串
     * @param elementClazz 集合元素类型
     * @return 集合
     */
    @SuppressWarnings("unchecked")
    public static <T> Set<T> toSet(String json, Class<T> elementClazz) {
        return toCollection(json, Set.class, elementClazz);
    }

    /**
     * Json字符串转集合
     * @param json            json 字符串
     * @param collectionClazz 集合类型
     * @param elementClazz    集合元素类型
     * @return 集合
     */
    private static <T extends Collection<?>, E> T toCollection(String json, Class<T> collectionClazz, Class<E> elementClazz) {
        if (json != null && collectionClazz != null && elementClazz != null) {
            JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClazz, elementClazz);
            try {
                return OBJECT_MAPPER.readValue(json, javaType);
            } catch (IOException e) {
                log.error("Parse String to Object error : {}", e.getMessage(), e);
            }
        }
        return null;
    }
}

package com.bujidao.seckill.util;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * 将对象序列化和反序列化
 */
@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        //对象的所有字段全部列入
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        //忽略空bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        //所有日期格式统一为以下样式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        //忽略在json字符串中存在，但在java对象中不存在对应属性的情况，防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    /**
     * <T> 只是标识这个方法为泛型方法，没有别的意思
     * object转化为json
     */
    public static <T> String objToString(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("对象转换json 解析错误", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * object转化为json
     * 返回的json是已经格式化好的
     */
    public static <T> String objToStringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj :
                    objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (IOException e) {
            log.warn("objToString解析错误", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将json串转化为对象
     * 这个方法无法反序列化集合对象例如 List<User>
     */
    public static <T> T stringToObject(String str,Class<T> clazz) {
        if(StringUtils.isEmpty(str)){
            return null;
        }
        try {
            return clazz.equals(String.class)? (T) str :objectMapper.readValue(str,clazz);
        } catch (IOException e) {
            log.warn("json转换为java对象 解析错误", e);
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 第一种放方式
     * 处理json转化为集合对象的多泛型问题
     */
    public static <T> T stringToObject(String str, TypeReference<T> typeReference) {
        if(StringUtils.isEmpty(str)||typeReference==null){
            return null;
        }
        try {
            objectMapper.readValue(str,typeReference);
            return typeReference.getType().equals(String.class)? (T) str :objectMapper.readValue(str,typeReference);
        } catch (Exception e) {
            log.warn("json转换为java对象 解析错误", e);
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 第二种放方式
     * 这里要用问号的原因是这里泛型尚不能确定，如果固定一个T，那么最终返回值就只能是T
     * 这里返回值并没有确定，所以要用问号
     * 处理json转化为集合对象的多泛型问题
     */
    public static <T> T stringToObject(String str, Class<?> collectionClass,Class<?>...elementClass) {
        JavaType javaType=objectMapper.getTypeFactory().constructParametricType(collectionClass,elementClass);
        try {
            return objectMapper.readValue(str,javaType);
        } catch (IOException e) {
            log.warn("json转换为java对象 解析错误", e);
            e.printStackTrace();
        }
        return null;
    }

}

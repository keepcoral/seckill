package com.bujidao.seckill.service;

import com.alibaba.fastjson.JSON;
import com.bujidao.seckill.redis.prefix.KeyPrefix;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@SuppressWarnings("all")
@Service
@Slf4j
public class RedisService {
    @Autowired
    private JedisPool jedisPool;

    public <T> T get(KeyPrefix keyPrefix, String key, Class<T> clazz) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //利用prefix生成真实的key
            String realKey = keyPrefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        } finally {
            jedis.close();
        }
    }

    public <T> boolean set(KeyPrefix keyPrefix, String key, T value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            //将对象转换为json串
            String str = beanToString(value);
            String realKey = keyPrefix.getPrefix() + key;
            //设置过期时间，根据过期时间设置
            int expireSeconds = keyPrefix.expireSeconds();
            if (expireSeconds <= 0) {
                //永不过期
                jedis.set(realKey, str);
            } else {
                jedis.setex(realKey, expireSeconds, str);
            }
            return true;
        } finally {
            jedis.close();
        }
    }

    /**
     * 自增，原子性操作
     */
    public <T> Long incr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            jedis.close();
        }
    }

    /**
     * 自减，原子性操作
     */
    public <T> Long decr(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            jedis.close();
        }
    }

    public <T> boolean exists(KeyPrefix keyPrefix, String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            jedis.close();
        }
    }

    public void flushAll(){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            jedis.flushAll();
        } finally {
            jedis.close();
        }
    }

    public Map<String,String> getAll() {
        Jedis jedis = null;
        Map<String,String> map=new HashMap<>();
        Set<String> set = null;
        try{
            jedis=jedisPool.getResource();
            set = jedis.keys("*");
            for(String key:set){
                map.put(key,jedis.get(key));
            }
        }finally {
            jedis.close();
        }
        return map;
    }

    public boolean delete(KeyPrefix keyPrefix,String key){
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            String realKey = keyPrefix.getPrefix() + key;
            long result=jedis.del(realKey);
            return result>0;
        } finally {
            jedis.close();
        }
    }
    /**
     * 从对象变为json串
     */
    public static  <T> String beanToString(T value) {
        if (value == null) return null;
        Class clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else {
            return JSON.toJSONString(value);
        }
    }

    /**
     * 从json串变为对象
     */
    public static  <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }
}

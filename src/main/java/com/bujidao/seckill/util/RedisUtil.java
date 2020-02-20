package com.bujidao.seckill.util;

import com.bujidao.seckill.config.RedisSharePool;
import com.bujidao.seckill.redis.prefix.KeyPrefix;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

@Slf4j
public class RedisUtil {
    private static ShardedJedisPool jedisPool=RedisSharePool.initPool();
    private static ShardedJedis getJedis() {
        return jedisPool.getResource();
    }


//    private static Jedis getJedis() {
//        return jedisPool.getResource();
//    }
//    private static JedisPool jedisPool= RedisPool.initPool();
    public static String get(KeyPrefix keyPrefix, String key) {
        ShardedJedis jedis = null;
//        Jedis jedis = null;
        try {
            jedis = getJedis();
            //利用prefix生成真实的key
            String realKey = keyPrefix.getPrefix() + key;
            String str = jedis.get(realKey);
            return str;
        }
        finally {
                if (jedis != null) {
                    jedis.close();
                }
        }



    }

    public static <T> boolean set(KeyPrefix keyPrefix, String key, T value) {
        ShardedJedis jedis = null;
//        Jedis jedis = null;

        try {
            jedis = getJedis();
            String valueStr = JsonUtil.objToString(value);
            String realKey = keyPrefix.getPrefix() + key;
            int expireSeconds = keyPrefix.expireSeconds();
            if (expireSeconds <= 0) {
                //永不过期
                jedis.set(realKey, valueStr);
            } else {
                jedis.setex(realKey, expireSeconds, valueStr);
            }
            return true;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 自增，原子性操作
     */
    public static <T> Long incr(KeyPrefix keyPrefix, String key) {
        ShardedJedis jedis = null;
//        Jedis jedis = null;
        try {
            jedis = getJedis();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.incr(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    /**
     * 自减，原子性操作
     */
    public static <T> Long decr(KeyPrefix keyPrefix, String key) {
        ShardedJedis jedis = null;
//        Jedis jedis = null;
        try {
            jedis = getJedis();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.decr(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static <T> boolean exists(KeyPrefix keyPrefix, String key) {
        ShardedJedis jedis = null;
//        Jedis jedis = null;
        try {
            jedis = getJedis();
            String realKey = keyPrefix.getPrefix() + key;
            return jedis.exists(realKey);
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public static boolean delete(KeyPrefix keyPrefix, String key) {
        ShardedJedis jedis = null;
//        Jedis jedis = null;
        try {
            jedis = getJedis();
            String realKey = keyPrefix.getPrefix() + key;
            long result = jedis.del(realKey);
            return result > 0;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

}

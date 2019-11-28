package com.bujidao.seckill.config;

import com.bujidao.seckill.util.PropertiesUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * 只有在组件@Component的情况下,才能使用@Value
 */
//@Configuration
public class RedisSharePool {
    private static String hostname1= PropertiesUtil.getProperty("redis.hostname1");
    private static String hostname2= PropertiesUtil.getProperty("redis.hostname2");
    private static int port1=Integer.parseInt(PropertiesUtil.getProperty("redis.port1","20"));
    private static int port2=Integer.parseInt(PropertiesUtil.getProperty("redis.port2","20"));
    private static int maxTotal=Integer.parseInt(PropertiesUtil.getProperty("redis.pool.maxActive","200"));
    private static int maxIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.pool.maxIdle","20"));
    private static int minIdle=Integer.parseInt(PropertiesUtil.getProperty("redis.pool.minIdle","20"));
    private static long maxWaitMillis=Long.parseLong(PropertiesUtil.getProperty("redis.pool.maxWait","20000"));
    private static boolean testOnBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.pool.testOnBorrow}","true"));
    private static String redisPassword= PropertiesUtil.getProperty("redis.password");
    private static int timeout=Integer.parseInt(PropertiesUtil.getProperty("redis.timeout","20"));

    public static ShardedJedisPool initPool() {
        JedisPoolConfig jedisPoolConfig = createJedisPoolConfig();

        JedisShardInfo info1 = new JedisShardInfo(hostname1, port1, timeout);
        JedisShardInfo info2 = new JedisShardInfo(hostname2, port2, timeout);
        info1.setPassword(redisPassword);
        info2.setPassword(redisPassword);
        List<JedisShardInfo> list = new ArrayList<>();
        list.add(info1);
        list.add(info2);

        //默认的是MURMUR_HASH，也就是一致性算法
        ShardedJedisPool pool = new ShardedJedisPool(jedisPoolConfig, list,
                Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);

        return pool;
    }

    /**
     * 创建redis连接池的设置
     */
    private static JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//		最多空闲连接
        jedisPoolConfig.setMaxIdle(maxIdle);
//		控制一个pool可分配多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxTotal);
//		最大等待时间，当没有可用连接时，超过该时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//		获取连接时检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
//		控制一个pool可分配最少jedis实例
        jedisPoolConfig.setMinIdle(minIdle);
        //连接耗尽是否阻塞，false就抛出异常，true阻塞到超时，默认为true
        jedisPoolConfig.setBlockWhenExhausted(true);
        return jedisPoolConfig;
    }


//    @Value("${redis.hostname1}")
//    public void setHostname1(String hostname) {
//        RedisSharePool.hostname1 = hostname;
//    }
//
//    @Value("${redis.port1}")
//    public void setPort(int port) {
//        RedisSharePool.port1 = port;
//    }
//
//
//    @Value("${redis.hostname2}")
//    public void setHostname2(String hostname) {
//        RedisSharePool.hostname2 = hostname;
//    }
//
//    @Value("${redis.port2}")
//    public void setPort2(int port) {
//        RedisSharePool.port2 = port;
//    }
//
//    @Value("${redis.pool.maxActive}")
//    public void setMaxTotal(int maxTotal) {
//        RedisSharePool.maxTotal = maxTotal;
//    }
//
//    @Value("${redis.pool.maxIdle}")
//    public void setMaxIdle(int maxIdle) {
//        RedisSharePool.maxIdle = maxIdle;
//    }
//
//    @Value("${redis.pool.minIdle}")
//    public  void setMinIdle(int minIdle) {
//        RedisSharePool.minIdle = minIdle;
//    }
//
//    @Value("${redis.pool.maxWait}")
//    public void setMaxWaitMillis(long maxWaitMillis) {
//        RedisSharePool.maxWaitMillis = maxWaitMillis;
//    }
//
//    @Value("${redis.pool.testOnBorrow}")
//    public void setTestOnBorrow(boolean testOnBorrow) {
//        RedisSharePool.testOnBorrow = testOnBorrow;
//    }
//
//    @Value("${redis.password}")
//    public void setRedisPassword(String redisPassword) {
//        RedisSharePool.redisPassword = redisPassword;
//    }
//
//    @Value("${redis.timeout}")
//    public void setTimeout(int timeout) {
//        RedisSharePool.timeout = timeout;
//    }
}

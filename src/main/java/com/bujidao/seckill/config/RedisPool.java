package com.bujidao.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 只有在组件@Component的情况下,才能使用@Value
 */
@Configuration
public class RedisPool {
    private static String hostname;
    private static int port;
    private static int maxTotal;
    private static int maxIdle;
    private static long maxWaitMillis;
    private static boolean testOnBorrow;
    private static String redisPassword;
    private static int timeout;
    private static int minIdle;

    public static JedisPool initPool() {
        JedisPoolConfig jedisPoolConfig = createJedisPoolConfig();
        JedisPool jedisPool = new JedisPool(jedisPoolConfig, hostname, port, timeout, redisPassword);
        return jedisPool;
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
        System.out.println("最小连接为"+minIdle);
        System.out.println("总共连接"+maxTotal);
        jedisPoolConfig.setMinIdle(minIdle);
//		最大等待时间，当没有可用连接时，超过该时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//		获取连接时检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);


        return jedisPoolConfig;
    }


    @Value("${redis.hostname1}")
    public void setHostname(String hostname) {
        RedisPool.hostname = hostname;
    }

    @Value("${redis.port1}")
    public void setPort(int port) {
        RedisPool.port = port;
    }

    @Value("${redis.pool.maxActive}")
    public void setMaxTotal(int maxTotal) {
        RedisPool.maxTotal = maxTotal;
    }

    @Value("${redis.pool.maxIdle}")
    public void setMaxIdle(int maxIdle) {
        RedisPool.maxIdle = maxIdle;
    }

    @Value("${redis.pool.maxWait}")
    public void setMaxWaitMillis(long maxWaitMillis) {
        RedisPool.maxWaitMillis = maxWaitMillis;
    }

    @Value("${redis.pool.testOnBorrow}")
    public void setTestOnBorrow(boolean testOnBorrow) {
        RedisPool.testOnBorrow = testOnBorrow;
    }

    @Value("${redis.password}")
    public void setRedisPassword(String redisPassword) {
        RedisPool.redisPassword = redisPassword;
    }

    @Value("${redis.timeout}")
    public void setTimeout(int timeout) {
        RedisPool.timeout = timeout;
    }

    @Value("${redis.pool.minIdle}")
    public  void setMinIdle(int minIdle) {
        RedisPool.minIdle = minIdle;
    }



}

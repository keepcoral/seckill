package com.bujidao.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * redis的配置类
 */
//@Configuration
//@ConfigurationProperties(prefix = "redis")//以redis开头的配置全部读到这个类
public class RedisConfig {
    @Value("${redis.hostname}")
    private String hostname;
    @Value("${redis.port}")
    private int port;
    @Value("${redis.pool.maxActive}")
    private int maxTotal;
    @Value("${redis.pool.maxIdle}")
    private int maxIdle;
    @Value("${redis.pool.maxWait}")
    private long maxWaitMillis;
    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;
    @Value("${redis.password}")
    private String redisPassword;
    @Value("${redis.timeout}")
    private int timeout;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Bean(name = "jedisPool")
    public JedisPool jedisPoolFactory() {
        JedisPool jedisPool = new JedisPool(jedisPoolConfig,hostname,port,timeout,redisPassword);
        return jedisPool;
    }

    /**
     * 创建redis连接池的设置
     */
    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//		最多空闲连接
        jedisPoolConfig.setMaxIdle(maxIdle);
//		控制一个pool可分配多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxTotal);
//		最大等待时间，当没有可用连接时，超过该时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
//		获取连接时检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

}

package com.bujidao.seckill.service;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Service
public class CacheService {
    private Cache<String, Object> commonCache = null;

    @PostConstruct//在对应bean加载的时候优先执行init()方法
    public void init() {
        commonCache = CacheBuilder.newBuilder()
                //初始缓存大小
                .initialCapacity(10)
                //设置缓存最大可以存储100个key，超过100个key将会被lru移除
                .maximumSize(100)
                //设置写入缓存多少秒过期
                .expireAfterWrite(60, TimeUnit.SECONDS)
                .build();
    }

    public void setCommonCache(String key,Object value){
        commonCache.put(key,value);
    }

    public Object getCommonCache(String key){
        //如果不存在返回一个null
        return commonCache.getIfPresent(key);
    }
}

package com.bujidao.seckill.redis.prefix;

public interface KeyPrefix {
    int expireSeconds();
    String getPrefix();
}

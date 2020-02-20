package com.bujidao.seckill.redis.prefix;

public class SeckillKeyPrefix extends BaseKeyPrefix {

    /**
     * 永不过期的构造器
     */
    private SeckillKeyPrefix(String prefix) {
        super(prefix);
    }
    private SeckillKeyPrefix(int expireSeconds, String prefix){super(expireSeconds,prefix);}


    public static SeckillKeyPrefix isGoodsOver = new SeckillKeyPrefix("go");
    //5分钟过期
    public static SeckillKeyPrefix getSeckillToken = new SeckillKeyPrefix(300,"seckillToken");

}




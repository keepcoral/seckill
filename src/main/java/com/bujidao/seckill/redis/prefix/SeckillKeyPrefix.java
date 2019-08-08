package com.bujidao.seckill.redis.prefix;

public class SeckillKeyPrefix extends BaseKeyPrefix {

    /**
     * 永不过期的构造器
     *
     * @param prefix
     */
    private SeckillKeyPrefix(String prefix) {
        super(prefix);
    }


    public static SeckillKeyPrefix isGoodsOver = new SeckillKeyPrefix("go");

}




package com.bujidao.seckill.redis.prefix;

public class GoodsKeyPrefix extends BaseKeyPrefix {

    /**
     * 永不过期的构造器
     *
     * @param prefix
     */
    private GoodsKeyPrefix(String prefix) {
        super(prefix);
    }

    private GoodsKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static GoodsKeyPrefix getGoodsList = new GoodsKeyPrefix(60,"goodlist");
    public static GoodsKeyPrefix getGoodsDetail=new GoodsKeyPrefix(60,"ggoodDetail");
    public static GoodsKeyPrefix getSeckillGoodsStock=new GoodsKeyPrefix(0,"goodStock");

}




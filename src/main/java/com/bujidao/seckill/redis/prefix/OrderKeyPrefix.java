package com.bujidao.seckill.redis.prefix;

public class OrderKeyPrefix extends BaseKeyPrefix {

    /**
     * 永不过期的构造器
     *
     * @param prefix
     */
    private OrderKeyPrefix(String prefix) {
        super(prefix);
    }

    private OrderKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static OrderKeyPrefix getOrderByUidGid=new OrderKeyPrefix("moug");

}




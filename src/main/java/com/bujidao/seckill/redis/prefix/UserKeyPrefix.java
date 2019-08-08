package com.bujidao.seckill.redis.prefix;

public class UserKeyPrefix extends BaseKeyPrefix {

    //token过期时间为半小时
    public static final int TOKEN_EXPIRE = 3600 / 2;

    /**
     * 永不过期的构造器
     *
     * @param prefix
     */
    private UserKeyPrefix(String prefix) {
        super(prefix);
    }

    private UserKeyPrefix(int expireSeconds, String prefix) {
        super(expireSeconds, prefix);
    }

    public static UserKeyPrefix token = new UserKeyPrefix(TOKEN_EXPIRE,"token");
    //设置为永不过期
    public static UserKeyPrefix getUserById = new UserKeyPrefix(0,"id");

}




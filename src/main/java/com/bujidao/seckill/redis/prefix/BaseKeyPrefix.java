package com.bujidao.seckill.redis.prefix;

public abstract class BaseKeyPrefix implements KeyPrefix {
    //过期时间
    private int expireSeconds;
    //要分类的前缀
    private String prefix;

    public BaseKeyPrefix(String prefix) {
        this(0,prefix);
    }

    public BaseKeyPrefix(int expireSeconds, String prefix) {
        this.expireSeconds = expireSeconds;
        this.prefix = prefix;
    }

    /**
     * 默认0代表永不过期
     * @return
     */
    @Override
    public int expireSeconds() {
        return expireSeconds;
    }


    @Override
    public String getPrefix() {
        //利用每个类的类名作为前缀这样旧不会重复了
        String className=getClass().getSimpleName();
        return className+":"+prefix;
    }
}

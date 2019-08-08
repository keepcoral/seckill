package com.bujidao.seckill.enums;

public enum OrderChannel {
    CHANNEL_PC(1,"PC"),
    CHANNEL_ANDROID(2,"ANDROID"),
    CHANNEL_IOS(3,"IOS");
    private int state;
    private String info;

    OrderChannel(int state, String info) {
        this.state = state;
        this.info = info;
    }

    public int getState() {
        return state;
    }

    public String getInfo() {
        return info;
    }

}

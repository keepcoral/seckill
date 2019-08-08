package com.bujidao.seckill.enums;

public enum OrderState {
    NOT_PAY(0,"未支付"),
    PAYED(1,"已支付"),
    SHIPPED(2,"已发货"),
    RECEIVED(3,"已收货"),
    REFUNDED(4,"已退款"),
    FINISHED(5,"已完成");
    private int state;
    private String info;

    OrderState(int state, String info) {
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

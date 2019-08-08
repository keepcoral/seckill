package com.bujidao.seckill.result;


public class CodeMsg {
    private int code;
    private String msg;

    //通用模块
    public static CodeMsg SUCCESS=new CodeMsg(0,"succcess");
    public static CodeMsg SERVER_ERROR=new CodeMsg(500100,"服务端异常");
    public static CodeMsg BIND_ERROR=new CodeMsg(500101,"参数校验异常:%s");
    //登陆模块 5002XX
    public static CodeMsg SESSION_ERROR=new CodeMsg(500210,"session不存在或已经失效");
    public static CodeMsg PASSWORD_EMPTY=new CodeMsg(500211,"密码不能为空");
    public static CodeMsg MOBILE_EMPTY=new CodeMsg(500212,"手机不能为空");
    public static CodeMsg MOBILE_ERROR=new CodeMsg(500213,"手机格式错误");
    public static CodeMsg MOBILE_NOT_EXIST=new CodeMsg(500214,"手机号不存在");
    public static CodeMsg PASSWORD_ERROR=new CodeMsg(500215,"密码错误");
    //商品模块 5003XX
    public static CodeMsg GOODS_NOT_FOUND=new CodeMsg(500300,"无法找到该商品");
    //订单模块 5004XX
    public static CodeMsg ORDER_NOT_EXIST=new CodeMsg(500300,"订单不存在");
    //秒杀模块 5005XX
    public static CodeMsg STOCK_OVER=new CodeMsg(500500,"商品已售罄");
    public static CodeMsg REPEATE_SECKILL=new CodeMsg(500501,"不可重复秒杀");
    private CodeMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object...args){
        int code= this.code;
        //BIND_ERROR的信息带了参数，故要格式化为字符串
        String message=String.format(this.msg,args);
        return new CodeMsg(code,message);
    }
    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    @Override
    public String toString() {
        return "CodeMsg{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}

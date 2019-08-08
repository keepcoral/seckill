package com.bujidao.seckill.result;

public class Result<T> {
    private int code;
    private String msg;
    private T data;

    //成功调用的构造器
    private Result(T data) {
        this.code = 0;
        this.msg = "succcess";
        this.data = data;
    }

    //失败调用的构造器
    private Result(CodeMsg codeMsg) {
        if (codeMsg == null) return;
        this.code = codeMsg.getCode();
        this.msg = codeMsg.getMsg();
    }

    public static String format(String formatString,Object...args){
        return String.format(formatString,args);
    }

    /**
     * 成功时调用
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data);
    }

    /**
     * 失败时调用
     */
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<T>(codeMsg);
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public T getData() {
        return data;
    }

}

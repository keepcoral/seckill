package com.bujidao.seckill.exception;

import com.bujidao.seckill.result.CodeMsg;

public class GlobleException extends RuntimeException {
    private CodeMsg codeMsg;

    public GlobleException(CodeMsg codeMsg){
        super(codeMsg.getMsg());
        this.codeMsg=codeMsg;
    }

    public CodeMsg getCodeMsg() {
        return codeMsg;
    }

}

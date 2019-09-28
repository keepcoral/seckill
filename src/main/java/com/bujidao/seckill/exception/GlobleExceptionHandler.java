package com.bujidao.seckill.exception;

import com.bujidao.seckill.result.CodeMsg;
import com.bujidao.seckill.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLDataException;
import java.util.List;

/**
 * 全局捕获异常类，只要作用在@RequestMapping上，所有的异常都会被捕获
 *
 */
@Slf4j
@ControllerAdvice
@ResponseBody
public class GlobleExceptionHandler {
    //拦截到的异常如果属于该类则调用该方法
    @ExceptionHandler(value= Exception.class)
    public Result<String> exceptionHandler(HttpServletRequest request,Exception e){
//        log.error("捕获到异常:{}",e.getMessage());
        e.printStackTrace();
        //拦截到的异常直接把异常输出到前端
        if(e instanceof GlobleException){
            GlobleException ex= (GlobleException) e;
            return Result.error(ex.getCodeMsg());
        }else if(e instanceof BindException) {
            BindException ex=(BindException) e;
            List<ObjectError> errors=ex.getAllErrors();
            //为了简单只取第一个异常
            ObjectError objectError=errors.get(0);
            String msg=objectError.getDefaultMessage();
            //这样就可以将错误信息作为ResponseBody传到前端
            return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
        }else{
            return Result.error(CodeMsg.SERVER_ERROR);
        }
    }
}

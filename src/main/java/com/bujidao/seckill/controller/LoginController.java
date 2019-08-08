package com.bujidao.seckill.controller;

import com.bujidao.seckill.result.Result;
import com.bujidao.seckill.service.UserService;
import com.bujidao.seckill.vo.LoginVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Controller
@RequestMapping("/local")
public class LoginController {

    @Autowired
    private UserService userService;

    private static Logger logger= LoggerFactory.getLogger(LoginController.class);
    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    @RequestMapping("/dologin")
    @ResponseBody
    public Result<Boolean> doLogin(HttpServletResponse response, @Valid LoginVo loginVo) {
        logger.info(loginVo.toString());
        //登陆校验
        userService.login(response,loginVo);
        return Result.success(true);
    }

    @RequestMapping("/dlogin")
    @ResponseBody
    public Result<Boolean> testDlogin(HttpServletResponse response){
        LoginVo loginVo=new LoginVo();
        loginVo.setMobile("18819489018");
        loginVo.setPassword("d3b1294a61a07da9b49b6e22b2cbd7f9");
        userService.login(response,loginVo);
        return Result.success(true);
    }
}


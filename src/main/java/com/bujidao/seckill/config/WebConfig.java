package com.bujidao.seckill.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private UserArgumentResolver userArgumentResolver;


    //在controller方法里面带了很多参数
    //如request,response,model，这些参数就是这个方法赋值的
    //springmvc会回调这个方法往controller的参数上赋值
    //添加解析器以支持自定义控制器方法参数类型。这不会覆盖用于解析处理程序方法参数的内置支持。
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(userArgumentResolver);
    }
}

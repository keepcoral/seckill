package com.bujidao.seckill.config;

import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private UserService userService;

    //判断解析器是否支持当前参数
    //如果直接返回true，那么支持所有参数
    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        //获取参数的类型
        Class<?> clazz = methodParameter.getParameterType();
        //如果遇到类型参数列表中有User.class的时候才会调用resolveArgument()这个方法
        return clazz == User.class;
    }

    //遇到支持参数所调用的方法
    @SuppressWarnings("all")
    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        String paramToken = request.getParameter(UserService.COOKIE_NAME_TOKEN);
        String cookieToken = null;
        if (StringUtils.isEmpty(paramToken)) {
            cookieToken = getCookieValue(request, UserService.COOKIE_NAME_TOKEN);
        }
        //如果两个都为空，证明没有登陆返回登陆页面
        if (StringUtils.isEmpty(paramToken) && StringUtils.isEmpty(cookieToken)) {
            return null;
        }
        //优先取request中的token
        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        //根据token取得user
        User user = userService.getByToken(response, token);
        return user;
    }

    /**
     * 根据cookie的Name来取得token
     */
    private String getCookieValue(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || cookies.length == 0) return null;
        for (Cookie c : cookies) {
            if (c.getName().equals(cookieName)) return c.getValue();
        }
        return null;
    }
}


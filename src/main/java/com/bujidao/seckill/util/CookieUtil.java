package com.bujidao.seckill.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class CookieUtil {
    public static final String COOKIE_NAME = "login_token";
    public static final Integer COOKIE_EXPIRETIME = 60 * 30;

    public static void writeLoginToken(HttpServletResponse response, String token) {
        //构造器前一个参数是key，后一个参数是value
        Cookie cookie = new Cookie(COOKIE_NAME, token);
        //单位是秒，-1表示永久
        //如果这个maxage不设置，cookie就不会写入硬盘，而是写在内存，只在当前页面有效
        cookie.setMaxAge(COOKIE_EXPIRETIME);
        //如果是"test"只有test目录下和test的子目录下的页面和代码才能获取到cookie
        cookie.setPath("/");//表示设置在根目录下
        cookie.setHttpOnly(true);//不许通过脚本访问cookie，保证信息的安全
        response.addCookie(cookie);
        log.info("写入的cookie为cookieName={},cookieValue={}", cookie.getName(), cookie.getValue());

    }

    public static String readLoginToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                log.info("遍历cookie--读取到的cookie为cookieName={}", ck.getName());
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    log.info("读取到的cookie为cookieName={},cookieValue={}", ck.getName(), ck.getValue());
                    return ck.getValue();
                }
            }
        }
        return null;
    }

    public static void deleteLoginToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie ck : cookies) {
                if (StringUtils.equals(ck.getName(), COOKIE_NAME)) {
                    log.info("删除的cookie为cookieName={},cookieValue={}", ck.getName(), ck.getValue());
                    ck.setPath("/");
                    ck.setHttpOnly(true);//不许通过脚本访问cookie，保证信息的安全
                    ck.setMaxAge(0);//设置为0，代表删除此Cookie
                    response.addCookie(ck);
                    return;
                }
            }
        }
    }
}

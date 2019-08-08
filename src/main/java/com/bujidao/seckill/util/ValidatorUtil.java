package com.bujidao.seckill.util;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidatorUtil {
    //模式匹配：以1开头，后面跟了十个数字
    private static Pattern mobile_pattern=Pattern.compile("1\\d{10}");

    public static boolean isMobile(String src){
        if(StringUtils.isEmpty(src)){
            return false;
        }
        //判断格式是否正确
        Matcher m=mobile_pattern.matcher(src);
        return m.matches();
    }

    public static void main(String[] args) {
        System.out.println(ValidatorUtil.isMobile("18819489018"));
    }
}

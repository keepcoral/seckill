package com.bujidao.seckill.util;

import org.apache.commons.codec.digest.DigestUtils;


public class MD5Util {
    //对字符串进行一次md5操作
    private static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    //直接做md5不够安全，故要写一个固定salt
    //和用户输入的密码做一次拼装，然后传递给服务端
    private static final String salt = "1a2b3c4d";

    /**
     * 将输入的password转换为FormPassword
     */
    public static String inputPassToFormPass(String inputPass) {
        String src = ""+salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        System.out.println(src);
        return md5(src);
    }

    /**
     * 将FormPassword转换为DBPassword
     * 传入一个随机的一个salt
     */
    public static String formPassToDBPass(String FormPass, String salt) {
        String src = ""+salt.charAt(0) + salt.charAt(2) + FormPass + salt.charAt(5) + salt.charAt(4);
        return md5(src);
    }

    public static String inputPassToDBPass(String inputPass, String salt) {
        return formPassToDBPass(inputPassToFormPass(inputPass), salt);
    }

    public static void main(String[] args) {
        System.out.println(MD5Util.inputPassToFormPass("123456"));//12123456c3
        //第一次md5 ：d3b1294a61a07da9b49b6e22b2cbd7f9
        //实际存到DB的是这个b7797cce01b4b131b433b6acf4add449，并且存入随机salt
        System.out.println(MD5Util.inputPassToDBPass("123456", "1a2b3c4d"));
    }
}

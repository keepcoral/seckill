package com.bujidao.seckill.util;

import java.util.UUID;

public class UUIDUtil {
    public static String uuid(){
        //原生的UUID带有'-'需要去掉
        return UUID.randomUUID().toString().replace("-","");
    }
}

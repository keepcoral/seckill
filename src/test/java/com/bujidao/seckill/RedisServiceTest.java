package com.bujidao.seckill;

import com.bujidao.seckill.service.RedisService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisServiceTest {
    @Autowired
    public RedisService redisService;

    @Test
    public  void testGet(){

    }


    @Test
    public void testGetALL(){
        System.out.println(redisService.getAll());
    }
}

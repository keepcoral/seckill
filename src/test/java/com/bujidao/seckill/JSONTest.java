package com.bujidao.seckill;

import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.rabbitmq.SeckillMessage;
import com.bujidao.seckill.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class JSONTest {

    @Test
    public void test(){
        User user = new User();
        user.setId(5L);
        user.setNickname("bujidao");
        SeckillMessage seckillMessage = new SeckillMessage(user, 1L);
        System.out.println("------------------------------------"+seckillMessage);
        String s=RedisService.beanToString(seckillMessage);
        System.out.println("-------------"+s);
        SeckillMessage seckillMessage2 = RedisService.stringToBean(s, SeckillMessage.class);
        System.out.println(seckillMessage2);
        //        String s="{\"goodsId\":1,\"user\":{\"id\":18819489018,\"loginCount\":0,\"nickname\":\"布吉岛\",\"password\":\"b7797cce01b4b131b433b6acf4add449\",\"salt\":\"1a2b3c4d\"}";
//        SeckillMessage seckillMessage= RedisService.stringToBean(s,SeckillMessage.class);
//        System.out.println(seckillMessage);
    }
    //{"goodsId":1,"user":{"id":18819489018,"loginCount":0,"nickname":"布吉岛","password":"b7797cce01b4b131b433b6acf4add449","salt":"1a2b3c4d"}
}

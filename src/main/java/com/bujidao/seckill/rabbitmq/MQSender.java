package com.bujidao.seckill.rabbitmq;

import com.bujidao.seckill.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MQSender {
    //引入依赖之后自动注入AmqpTemplate
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendSeckillMessage(SeckillMessage message){
        String msg= JsonUtil.objToString(message);
        log.info("send msg:{}",msg);
        amqpTemplate.convertAndSend(MQConfig.SECKILL_QUEUE,msg);
    }

    public void sendTestMessage(String message){
        log.info("send msg:{}",message);
        amqpTemplate.convertAndSend(MQConfig.TEST_QUEUE,message);
    }
}

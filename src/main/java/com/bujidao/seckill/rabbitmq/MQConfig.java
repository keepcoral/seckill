package com.bujidao.seckill.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@SuppressWarnings("all")
@Configuration
public class MQConfig {
    public static final String SECKILL_QUEUE="seckill.queue";

    @Bean
    public Queue queue() {
        //两个参数，一个是队列的名称，二是是否要做持久化
        return new Queue(SECKILL_QUEUE, true);
    }

}

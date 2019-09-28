package com.bujidao.seckill.rabbitmq;

import com.bujidao.seckill.domain.SeckillOrder;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.service.OrderService;
import com.bujidao.seckill.service.SeckillService;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class MQReceiver {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private OrderService orderService;


    //监听的队列
    @RabbitListener(queues = {MQConfig.SECKILL_QUEUE})
    public void receive(String message) {
        log.info("receive msg:{}", message);
        SeckillMessage seckillMessage = JsonUtil.stringToObject(message, SeckillMessage.class);
        User user = seckillMessage.getUser();
        long goodsId = seckillMessage.getGoodsId();

        //判断商品非空
        GoodsVo goodsVo = goodsService.getSeckillGoodsById(goodsId);
        if (goodsVo == null) {
            log.info("无法查询到商品id为{}的商品", goodsId);
            return;
        }

        //库存<=0秒杀失败
        int stock = goodsVo.getStockCount();
        log.info("库存为{}", stock);
        if (stock <= 0) {
            log.info("库存<0秒杀失败");
            return;
        }

        //判断是否已经秒杀到，防止重复秒杀，底层是访问redis
        SeckillOrder seckillOrder = orderService.getOrderByUserIdGoodsId(user.getId(), goodsVo.getId());
        if (seckillOrder != null) {
            log.info("订单不可秒杀！");
            return;
        }

        //减库存 下订单 写入秒杀订单
        seckillService.seckill(user,goodsVo);
    }


    @RabbitListener(queues = {MQConfig.TEST_QUEUE})
    public void receiveTestMessage(String message) {
        log.info("receive test-msg:{}", message);
    }
}

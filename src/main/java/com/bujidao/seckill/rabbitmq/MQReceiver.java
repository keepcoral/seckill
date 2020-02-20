package com.bujidao.seckill.rabbitmq;

import com.bujidao.seckill.domain.SeckillOrder;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.exception.GlobleException;
import com.bujidao.seckill.result.CodeMsg;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.service.OrderService;
import com.bujidao.seckill.service.SeckillService;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.*;


@Slf4j
@Service
public class MQReceiver {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private OrderService orderService;

    private ExecutorService executorService;

    @PostConstruct
    public void init(){
        //这里一定要手写一个线程池
        executorService=  new ThreadPoolExecutor(
                20,
                20,
                0,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(10000),//如果不设置初始值长度为MAX_VALUE
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    //监听的队列
    @RabbitListener(queues = {MQConfig.SECKILL_QUEUE})
    public void receive(String message) {
        log.info("receive msg:{}", message);
        SeckillMessage seckillMessage = JsonUtil.stringToObject(message, SeckillMessage.class);
        User user = seckillMessage.getUser();
        long goodsId = seckillMessage.getGoodsId();

        //判断商品非空
        GoodsVo goodsVo = goodsService.getSeckillGoodsById(goodsId);
//        if (goodsVo == null) {
//            log.info("无法查询到商品id为{}的商品", goodsId);
//            return;
//        }

        //库存<=0秒杀失败
//        int stock = goodsVo.getStockCount();
//        if (stock <= 0) {
//            log.info("库存<0秒杀失败");
//            return;
//        }

        //判断是否已经秒杀到，防止重复秒杀，底层是访问redis
//        SeckillOrder seckillOrder = orderService.getOrderByUserIdGoodsId(user.getId(), goodsVo.getId());
//        if (seckillOrder != null) {
//            log.info("订单不可秒杀！");
//            return;
//        }

        //这里利用一个线程池来排队解决，防止数据库太多请求
        //拥塞窗口为20的等待队列来泄洪，其它请求都要排队
        //减库存 下订单 写入秒杀订单
        Future<Object> future = executorService.submit(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                seckillService.seckill(user, goodsVo);
                return null;
            }
        });

        try {
            //等待返回
            future.get();
        } catch (Exception e) {
            throw new GlobleException(CodeMsg.UNKNOWED_ERROR);
        }
    }


    @RabbitListener(queues = {MQConfig.TEST_QUEUE})
    public void receiveTestMessage(String message) {
        log.info("receive test-msg:{}", message);
    }
}

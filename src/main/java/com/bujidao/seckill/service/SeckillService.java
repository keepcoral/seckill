package com.bujidao.seckill.service;

import com.bujidao.seckill.domain.Order;
import com.bujidao.seckill.domain.SeckillOrder;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.redis.prefix.GoodsKeyPrefix;
import com.bujidao.seckill.redis.prefix.OrderKeyPrefix;
import com.bujidao.seckill.redis.prefix.SeckillKeyPrefix;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.util.RedisUtil;
import com.bujidao.seckill.util.UUIDUtil;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SeckillService {
    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;

    /**
     * 秒杀的业务：减库存，下订单，写入秒杀订单
     */
    @Transactional
    public Order seckill(User user, GoodsVo goodsVo) {
        //由于redis可能会出现奇怪的错误这里要预防特判一次
        //这段代码要保证在减库存操作之前，否则会出现并发问题减了库存，但是没有插入订单的问题
        SeckillOrder so = orderService.getOrderByUserIdGoodsIdByDb(user.getId(), goodsVo.getId());
        if (so != null) {
            RedisUtil.set(OrderKeyPrefix.getOrderByUidGid, "" + user.getId() + "-" + goodsVo.getId(), so);
            //既然来到这里，那么redis的库存一定要加回去保证一致性
            RedisUtil.incr(GoodsKeyPrefix.getSeckillGoodsStock, "" + goodsVo.getId());
            return null;
        }
        //减库存
        boolean success = goodsService.reduceStock(goodsVo);
        if (!success) {
            //卖完了要在redis中添加该商品卖完的标记
            setGoodsOver(goodsVo.getId());
            return null;
        }


        //生成订单，并写入秒杀订单
        Order order = orderService.createOrder(user, goodsVo);
        return order;
    }

    public String generateSeckillToken(long userId, long goodsId) {
        GoodsVo goodsVo = goodsService.getSeckillGoodsById(goodsId);
        //没到开始时间无法下单，商品不存在也不能下单
        if (goodsVo == null || System.currentTimeMillis() > goodsVo.getEndDate().getTime()
                || System.currentTimeMillis() < goodsVo.getStartDate().getTime()) {
            return null;
        }
        //令牌数自减一
        Long num = RedisUtil.decr(GoodsKeyPrefix.getSeckillGoodsStock, "door_num");
        if (num < 0) return null;

        String token = UUIDUtil.uuid();
        //将秒杀token放入redis中
        RedisUtil.set(SeckillKeyPrefix.getSeckillToken, userId + " " + goodsId, token);
        return token;
    }

    /**
     * 获取下单的结果
     */
    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        log.info("秒杀后查询userID=={}--goodsId=={}---的商品订单=={}", userId, goodsId, order);
        if (order != null) {
            //秒杀成功
            return order.getOrderId();
        } else {
            //判断商品是否卖完，如果没有卖完继续轮询
            boolean isOver = getGoodsOver(goodsId);
            if (isOver) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    public boolean getGoodsOver(long goodsId) {
        return RedisUtil.exists(SeckillKeyPrefix.isGoodsOver, "" + goodsId);
    }

    public void setGoodsOver(long goodsId) {
        RedisUtil.set(SeckillKeyPrefix.isGoodsOver, "" + goodsId, JsonUtil.objToString(true));
    }
}

package com.bujidao.seckill.service;

import com.alibaba.fastjson.JSON;
import com.bujidao.seckill.domain.Order;
import com.bujidao.seckill.domain.SeckillOrder;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.redis.prefix.SeckillKeyPrefix;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.util.RedisUtil;
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
        //减库存
        boolean success = goodsService.reduceStock(goodsVo);
        if (!success) {
            //卖完了要在redis中添加该商品卖完的标记
            log.info("商品卖完打入标记");
            setGoodsOver(goodsVo.getId());
            return null;
        }
        //生成订单，并写入秒杀订单
        Order order = orderService.createOrder(user, goodsVo);
        return order;
    }

    public long getSeckillResult(Long userId, long goodsId) {
        SeckillOrder order = orderService.getOrderByUserIdGoodsId(userId, goodsId);
        log.info("秒杀后查询userID=={}--goodsId=={}---的商品订单=={}", userId,goodsId,order);
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

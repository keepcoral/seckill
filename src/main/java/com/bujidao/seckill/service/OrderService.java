package com.bujidao.seckill.service;

import com.bujidao.seckill.dao.OrderDao;
import com.bujidao.seckill.domain.Order;
import com.bujidao.seckill.domain.SeckillOrder;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.enums.OrderChannel;
import com.bujidao.seckill.enums.OrderState;
import com.bujidao.seckill.redis.prefix.OrderKeyPrefix;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.util.RedisUtil;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;

@Slf4j
@Service
public class OrderService {
    @Autowired
    private OrderDao orderDao;


    /**
     * 根据userId和goodId返回秒杀订单
     */
    public SeckillOrder getOrderByUserIdGoodsId(long userId, long goodsId){
        String str=RedisUtil.get(OrderKeyPrefix.getOrderByUidGid,""+userId+"-"+goodsId);
        return JsonUtil.stringToObject(str,SeckillOrder.class);
    }

    /**
     * 创建秒杀订单的订单
     */
    @Transactional
    public Order createOrder(User user, GoodsVo goodsVo){
        //新建订单
        Order order=new Order();
        order.setCreateDate(new Date());
        order.setDeliveryAddrId(0L);
        order.setGoodsId(goodsVo.getId());
        order.setGoodsCount(1);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsPrice(goodsVo.getSeckillPrice());
        order.setOrderChannel(OrderChannel.CHANNEL_ANDROID.getState());
        order.setStatus(OrderState.NOT_PAY.getState());
        order.setUserId(user.getId());
        orderDao.insertOrder(order);
        //拿到订单id，新建秒杀订单
        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setUserId(user.getId());
        seckillOrder.setGoodsId(goodsVo.getId());
        seckillOrder.setOrderId(order.getId());
        orderDao.insertSeckillOrder(seckillOrder);
        log.info("插入的秒杀订单表为--"+seckillOrder);
        RedisUtil.set(OrderKeyPrefix.getOrderByUidGid,""+user.getId()+"-"+goodsVo.getId(),seckillOrder);
        return order;
    }

    public Order getOrderById(long orderId){
        return orderDao.getOrderById(orderId);
    }

    public boolean deleteAllOrder(){
        orderDao.deleteOrder();
        orderDao.deleteSeckillOrder();
        return true;
    }
}

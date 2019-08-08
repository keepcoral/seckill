package com.bujidao.seckill;

import com.bujidao.seckill.dao.OrderDao;
import com.bujidao.seckill.domain.Order;
import com.bujidao.seckill.domain.SeckillOrder;
import com.bujidao.seckill.enums.OrderChannel;
import com.bujidao.seckill.enums.OrderState;
import com.bujidao.seckill.vo.GoodsVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderDaoTest {
    @Autowired
    private OrderDao orderDao;

    @Test
    public void testInsertOrder(){
        GoodsVo goodsVo=new GoodsVo();
        goodsVo.setId(1L);
        goodsVo.setGoodsName("bujidao");
        goodsVo.setSeckillPrice(20D);
        Order order=new Order();
        order.setCreateDate(new Date());
        order.setDeliveryAddrId(0L);
        order.setGoodsId(goodsVo.getId());
        order.setGoodsCount(1);
        order.setGoodsName(goodsVo.getGoodsName());
        order.setGoodsPrice(goodsVo.getSeckillPrice());
        order.setOrderChannel(OrderChannel.CHANNEL_ANDROID.getState());
        order.setStatus(OrderState.NOT_PAY.getState());
        order.setUserId(1L);
        System.out.println(orderDao.insertOrder(order));
        System.out.println( "id为"+order.getId());
    }

    @Test
    public void testInsertSeckillOrder(){
        SeckillOrder seckillOrder=new SeckillOrder();
        seckillOrder.setOrderId(3L);
        seckillOrder.setGoodsId(1L);
        seckillOrder.setUserId(1L);
        orderDao.insertSeckillOrder(seckillOrder);

    }
}

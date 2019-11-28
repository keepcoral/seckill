package com.bujidao.seckill.dao;

import com.bujidao.seckill.domain.Order;
import com.bujidao.seckill.domain.SeckillOrder;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface OrderDao {
    @Select("select * from tb_seckill_order where user_id=#{userId} and goods_id=#{goodsId}")
    SeckillOrder getOrderByUserIdGoodsId(@Param("userId") long userId, @Param("goodsId") long goodsId);

    @Insert("INSERT INTO tb_order(user_id, goods_id, delivery_addr_id, goods_name, goods_count, goods_price, order_channel, status, create_date, pay_date)" +
            "VALUES(#{userId}, #{goodsId}, #{deliveryAddrId}, #{goodsName}, #{goodsCount}, #{goodsPrice}, #{orderChannel}, #{status}, #{createDate}, #{payDate})")
    @Options(keyColumn = "id",keyProperty = "id",useGeneratedKeys = true)
    int insertOrder(Order order);

    @Insert("INSERT INTO tb_seckill_order(user_id, order_id, goods_id)VALUES(#{userId}, #{orderId}, #{goodsId})")
    @Options(keyColumn = "id",keyProperty = "id",useGeneratedKeys = true)
    int insertSeckillOrder(SeckillOrder seckillOrder);

    @Select("select * from tb_order where id=#{orderId}")
    Order getOrderById(@Param("orderId") long orderId);

    @Select("select * from tb_order")
    List<Order> queryAllOrder();

    @Select("select * from tb_seckill_order where user_id=#{userId} and goods_id=#{goodsId}")
    SeckillOrder queryOrderByUserIdGoodsIdByDb(long userId, long goodsId);


    @Delete("DELETE FROM tb_seckill_order")
    int deleteSeckillOrder();

    @Delete("DELETE FROM tb_order")
    int deleteOrder();
}

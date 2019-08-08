package com.bujidao.seckill.controller;

import com.bujidao.seckill.dao.GoodsDao;
import com.bujidao.seckill.domain.Order;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.result.CodeMsg;
import com.bujidao.seckill.result.Result;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.service.OrderService;
import com.bujidao.seckill.vo.GoodsVo;
import com.bujidao.seckill.vo.OrderDetailVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private GoodsService goodsService;
    @RequestMapping("/detail")
    public Result<OrderDetailVo> detail(User user, @RequestParam("orderId") long orderId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        Order order= orderService.getOrderById(orderId);
        if(order==null){
            return Result.error(CodeMsg.ORDER_NOT_EXIST);
        }
        GoodsVo goodsVo=goodsService.getSeckillGoodsById(order.getGoodsId());
        OrderDetailVo orderDetailVo=new OrderDetailVo();
        orderDetailVo.setGoods(goodsVo);
        orderDetailVo.setOrder(order);
        return Result.success(orderDetailVo);
    }
}

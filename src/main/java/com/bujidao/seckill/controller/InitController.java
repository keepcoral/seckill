package com.bujidao.seckill.controller;

import com.bujidao.seckill.domain.Order;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.redis.prefix.GoodsKeyPrefix;
import com.bujidao.seckill.redis.prefix.OrderKeyPrefix;
import com.bujidao.seckill.result.Result;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.service.OrderService;
import com.bujidao.seckill.service.UserService;
import com.bujidao.seckill.util.RedisUtil;
import com.bujidao.seckill.vo.GoodsVo;
import com.bujidao.seckill.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.aspectj.annotation.PrototypeAspectInstanceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Slf4j
@Controller
//@RequestMapping("/init")
public class InitController {

    @Autowired
    private UserService userService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/startseckill")
    public String startSeckill(HttpServletResponse response){

        List<Order> allOrder = orderService.getAllOrder();
        for(Order order:allOrder){
//            log.info("要删除的订单为:{}",order);
            RedisUtil.delete(OrderKeyPrefix.getOrderByUidGid,order.getUserId()+"-"+order.getGoodsId());
        }

        goodsService.updateAllGoods();
        orderService.deleteAllOrder();
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goodsVo : goodsList) {
            RedisUtil.set(GoodsKeyPrefix.getSeckillGoodsStock, "" + goodsVo.getId(), +goodsVo.getStockCount());
        }

        return "/local/login";
    }


    @RequestMapping("/testget")
    public boolean testGet(User user){
        System.out.println("当前用户为"+user);
        return false;
    }





}

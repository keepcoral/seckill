package com.bujidao.seckill.controller;

import com.bujidao.seckill.redis.prefix.GoodsKeyPrefix;
import com.bujidao.seckill.result.Result;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.service.OrderService;
import com.bujidao.seckill.service.RedisService;
import com.bujidao.seckill.service.UserService;
import com.bujidao.seckill.vo.GoodsVo;
import com.bujidao.seckill.vo.LoginVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/redis")
public class RedisController {
    @Autowired
    private RedisService redisService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @RequestMapping("/getall")
    public Result<Map> getAll() {
        return null;
    }

    @RequestMapping("/flushall")
    public Result<Boolean> flushAll() {
        redisService.flushAll();
        return null;
    }

    /**
     * 测试使用的方法
     *
     * @return
     */
    @RequestMapping("/initall")
    public Result<Boolean> initAll(HttpServletResponse response) {
        //初始化商品
        redisService.flushAll();
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        for (GoodsVo goodsVo : goodsList) {
            redisService.set(GoodsKeyPrefix.getSeckillGoodsStock, "" + goodsVo.getId(), +goodsVo.getStockCount());
        }

        //初始化登陆
        LoginVo loginVo = new LoginVo();
        loginVo.setMobile("18819489018");
        loginVo.setPassword("d3b1294a61a07da9b49b6e22b2cbd7f9");
        userService.login(response, loginVo);

        //初始化订单表
        orderService.deleteAllOrder();
        return null;
    }
}

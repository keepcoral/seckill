package com.bujidao.seckill.controller;

import com.bujidao.seckill.domain.SeckillOrder;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.rabbitmq.MQSender;
import com.bujidao.seckill.rabbitmq.SeckillMessage;
import com.bujidao.seckill.redis.prefix.GoodsKeyPrefix;
import com.bujidao.seckill.redis.prefix.SeckillKeyPrefix;
import com.bujidao.seckill.result.CodeMsg;
import com.bujidao.seckill.result.Result;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.service.OrderService;
import com.bujidao.seckill.service.SeckillService;
import com.bujidao.seckill.util.RedisUtil;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@Slf4j
@RestController
@RequestMapping("/seckill")
public class SeckillController implements InitializingBean{


    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private SeckillService seckillService;

    @Autowired
    private MQSender sender;

    private Map<Long,Boolean> localOverMap=new HashMap<>();

    /**
     * 当创建SeckillController并赋值好之后就会调用初始化方法
     * 系统启动的时候就会加载商品数量到redis中
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("----------");
        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        if (goodsList == null) {
            return;
        }
        //把所有商品库存*3就是大闸的令牌数
        int sum=0;
        for (GoodsVo goodsVo : goodsList) {
            sum+=goodsVo.getStockCount();
            RedisUtil.set(GoodsKeyPrefix.getSeckillGoodsStock, "" + goodsVo.getId(), +goodsVo.getStockCount());
            localOverMap.put(goodsVo.getId(),false);
        }
        RedisUtil.set(GoodsKeyPrefix.getSeckillGoodsStock,"door_num",sum*3);
    }

    @PostMapping(value = "/doseckill")
    public Result<Integer> doSeckill(User user,
                                     @RequestParam("goodsId") long goodsId,
                                     @RequestParam("seckillToken")String seckillToken) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }

        //校验秒杀token
        if(seckillToken==null) {
            return Result.error(CodeMsg.FAIL_CHECK_SECKILL_TOKEN);
        }
        String skToken=RedisUtil.get(SeckillKeyPrefix.getSeckillToken,user.getId()+ " " + goodsId);
        if(!StringUtils.equals(skToken,seckillToken)){
            return Result.error(CodeMsg.FAIL_CHECK_SECKILL_TOKEN);
        }

        //再做一层减少redis的访问的优化
        Boolean isOver=localOverMap.get(goodsId);
        if(isOver){
            return Result.error(CodeMsg.STOCK_OVER);
        }

        //判断是否已经秒杀到，防止重复秒杀
        SeckillOrder seckillOrder = orderService.getOrderByUserIdGoodsId(user.getId(), goodsId);
        if (seckillOrder != null) {
            return Result.error(CodeMsg.REPEATE_SECKILL);
        }

        //预减库存
        long stock = RedisUtil.decr(GoodsKeyPrefix.getSeckillGoodsStock, "" + goodsId);
        if (stock < 0) {
            localOverMap.put(goodsId,true);
            return Result.error(CodeMsg.STOCK_OVER);
        }

        //入队列
        SeckillMessage seckillMessage=new SeckillMessage(user,goodsId);
        sender.sendSeckillMessage(seckillMessage);
        return Result.success(0);//0表示排队中
    }

    /**
     * 获取秒杀访问令牌，如果没有令牌那么无法下单
     */
    @PostMapping(value = "/generatetoken")
    @ResponseBody
    public Result<String> generateSeckillToken(User user,@RequestParam("goodsId") long goodsId){
        if(user==null){
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        String token=seckillService.generateSeckillToken(user.getId(),goodsId);
        //一般只有非法请求过来才会走到这段逻辑
        if(token==null) return Result.error(CodeMsg.FAIL_GENERATE_SECKILL_TOKEN);
        return Result.success(token);
    }
    /**
     * orderId:成功
     * -1:秒杀失败
     * 0:排队中
     */
    @GetMapping(value = "/result")
    public Result<Long> result(User user, @RequestParam("goodsId") long goodsId) {
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        long orderId=seckillService.getSeckillResult(user.getId(),goodsId);
        return Result.success(orderId);
    }
}

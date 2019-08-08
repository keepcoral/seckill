package com.bujidao.seckill.controller;

import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.rabbitmq.MQSender;
import com.bujidao.seckill.result.Result;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.vo.GoodsVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class JMTestController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private MQSender mqSender;

    @RequestMapping("/info")
    public Boolean info(Model model, User user) {
        model.addAttribute("user", user);
        return true;
    }

    @RequestMapping("/getlist")
    public List<GoodsVo> getlist() {
        return goodsService.listGoodsVo();
    }

}

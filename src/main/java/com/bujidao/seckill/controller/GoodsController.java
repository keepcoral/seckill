package com.bujidao.seckill.controller;

import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.redis.prefix.GoodsKeyPrefix;
import com.bujidao.seckill.result.CodeMsg;
import com.bujidao.seckill.result.Result;
import com.bujidao.seckill.service.GoodsService;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.util.RedisUtil;
import com.bujidao.seckill.vo.GoodsDetailVo;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


@Slf4j
@Controller
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ThymeleafViewResolver thymeleafViewResolver;

    /**
     * 页面缓存
     * 1取缓存
     * 2手动渲染模板
     * 3结果输出
     * produces是返回数据类型
     * 返回json数据application/json
     * 返回html数据text/html
     */
    @RequestMapping(value="/goodslist",produces = "text/html")
    @ResponseBody
    public String listgoods(HttpServletRequest request,
                            HttpServletResponse response,
                            Model model,
                            User user) {
        if (user == null) {
            //如果从redis没取到数据，跳转到login
//            return "login";
        }
        //1.取缓存
        String html= JsonUtil.stringToObject(RedisUtil.get(GoodsKeyPrefix.getGoodsList,""),String.class);
        //如果redis存在，直接取出页面进行渲染
        if(!StringUtils.isEmpty(html)) {
            return html;
        }

        List<GoodsVo> goodsList = goodsService.listGoodsVo();
        model.addAttribute("user", user);
        model.addAttribute("goodsList",goodsList);
        //        return "goodslist";
        //2.手动渲染
        WebContext webContext=new WebContext(request,response,request.getServletContext(),request.getLocale(),model.asMap()) ;
        //渲染的模板的名字就是要渲染的那个页面的名字，如"goodslist.html"
        html=thymeleafViewResolver.getTemplateEngine().process("goodslist",webContext);
        if(!StringUtils.isEmpty(html)) {
            RedisUtil.set(GoodsKeyPrefix.getGoodsList,"",html);
        }
        //3.输出结果
        return html;
    }

    @RequestMapping(value="/detail/{goodsId}")
    @ResponseBody
    public Result<GoodsDetailVo> toDetail(User user,
                                          @PathVariable("goodsId") long goodsId) {
        GoodsDetailVo goodsDetailVo=new GoodsDetailVo();
        if (user == null) {
            return Result.error(CodeMsg.SESSION_ERROR);
        }
        log.info("利用token获取的user=="+user);
        GoodsVo goodsVo = goodsService.getSeckillGoodsById(goodsId);
        if (goodsVo == null) {
            log.info("页面详情页未找到商品id为{}的商品",goodsId);
            return Result.error(CodeMsg.GOODS_NOT_FOUND);
        }
        goodsDetailVo.setGoodsVo(goodsVo);
        goodsDetailVo.setUser(user);
        long startAt = goodsVo.getStartDate().getTime();
        long endAt = goodsVo.getEndDate().getTime();
        long now = System.currentTimeMillis();
        int seckillState = 0;
        long remainTime = 0;

//        log.info("当前时间{}",new Date(now));
//        log.info("秒杀开始时间{}", goodsVo.getStartDate());
//        log.info("秒杀结束时间{}",goodsVo.getEndDate());


        if (now < startAt) {//秒杀还没开始
            seckillState = 0;
            remainTime = (startAt - now)/1000;//倒计时还有多少秒
        } else if (now > endAt) {//秒杀已经结束
            seckillState = 2;
            remainTime = -1;
        } else {//秒杀正在进行
            seckillState = 1;
            remainTime = 0;
        }
        goodsDetailVo.setRemainTime(remainTime);
        goodsDetailVo.setSeckillState(seckillState);
        return Result.success(goodsDetailVo);
    }

}

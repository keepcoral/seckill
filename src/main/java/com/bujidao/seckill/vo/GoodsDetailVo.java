package com.bujidao.seckill.vo;

import com.bujidao.seckill.domain.User;
import lombok.Data;

@Data
public class GoodsDetailVo {
    private int seckillState;
    private long remainTime;
    private GoodsVo goodsVo;
    private User user;

}

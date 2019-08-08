package com.bujidao.seckill.vo;

import com.bujidao.seckill.domain.Order;
import lombok.Data;

@Data
public class OrderDetailVo {
    private GoodsVo goods;
    private Order order;

}

package com.bujidao.seckill.rabbitmq;

import com.bujidao.seckill.domain.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class SeckillMessage {
    private User user;
    private long goodsId;
}

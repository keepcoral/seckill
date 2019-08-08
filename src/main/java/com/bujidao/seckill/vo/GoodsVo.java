package com.bujidao.seckill.vo;

import com.bujidao.seckill.domain.Goods;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.StringJoiner;

/**
 * 把商品和秒杀商品的信息都拼接在一块的包装类
 */
@Data
public class GoodsVo extends Goods {
    private Integer stockCount;
    private Date startDate;
    private Date endDate;
    private Double seckillPrice;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("GoodsVo{");
        sb.append("stockCount=").append(stockCount);
        sb.append(", startDate=").append(startDate);
        sb.append(", endDate=").append(endDate);
        sb.append(", seckillPrice=").append(seckillPrice);
        sb.append('}').append(super.toString());
        return sb.toString();
    }
}

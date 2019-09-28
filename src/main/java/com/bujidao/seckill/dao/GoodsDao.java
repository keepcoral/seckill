package com.bujidao.seckill.dao;

import com.bujidao.seckill.domain.SeckillGoods;
import com.bujidao.seckill.vo.GoodsVo;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;


@Mapper
public interface GoodsDao {
    /**
     * 对秒杀商品goods减少1的库存
     */
    @Update("UPDATE tb_seckill_goods SET stock_count = stock_count -1 where id=#{goodsId} and stock_count>0")
    int reduceStock(SeckillGoods seckillGoods);

    /**
     * 根据秒杀商品id获取商品
     */
    @Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.seckill_price from tb_seckill_goods sg left join tb_goods g on sg.goods_id=g.id where g.id=#{goodsId}")
    GoodsVo getSeckillGoodsById(@Param("goodsId") long goodsId);

    /**
     * 列出所有秒杀商品
     */
    @Select("select g.*,sg.stock_count,sg.start_date,sg.end_date,sg.seckill_price from tb_seckill_goods sg left join tb_goods g on sg.goods_id=g.id")
    List<GoodsVo> listGoodsVo();

    @Update("UPDATE tb_seckill_goods SET start_date=#{startDate},end_date=#{endDate}")
    int updateAllGoods(@Param("startDate") Date startDate,@Param("endDate") Date endDate);
}

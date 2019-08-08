package com.bujidao.seckill.service;

import com.bujidao.seckill.dao.GoodsDao;
import com.bujidao.seckill.domain.SeckillGoods;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GoodsService {
    @SuppressWarnings("all")
    @Autowired
    private GoodsDao goodsDao;

    public List<GoodsVo> listGoodsVo(){
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getSeckillGoodsById(long goodsId){
        return goodsDao.getSeckillGoodsById(goodsId);
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        log.info("要减少库存的商品为---{}",goodsVo);
        SeckillGoods seckillGoods=new SeckillGoods();
        seckillGoods.setGoodsId(goodsVo.getId());
        int effectedNum=goodsDao.reduceStock(seckillGoods);
        return effectedNum>0;
    }

}

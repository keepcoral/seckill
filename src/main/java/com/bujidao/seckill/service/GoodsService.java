package com.bujidao.seckill.service;

import com.bujidao.seckill.dao.GoodsDao;
import com.bujidao.seckill.domain.SeckillGoods;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.redis.prefix.GoodsKeyPrefix;
import com.bujidao.seckill.redis.prefix.UserKeyPrefix;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.util.RedisUtil;
import com.bujidao.seckill.vo.GoodsVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class GoodsService {
    @SuppressWarnings("all")
    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private CacheService cacheService;

    public List<GoodsVo> listGoodsVo() {
        return goodsDao.listGoodsVo();
    }

    public GoodsVo getSeckillGoodsById(long goodsId) {
        //先找本地缓存在找redis，最后找数据库
        GoodsVo goodsVo = (GoodsVo) cacheService.getCommonCache("goods:" + goodsId);
        if (goodsVo == null) {
            goodsVo = JsonUtil.stringToObject(RedisUtil.get(GoodsKeyPrefix.getGoodsDetail, goodsId + ""), GoodsVo.class);
            log.info("从redis拿到的缓存{}",goodsVo);
            if (goodsVo == null) {
                goodsVo = goodsDao.getSeckillGoodsById(goodsId);
                RedisUtil.set(GoodsKeyPrefix.getGoodsDetail, "" + goodsId, goodsVo);
            }
            cacheService.setCommonCache("goods:" + goodsId, goodsVo);
        }
        return goodsVo;
    }

    public boolean reduceStock(GoodsVo goodsVo) {
        SeckillGoods seckillGoods = new SeckillGoods();
        seckillGoods.setGoodsId(goodsVo.getId());
        int effectedNum = goodsDao.reduceStock(seckillGoods);
        return effectedNum > 0;
    }

    /**
     * 让所有商品开始秒杀倒计时
     */
    public boolean updateAllGoods() {
        long curTime = System.currentTimeMillis();
        long startTime = curTime + 60 * 1000;
        long endTime = curTime + 30 * 60 * 1000;
        int effected = goodsDao.updateAllGoods(new Date(startTime), new Date(endTime));
        return effected > 0;
    }
}

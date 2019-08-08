package com.bujidao.seckill;

import com.bujidao.seckill.dao.GoodsDao;
import com.bujidao.seckill.domain.SeckillGoods;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsDaoTest {
    @Autowired
    private GoodsDao goodsDao;

    @Test
    public void testListALlGoods(){
        System.out.println(goodsDao.listGoodsVo());
    }

    @Test
    public  void testGetById(){
        System.out.println(goodsDao.getSeckillGoodsById(1L));
    }

    @Test
    public void testReduceStock(){
        SeckillGoods seckillGoods=new SeckillGoods();
        seckillGoods.setId(1L);
        goodsDao.reduceStock(seckillGoods);
    }
}

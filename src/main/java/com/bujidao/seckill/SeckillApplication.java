package com.bujidao.seckill;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@MapperScan(basePackages = {"com.bujidao.seckill.dao"})
@Slf4j
public class SeckillApplication
//        extends SpringBootServletInitializer
{

    public static void main(String[] args) {
        SpringApplication.run(SeckillApplication.class, args);
    }
//    @Override
//    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
//        return builder.sources(SeckillApplication.class);
//    }

}
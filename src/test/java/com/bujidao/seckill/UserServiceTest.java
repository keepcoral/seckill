package com.bujidao.seckill;

import com.bujidao.seckill.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;


@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {
    @Autowired
    public UserService userService;

    @Autowired
    public DataSource dataSource;

    @Test
    public void testGetById(){
        System.out.println(dataSource.getClass().getName());
        System.out.println(userService.getUserById(1));
    }
}

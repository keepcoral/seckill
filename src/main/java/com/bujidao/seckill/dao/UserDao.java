package com.bujidao.seckill.dao;
import com.bujidao.seckill.domain.User;
import org.apache.ibatis.annotations.*;


@Mapper//只有添加了@Mapper才能被Spring管理
public interface UserDao {
    @Select("select * from tb_user where id=#{id}")
    User getById(@Param("id") long id);

    @Insert("insert into tb_user(id,nickname,password,salt) values(#{id},#{nickname},#{password},#{salt})")
    int InsertUser(User user);

    @Update("update tb_user set password=#{password} where id=#{id}")
    int update(User user);


    @Delete("delete from tb_user where id <> 18819489018")
    int deleteUser();
}

package com.bujidao.seckill.service;

import com.bujidao.seckill.dao.UserDao;
import com.bujidao.seckill.domain.User;
import com.bujidao.seckill.exception.GlobleException;
import com.bujidao.seckill.redis.prefix.UserKeyPrefix;
import com.bujidao.seckill.result.CodeMsg;
import com.bujidao.seckill.util.JsonUtil;
import com.bujidao.seckill.util.MD5Util;
import com.bujidao.seckill.util.RedisUtil;
import com.bujidao.seckill.util.UUIDUtil;
import com.bujidao.seckill.vo.LoginVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class UserService {
    public static final String COOKIE_NAME_TOKEN = "token";
    @Autowired
    private UserDao userDao;


    /**
     * 将从数据库中取User变为从缓存中取User
     */
    public User getUserById(long id) {
        //取缓存
        User user = JsonUtil.stringToObject(RedisUtil.get(UserKeyPrefix.getUserById, "" + id), User.class);
        if (user != null) {
            return user;
        }
        //取数据库
        user = userDao.getById(id);
        if (user != null) {
            RedisUtil.set(UserKeyPrefix.getUserById, "" + id, JsonUtil.objToString(user));
        }
        return user;
    }

    /**
     * 涉及到数据库以及缓存的更新问题:
     * 先更新数据库
     * 再让缓存失效
     * 防止出现一种情况：
     * 缓存失效但数据库还未更新，再次访问获取到仍是旧信息
     */
    public boolean updatePass(String token, long id, String passwordNew) {
        User user = getUserById(id);
        if (user == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //这里new一个新对象的是为了优化数据库访问
        //如果更新旧对象，那么产生的sql和binlog就越多
        //修改哪个字段就只更新哪个字段
        User toBeUpdate = new User();
        toBeUpdate.setId(id);
        toBeUpdate.setPassword(MD5Util.formPassToDBPass(passwordNew, user.getSalt()));
        //更新数据库
        userDao.update(user);
        //修改缓存,token以及user都要删除
        RedisUtil.delete(UserKeyPrefix.getUserById, "" + id);
        user.setPassword(passwordNew);
        RedisUtil.set(UserKeyPrefix.token, token, JsonUtil.objToString(user));
        return true;
    }

    public boolean login(HttpServletResponse response, LoginVo loginVo) {
        //这里抛出的异常由异常处理器GlobleExceptionHandler统一处理
        if (loginVo == null) {
            throw new GlobleException(CodeMsg.SERVER_ERROR);
        }
        String mobile = loginVo.getMobile();
        String formPass = loginVo.getPassword();
        //服务端需要再次校验前端传过来的参数
        if (StringUtils.isEmpty(mobile)) {
            throw new GlobleException(CodeMsg.MOBILE_EMPTY);
        } else if (StringUtils.isEmpty(formPass)) {
            throw new GlobleException(CodeMsg.PASSWORD_EMPTY);
        }
        //校验手机号是否存在
        User user = getUserById(Long.parseLong(mobile));
        if (user == null) {
            throw new GlobleException(CodeMsg.MOBILE_NOT_EXIST);
        }
        //校验密码
        String dbPass = user.getPassword();
        String salt = user.getSalt();
        String calcPass = MD5Util.formPassToDBPass(formPass, salt);
        if (!calcPass.equals(dbPass)) {
            throw new GlobleException(CodeMsg.PASSWORD_ERROR);
        }
        //生成cookie
        String token = UUIDUtil.uuid();
        addCookie(response, user, token);
        return true;
    }


    public User getByToken(HttpServletResponse response, String token) {
        if (StringUtils.isEmpty(token)) return null;
        User user = JsonUtil.stringToObject(RedisUtil.get(UserKeyPrefix.token, token), User.class);
        if (user == null) {
            return null;
        }
        //延长有效期
        addCookie(response, user, token);
        return user;
    }

    //延长session的有效期
    private void addCookie(HttpServletResponse response, User user, String token) {
        //将token放到redis中
        RedisUtil.set(UserKeyPrefix.token, token, JsonUtil.objToString(user));
        //将该token放到cookie传给客户端
        Cookie cookie = new Cookie(COOKIE_NAME_TOKEN, token);
        //设置cookie有效期
        cookie.setMaxAge(UserKeyPrefix.token.expireSeconds());
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public void addUser(User user) {
        userDao.InsertUser(user);
    }
}

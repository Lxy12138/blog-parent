package com.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.entity.SysUser;
import com.blog.service.LoginService;
import com.blog.service.SysUserService;
import com.blog.utils.JWTUtils;
import com.blog.vo.ErrorCode;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParam;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;


    @Override
    public Result login(LoginParam loginParam) {
        /**
         * 1. 检查参数是否合法
         * 2. 根据用户名和密码去user表中查询是否存在
         * 3. 如果不存在，登陆失败
         * 4. 如果存在，使用jwt 生成token 返回给前端
         * 5. token放入redis当中，redis token:user信息 设置过期时间
         * 6. 登录认证时先验证token字符串是否合法，去redis认真是否存在
         */
        String account = loginParam.getAccount();
        String password = loginParam.getPassword();
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password)) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }
        password = DigestUtils.md5Hex(password + loginParam.getSlat());
        SysUser user = sysUserService.findUser(account, password);
        if (user == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(),ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        String token = JWTUtils.createToken(user.getId());
        redisTemplate.opsForValue().set("TOKEN_"+token, JSON.toJSONString(user),1, TimeUnit.DAYS);

        return Result.success(token);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete("TOKEN_"+token);
        return Result.success(null);
    }
}

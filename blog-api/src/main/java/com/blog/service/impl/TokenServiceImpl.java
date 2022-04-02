package com.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.blog.entity.SysUser;
import com.blog.service.LoginService;
import com.blog.service.TokenService;
import com.blog.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

    @Autowired
    LoginService loginService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    @Override
    public SysUser checkToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null) {
            return null;
        }
        String userJSON = redisTemplate.opsForValue().get("TOKEN_" + token);
        if (StringUtils.isBlank(userJSON)) {
            return null;
        }
        SysUser sysUser = JSON.parseObject(userJSON, SysUser.class);
        return sysUser;
    }
}

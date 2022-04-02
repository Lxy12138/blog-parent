package com.blog.service;

import com.blog.entity.SysUser;

public interface TokenService {

    SysUser checkToken(String token);
}

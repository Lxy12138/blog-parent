package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.LoginParam;

public interface LoginService {
    /**
     * 登录
     * @param loginParam
     * @return
     */
    Result login(LoginParam loginParam);

    Result logout(String token);
}
package com.blog.service;

import com.blog.vo.Result;
import com.blog.vo.params.LoginParam;

public interface RegisterService {
    Result register(LoginParam loginParam);
}

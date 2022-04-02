package com.blog.service;

import com.blog.entity.SysUser;
import com.blog.vo.Result;
import com.blog.vo.UserVo;

public interface SysUserService {
    SysUser findUserById(Long id);

    SysUser findUser(String account, String password);

    Result findUserByToken(String token);

    SysUser finUserByAccount(String account);

    void save(SysUser sysUser);

    UserVo findUserVoById(Long authorId);
}

package com.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.blog.entity.SysUser;
import com.blog.mapper.SysUserMapper;
import com.blog.service.SysUserService;
import com.blog.service.TokenService;
import com.blog.vo.ErrorCode;
import com.blog.entity.LoginUser;
import com.blog.vo.Result;
import com.blog.vo.UserVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysUserServiceImpl implements SysUserService {

    @Autowired
    SysUserMapper sysUserMapper;

    @Autowired
    TokenService tokenService;

    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("心疼giegie");
        }
        return sysUser;
    }

    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.eq(SysUser::getPassword,password);
        queryWrapper.select(SysUser::getId,SysUser::getAccount,SysUser::getAvatar,SysUser::getNickname);
        queryWrapper.last("limit 1");
        return sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public Result findUserByToken(String token) {
        SysUser sysUser = tokenService.checkToken(token);
        if (sysUser == null) {
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(),ErrorCode.PARAMS_ERROR.getMsg());
        }

        LoginUser loginUserVo = new LoginUser();
        loginUserVo.setId(sysUser.getId());
        loginUserVo.setAccount(sysUser.getAccount());
        loginUserVo.setAvatar(sysUser.getAvatar());
        loginUserVo.setNickname(sysUser.getNickname());

        Result success = Result.success(loginUserVo);
        return success;
    }

    @Override
    public SysUser finUserByAccount(String account) {

        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SysUser::getAccount,account);
        queryWrapper.last("limit 1");
        return this.sysUserMapper.selectOne(queryWrapper);
    }

    @Override
    public void save(SysUser sysUser) {
        this.sysUserMapper.insert(sysUser);
    }

    @Override
    public UserVo findUserVoById(Long authorId) {
        SysUser sysUser = sysUserMapper.selectById(authorId);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("游客");
            sysUser.setAvatar("/static/user/user_1.png");
            sysUser.setId(1000000001L);
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(sysUser,userVo);
        return userVo;
    }
}

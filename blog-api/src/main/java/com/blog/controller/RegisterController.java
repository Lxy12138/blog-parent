package com.blog.controller;

import com.blog.service.RegisterService;
import com.blog.vo.Result;
import com.blog.vo.params.LoginParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("register")
public class RegisterController {

    @Autowired
    RegisterService registerService;

    @PostMapping
    public Result register(@RequestBody LoginParam loginParam) {
        return registerService.register(loginParam);
    }

}

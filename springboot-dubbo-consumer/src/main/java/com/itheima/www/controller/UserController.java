package com.itheima.www.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.www.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private IUserService iUserService;

    @RequestMapping("/sayHello")
    public String sayHello(String name){
        return  iUserService.sayHello(name);
    }

}

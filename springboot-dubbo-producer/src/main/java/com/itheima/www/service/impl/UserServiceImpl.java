package com.itheima.www.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.www.service.IUserService;
import org.springframework.stereotype.Component;

@Component
// 新版的已经改为DubboService老版的得写两个Service，推荐xml方式，因为xml可以具体到方法级别，更适合工业要求
@Service(interfaceClass = IUserService.class)
public class UserServiceImpl implements IUserService {
    @Override
    public String sayHello(String name) {
        return "Hello, name. This is provider.";
    }
}

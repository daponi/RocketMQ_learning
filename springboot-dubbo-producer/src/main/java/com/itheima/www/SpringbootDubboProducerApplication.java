package com.itheima.www;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 生产者Demo
 * 结合springboot、dubbo、zookeeper
 *  dubbo作为RPC远程调用，Zookeeper作为注册中心
 */
@EnableDubboConfiguration
@SpringBootApplication
public class SpringbootDubboProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringbootDubboProducerApplication.class, args);
    }

}

package com.itheima.www;

import com.alibaba.dubbo.spring.boot.annotation.EnableDubboConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 消费者Demo
 * 结合springboot、dubbo、zookeeper
 * dubbo作为RPC远程调用，Zookeeper作为注册中心
 */
@SpringBootApplication
@EnableDubboConfiguration
public class ConsumerBootstrap {
    public static void main(String[] args) {
        SpringApplication.run(ConsumerBootstrap.class, args);
    }
}

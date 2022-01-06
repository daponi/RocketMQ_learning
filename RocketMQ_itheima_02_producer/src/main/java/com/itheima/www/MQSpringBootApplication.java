package com.itheima.www;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * rocketMQ和springboot整合
 * 生产者
 */
@SpringBootApplication
@Slf4j
public class MQSpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(MQSpringBootApplication.class, args);
        log.info("==========成功启动生产者");
    }
}

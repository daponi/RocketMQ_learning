package com.itheima.www;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * rocketMQ和springboot整合
 * 消费者 consumer
 */
@SpringBootApplication
@Slf4j
public class MQConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MQConsumerApplication.class, args);
        log.info("===========消费者成功启动!");
    }
}

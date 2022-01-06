package com.itheima.www.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Service;

/**
 * 实现消息监听接口实现，进行消费消息回调
 */

@Slf4j
@Service
@RocketMQMessageListener(topic = "springboot-mq",consumerGroup = "${rocketmq.consumer.group}",
        selectorExpression = "myTagDemo")
public class ConsumerDemo implements RocketMQListener<String> {
    @Override
    public void onMessage(String message) {
      log.info("**************************   received message: {}", message);
    }
}

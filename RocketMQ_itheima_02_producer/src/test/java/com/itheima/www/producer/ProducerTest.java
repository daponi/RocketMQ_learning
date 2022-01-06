package com.itheima.www.producer;

import com.itheima.www.MQSpringBootApplication;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.context.junit4.SpringRunner;

//在正常情况下测试类是需要@RunWith的，作用是告诉java你这个类通过用什么运行环境运行，
// 例如启动和创建spring的应用上下文。否则你需要为此在启动时写一堆的环境配置代码。你在IDEA里去掉@RunWith仍然能跑是因为在IDEA里识别为一个JUNIT的运行环境，相当于就是一个自识别的RUNWITH环境配置。但在其他IDE里并没有。
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MQSpringBootApplication.class})
@Slf4j
public class ProducerTest {
    @Autowired
    private RocketMQTemplate rocketMQTemplate;


    @Test
    public void test1() {
        // 建立消息，并设置自定义参数
        Message message = MessageBuilder.withPayload("【你好，我是message002！！！】").setHeader(MessageConst.PROPERTY_KEYS, 15103111039L).build();

        /**
         * 发送消息，topic与tag合并成了一个参数
         * Params:
         * destination – formats: `topicName:tags`
         * message – Message
         */
        // rocketMQTemplate.convertAndSend("springboot-mq:mytagDemo","hello springboot rocketmq");
        rocketMQTemplate.convertAndSend("springboot-mq:myTagDemo",message);
        log.info("==================消息发送成功");
    }
}

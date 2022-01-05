package com.itheima.www.base.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * 生产者 单向发送信息
 * 1.实例化消息生产者Producer
 * 2.指定Nameserver地址
 * 3.启动producer
 * 4.创建消息对象，指定主题Topic、Tag和消息体
 * 5.发送单向消息
 * 6.关闭生产者producer
 */
public class OneWayProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        // 1.实例化消息生产者Producer
        DefaultMQProducer producer = new DefaultMQProducer("producerGroupDemo02");
        // 2.指定Nameserver地址
        producer.setNamesrvAddr("my01:9876;my02:9876");
        // 3.启动producer
        producer.start();
        for (int i = 0; i < 10; i++) {
            // 4.创建消息对象，指定主题Topic、Tag和消息体
            /**
             * 参数一：消息主题Topic
             * 参数二：消息Tag
             * 参数三：消息内容
             */
            byte[] body = ("OnewayProducer,Msg " + i + " ...").getBytes(StandardCharsets.UTF_8);
            Message message = new Message("TopicDemo02", "TagsDemo-Oneway", body);
            // 5.发送单向消息
            producer.sendOneway(message);
        }
        // 6.关闭生产者producer
        producer.shutdown();
    }
}

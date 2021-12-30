package com.atguigu.www.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;

/**
 * 生产者 同步消发送息
 */
public class SyncProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 创建一个producer，参数为Producer Group名称
        DefaultMQProducer producerGroup = new DefaultMQProducer("producerGroupDemo");
        // 指定nameServer地址
        producerGroup.setNamesrvAddr("my01:9876");
        // 设置当发送失败时重试发送的次数，默认为2次
        producerGroup.setRetryTimesWhenSendFailed(3);
        // 设置发送超时时限为5s，默认3s
        producerGroup.setSendMsgTimeout(5000);

        // 开启生产者
        producerGroup.start();

        // 生产者发送100条消息
        for (int i = 0; i < 100; i++) {
            byte[] body = ("Hello,Msg no." + i + " ...").getBytes(StandardCharsets.UTF_8);
            Message msg = new Message("TopicDemo", "TagsDemo", body);
            //为消息指定key
            msg.setKeys("keys-"+i);
            //同步发送消息
            SendResult result = producerGroup.send(msg);
            // 打印结果
            System.out.println(result);
        }
        // 关闭producer
        producerGroup.shutdown();
    }
}

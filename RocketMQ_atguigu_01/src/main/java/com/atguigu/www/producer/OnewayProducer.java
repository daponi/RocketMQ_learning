package com.atguigu.www.producer;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

public class OnewayProducer {
    public static void main(String[] args) throws Exception{
        DefaultMQProducer producer = new DefaultMQProducer("producerGroupDemo");
        producer.setNamesrvAddr("my01:9876");
        producer.start();

        for (int i = 0; i < 10; i++) {
            byte[] body = ("Hello, OnewayProducer No.," + i+" ....").getBytes();
            Message msg = new Message("TopicOneway", "TagsDemo", body);
            //为消息指定key
            msg.setKeys("keys-"+i);
            // 单向发送
            producer.sendOneway(msg);
        }
        producer.shutdown();
        System.out.println("producer shutdown");
    }
}

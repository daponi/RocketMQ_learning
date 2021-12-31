package com.atguigu.www.producer;

import com.atguigu.www.splitter.MessageListSplitter;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * 生产者 批量发送消息Demo
 */
public class BatchProducer {
    public static void main(String[] args) throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("consumerGroupDemo");
        producer.setNamesrvAddr("my01:9876");
        // 指定要发送的消息的最大大小，默认是4M。 不过，仅修改该属性是不行的，还需要同时修改broker加载的配置文件中的maxMessageSize属性
        // producer.setMaxMessageSize(8 * 1024 * 1024);
        producer.start();

        // 定义要发送的消息集合
        List<Message> messages = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            byte[] body = ("Hello, Message Collection，No." + i).getBytes();
            Message msg = new Message("TopicBatchDemo", "TagsDemo", body);
            messages.add(msg);
        }

        // 定义消息列表分割器，将消息列表分割为多个不超出4M大小的小列表
        MessageListSplitter splitter = new MessageListSplitter(messages);
        while (splitter.hasNext()) {
            try {
                List<Message>  listItem = splitter.next();
                producer.send(listItem);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        producer.shutdown();
    }
}
package com.itheima.www.batch;

import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 生产者 批量发送消息
 */
public class Producer {

    public static void main(String[] args) throws Exception {
        //1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("192.168.25.135:9876;192.168.25.138:9876");
        //3.启动producer
        producer.start();


        List<Message> msgs = new ArrayList<Message>();


        //4.创建消息对象，指定主题Topic、Tag和消息体
        /**
         * 参数一：消息主题Topic
         * 参数二：消息Tag
         * 参数三：消息内容
         */
        Message msg1 = new Message("BatchTopic", "Tag1", ("Hello World" + 1).getBytes());
        Message msg2 = new Message("BatchTopic", "Tag1", ("Hello World" + 2).getBytes());
        Message msg3 = new Message("BatchTopic", "Tag1", ("Hello World" + 3).getBytes());

        Collections.addAll(msgs, msg1, msg2, msg3);

        //5.批量发送消息
        // 定义消息列表分割器，将消息列表分割为多个不超出4M大小的小列表
        MessageListSplitter splitter = new MessageListSplitter(msgs);
        while (splitter.hasNext()) {
            try {
                List<Message> listItem = splitter.next();
                SendResult result = producer.send(listItem);
                //发送状态
                SendStatus status = result.getSendStatus();
                System.out.println("发送结果:" + result);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        //线程睡1秒
        TimeUnit.SECONDS.sleep(2);


        //6.关闭生产者producer
        producer.shutdown();
    }

}

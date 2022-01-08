package com.itheima.www.base.producer;


import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

/**
 * 生产者 异步发送消息
 * 1.创建消息生产者producer，并制定生产者组名
 * 2.指定Nameserver地址
 * 3.启动producer
 * 4.创建消息对象，指定主题Topic、Tag和消息体
 * 5.发送异步消息
 * 6.关闭生产者producer
 */
public class AsyncProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        // 1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("producerGroupDemo02");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("my01:9876;my02:9876");
        // 设置当发送失败时重试发送的次数，默认为2次
        producer.setRetryTimesWhenSendFailed(3);
        // 设置发送超时时限为5s，默认3s
        producer.setSendMsgTimeout(5000);
        // 3.启动producer
        producer.start();
        for (int i = 0; i < 10; i++) {
            byte[] body = ("SyncProducer,Msg " + i + " ...").getBytes(StandardCharsets.UTF_8);
            // 4.创建消息对象，指定主题Topic、Tag和消息体
            Message msg = new Message("TopicDemo02", "TagsDemo-Async", body);
            ;
            // 5.发送异步消息
            producer.send(msg, new SendCallback() {
                // 收到ack的回调函数
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("发送结果:" + sendResult);
                }

                // 发送失败回调函数
                @Override
                public void onException(Throwable e) {
                    System.out.println("发送异常:" + e);
                }
            });
        }
        // 由于采用的是异步发送，所以若这里不sleep， 则消息还未发送就会将producer给关闭，报错
        TimeUnit.SECONDS.sleep(3);
        producer.shutdown();
    }
}

package com.itheima.www.order;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;

import java.util.List;

/**
 * 消费者 消费顺序消息
 * 消费者一个线程负责顺序消费一个队列里的全部消息，一个订单号的所有消息都放在一个队列
 */
public class Consumer {
    public static void main(String[] args) throws MQClientException {
        //1.创建消费者Consumer，制定消费者组名
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("group1");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("my01:9876;my02:9876");
        //3.订阅主题Topic和Tag
        consumer.subscribe("OrderTopicDemo", "*");
        //4.注册消息监听器
        consumer.registerMessageListener(new MessageListenerOrderly() {
            // 顺序消息监听接口 MessageListenerOrderly
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                // 消费者一个线程负责顺序消费一个队列里的全部消息，一个订单号的所有消息都放在一个队列
                for (MessageExt msg : msgs) {
                    System.out.println("线程名称：【" + Thread.currentThread().getName() + "】，" +"消息："+ new String(msg.getBody()));
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        // 5.启动消费者
        consumer.start();
        System.out.println("消费者启动成功==========");
    }
}

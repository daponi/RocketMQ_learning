package com.itheima.www.base.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 消费者
 * 1.创建消费者Consumer，制定消费者组名
 * 2.指定Nameserver地址
 * 3.订阅主题Topic和Tag
 * 4.设置回调函数，处理消息
 * 5.启动消费者consumer
 */
public class Consumer {
    public static void main(String[] args) throws MQClientException {
        // 定义一个pull消费者,Consumer主动从Broker中拉取消息
        // DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cg");
        // 1.创建消费者Consumer，制定消费者组名.定义一个push消费者,Broker收到数据后会主动推送给Consumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroupDemo02");
        //2.指定Nameserver地址
        consumer.setNamesrvAddr("my01:9876;my02:9876");
        //3.订阅主题Topic和Tag
        consumer.subscribe("TopicDemo02", "TagsDemo-Async");
        //设定消费模式：负载均衡|广播模式 若consumer先开启没有和producer建立连接，要30s心跳后才能建立连接。
        consumer.setMessageModel(MessageModel.BROADCASTING);
        //4.设置回调函数，处理消息，注册回调实现类来处理从broker拉取回来的消息
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                for (MessageExt msg : msgs) {
                    System.out.println("消息:"+msg);
                System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msg);

                }
                // 标记该消息已经被成功消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        // 5.启动消费者consumer
        consumer.start();
    }
}

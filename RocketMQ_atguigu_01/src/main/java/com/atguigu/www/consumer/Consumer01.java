package com.atguigu.www.consumer;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.*;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.consumer.ConsumeFromWhere;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import java.util.List;

/**
 * 消费者Demo
 */
public class Consumer01 {
    public static void main(String[] args) throws MQClientException {

        // 定义一个pull消费者,Consumer主动从Broker中拉取消息
        // DefaultLitePullConsumer consumer = new DefaultLitePullConsumer("cg");

        // 定义一个push消费者,Broker收到数据后会主动推送给Consumer
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumerGroupDemo");
        // 指定nameServer
        consumer.setNamesrvAddr("my01:9876");
        // 指定从第一条消息开始消费
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_FIRST_OFFSET);
        // 指定消费topic与tag
        consumer.subscribe("TopicActionDemo", "*");
        // 指定采用“广播模式”进行消费，广播消费模式下，相同Consumer Group的每个Consumer实例都接收同一个Topic的全量消息.
        // 默认为“集群模式”,群消费模式下，相同Consumer Group的每个Consumer实例平均分摊同一个Topic的消息。即每条消息只会被发送到Consumer Group中的某个Consumer。
        consumer.setMessageModel(MessageModel.BROADCASTING);

        // 注册消息监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            // 一旦broker中有了其订阅的消息就会触发该方法的执行，其返回值为当前consumer消费的状态
             @Override
             public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                 // 逐条消费消息
                 for (MessageExt msg : msgs) {
                     System.out.println("开始消费消息："+msg);
                 }
                 // 返回消费状态：消费成功
                 return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
             }
         });

        // 开启消费者消费
        consumer.start();
        System.out.println("Consumer started...");

        // consumer.shutdown();
    }
}

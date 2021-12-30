package com.atguigu.www.producer;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.util.List;

/**
 * 生产者生产顺序消息
 * RocketMQ可以严格地保证两种消息的有序性：分区有序与全局有序。
 */
public class OrderedProducer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("consumerGroupDemo");
        producer.setNamesrvAddr("my01:9876");

        // 若为全局有序，则需要设置Queue数量为1
        //producer.setDefaultTopicQueueNums(1);
        producer.start();
        for (int i = 0; i < 109; i++) {
            // 为了演示简单，使用整型数作为orderId
            Integer orderId = i;
            byte[] body = ("Hello, OrderedProducer No.," + i + " ....").getBytes();
            Message msg = new Message("Topic-orderProducer", "TagsDemo", body);
            // 将orderId作为消息key
            msg.setKeys(orderId.toString());

            // 该send()为同步发送， send()的第三个参数值会传递给选择器的select()的第三个参数
            SendResult sendResult = producer.send(msg, new MessageQueueSelector() {
                // 具体的选择算法在该方法中定义
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    String keys = msg.getKeys();
                    Integer id = Integer.valueOf(keys);

                    //以下是使用arg作为选择key的选择算法
                    //Integer id =(Integer) arg;

                    //此处选择求模计算下标
                    int index = id % mqs.size();
                    return mqs.get(index);
                }
            }, orderId);
            System.out.println(sendResult);
        }
        producer.shutdown();

    }
}

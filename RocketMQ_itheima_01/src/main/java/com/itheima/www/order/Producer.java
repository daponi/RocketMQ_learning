package com.itheima.www.order;

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
 * 生产者 顺序消息生产
 * 3个订单OrderId，共10条消息，OrderId相同的发送到同一个队列，组成分区有序的消息
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, MQBrokerException, RemotingException, InterruptedException {
        //1.创建消息生产者producer，并制定生产者组名
        DefaultMQProducer producer = new DefaultMQProducer("group1");
        //2.指定Nameserver地址
        producer.setNamesrvAddr("my01:9876;my02:9876");
        //3.启动producer
        producer.start();
        //构建消息集合
        List<OrderStep> orderSteps = OrderStep.buildOrders();
        //发送消息
        for (int i = 0; i < orderSteps.size(); i++) {
            String body = String.valueOf(orderSteps.get(i));
            // 将orderId作为消息key
            long orderId = orderSteps.get(i).getOrderId();

            Message message = new Message("OrderTopicDemo", "OrderTagsDemo", body.getBytes());
            /**
             * 参数一：消息对象
             * 参数二：消息队列的选择器
             * 参数三：选择队列的业务标识（订单ID）
             */
            SendResult sendResult = producer.send(message, new MessageQueueSelector() {
                /**
                 *
                 * @param mqs：队列集合
                 * @param msg：消息对象
                 * @param arg：业务标识的参数
                 */
                @Override
                public MessageQueue select(List<MessageQueue> mqs, Message msg, Object arg) {
                    long orderId = (long) arg;
                    long index = orderId % mqs.size();
                    return mqs.get((int) index);
                }
            }, orderId);
            System.out.println("发送结果:"+sendResult);
        }
        // 关闭实例
        producer.shutdown();
    }
}

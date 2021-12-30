package com.atguigu.www.producer;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.exception.RemotingException;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;


/**
 * 生产者 异步送消息发
 */
public class AsyncProducer {
    public static void main(String[] args) throws MQClientException, RemotingException, InterruptedException {
        DefaultMQProducer producer = new DefaultMQProducer("producerGroupDemo");
        producer.setNamesrvAddr("my01:9876");
        // 指定异步发送失败后不进行重试发送 ,默认发送2次
        producer.setRetryTimesWhenSendAsyncFailed(0);
        // 指定新创建的Topic的Queue数量为2，默认为4
        producer.setDefaultTopicQueueNums(2);
        producer.start();

        for (int i = 0; i < 100; i++) {
            byte[] body = ("Hello,AsyncProducer no." + i + " ...").getBytes(StandardCharsets.UTF_8);
            Message msg = new Message("Topic-AsyncProducer", "TagsDemo", body);
            //为消息指定key
            msg.setKeys("AsyncProducer-keys-"+i);
            // 异步发送。指定回调
            producer.send(msg, new SendCallback() {
                // 当producer接收到MQ发送来的ACK后就会触发该回调方法的执行
                @Override
                public void onSuccess(SendResult sendResult) {
                    System.out.println("成功发送消息，响应："+sendResult);
                }
                // 当发送消息发生异常的回调方法
                @Override
                public void onException(Throwable throwable) {

                }
            });
        }
        // 由于采用的是异步发送，所以若这里不sleep， 则消息还未发送就会将producer给关闭，报错
        TimeUnit.SECONDS.sleep(3);
        producer.shutdown();
    }
}

package com.atguigu.www.producer;

import com.atguigu.www.listener.ICBCTransactionListener;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.client.producer.TransactionSendResult;
import org.apache.rocketmq.common.message.Message;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 生产者 发送事务消息Demo
 */
public class TransactionProducer {
    public static void main(String[] args) throws MQClientException {
        // 创建发送事务消息的生产者producer
        TransactionMQProducer producer = new TransactionMQProducer("producerTransactionDemo");
        producer.setNamesrvAddr("my01:9876");
        /**
         *  定义一个线程池
         * @param corePoolSize 线程池中核心线程数量
         * @param maximumPoolSize 线程池中最多线程数
         * @param keepAliveTime 这是一个时间。当线程池中线程数量大于核心线程数量是，
         *                      多余空闲线程的存活时长
         * @param unit 时间单位
         * @param workQueue 临时存放任务的队列，其参数就是队列的长度
         * @param threadFactory 线程工厂
         */
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "client-transaction-msg-check-thread");
            }
        });
        // 为生产者指定一个线程池
        producer.setExecutorService(poolExecutor);
        // 为生产者添加事务监听器
        producer.setTransactionListener(new ICBCTransactionListener());
        producer.start();

        String[] tags={"TagA","TagB","TagC"};
        for (int i = 0; i < 3; i++) {
            byte[] body = ("Hello,Transaction Message No." + i).getBytes(StandardCharsets.UTF_8);
            Message msg = new Message("TopicActionDemo", tags[i], body);
            // 发送事务消息,第二个参数用于指定在执行本地事务时要使用的业务参数，第二个参数可以传给监听器，args参数与本地事务执行器一起使用。
            TransactionSendResult sendResult = producer.sendMessageInTransaction(msg, "这是第二参数");
            System.out.println("发送结果:"+sendResult.getSendStatus());//发送结果:SEND_OK
        }

    }
}

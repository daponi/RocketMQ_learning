package com.atguigu.www.listener;

import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;

/**
 * 发送事务消息的生产者的 事务监听器
 */
public class ICBCTransactionListener implements TransactionListener {

    // 回调操作方法，消息预提交成功就会触发该方法的执行，用于完成本地事务，第二个参数可以是producer发送消息时锁传送的
    @Override
    public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
        System.out.println("预提交消息成功：" + msg);
        System.out.println("回调方法的参数arg:"+arg);
        // 假设接收到TAGA的消息就表示扣款操作成功，TAGB的消息表示扣款失败，TAGC表示扣款结果不清楚，需要执行消息回查
        if (StringUtils.equals("TagA", msg.getTags())) {
            return LocalTransactionState.COMMIT_MESSAGE;
        } else if (StringUtils.equals("TagB", msg.getTags())) {
            return LocalTransactionState.ROLLBACK_MESSAGE;
        } else if (StringUtils.equals("TagC", msg.getTags())) {
            return LocalTransactionState.UNKNOW;
        }
        return LocalTransactionState.UNKNOW;
    }

    /**
     * 消息回查方法
     * 回查有默认等待时间
     * 引发消息回查的原因最常见的有两个： 1)回调操作返回UNKNWON  2)TC没有接收到TM的最终全局事务确认指令
     */
    @Override
    public LocalTransactionState checkLocalTransaction(MessageExt msg) {
        System.out.println("执行消息回查的Tag：" + msg.getTags());
        return LocalTransactionState.COMMIT_MESSAGE;
    }
}

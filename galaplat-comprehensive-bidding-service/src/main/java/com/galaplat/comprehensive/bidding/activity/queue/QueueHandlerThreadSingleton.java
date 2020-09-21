package com.galaplat.comprehensive.bidding.activity.queue;

import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.enums.HandlerEnum;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/***
 * 队列处理单例线程
 */
public class QueueHandlerThreadSingleton extends Thread {

    private final MessageQueue messageQueue;
    private final HandlerFacade handler;
    private final Logger LOGGER = LoggerFactory.getLogger(QueueHandlerThreadSingleton.class);
    private static volatile QueueHandlerThreadSingleton queueHandler;

    public static QueueHandlerThreadSingleton getInstance() {
        if (queueHandler == null) {
            synchronized (QueueHandlerThreadSingleton.class) {
                if (queueHandler == null) {
                    queueHandler = new QueueHandlerThreadSingleton();
                }
            }
        }
        return queueHandler;
    }

    private QueueHandlerThreadSingleton() {
        this.messageQueue = SpringUtil.getBean(MessageQueue.class);
        this.handler = new HandlerFacade();
    }

    private void handler(QueueMessage queueMessage) {
        this.handler.problem(queueMessage.getType(), queueMessage);
    }

    public void run() {
        LOGGER.info("消息队列线程 QueueHandlerThreadSingleton 启动成功");
        while (true) {
            //下面是所有的数据内容大致格式
            //{type: 111, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 200, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 211, data: {userCode: "235235345", activityCode: "s34534534"}}
            //{type: 212, data: {activityCode: "s34534534", goodsId: 234235}}
            //{type: 213, data: {bidPrice: 32.345, goodsId: 234, userCode: "235235345", activityCode: "s34534534"}}
            //{type: 214, data: {activityCode: "s34534534", goodsId: 234235}}
            //{type: 215, data: {activityCode: "s34534534", goodsId: 234235, status: 3 or 1 }}
            //{type: 216, data: {activityCode: "s34534534"}} or {type: 216, data: {activityCode: "s34534534", userCode: 234235}}
            //{type: 300, data: {adminCode: "235235345", activityCode: "s34534534"}}
            //{type: 301, data: { activityCode: 23423345, goodsId: 234235, userCode: 234325 }}
            //{type: 302, data: {activityCode: "s34534534", goodsId: 234235, adminCode: 345dfg}}
            QueueMessage queueMessage = messageQueue.take();
            try {
                LOGGER.info("队列线程收到消息：" + queueMessage.toString());
                handler(queueMessage);
            } catch (Exception e) {
                LOGGER.info("run(ERROR): 队列处理器发生异常。 " + e.getMessage());
            }
        }
    }

}

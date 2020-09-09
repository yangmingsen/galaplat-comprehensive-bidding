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
    private final Map<Integer, HandlerEnum> handlerRecords;
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
        this.handlerRecords = new HandlerRecordsMap<>();
        this.initHandlerRecords();
    }

    private void handler(QueueMessage queueMessage) {
        final Integer type = queueMessage.getType();
        switch (handlerRecords.get(type)) { //匹配相应的策略算法
            case SUPPLIER_IN: {
                this.handler.supplierInHandler(queueMessage);
            }
            break;

            case SUPPLER_OUT: {
                this.handler.supplierOutHandler(queueMessage);
            }
            break;

            case ADMIN_IN: {
                this.handler.adminInHandler(queueMessage);
            }
            break;

            case ADMIN_OUT: {
                this.handler.adminOutHandler(queueMessage);
            }
            break;

            case OTHER: {
                LOGGER.info("handler(msg): 错误消息" + queueMessage.toString());
            }
        }

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
            QueueMessage takeQueuemsg = messageQueue.take();
            try {
                LOGGER.info("队列线程收到消息：" + takeQueuemsg.toString());
                handler(takeQueuemsg);
            } catch (Exception e) {
                LOGGER.info("run(ERROR): 队列处理器发生异常。 " + e.getMessage());
            }
        }
    }

    /**
     * 配置映射关系
     */
    private void initHandlerRecords() {
        //处理第一名发生变化时 同步数据给供应商
        handlerRecords.put(111, HandlerEnum.SUPPLER_OUT);
        //当某个供应商端中途加入（或掉线从新加入） 同步供应商数据
        handlerRecords.put(200, HandlerEnum.SUPPLER_OUT);
        //当某个供应商端中途加入（或掉线从新加入） 同步供应商数据
        handlerRecords.put(211, HandlerEnum.SUPPLER_OUT);
        //当管理端数据重置时，通知供应商端清理排名数据
        handlerRecords.put(212, HandlerEnum.SUPPLER_OUT);
        //处理供应商端提交竞价
        handlerRecords.put(213, HandlerEnum.SUPPLIER_IN);
        //处理当管理端切换下一个竞品时，提示所有供应商端更新
        handlerRecords.put(214, HandlerEnum.SUPPLER_OUT);
        //处理当管理端点击暂停或者继续后，通知供应商端暂停某个正在进行的竞品
        handlerRecords.put(215, HandlerEnum.SUPPLER_OUT);
        //处理当本场活动结束，通知供应商端退出登录
        handlerRecords.put(216, HandlerEnum.SUPPLER_OUT);
        //当某个管理端中途加入（或掉线从新加入） 同步数据给管理端
        handlerRecords.put(300, HandlerEnum.ADMIN_OUT);
        //当供应商端提交最新竞价信息时，推数据给管理端（同步一些数据给管理端）
        handlerRecords.put(301, HandlerEnum.ADMIN_OUT);
        //处理管理端主动请求获取某个竞品数据时
        handlerRecords.put(302, HandlerEnum.ADMIN_OUT);
    }

    private static class HandlerRecordsMap<K, V> extends HashMap<K, V> {
        public HandlerRecordsMap() {
            super();
        }

        public V put(K key, V value) {
            return super.put(key, value);
        }

        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                return (V) HandlerEnum.OTHER;
            }
            return value;
        }
    }
}

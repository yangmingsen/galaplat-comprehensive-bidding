package com.galaplat.comprehensive.bidding.activity.queue;

import com.galaplat.comprehensive.bidding.activity.ActivityThread;
import com.galaplat.comprehensive.bidding.activity.queue.handler.*;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueueHandlerThread extends Thread {

    private final MessageQueue messageQueue;
    private final ProblemHandler supplierOutHandler;
    private final ProblemHandler supplierInHandler;
    private final ProblemHandler adminOutHandler;
    private final ProblemHandler adminInHandler;
    private final Logger LOGGER = LoggerFactory.getLogger(QueueHandlerThread.class);


    private static QueueHandlerThread queueHandler;

    public static QueueHandlerThread getInstance() {
        if (queueHandler == null) {
            synchronized (QueueHandlerThread.class) {
                if (queueHandler == null) {
                    queueHandler = new QueueHandlerThread();
                    return queueHandler;
                }
            }
        }
        return queueHandler;
    }

    private QueueHandlerThread() {
        this.messageQueue = SpringUtil.getBean(MessageQueue.class);
        this.supplierOutHandler = new SupplierOutProblemHandler();
        this.supplierInHandler = new SupplierInProblemHandler();
        this.adminOutHandler = new AdminOutProblemHandler();
        this.adminInHandler = new AdminInProblemHandler();
    }


    public void run() {
        LOGGER.info("消息队列线程 QueueHandlerThread 启动成功");
        while (true) {
            QueueMessage takeQueuemsg = messageQueue.take();
            LOGGER.info("队列线程收到msg: takeQueuemsg="+takeQueuemsg.toString());

            //System.out.println("QueueMessage: "+JSON.toJSONString(takeQueuemsg));

            //{type: 111, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 200, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 211, data: {userCode: "235235345", activityCode: "s34534534"}}
            //{type: 212, data: {activityCode: "s34534534", goodsId: 234235}}
            //{type: 213, data: {bidPrice: 32.345, goodsId: 234, userCode: "235235345", activityCode: "s34534534"}}
            //{type: 214, data: {activityCode: "s34534534", goodsId: 234235}}
            //{type: 215, data: {activityCode: "s34534534", goodsId: 234235, status: 3 or 1 }}
            //{type: 216, data: {activityCode: "s34534534"}} or {type: 216, data: {activityCode: "s34534534", userCode: 234235}}
            //{type: 300, data: {adminCode: "235235345", activityCode: "s34534534"}}
            //{type: 301, data: { activityCode: 23423345, bidTime: 15:32, bid: 2.345, supplierCode: 234903945834, CodeName: '小红', supplierName: '小米科技电子有限公司'}}
            //{type: 302, data: {activityCode: "s34534534", goodsId: 234235, adminCode: 345dfg}}
            switch (takeQueuemsg.getType()) {
                case 111: //处理第一名发生变化时 同步数据给供应商
                    supplierOutHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 200: //处理供应商端提交数据问题（同步数据给所有供应商端）
                    supplierOutHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 211:  //当某个供应商端中途加入（或掉线从新加入） 同步供应商数据
                    supplierOutHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 212: //当管理端数据重置时，通知供应商端清理排名数据
                    supplierOutHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 213: //处理供应商端提交竞价
                    supplierInHandler.problem(takeQueuemsg.getType(), takeQueuemsg);
                    break;

                case 214: //处理当管理端切换下一个竞品时，提示所有供应商端更新
                    supplierOutHandler.problem(takeQueuemsg.getType(), takeQueuemsg);
                    break;

                case 215: //处理当管理端点击暂停或者继续后，通知供应商端暂停某个正在进行的竞品
                    supplierOutHandler.problem(takeQueuemsg.getType(), takeQueuemsg);
                    break;

                case 216: //处理当本场活动结束，通知供应商端退出登录
                    supplierOutHandler.problem(takeQueuemsg.getType(), takeQueuemsg);
                    break;

                case 300: //当某个管理端中途加入（或掉线从新加入） 同步数据给管理端
                    adminOutHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 301: //当供应商端提交最新竞价信息时，推数据给管理端（同步一些数据给管理端）
                    adminOutHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 302: //处理管理端主动请求获取某个竞品数据时
                    adminOutHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;
            }
        }
    }


}

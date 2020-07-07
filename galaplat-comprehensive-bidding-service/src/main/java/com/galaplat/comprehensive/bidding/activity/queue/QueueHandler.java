package com.galaplat.comprehensive.bidding.activity.queue;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.queue.handler.AdminProblemHandler;
import com.galaplat.comprehensive.bidding.activity.queue.handler.ProblemHandler;
import com.galaplat.comprehensive.bidding.activity.queue.handler.SupplierProblemHandler;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;

public class QueueHandler extends Thread {

    private PushQueue pushQueue;
    private ProblemHandler supplierHandler;
    private ProblemHandler adminHandler;


    public QueueHandler() {
        this.pushQueue = SpringUtil.getBean(PushQueue.class);
        this.supplierHandler = new SupplierProblemHandler();
        this.adminHandler = new AdminProblemHandler();
    }


    public void run() {
        while (true) {
            QueueMessage takeQueuemsg = pushQueue.take();

            System.out.println("ServerToClient: "+JSON.toJSONString(takeQueuemsg));

            //{type: 111, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 200, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 211, data: {userCode: "235235345", activityCode: "s34534534"}}
            //{type: 212, data: {activityCode: "s34534534", goodsId: 234235}}
            //{type: 300, data: {adminCode: "235235345", activityCode: "s34534534"}}
            //{type: 301, data: { activityCode: 23423345, bidTime: 15:32, bid: 2.345, supplierCode: 234903945834, CodeName: '小红', supplierName: '小米科技电子有限公司'}}
            switch (takeQueuemsg.getType()) {
                case 111: //处理第一名发生变化时 同步数据给供应商
                    supplierHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 200: //处理供应商端提交数据问题（同步数据给所有供应商端）
                    supplierHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 211:  //当某个供应商端中途加入（或掉线从新加入） 同步供应商数据
                    supplierHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 212: //当管理端数据重置时，通知供应商端清理排名数据
                    supplierHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 300: //当某个管理端中途加入（或掉线从新加入） 同步数据给管理端
                    adminHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;

                case 301: //推数据给管理端（同步一些数据给管理端）
                    adminHandler.problem(takeQueuemsg.getType(),takeQueuemsg);
                    break;
            }
        }
    }


}

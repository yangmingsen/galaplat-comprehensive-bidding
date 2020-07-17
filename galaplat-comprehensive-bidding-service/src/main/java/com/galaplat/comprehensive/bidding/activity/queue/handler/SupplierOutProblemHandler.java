package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.Map;


public class SupplierOutProblemHandler extends BaseProblemHandler {
    @Override
    public void handlerProblem(int type, QueueMessage queuemsg) {
        switch (type) {
            case 111: //处理第一名发生变化时
                handler111Problem(queuemsg);
                break;
            case 200: //处理供应商端提交数据问题（同步数据给所有供应商端）
                handler200Problem(queuemsg);
                break;
            case 211: //当某个供应商端中途加入（或掉线从新加入） 同步数据
                handler211Problem(queuemsg);
                break;
            case 212: //当管理端数据重置时，通知供应商端清理排名数据
                handler212Problem(queuemsg);
                break;
            case 214:  //处理当管理端切换下一个竞品时，提示所有供应商端更新
                handler214Problem(queuemsg);
                break;
            case 215: ////处理当管理端点击暂停或者继续后，通知供应商端暂停某个正在进行的竞品
                handler215Problem(queuemsg);
                break;
        }
    }

    private void handler215Problem(QueueMessage queuemsg) {
        Message message = new Message(215, queuemsg.getData());
        String activityCode = queuemsg.getData().get("activityCode");
        notifyAllSupplier(message, activityCode);
    }

    private void handler214Problem(QueueMessage queuemsg) {
        Message message = new Message(214, queuemsg.getData());
        String activityCode = queuemsg.getData().get("activityCode");
        notifyAllSupplier(message, activityCode);
    }



    private void handlerSendOneSupplier(String activityCode, Integer goodsId, String userCode) {
        if (userChannelMap.getUserFocusActivity(userCode).equals(activityCode)) {
            CustomBidVO info = iJbxtGoodsService.getUserBidRankInfoByUserCodeAndActivity(goodsId, userCode, activityCode);
            Map<String, String> map = new HashMap<>();
            map.put("userRank", info.getUserRank().toString());
            map.put("goodsPrice", info.getGoodsPrice().toString());
            map.put("goodsId", info.getGoodsId().toString());

            //推流到供应商客户端
            Message message = new Message(200, map);
            userChannelMap.get(userCode).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }


    private void handler111Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");

        Message message = new Message(111, takeQueuemsg.getData());
        notifyAllSupplier(message, activityCode);
    }


    private void handler200Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");
        Integer goodsId = Integer.parseInt(takeQueuemsg.getData().get("goodsId"));
        userChannelMap.getAllUser().forEach(supplier -> handlerSendOneSupplier(activityCode, goodsId, supplier));
    }


    /***
     * 同步当前竞品状态() 给 指定供应商端
     * @param takeQueuemsg
     */
    private void sendStatusToSomeOne(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");
        String userCode = takeQueuemsg.getData().get("userCode");
        takeQueuemsg.getData().put("userCode", null);

        Message message = new Message(215, takeQueuemsg.getData());
        notifyOptionSupplier(message ,activityCode, userCode);

        //传递当前活动剩余时长
        CurrentActivity currentActivity = activityMap.get(activityCode);
        if (currentActivity != null) {
            if (currentActivity.getStatus() == 2) { //如果暂停为暂停状态
                Map<String, String> t_map = new HashMap<>();
                t_map.put("remainingTime",currentActivity.getRemainingTimeString());
                Message remainingTimeMessage = new Message(100, t_map);
                notifyOptionSupplier(remainingTimeMessage ,activityCode, userCode);
            }
        }
    }

    private void handler211Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");
        String userCode = takeQueuemsg.getData().get("userCode");
        String goodsId = activityMap.get(activityCode).getCurrentGoodsId();
        takeQueuemsg.getData().put("goodsId", goodsId);
        int status = activityMap.get(activityCode).getStatus();
        if (status == 2) {
            takeQueuemsg.getData().put("status","3");
        } else {
            takeQueuemsg.getData().put("status","1");
        }

        handlerSendOneSupplier(activityCode, new Integer(goodsId), userCode);
        sendStatusToSomeOne(takeQueuemsg);
    }



    private void handler212Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");

        Message message = new Message(212, takeQueuemsg.getData());
        notifyAllSupplier(message,activityCode);
    }

}

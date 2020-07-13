package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
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
            case 214: //当管理端数据重置时，通知供应商端清理排名数据
                handler214Problem(queuemsg);
                break;
        }
    }

    private void handler214Problem(QueueMessage queuemsg) {
        Message message = new Message(214, queuemsg.getData());
        String activityCode = queuemsg.getData().get("activityCode");
        notifyAllSupplier(message, activityCode);
    }


//    /***
//     *  推数据流到所有供应商端（适用于所有供应商发同一个数据）
//     * @param message
//     * @param activityCode
//     */
//    private void notifyAllSupplier(Message message, String activityCode) {
//        userChannelMap.getAllUser().forEach(supplier -> notifyOptionSupplier(message, activityCode, supplier));
//    }
//
//    /***
//     * 推送数据到指定供应商端
//     * @param message
//     * @param activityCode
//     * @param userCode
//     */
//    private void notifyOptionSupplier(Message message, String activityCode, String userCode) {
//        if (userChannelMap.getUserFocusActivity(userCode).equals(activityCode)) {
//            userChannelMap.get(userCode).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
//        }
//    }


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


    private void handler211Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");
        String userCode = takeQueuemsg.getData().get("userCode");
        String goodsId = activityMap.get(activityCode).getCurrentGoodsId();
        handlerSendOneSupplier(activityCode, new Integer(goodsId), userCode);
    }



    private void handler212Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");

        Message message = new Message(212, takeQueuemsg.getData());
        notifyAllSupplier(message,activityCode);
    }

}

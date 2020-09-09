package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityTask;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.netty.pojo.ResponseMessage;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.HashMap;
import java.util.Map;


public class SupplierOutProblemHandler extends BaseProblemHandler {
    @Override
    public void handlerProblem(final int type, final QueueMessage queuemsg) {
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
            case 216: //处理当本场活动结束，通知供应商端退出登录
                handler216Problem(queuemsg);
                break;
        }
    }

    private void handler216Problem(final QueueMessage queuemsg) {
        final ResponseMessage message = new ResponseMessage(216, queuemsg.getData());
        final String activityCode = queuemsg.getData().get("activityCode");
        final String userCode = queuemsg.getData().get("userCode");

        if (userCode != null) { //发给指定的供应商
            notifyOptionSupplier(message,activityCode,userCode);
        } else {
            notifyAllSupplier(message, activityCode);
        }
    }


    private void handler215Problem(final QueueMessage queuemsg) {
        final ResponseMessage message = new ResponseMessage(215, queuemsg.getData());
        final String activityCode = queuemsg.getData().get("activityCode");
        notifyAllSupplier(message, activityCode);
    }

    private void handler214Problem(final QueueMessage queuemsg) {
        final ResponseMessage message = new ResponseMessage(214, queuemsg.getData());
        final String activityCode = queuemsg.getData().get("activityCode");
        notifyAllSupplier(message, activityCode);
    }


    /***
     * 推送排名信息到指定供应商
     * @param activityCode
     * @param goodsId
     * @param userCode
     */
    private void handlerSendOneSupplier(final String activityCode, final Integer goodsId, final String userCode) {
        if (userChannelMap.getUserFocusActivity(userCode).equals(activityCode)) {
            final CustomBidVO info = iJbxtGoodsService.getUserBidRankInfoByUserCodeAndActivity(goodsId, userCode, activityCode); //issue sort
            final Boolean parataxis = activityManager.get(activityCode).isParataxis(userCode) ? Boolean.TRUE : Boolean.FALSE;
            final Map<String, String> map = new HashMap<>();
            map.put("userRank", info.getUserRank().toString());
            map.put("goodsPrice", info.getGoodsPrice().toString());
            map.put("goodsId", info.getGoodsId().toString());
            map.put("parataxis", parataxis.toString());


            //推流到供应商客户端
            final ResponseMessage message = new ResponseMessage(200, map);
            userChannelMap.get(userCode).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }


    private void handler111Problem(final QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");

        final ResponseMessage message = new ResponseMessage(111, takeQueuemsg.getData());
        notifyAllSupplier(message, activityCode);
    }


    private void handler200Problem(final QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");
        final Integer goodsId = Integer.parseInt(takeQueuemsg.getData().get("goodsId"));
        userChannelMap.getAllUser().forEach(supplier -> handlerSendOneSupplier(activityCode, goodsId, supplier));
    }


    /***
     * 同步当前竞品状态() 给 指定供应商端
     * @param takeQueuemsg
     */
    private void sendStatusToSomeOne(final QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");
        final String userCode = takeQueuemsg.getData().get("userCode");
        takeQueuemsg.getData().put("userCode", null);

        final ResponseMessage message = new ResponseMessage(215, takeQueuemsg.getData());
        notifyOptionSupplier(message ,activityCode, userCode);

        //传递当前活动剩余时长
        final ActivityTask currentActivity = activityManager.get(activityCode);
        if (currentActivity != null) {
            if (currentActivity.getStatus() == 2) { //如果暂停为暂停状态
                Map<String, String> t_map = new HashMap<>();
                t_map.put("remainingTime",currentActivity.getRemainingTimeString());
                ResponseMessage remainingTimeMessage = new ResponseMessage(100, t_map);
                notifyOptionSupplier(remainingTimeMessage ,activityCode, userCode);
            }
        }
    }

    private void handler211Problem(final QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");
        final String userCode = takeQueuemsg.getData().get("userCode");
        final String goodsId = activityManager.get(activityCode).getCurrentGoodsId().toString();
        takeQueuemsg.getData().put("goodsId", goodsId);
        final int status = activityManager.get(activityCode).getStatus();
        if (status == 2) {
            takeQueuemsg.getData().put("status","3");
        } else {
            takeQueuemsg.getData().put("status","1");
        }

        handlerSendOneSupplier(activityCode, new Integer(goodsId), userCode);
        sendStatusToSomeOne(takeQueuemsg);
    }



    private void handler212Problem(final QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");
        final ResponseMessage message = new ResponseMessage(212, takeQueuemsg.getData());
        notifyAllSupplier(message,activityCode);
    }

}

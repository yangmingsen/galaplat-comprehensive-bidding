package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t1;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t2;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



public class AdminOutProblemHandler extends BaseProblemHandler {
    @Override
    public void handlerProblem(int type, QueueMessage queuemsg) {
        switch (type) {
            case 300: //当某个管理端中途加入（或掉线从新加入） 同步数据
                handler300Problem(queuemsg);
                break;
            case 301: //推数据给管理端（同步一些数据给管理端）
                handler301Problem(queuemsg);
                break;
        }
    }


//    /***
//     *  推数据流到所有管理端
//     * @param message
//     * @param activityCode
//     */
//    private void notifyAllAdmin(Message message, String activityCode) {
//        adminChannel.getAllAdmin().forEach(adminCode -> notifyOptionAdmin(message, activityCode, adminCode));
//    }
//
//    /***
//     * 推送数据到指定管理端
//     * @param message
//     * @param activityCode
//     * @param adminCode
//     */
//    private void notifyOptionAdmin(Message message, String activityCode, String adminCode) {
//        AdminInfo adminInfo = adminChannel.get(adminCode);
//        if (adminInfo.getFocusActivity().equals(activityCode)) {
//            //推数据到管理端
//            adminInfo.getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
//        }
//    }

    private void handler300Problem(QueueMessage takeQueuemsg) {

        String activityCode = takeQueuemsg.getData().get("activityCode");
        String adminCode = takeQueuemsg.getData().get("adminCode");
        Channel caChannel = adminChannel.get(adminCode).getChannel();


        CurrentActivity currentActivity = activityMap.get(activityCode);
        Integer goodsId = Integer.parseInt(currentActivity.getCurrentGoodsId());
        List<JbxtUserDVO> userLists = iJbxtUserService.findAllByActivityCode(activityCode);


        List<Res300t1> t1s = new ArrayList<>();
        for (int i = 0; i < userLists.size(); i++) {
            JbxtUserDVO user1 = userLists.get(i);

            Res300t1 res300t1 = new Res300t1();
            res300t1.setSupplierName(user1.getSupplierName());
            res300t1.setCodeName(user1.getCodeName());
            res300t1.setSupplierCode(user1.getCode());

            JbxtBiddingDO cUserMinBid = iJbxtBiddingService.selectMinBidTableBy(user1.getCode(), goodsId, activityCode);
            if (cUserMinBid != null) {
                res300t1.setMinBid(cUserMinBid.getBid());
            } else {
                res300t1.setMinBid(new BigDecimal("0.000"));
            }

            List<JbxtBiddingDVO> cUserBidHistory = iJbxtBiddingService.findAllByUserCodeAndActivityCode(user1.getCode(), activityCode);
            List<Res300t2> t2s = new ArrayList<>();
            if (cUserBidHistory.size() > 0) {

                for (int j = 0; j <cUserBidHistory.size(); j++) {
                    JbxtBiddingDVO ubid1 = cUserBidHistory.get(j);

                    Res300t2 res300t2 = new Res300t2();
                    res300t2.setBid(ubid1.getBid());
                    res300t2.setBidTime(ubid1.getBidTime());

                    t2s.add(res300t2);
                }
            }
            res300t1.setBids(t2s);

            t1s.add(res300t1);
        }

        List<Res300t1> t1sCollect = t1s.stream().sorted(Comparator.comparing(Res300t1::getMinBid)).collect(Collectors.toList());

        Res300 res300 = new Res300();
        res300.setGoodsId(goodsId);
        res300.setMinPrice(t1sCollect.get(0).getMinBid());
        res300.setList(t1sCollect);


        //处理返回数据
        Message tmsg = new Message(300, res300);
        caChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(tmsg)));
    }


    private void handler301Problem( QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");

        //推数据给管理端
        Message message = new Message(301, takeQueuemsg.getData());
        notifyAllAdmin(message,activityCode);
    }


}

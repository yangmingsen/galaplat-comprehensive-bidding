package com.galaplat.comprehensive.bidding.activity.queue;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.AdminChannelMap;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t1;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t2;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class QueueHandler extends Thread {

    private UserChannelMap userChannelMap;
    private AdminChannelMap adminChannel;
    private PushQueue pushQueue;
    private IJbxtGoodsService iJbxtGoodsService; //竞品服务
    private ActivityMap activityMap;
    private IJbxtUserService iJbxtUserService;
    private IJbxtBiddingService iJbxtBiddingService;

    public QueueHandler() {
        this.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
        this.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
        this.pushQueue = SpringUtil.getBean(PushQueue.class);
        this.iJbxtGoodsService = SpringUtil.getBean(IJbxtGoodsService.class);
        this.activityMap = SpringUtil.getBean(ActivityMap.class);
        this.iJbxtUserService = SpringUtil.getBean(IJbxtUserService.class);
        this.iJbxtBiddingService = SpringUtil.getBean(IJbxtBiddingService.class);
    }


    private void handlerSendOneSupplier(String activityCode, Integer goodsId, String userCode) {
        if (userChannelMap.getUserFocusActivity(userCode).equals(activityCode)) {
            CustomBidVO info = iJbxtGoodsService.getUserBidRankInfoByUserCodeAndActivity(goodsId, userCode, activityCode);
            Map<String, String> map = new HashMap<>();
            map.put("userRank",info.getUserRank().toString());
            map.put("goodsPrice",info.getGoodsPrice().toString());
            map.put("goodsId",info.getGoodsId().toString());

            //推流到供应商客户端
            Message message = new Message(200, map);
            userChannelMap.get(userCode).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));;
        }
    }

    private void handler111Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");
        userChannelMap.getAllUser().forEach(supplier -> {
            if (userChannelMap.getUserFocusActivity(supplier).equals(activityCode)) {
                //推流到供应商客户端
                Message message = new Message(200, takeQueuemsg.getData());
                userChannelMap.get(supplier).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            }
        });

    }

    private void handler211Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");
        String userCode = takeQueuemsg.getData().get("userCode");
        String goodsId = activityMap.get(activityCode).getCurrentGoodsId();
        handlerSendOneSupplier(activityCode,new Integer(goodsId),userCode);
    }

    private void handler200Problem(QueueMessage takeQueuemsg) {
        String activityCode = takeQueuemsg.getData().get("activityCode");
        Integer goodsId = Integer.parseInt(takeQueuemsg.getData().get("goodsId"));
        userChannelMap.getAllUser().forEach(supplier -> handlerSendOneSupplier(activityCode,goodsId,supplier));
    }

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
        adminChannel.getAllAdmin().forEach(admin -> {
            AdminInfo adminInfo = adminChannel.get(admin);
            if (adminInfo.getFocusActivity().equals(activityCode)) {
                //推数据到管理端
                Message message = new Message(301, takeQueuemsg.getData());
                adminInfo.getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            }
        });
    }


    public void run() {
        while (true) {
            QueueMessage takeQueuemsg = pushQueue.take();

            System.out.println("ServerToClient: "+JSON.toJSONString(takeQueuemsg));

            //{type: 111, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 200, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 211, data: {userCode: "235235345", activityCode: "s34534534"}}
            //{type: 300, data: {adminCode: "235235345", activityCode: "s34534534"}}
            //{type: 301, data: { activityCode: 23423345, bidTime: 15:32, bid: 2.345, supplierCode: 234903945834, CodeName: '小红', supplierName: '小米科技电子有限公司'}}
            switch (takeQueuemsg.getType()) {
                case 111: { //处理第一名发生变化时
                    handler111Problem(takeQueuemsg);
                }
                break;

                case 200: {//处理供应商端提交数据问题
                    handler200Problem(takeQueuemsg);
                }
                break;

                case 211: { //当某个客户端中途加入（或掉线从新加入） 同步数据
                    handler211Problem(takeQueuemsg);
                }
                break;

                case 300: {//当某个管理端中途加入（或掉线从新加入） 同步数据
                    handler300Problem(takeQueuemsg);
                }
                break;

                case 301: {//推数据给管理端
                    handler301Problem(takeQueuemsg);
                }
                break;

            }
        }
    }


}

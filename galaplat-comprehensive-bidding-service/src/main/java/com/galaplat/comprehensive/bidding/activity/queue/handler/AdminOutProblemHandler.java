package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.netty.BidHandler;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t1;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t2;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;



public class AdminOutProblemHandler extends BaseProblemHandler {

    private Logger LOGGER = LoggerFactory.getLogger(AdminOutProblemHandler.class);

    @Override
    public void handlerProblem(int type, QueueMessage queuemsg) {
        switch (type) {
            case 300: //当某个管理端中途加入（或掉线从新加入） 同步数据
                handler300Problem(queuemsg);
                break;
            case 301: //推数据给管理端（同步一些数据给管理端）
                handler301Problem(queuemsg);
                break;

            case 302: //处理管理端主动请求获取某个竞品数据时
                handler302Problem(queuemsg);
                break;
        }
    }

    private void handler302Problem(QueueMessage queuemsg) {
        this.handler300Problem(queuemsg);
    }


    private void handler300Problem(QueueMessage takeQueuemsg) {

        String activityCode = takeQueuemsg.getData().get("activityCode");
        String adminCode = takeQueuemsg.getData().get("adminCode");
        Channel caChannel = adminChannel.get(adminCode).getChannel();

        String goodsIdStr = takeQueuemsg.getData().get("goodsId");
        Integer goodsId = null;
        if (goodsIdStr == null) { //如果传入的goodsId为 null 意味着是300问题
            CurrentActivity currentActivity = activityMap.get(activityCode);
            goodsId = Integer.parseInt(currentActivity.getCurrentGoodsId());
        } else { //不为null 意味着302问题
            try {
                goodsId = Integer.parseInt(goodsIdStr);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                LOGGER.info("handler300Problem(ERROR): "+e.getMessage());
                return;
            }

        }

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

            List<JbxtBiddingDVO> cUserBidHistory = iJbxtBiddingService.findAllByUserCodeAndGooodsIdAndActivityCode(user1.getCode(), goodsId, activityCode);
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

        BigDecimal minPrice = new BigDecimal("0.000");
        for (int i = 0; i < t1sCollect.size(); i++) {
            Res300t1 tres = t1sCollect.get(i);
            if (tres.getMinBid().compareTo(minPrice) == 1) { //如果当前 > minPrice
                minPrice = tres.getMinBid();
                break;
            }
        }
        res300.setMinPrice(minPrice);
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

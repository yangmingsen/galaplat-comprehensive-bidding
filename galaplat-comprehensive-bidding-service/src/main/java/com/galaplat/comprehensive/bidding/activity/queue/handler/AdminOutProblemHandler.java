package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityThread;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;



public class AdminOutProblemHandler extends BaseProblemHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminOutProblemHandler.class);

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

        final String activityCode = takeQueuemsg.getData().get("activityCode");
        final String adminCode = takeQueuemsg.getData().get("adminCode");
        final Channel caChannel = adminChannel.get(adminCode).getChannel();

        final String goodsIdStr = takeQueuemsg.getData().get("goodsId");
        final Integer goodsId ;
        if (goodsIdStr == null) { //如果传入的goodsId为 null 意味着是300问题
            ActivityThread currentActivity = activityManager.get(activityCode);
            goodsId = Integer.parseInt(currentActivity.getCurrentGoodsId());
        } else { //不为null 意味着302问题
            try {
                goodsId = Integer.parseInt(goodsIdStr);
            } catch (NumberFormatException e) {
                LOGGER.info("handler300Problem(ERROR): "+e.getMessage());
                return;
            }

        }
        final List<JbxtUserDVO> userLists = iJbxtUserService.findAllByActivityCode(activityCode);
        final List<Res300t1> t1s = new ArrayList<>();
        for (final JbxtUserDVO user1 : userLists) {
            final Res300t1 res300t1 = new Res300t1();
            res300t1.setSupplierName(user1.getSupplierName());
            res300t1.setCodeName(user1.getCodeName());
            res300t1.setSupplierCode(user1.getCode());

            final JbxtBiddingDO cUserMinBid = iJbxtBiddingService.selectMinBidTableBy(user1.getCode(), goodsId, activityCode);
            if (cUserMinBid != null) {
                res300t1.setMinBid(cUserMinBid.getBid());
            } else {
                res300t1.setMinBid(new BigDecimal("0.000"));
            }

            final List<JbxtBiddingDVO> cUserBidHistory = iJbxtBiddingService.findAllByUserCodeAndGooodsIdAndActivityCode(user1.getCode(), goodsId, activityCode);
            final List<Res300t2> t2s = new ArrayList<>();
            if (cUserBidHistory.size() > 0) {

                for (final JbxtBiddingDVO ubid1 : cUserBidHistory) {
                    final Res300t2 res300t2 = new Res300t2();
                    res300t2.setBid(ubid1.getBid());
                    res300t2.setBidTime(ubid1.getBidTime());

                    t2s.add(res300t2);
                }
            }
            res300t1.setBids(t2s);

            t1s.add(res300t1);
        }
        final List<Res300t1> t1sCollect = t1s.stream().sorted(Comparator.comparing(Res300t1::getMinBid)).collect(Collectors.toList());
        final Res300 res300 = new Res300();
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
        final Message tmsg = new Message(300, res300);
        caChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(tmsg)));

        //处理当管理端点击暂停后无故刷新控制台界面 时间同步问题
        //传递当前活动剩余时长
        final ActivityThread currentActivity = activityManager.get(activityCode);
        if (currentActivity != null) {
            if (currentActivity.getStatus() == 2) { //如果暂停为暂停状态
                final Map<String, String> t_map = new HashMap<>();
                t_map.put("remainingTime",currentActivity.getRemainingTimeString());
                final Message remainingTimeMessage = new Message(100, t_map);
                notifyOptionAdmin(remainingTimeMessage ,activityCode, adminCode);
            }
        }

    }


    private void handler301Problem( QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");

        //推数据给管理端
        final Message message = new Message(301, takeQueuemsg.getData());
        notifyAllAdmin(message,activityCode);
    }


}

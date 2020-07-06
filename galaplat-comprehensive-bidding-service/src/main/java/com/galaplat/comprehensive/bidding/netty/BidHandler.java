package com.galaplat.comprehensive.bidding.netty;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.AdminChannelMap;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.pojo.RequestMessage;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t1;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t2;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BidHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    // 用来保存所有的客户端连接
    private static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:MM");

    AdminChannelMap adminChannelMap = SpringUtil.getBean(AdminChannelMap.class);
    UserChannelMap userChannelMapBean = SpringUtil.getBean(UserChannelMap.class);
    PushQueue pushQueue = SpringUtil.getBean(PushQueue.class);
//    IJbxtUserService iJbxtUserService = SpringUtil.getBean(IJbxtUserService.class);
//    IJbxtBiddingService iJbxtBiddingService = SpringUtil.getBean(IJbxtBiddingService.class);

//    ActivityMap activityMap = SpringUtil.getBean(ActivityMap.class);

    // 当Channel中有新的事件消息会自动调用
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        // 当接收到数据后会自动调用

        // 获取客户端发送过来的文本消息
        String text = msg.text();
        System.out.println("接收到消息数据为：" + text);
        RequestMessage message = JSON.parseObject(text, RequestMessage.class);

        // {type: 101, data: {userCode: 22343423, activityCode: 23426783345}}
        // {type: 102, data: {adminCode: 22343423}}

        switch (message.getType()) {
            // 建立供应商客户端连接的消息
            case 101: {
                String userCode = message.getData().get("userCode");
                String focusActivity = message.getData().get("activityCode");
                userChannelMapBean.put(userCode, ctx.channel());
                userChannelMapBean.put(userCode,focusActivity);
                System.out.println("建立用户:" + userCode + "与通道" + ctx.channel().id() + "的关联");
                userChannelMapBean.print();

                //同步数据
                QueueMessage queueMessage = new QueueMessage(211,message.getData());
                pushQueue.offer(queueMessage);

            }
            break;

            // 建立管理员客户端连接的消息
            case 102: {
                String adminCode = message.getData().get("adminCode");
                String focusActivity = "";
                AdminInfo adminInfo = new AdminInfo(focusActivity,ctx.channel());
                adminChannelMap.put(adminCode, adminInfo);
                System.out.println("建立（admin）用户:" + adminCode + "与通道" + ctx.channel().id() + "的关联");
                adminChannelMap.print();
            }
             break;

                //处理管理端主动请求
            case 300: {
              handler300Problem(message,ctx);
            }
            break;
        }

    }

    private void handler300Problem(RequestMessage message, ChannelHandlerContext ctx) {
        String activityCode = message.getData().get("activityCode");
        String adminCode = adminChannelMap.getAdminIdByChannelId(ctx.channel().id());
        AdminInfo tAdminInfo1 = adminChannelMap.get(adminCode);
        tAdminInfo1.setFocusActivity(activityCode); //设置当前管理员聚焦的活动

        Map<String, String > map = new HashMap<>();
        map.put("activityCode", activityCode);
        map.put("adminCode", adminCode);

        QueueMessage queueMessage = new QueueMessage(300, map);
        pushQueue.offer(queueMessage);


//        CurrentActivity currentActivity = activityMap.get(activityCode);
//        Integer goodsId = Integer.parseInt(currentActivity.getCurrentGoodsId());
//        List<JbxtUserDVO> userLists = iJbxtUserService.findAllByActivityCode(activityCode);
//
//
//        List<Res300t1> t1s = new ArrayList<>();
//        for (int i = 0; i < userLists.size(); i++) {
//            JbxtUserDVO user1 = userLists.get(i);
//
//            Res300t1 res300t1 = new Res300t1();
//            res300t1.setSupplierName(user1.getSupplierName());
//            res300t1.setCodeName(user1.getCodeName());
//            res300t1.setSupplierCode(user1.getCode());
//
//            JbxtBiddingDO cUserMinBid = iJbxtBiddingService.selectMinBidTableBy(user1.getCode(), goodsId, activityCode);
//            if (cUserMinBid != null) {
//                res300t1.setMinBid(cUserMinBid.getBid());
//            } else {
//                res300t1.setMinBid(new BigDecimal("0.000"));
//            }
//
//            List<JbxtBiddingDVO> cUserBidHistory = iJbxtBiddingService.findAllByUserCodeAndActivityCode(user1.getCode(), activityCode);
//            List<Res300t2> t2s = new ArrayList<>();
//            if (cUserBidHistory.size() > 0) {
//
//                for (int j = 0; j <cUserBidHistory.size(); j++) {
//                    JbxtBiddingDVO ubid1 = cUserBidHistory.get(j);
//
//                    Res300t2 res300t2 = new Res300t2();
//                    res300t2.setBid(ubid1.getBid());
//                    res300t2.setBidTime(ubid1.getBidTime());
//
//                    t2s.add(res300t2);
//                }
//            }
//            res300t1.setBids(t2s);
//
//            t1s.add(res300t1);
//        }
//
//        List<Res300t1> t1sCollect = t1s.stream().sorted(Comparator.comparing(Res300t1::getMinBid)).collect(Collectors.toList());
//
//        Res300 res300 = new Res300();
//        res300.setGoodsId(goodsId);
//        res300.setMinPrice(t1sCollect.get(0).getMinBid());
//        res300.setList(t1sCollect);
//
//
//        //处理返回数据
//        Message tmsg = new Message(300, res300);
//        ctx.channel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(tmsg)));
    }


    // 当有新的客户端连接服务器之后，会自动调用这个方法
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 将新的通道加入到clients
        clients.add(ctx.channel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        ctx.channel().close();
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("关闭通道");
        //UserChannelMap.removeByChannelId(ctx.channel().id().asLongText());
        //UserChannelMap.print();
    }
}
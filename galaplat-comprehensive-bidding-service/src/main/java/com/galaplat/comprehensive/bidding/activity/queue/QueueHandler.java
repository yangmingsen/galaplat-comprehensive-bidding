package com.galaplat.comprehensive.bidding.activity.queue;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.AdminChannelMap;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.List;

public class QueueHandler extends Thread {

    private UserChannelMap userChannelMap;
    private AdminChannelMap adminChannel;
    private PushQueue pushQueue;


    public QueueHandler() {
        this.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
        this.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
        this.pushQueue = SpringUtil.getBean(PushQueue.class);
    }


    public void run() {
        while (true) {
            QueueMessage takeQueuemsg = pushQueue.take();


            //{type: 100, data: {userRank: -1, goodsPrice: 23.343, goodsId: 23423}}
            switch (takeQueuemsg.getType()) {
                //处理供应商端提交数据问题
                case 200: {
                    String activityCode = takeQueuemsg.getData().get("activityCode");
                    //推数据给管理端
                    userChannelMap.getAllUser().forEach(supplier -> {
                        if (userChannelMap.getUserFocusActivity(supplier).equals(activityCode)) {
                            //推流到供应商客户端
                            Message message = new Message(200, takeQueuemsg.getData());
                            Channel channel = userChannelMap.get(supplier);
                            channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                        }
                    });
                }
                break;

                case 301: {
                    String activityCode = takeQueuemsg.getData().get("activityCode");
                    //推数据给供应商端
                    adminChannel.getAllAdmin().forEach(admin -> {
                        AdminInfo adminInfo = adminChannel.get(admin);
                        if (adminInfo.getFocusActivity().equals(activityCode)) {
                            //推数据到管理端
                            Message message = new Message(301, takeQueuemsg.getData());
                            adminInfo.getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                        }
                    });
                }
            }

        }
    }


}

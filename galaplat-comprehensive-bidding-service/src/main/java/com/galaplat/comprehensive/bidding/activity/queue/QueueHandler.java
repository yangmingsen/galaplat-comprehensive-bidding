package com.galaplat.comprehensive.bidding.activity.queue;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.AdminChannelMap;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueueHandler extends Thread {

    private UserChannelMap userChannelMap;
    private AdminChannelMap adminChannel;
    private PushQueue pushQueue;
    private IJbxtGoodsService iJbxtGoodsService; //竞品服务


    public QueueHandler() {
        this.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
        this.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
        this.pushQueue = SpringUtil.getBean(PushQueue.class);
        this.iJbxtGoodsService = SpringUtil.getBean(IJbxtGoodsService.class);
    }

    private void handler200Problem(QueueMessage takeQueuemsg) {

        String activityCode = takeQueuemsg.getData().get("activityCode");
        Integer goodsId = Integer.parseInt(takeQueuemsg.getData().get("goodsId"));

        userChannelMap.getAllUser().forEach(supplier -> {
            if (userChannelMap.getUserFocusActivity(supplier).equals(activityCode)) {
                CustomBidVO info = iJbxtGoodsService.getUserBidRankInfoByUserCodeAndActivity(goodsId, supplier, activityCode);
                Map<String, String> map = new HashMap<>();
                map.put("userRank",info.getUserRank().toString());
                map.put("goodsPrice",info.getGoodsPrice().toString());
                map.put("goodsId",info.getGoodsId().toString());

                //推流到供应商客户端
                Message message = new Message(200, map);
                userChannelMap.get(supplier).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));;
            }
        });
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

            //{type: 200, data: {goodsId: 23423, activityCode: 23423345}}
            //{type: 301, data: { activityCode: 23423345, bidTime: 15:32, bid: 2.345, supplierCode: 234903945834, CodeName: '小红', supplierName: '小米科技电子有限公司'}}
            switch (takeQueuemsg.getType()) {
                case 200: {//处理供应商端提交数据问题
                    handler200Problem(takeQueuemsg);
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

package com.galaplat.comprehensive.bidding.activity;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CurrentActivity extends Thread {

    private UserChannelMap userChannelMap = SpringUtil.getBean(UserChannelMap.class);
    private AdminChannelMap adminChannel = SpringUtil.getBean(AdminChannelMap.class);
    private String currentActivityCode;
    private String currentGoodsId;
    private int initTime; //秒
    private int status;//1 进行 2暂停  3//重置
    private int remainingTime;  //秒
    private List<String> userList;
    private Map<String, String> t_map = new HashMap<>();
    private Message message = new Message(100,null);

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getCurrentActivityCode() {
        return currentActivityCode;
    }

    public String getCurrentGoodsId() {
        return currentGoodsId;
    }

    /**
     * 获取剩余时间 （秒）
     * @return
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    public String getRemainingTimeString() {
        int ttime = this.remainingTime;
        int minute = ttime/60;
        int second = ttime%60;

        StringBuilder stringBuilder = new StringBuilder();
        if (minute < 10) {
            stringBuilder.append("0").append(minute);
        } else {
            stringBuilder.append(minute);
        }
        stringBuilder.append(":");
        if (second < 10) {
            stringBuilder.append("0").append(second);
        } else {
            stringBuilder.append(second);
        }

        return stringBuilder.toString();
    }



    /***
     *
     * @param currentActivityCode
     * @param currentGoodsId
     * @param initTime 秒
     * @param status 1 进行 2暂停  3重置
     */
    public CurrentActivity(String currentActivityCode, String currentGoodsId,
                           int initTime, int status) {
        this.currentActivityCode = currentActivityCode;
        this.currentGoodsId = currentGoodsId;
        this.initTime = initTime;
        this.status = status;
        this.remainingTime = initTime;

        initUser(currentActivityCode);
    }

    private void initUser(String currentActivityCode) {
        userList = new ArrayList<>();
        IJbxtUserService iJbxtUserService = SpringUtil.getBean(IJbxtUserService.class);

        List<JbxtUserDVO> userLists = iJbxtUserService.findAllByActivityCode(currentActivityCode);
        userLists.forEach(user -> {
            this.userList.add(user.getCode());
        });
    }


    public void setStatus(int status) {
        this.status = status;

        if (this.status == 3) {
            IJbxtBiddingService iJbxtBiddingService = SpringUtil.getBean(IJbxtBiddingService.class);
            try {
                iJbxtBiddingService.deleteByGoodsIdAndActivityCode(Integer.parseInt(this.getCurrentGoodsId()), this.currentActivityCode);
                iJbxtBiddingService.deleteMinbidTableByGoodsIdAndActivityCode(Integer.parseInt(this.getCurrentGoodsId()), this.currentActivityCode);

                //同步数据（管理端 N，供应商端 Y）



                this.status = 1;
                this.remainingTime = this.initTime;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    private void startRemainingTime() throws InterruptedException{
        while (remainingTime > -1) {
            if (status == 2) {
                continue;
            } else if (status == 3) {
                this.remainingTime = this.initTime;
                status = 1;
                continue;
            }

            String remainingTimeString = getRemainingTimeString();
            System.out.println("currentActivityCode="+currentActivityCode+" goodsId="+currentGoodsId+" 剩余时间:"+remainingTimeString);
            t_map.put("remainingTime",remainingTimeString);
            message.setData(t_map);
            for (int i = 0; i < userList.size(); i++) {
                String user  = userList.get(i);
                Channel channel = userChannelMap.get(user);
                if (channel != null) {
                    channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
            }

            adminChannel.getAllAdmin().forEach(admin -> {
                if (adminChannel.get(admin).getFocusActivity().equals(this.currentActivityCode)) {
                    adminChannel.get(admin).getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
            });

            Thread.sleep(1*1000);
            remainingTime --;
        }

    }


    public void run() {
        try {
            startRemainingTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

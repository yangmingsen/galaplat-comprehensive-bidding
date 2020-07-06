package com.galaplat.comprehensive.bidding.activity;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
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

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getCurrentActivityCode() {
        return currentActivityCode;
    }

    public String getCurrentGoodsId() {
        return currentGoodsId;
    }

    public int getRemainingTime() {
        return remainingTime;
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
    }

    private void startRemainingTime() throws InterruptedException{
        while (remainingTime > 0) {
            if (status == 2) {
                continue;
            } else if (status == 3) {
                this.remainingTime = this.initTime;
                status = 1;
                continue;
            }

            int minute = remainingTime / 60;
            int second = remainingTime % 60;
            String rt = minute+":"+second;
            System.out.println("currentActivityCode="+currentActivityCode+" goodsId="+currentGoodsId+" 剩余时间:"+rt);

            Map<String, String> t_map = new HashMap<>();
            t_map.put("remainingTime",rt);
            Message message = new Message(100,t_map);


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

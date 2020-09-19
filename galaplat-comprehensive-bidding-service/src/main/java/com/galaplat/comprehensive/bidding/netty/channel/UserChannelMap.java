package com.galaplat.comprehensive.bidding.netty.channel;

import io.netty.channel.Channel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建立用户ID与通道的关联
 */
public class UserChannelMap {
    // 用户保存用户id与通道的Map对象 的关联
    private final Map<String, Channel> userChannelMap;

    private final Map<Channel, String> channelRelevanceUser;

    // 用户保存用户id与活动Code的关联
    private final Map<String, String> userFocusActivity;


    public UserChannelMap() {
        userChannelMap = new HashMap<>();
        channelRelevanceUser = new HashMap<>();
        userFocusActivity = new HashMap<>();
    }

    /***
     * 获取通道的所有用户
     * @return
     */
    public List<String> getAllUser() {
        List<String> res = new ArrayList<>();
        for(String user : userChannelMap.keySet()) {
            res.add(user);
        }
        return res;
    }


    /**
     * 添加供应商与channel的关联
     * @param userid
     * @param channel
     */
    public  void put(String userid, Channel channel) {
        userChannelMap.put(userid, channel);
    }

    public void putChannelToUser(Channel channel, String userCode) {
        channelRelevanceUser.put(channel, userCode);
    }


    public void put(String userid, String focusActivity) {
        userFocusActivity.put(userid,focusActivity);
    }


    public String getUserByChannel(Channel channel) {
        return channelRelevanceUser.get(channel);
    }

    /***
     * 获取当前供应商关联的活动code
     * @param userid
     * @return
     */
    public String getUserFocusActivity(String userid) {
        return userFocusActivity.get(userid);
    }



    /**
     * 根据用户id移除用户id与channel的关联
     * @param userid
     */
    public  void remove(String userid) {
        this.removeRelevance(userid);
    }

    private void removeRelevance(String userCode) {
        Channel channel = this.userChannelMap.remove(userCode);
        this.channelRelevanceUser.remove(channel);
        this.userChannelMap.remove(userCode);
    }


    // 打印所有的用户与通道的关联数据
    public  void print() {
        System.out.println("----------------");
        for (String s : userChannelMap.keySet()) {
            System.out.println("用户id:" + s + " 通道:" + userChannelMap.get(s).id());
        }
        System.out.println("----------------");
    }

    /**
     * 根据userCode获取对应的通道
     * @return Netty通道
     */
    public  Channel get(String userCode) {
        return userChannelMap.get(userCode);
    }
}

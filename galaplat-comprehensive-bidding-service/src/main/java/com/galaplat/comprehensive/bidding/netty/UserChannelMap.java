package com.galaplat.comprehensive.bidding.netty;

import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 建立用户ID与通道的关联
 */
public class UserChannelMap {
    // 用户保存用户id与通道的Map对象 的关联
    private  Map<String, Channel> userChannelMap;

    // 用户保存用户id与活动Code的关联
    private Map<String, String> userFocusActivity;


    public UserChannelMap() {
        userChannelMap = new HashMap<String, Channel>();
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
     * 添加用户id与channel的关联
     * @param userid
     * @param channel
     */
    public  void put(String userid, Channel channel) {
        userChannelMap.put(userid, channel);
    }

    public void put(String userid, String focusActivity) {
        userFocusActivity.put(userid,focusActivity);
    }

    public String getUserFocusActivity(String userid) {
        return userFocusActivity.get(userid);
    }



    /**
     * 根据用户id移除用户id与channel的关联
     * @param userid
     */
    public  void remove(String userid) {
        userChannelMap.remove(userid);
    }

    /**
     * 根据通道id移除用户与channel的关联
     * @param channelId 通道的id
     */
    public  void removeByChannelId(String channelId) {
        if(!StringUtils.isNotBlank(channelId)) {
            return;
        }

        for (String s : userChannelMap.keySet()) {
            Channel channel = userChannelMap.get(s);
            if(channelId.equals(channel.id().asLongText())) {
                System.out.println("客户端连接断开,取消用户" + s + "与通道" + channelId + "的关联");
                userChannelMap.remove(s);
                break;
            }
        }
    }


    // 打印所有的用户与通道的关联数据
    public  void print() {
        for (String s : userChannelMap.keySet()) {
            System.out.println("用户id:" + s + " 通道:" + userChannelMap.get(s).id());
        }
    }

    /**
     * 根据好友id获取对应的通道
     * @param friendid 好友id
     * @return Netty通道
     */
    public  Channel get(String friendid) {
        return userChannelMap.get(friendid);
    }
}

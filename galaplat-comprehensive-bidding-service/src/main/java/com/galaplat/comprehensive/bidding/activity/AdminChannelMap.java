package com.galaplat.comprehensive.bidding.activity;

import io.netty.channel.ChannelId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminChannelMap {

    private Map<String, AdminInfo> map; //adminCode => AdminInfo
    private Map<ChannelId,String> channelRelevanceAdminIdMap; //channelId => adminCode

    public AdminChannelMap() {
        map = new HashMap<>();
        channelRelevanceAdminIdMap = new HashMap<>();
    }

    /***
     * 获取通道的所有管理员
     * @return
     */
    public List<String> getAllAdmin() {
        List<String> res = new ArrayList<>();
        for(String user : map.keySet()) {
            res.add(user);
        }
        return res;
    }


    public void put(String key, AdminInfo value) {
         map.put(key, value);
         channelRelevanceAdminIdMap.put(value.getChannel().id(), key);
    }

    public AdminInfo get(String key) {
        return map.get(key);
    }

    public String getAdminIdByChannelId(ChannelId key){
        return channelRelevanceAdminIdMap.get(key);
    }


    // 打印所有的用户与通道的关联数据
    public  void print() {
        for (String s : map.keySet()) {
            System.out.println("用户id:" + s + " 通道:" + map.get(s).getChannel().id());
        }
    }

}

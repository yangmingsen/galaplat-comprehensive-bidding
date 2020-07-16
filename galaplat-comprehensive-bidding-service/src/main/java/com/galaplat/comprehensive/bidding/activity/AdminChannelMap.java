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

    /***
     * 根据adminCode移除相关channel 关联
     * @param adminCode
     */
    public void remove(String adminCode) {
        this.removeRelevance(adminCode);
    }

    private void removeRelevance(String adminCode) {
        AdminInfo adminInfo = this.map.remove(adminCode);
        this.channelRelevanceAdminIdMap.remove(adminInfo.getChannel().id());
    }


    public void put(String key, AdminInfo value) {
         this.map.put(key, value);
         this.channelRelevanceAdminIdMap.put(value.getChannel().id(), key);
    }

    /***
     * 获取 AdminInfo by adminCode
     * @param key
     * @return
     */
    public AdminInfo get(String key) {
        return map.get(key);
    }

    /***
     *  获取 AdminCode by channel
     * @param key
     * @return
     */
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

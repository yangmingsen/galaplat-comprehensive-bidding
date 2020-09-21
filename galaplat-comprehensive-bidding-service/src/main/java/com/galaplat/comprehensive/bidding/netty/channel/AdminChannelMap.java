package com.galaplat.comprehensive.bidding.netty.channel;

import io.netty.channel.Channel;
import io.netty.channel.ChannelId;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AdminChannelMap implements ChannelMap{

    private final Map<String, AdminInfo> map; //adminCode => AdminInfo
    private final Map<ChannelId,String> channelRelevanceAdminIdMap; //channelId => adminCode

    public AdminChannelMap() {
        map = new HashMap<>();
        channelRelevanceAdminIdMap = new HashMap<>();
    }


    @Override
    public List<Channel> getAllSpecifixChannel(String activityCode) {
        List<Channel> channels = new ArrayList<>();
        for(Map.Entry<String, AdminInfo> entry : map.entrySet()) {
            AdminInfo adminInfo = entry.getValue();
            if (adminInfo.getFocusActivity().equals(activityCode)) {
                channels.add(adminInfo.getChannel());
            }
        }
        return channels;
    }

    @Override
    public List<String> getAllCodeSpecifixCode(String activityCode) {
        List<String> adminCodes = new ArrayList<>();
        for(Map.Entry<String, AdminInfo> entry : map.entrySet()) {
            String adminCode = entry.getKey();
            AdminInfo adminInfo = entry.getValue();
            if (adminInfo.getFocusActivity().equals(activityCode)) {
                adminCodes.add(adminCode);
            }
        }
        return adminCodes;
    }

    @Override
    public Channel getChannel(String code) {
        return map.get(code) == null ? null : map.get(code).getChannel();
    }

    @Override
    public Object getDetailInfo(String code) {
        return map.get(code);
    }


    @Override
    public List<Channel> getAllChannel() {

        List<Channel> list = new ArrayList<>();
        for(Map.Entry<String, AdminInfo> infoMap : map.entrySet()) {
            list.add(infoMap.getValue().getChannel());
        }
        return list;
    }

    @Override
    public List<String> getAllCode() {
        return getAllAdmin();
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

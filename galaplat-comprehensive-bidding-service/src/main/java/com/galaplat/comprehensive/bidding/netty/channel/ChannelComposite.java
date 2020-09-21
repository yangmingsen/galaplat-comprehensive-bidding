package com.galaplat.comprehensive.bidding.netty.channel;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.netty.pojo.ResponseMessage;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ChannelComposite {
    protected final List<ChannelMap> channelMapList = new ArrayList<>();

    @Autowired
    protected AdminChannelMap adminChannelMap;
    private volatile boolean adminChannelFlag = false;

    @Autowired
    protected SupplierChannelAdapter supplierChannelAdapter;
    private volatile boolean supplierChannelFlag = false;


    public void addChannelMap(ChannelMap channelMap) {
        this.channelMapList.add(channelMap);
    }

    public AdminChannelMap getAdminChannelMap() {
        return adminChannelMap;
    }

    public SupplierChannelAdapter getSupplierChannelAdapter() {
        return supplierChannelAdapter;
    }

    public AdminInfo getAdmin(String adminCode) {
        return (AdminInfo)adminChannelMap.getDetailInfo(adminCode);
    }

    public SupplierInfo getSupplier(String supplierCode) {
        return (SupplierInfo)supplierChannelAdapter.getDetailInfo(supplierCode);
    }

    /**
     * 推数据流到所有管理端
     *
     * @param message
     * @param activityCode
     */
    public void notifyAllAdmin(ResponseMessage message, String activityCode) {
        adminChannelMap.
                getAllSpecifixChannel(activityCode).
                forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))));
    }

    /**
     * 为兼容老版本的api设计
     *
     * @param message
     * @param activityCode
     * @param adminCode
     */
    public void notifyOptionAdmin(ResponseMessage message, String activityCode, String adminCode) {
        notifyOptionAdmin(message, adminCode);
    }

    public void notifyOptionAdmin(ResponseMessage message, String adminCode) {
        adminChannelMap.
                getChannel(adminCode).
                writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
    }

    public void notifyAllSupplier(ResponseMessage message, String activityCode) {
        supplierChannelAdapter.
                getAllSpecifixChannel(activityCode).
                forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))));
    }

    public void notifyOptionSupplier(ResponseMessage message, String userCode) {
        supplierChannelAdapter.
                getChannel(userCode).
                writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
    }

    /**
     * 为兼容老版本的api设计
     *
     * @param message
     * @param activityCode
     * @param userCode
     */
    public void notifyOptionSupplier(ResponseMessage message, String activityCode, String userCode) {
        notifyOptionSupplier(message, userCode);
    }


    public void notifyAllClient(ResponseMessage message) {
        if (!supplierChannelFlag) {
            if (supplierChannelAdapter != null) {
                channelMapList.add(supplierChannelAdapter);
                supplierChannelFlag = true;
            }
        }

        if (!adminChannelFlag) {
            if (adminChannelMap != null) {
                channelMapList.add(adminChannelMap);
                adminChannelFlag = true;
            }
        }


        for (ChannelMap channelMap : channelMapList) {
            channelMap.
                    getAllChannel().
                    forEach(channel -> channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message))));
        }
    }


}

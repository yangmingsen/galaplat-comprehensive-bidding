package com.galaplat.comprehensive.bidding.netty.channel;

import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SupplierChannelAdapter implements ChannelMap {

    @Autowired
    private UserChannelMap userChannelMap;

    @Override
    public Channel getChannel(String supplierCode) {
        return userChannelMap.get(supplierCode);
    }

    public UserChannelMap getSupplierChannelMap() {
        return this.userChannelMap;
    }

    @Override
    public Object getDetailInfo(String supplierCode) {
        Channel channel = this.userChannelMap.get(supplierCode);
        String activityCode = this.userChannelMap.getUserFocusActivity(supplierCode);
        return new SupplierInfo(supplierCode, channel, activityCode);
    }

    @Override
    public List<Channel> getAllSpecifixChannel(String activityCode) {
        List<String> supplierCodeList = getAllCode();
        List<Channel> channels = new ArrayList<>();
        for(String supplierCode : supplierCodeList) {
            SupplierInfo info = (SupplierInfo) getDetailInfo(supplierCode);
            if (info.getActivityCode().equals(activityCode)) {
                channels.add(info.getChannel());
            }
        }

        return channels;
    }

    @Override
    public List<String> getAllCodeSpecifixCode(String activityCode) {
        List<String> supplierCodeList = userChannelMap.getAllUser();
        List<String> newSupplierCodeList = new ArrayList<>();
        for(String supplierCode : supplierCodeList) {
            String userFocusActivity = userChannelMap.getUserFocusActivity(supplierCode);
            if (activityCode.equals(userFocusActivity)) {
                newSupplierCodeList.add(supplierCode);
            }
        }
        return newSupplierCodeList;
    }

    @Override
    public List<Channel> getAllChannel() {
        List<Channel> channels = new ArrayList<>();
        for(String supplierCode : userChannelMap.getAllUser()) {
            channels.add(userChannelMap.get(supplierCode));
        }

        return channels;
    }

    @Override
    public List<String> getAllCode() {
        return userChannelMap.getAllUser();
    }



}

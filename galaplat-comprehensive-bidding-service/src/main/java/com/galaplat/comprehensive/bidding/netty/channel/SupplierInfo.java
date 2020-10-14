package com.galaplat.comprehensive.bidding.netty.channel;

import io.netty.channel.Channel;

public class SupplierInfo {
    private String supplierCode;
    private Channel channel;
    private String activityCode;

    public SupplierInfo(String supplierCode, Channel channel,
                        String activityCode) {
        this.supplierCode = supplierCode;
        this.channel = channel;
        this.activityCode = activityCode;
    }

    public SupplierInfo() {

    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }
}

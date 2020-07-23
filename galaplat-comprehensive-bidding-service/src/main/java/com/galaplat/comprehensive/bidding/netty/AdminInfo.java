package com.galaplat.comprehensive.bidding.netty;

import io.netty.channel.Channel;

public class AdminInfo {
    private String focusActivity; //聚焦那场活动
    private Channel channel;

    public AdminInfo(String focusActivity, Channel channel) {
        this.focusActivity = focusActivity;
        this.channel = channel;
    }

    public String getFocusActivity() {
        return focusActivity;
    }

    public void setFocusActivity(String focusActivity) {
        this.focusActivity = focusActivity;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "AdminInfo{" +
                "focusActivity='" + focusActivity + '\'' +
                ", channel=" + channel +
                '}';
    }
}

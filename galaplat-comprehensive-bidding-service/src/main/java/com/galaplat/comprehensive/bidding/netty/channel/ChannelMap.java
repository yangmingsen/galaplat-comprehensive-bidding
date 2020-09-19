package com.galaplat.comprehensive.bidding.netty.channel;

import io.netty.channel.Channel;

import java.util.List;

public interface ChannelMap {
    Channel getChannel(String code);

    Object getDetailInfo(String code);

    List<Channel> getAllChannel();

    List<String> getAllCode();

    default List<Channel> getAllSpecifixChannel(String activityCode) {
        return null;
    }

    default List<String> getAllCodeSpecifixCode(String activityCode) {
        return null;
    }

}

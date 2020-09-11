package com.galaplat.comprehensive.bidding.netty.channel;

import io.netty.channel.Channel;

public interface ChannelMap {
    public Channel getChannel();

    public Object getDetailInfo(String code);
}

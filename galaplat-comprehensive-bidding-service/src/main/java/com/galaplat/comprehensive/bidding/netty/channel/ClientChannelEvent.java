package com.galaplat.comprehensive.bidding.netty.channel;

public interface ClientChannelEvent {

    void supplierEvent(String type, Object message);

    void adminEvent(String type, Object message);
}

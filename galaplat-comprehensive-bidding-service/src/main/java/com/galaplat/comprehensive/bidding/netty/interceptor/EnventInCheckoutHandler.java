package com.galaplat.comprehensive.bidding.netty.interceptor;

import com.galaplat.comprehensive.bidding.netty.pojo.RequestMessage;

public class EnventInCheckoutHandler implements EventInInterceptor{
    @Override
    public boolean checkout(RequestMessage requestMessage) {


        return false;
    }
}

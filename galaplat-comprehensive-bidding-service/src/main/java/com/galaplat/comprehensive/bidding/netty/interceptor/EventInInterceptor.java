package com.galaplat.comprehensive.bidding.netty.interceptor;

import com.galaplat.comprehensive.bidding.netty.pojo.RequestMessage;

public interface EventInInterceptor {
    boolean checkout(final RequestMessage requestMessage);
}

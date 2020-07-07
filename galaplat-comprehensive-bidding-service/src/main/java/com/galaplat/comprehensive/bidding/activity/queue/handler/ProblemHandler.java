package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;

public interface ProblemHandler {

    public void problem(int type, QueueMessage queuemsg);
}

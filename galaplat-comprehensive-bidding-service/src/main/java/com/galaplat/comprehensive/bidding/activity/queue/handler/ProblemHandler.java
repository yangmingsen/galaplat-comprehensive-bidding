package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;

/***
 * <h1>简介</h1>
 *  <p>所有需要处理的消息，我们把它抽象为需要处理的问题</p>
 *
 */
public interface ProblemHandler {


    void problem(int type, QueueMessage queuemsg);
}

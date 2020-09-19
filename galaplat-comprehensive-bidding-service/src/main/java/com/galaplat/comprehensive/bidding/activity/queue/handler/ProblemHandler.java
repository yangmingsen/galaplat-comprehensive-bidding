package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;

/***
 * <h1>简介</h1>
 *  <p>所有需要处理的消息，我们把它抽象为需要处理的问题</p>
 *
 */
public interface ProblemHandler {

    /**
     * 抽象问题处理接口， 请把所有问题丢给他
     * @param type
     * @param queuemsg
     */
    void problem(int type, QueueMessage queuemsg);
}

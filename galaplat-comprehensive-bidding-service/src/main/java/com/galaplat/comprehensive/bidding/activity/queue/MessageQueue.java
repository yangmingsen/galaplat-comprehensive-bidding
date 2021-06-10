package com.galaplat.comprehensive.bidding.activity.queue;

import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class MessageQueue {
    private final ConcurrentLinkedQueue<QueueMessage> queue = new ConcurrentLinkedQueue<>();;
    private final Logger LOGGER = LoggerFactory.getLogger(MessageQueue.class);
    /***
     * 如果试图的操作无法立即执行，返回一个特定的值(常常是 true / false)。
     */
    public boolean offer(QueueMessage queueMessage) {
        LOGGER.info("offer(ms): 消息队列收到消息"+queueMessage.toString());

        //记录消息至DB

        return this.queue.offer(queueMessage);
    }

    /***
     * wait for get data
     * @return
     */
    public QueueMessage take() {
        try {
            //标记消费消息 sync至DB

            return this.queue.poll();
        } catch (Exception e) {
            LOGGER.info("take(ERROR): 我在take数据报错,错误信息["+e.getMessage()+"]");
        }
        return null;
    }

    /**
     * Retrieves and removes the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public QueueMessage poll() {
        return this.queue.poll();
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public QueueMessage peek() {
        return this.queue.peek();
    }


}

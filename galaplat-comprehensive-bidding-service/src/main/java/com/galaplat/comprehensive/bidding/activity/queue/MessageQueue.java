package com.galaplat.comprehensive.bidding.activity.queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@Component
public class MessageQueue {
    private final BlockingQueue<QueueMessage> blockingQueue = new ArrayBlockingQueue<>(6000);;
    private final Logger LOGGER = LoggerFactory.getLogger(MessageQueue.class);
    /***
     * 如果试图的操作无法立即执行，返回一个特定的值(常常是 true / false)。
     */
    public boolean offer(QueueMessage queueMessage) {
        LOGGER.info("offer(ms): 消息队列收到消息"+queueMessage.toString());
        return this.blockingQueue.offer(queueMessage);
    }

    /***
     * wait for get data
     * @return
     */
    public QueueMessage take() {
        try {
            return this.blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
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
        return this.blockingQueue.poll();
    }

    /**
     * Retrieves, but does not remove, the head of this queue,
     * or returns {@code null} if this queue is empty.
     *
     * @return the head of this queue, or {@code null} if this queue is empty
     */
    public QueueMessage peek() {
        return this.blockingQueue.peek();
    }


}

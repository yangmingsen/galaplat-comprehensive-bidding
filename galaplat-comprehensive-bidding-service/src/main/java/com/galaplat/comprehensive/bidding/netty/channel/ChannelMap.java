package com.galaplat.comprehensive.bidding.netty.channel;

import io.netty.channel.Channel;

import java.util.List;

/**
 * 这里是保存所有Client连接的顶级Root接口
 */
public interface ChannelMap {

    /**
     * 获取某个Channel
     * @param code the id
     * @return Channel
     */
    Channel getChannel(String code);

    /**
     * 获取一个对象信息
     * @param code the id
     * @return Object
     */
    Object getDetailInfo(String code);

    /**
     * 获取所有的Channel
     * @return
     */
    List<Channel> getAllChannel();

    /**
     * 获取所有的id
     * @return
     */
    List<String> getAllCode();

    /**
     * 获取所有activityCode对应的Channel
     * @param activityCode
     * @return
     */
    default List<Channel> getAllSpecifixChannel(String activityCode) {
        return null;
    }

    /**
     * 获取所有activityCode对应的id
     * @param activityCode
     * @return
     */
    default List<String> getAllCodeSpecifixCode(String activityCode) {
        return null;
    }

}

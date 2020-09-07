package com.galaplat.comprehensive.bidding.activity.queue.msg;

import java.util.Map;

public class ObjectQueueMessage extends QueueMessage{
    private Map<String,Object> objData;  // 消息字段

    public ObjectQueueMessage(Integer type, Map<String, Object> data) {
        super(type,null);
        this.objData = data;
    }

    @Override
    public Integer getType() {
        return super.getType();
    }

    public Map<String, Object> getObjData() {
        return objData;
    }

    public void setObjData(Map<String, Object> objData) {
        this.objData = objData;
    }

    @Override
    public String toString() {
        return "ObjectQueueMessage{" +
                "objData=" + objData +
                ", type=" + type +
                "} ";
    }
}

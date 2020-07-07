package com.galaplat.comprehensive.bidding.netty.pojo;


public class Message {
    private Integer type; // 消息类型
    private Object data;  // 扩展消息字段

    public Message(Integer type, Object data) {
        this.type = type;
        this.data = data;
    }

    public Message() {
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Message{" +
                "type=" + type +
                ", data=" + data +
                '}';
    }
}

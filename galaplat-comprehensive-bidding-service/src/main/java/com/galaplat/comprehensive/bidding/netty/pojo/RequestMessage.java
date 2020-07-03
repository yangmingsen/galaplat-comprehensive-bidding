package com.galaplat.comprehensive.bidding.netty.pojo;

import java.util.Map;

public class RequestMessage {
    private Integer type; // 消息类型
    private Map<String,String> data;  // 扩展消息字段

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}

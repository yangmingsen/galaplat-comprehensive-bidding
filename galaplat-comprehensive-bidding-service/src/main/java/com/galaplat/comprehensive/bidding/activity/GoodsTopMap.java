package com.galaplat.comprehensive.bidding.activity;

import java.util.HashMap;
import java.util.Map;

public class GoodsTopMap {
    private Map<String,String> map; //activityCode+goodsId <=> userCode

    public GoodsTopMap() {
        map = new HashMap<>();
    }

    /***
     * key = activityCode+goodsId
     * @param key
     * @return
     */
    public String get(String key) {
        return map.get(key);
    }

    public void put(String key, String value) {
        map.put(key,value);
    }

}

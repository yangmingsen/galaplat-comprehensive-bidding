package com.galaplat.comprehensive.bidding.activity;

import java.util.HashMap;
import java.util.Map;

public class ActivityMap {
    private final Map<String, ActivityThread> map; //activityCode => ActivityThread

    public ActivityMap() {
        map = new HashMap<>();
    }

    public void put(String key, ActivityThread value) {
        map.put(key, value);
    }

    /***
     * 根据 activityCode 获取关联的 Activity
     * @param key
     * @return
     */
    public ActivityThread get(String key) {
        return map.get(key);
    }

}

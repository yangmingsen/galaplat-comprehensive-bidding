package com.galaplat.comprehensive.bidding.activity;

import java.util.HashMap;
import java.util.Map;

public class ActivityThreadManager {
    private final Map<String, ActivityThread> map = new HashMap<>();; //activityCode => ActivityThread

    /***
     * 建立活动与活动线程的关联
     * @param key
     * @param value
     */
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

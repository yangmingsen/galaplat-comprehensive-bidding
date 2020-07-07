package com.galaplat.comprehensive.bidding.activity;

import java.util.HashMap;
import java.util.Map;

public class ActivityMap {
    private Map<String, CurrentActivity> map; //activityCode => CurrentActivity

    public ActivityMap() {
        map = new HashMap<>();
    }

    public void put(String key, CurrentActivity value) {
        map.put(key, value);
    }

    /***
     * 根据 activityCode 获取关联的 Activity
     * @param key
     * @return
     */
    public CurrentActivity get(String key) {
        return map.get(key);
    }

}

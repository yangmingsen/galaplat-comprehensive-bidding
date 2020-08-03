package com.galaplat.comprehensive.bidding.activity;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ActivityThreadManager {
    private final Executor executor = Executors.newCachedThreadPool();
    private final Map<String, ActivityThread> map = new HashMap<>(); //activityCode => ActivityThread

    /**
     * 这是 使用例子
     */
    private void example() {
        ActivityTask activityTask = new ActivityTask.Builder().activityCode("1234").goodsId(234234).initTime(14).build();
        this.executor.execute(activityTask);
    }

    public void doTask(Runnable task) {
        this.executor.execute(task);
    }

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

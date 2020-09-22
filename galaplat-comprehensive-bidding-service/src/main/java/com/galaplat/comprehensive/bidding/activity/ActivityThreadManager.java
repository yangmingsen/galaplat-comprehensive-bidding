package com.galaplat.comprehensive.bidding.activity;


import com.galaplat.comprehensive.bidding.controllers.AdminController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * the class 用于管理所有的ActivityTask线程
 */
public class ActivityThreadManager {
    private final Executor executor = Executors.newCachedThreadPool();
    private final Map<String, ActivityTask> activityTaskMap =new HashMap<>();
    Logger LOGGER = LoggerFactory.getLogger(ActivityThreadManager.class);


    /**
     * 这是 使用例子
     */
    private void example() {
        ActivityTask activityTask = new ActivityTask.Builder().activityCode("1234").goodsId(234234).initTime(14).build();
        this.executor.execute(activityTask);
    }

    /**
     *need call this method after you created a ActivityTask
     * @param task
     */
    public void doTask(Runnable task) {
        this.executor.execute(task);
        LOGGER.info("doTask(INFO): -----启动活动线程成功------");
    }

    /***
     * 建立活动与活动线程的关联
     * @param key
     * @param value
     */
    public void put(String key, ActivityTask value) {
        activityTaskMap.put(key, value);
    }

    /**
     * 根据 activityCode 获取关联的 ActivityTask
     * @param key
     * @return
     */
    public ActivityTask get(String key) {
        return activityTaskMap.get(key);
    }

    /**
     * 将当前活动从管理器中移除
     * @param key
     */
    public void remove(String key) {
        activityTaskMap.remove(key);
    }


}

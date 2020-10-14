package com.galaplat.comprehensive.bidding.netty;

import com.galaplat.comprehensive.bidding.activity.ActivityTask;
import com.galaplat.comprehensive.bidding.activity.ActivityThreadManager;
import com.galaplat.comprehensive.bidding.activity.queue.QueueHandlerThreadSingleton;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.ActivityDVO;
import com.galaplat.comprehensive.bidding.service.ActivityService;
import com.galaplat.comprehensive.bidding.service.GoodsService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {

    private final Logger LOGGER = LoggerFactory.getLogger(NettyListener.class);

    @Autowired
    private WebSocketServer websocketServer;

    @Autowired
    private ActivityThreadManager activityThreadManager;

    private void runNetty(ApplicationArguments args) {
        websocketServer.start(); //the Netty start point

        final ActivityService activityService = SpringUtil.getBean(ActivityService.class);
        final ActivityThreadManager activityMap = SpringUtil.getBean(ActivityThreadManager.class);
        final List<ActivityDVO> lists = activityService.findAllByStatus(3);
        for (ActivityDVO activityDO : lists) {
            if (activityDO.getStatus() == 3) {
                final GoodsService goodsService = SpringUtil.getBean(GoodsService.class);
                final GoodsDO activeGoods = goodsService.selectActiveGoods(activityDO.getCode());
                if (activeGoods != null) {
                    final ActivityTask.Builder builder = new ActivityTask.Builder();

                    builder.activityCode(activityDO.getCode()).
                            goodsId(activeGoods.getGoodsId()).
                            initTime(activeGoods.getTimeNum() * 60).
                            supplierNum(activityDO.getSupplierNum()).
                            delayedCondition(activeGoods.getLastChangTime()).
                            allowDelayedLength(activeGoods.getPerDelayTime()).
                            allowDelayedTime(activeGoods.getDelayTimes()).
                            bidType(activityDO.getBidingType());

                    ActivityTask activityTask = builder.build();
                    activityMap.put(activityDO.getCode(), activityTask);
                    activityThreadManager.doTask(activityTask);
                    LOGGER.info("启动 " + activityDO.getCode() + " 活动");
                }
            }
        }

    }

    private void runQueueMessageHandler(ApplicationArguments args) {
        //启动事件处理者
        QueueHandlerThreadSingleton queueHandlerThreadSingleton = QueueHandlerThreadSingleton.getInstance();
        queueHandlerThreadSingleton.start();
        LOGGER.info("runQueueMessageHandler(INFO): 问题处理者线程启动成功");
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //run messageHandler
        runQueueMessageHandler(args);

        //run Netty
        runNetty(args);

        // do what you want run？ ....

    }
}

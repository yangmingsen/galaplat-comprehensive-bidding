package com.galaplat.comprehensive.bidding.netty;

import com.galaplat.comprehensive.bidding.activity.ActivityTask;
import com.galaplat.comprehensive.bidding.activity.ActivityThreadManager;
import com.galaplat.comprehensive.bidding.activity.queue.QueueHandlerThreadSingleton;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
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

        final IJbxtActivityService activityService = SpringUtil.getBean(IJbxtActivityService.class);
        final ActivityThreadManager activityMap = SpringUtil.getBean(ActivityThreadManager.class);
        final List<JbxtActivityDVO> lists = activityService.findAll();
        for (JbxtActivityDVO activityDO : lists) {
            if (activityDO.getStatus() == 3) {
                final IJbxtGoodsService goodsService = SpringUtil.getBean(IJbxtGoodsService.class);
                final JbxtGoodsDO activeGoods = goodsService.selectActiveGoods(activityDO.getCode());
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


        //启动事件处理者
        QueueHandlerThreadSingleton queueHandlerThreadSingleton = QueueHandlerThreadSingleton.getInstance();
        queueHandlerThreadSingleton.start();
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        //考虑在这里启动Netty

        //run Netty
        runNetty(args);

    }
}

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

        final IJbxtActivityService iJbxtActivityServiceBeans = SpringUtil.getBean(IJbxtActivityService.class);
        final ActivityThreadManager activityMap = SpringUtil.getBean(ActivityThreadManager.class);
        final List<JbxtActivityDVO> lists = iJbxtActivityServiceBeans.findAll();
        for (JbxtActivityDVO jbxtActivityDVO : lists) {
            if (jbxtActivityDVO.getStatus() == 3) {
                final IJbxtGoodsService iJbxtGoodsService = SpringUtil.getBean(IJbxtGoodsService.class);
                final JbxtGoodsDO activeGoods = iJbxtGoodsService.selectActiveGoods(jbxtActivityDVO.getCode());
                if (activeGoods != null) {
                    final ActivityTask.Builder builder = new ActivityTask.Builder();

                    builder.activityCode(jbxtActivityDVO.getCode()).
                            goodsId(activeGoods.getGoodsId()).
                            initTime(activeGoods.getTimeNum() * 60).
                            supplierNum(jbxtActivityDVO.getSupplierNum()).
                            delayedCondition(activeGoods.getLastChangTime()).
                            allowDelayedLength(activeGoods.getPerDelayTime()).
                            allowDelayedTime(activeGoods.getDelayTimes());

                    ActivityTask activityTask = builder.build();
                    activityMap.put(jbxtActivityDVO.getCode(), activityTask);
                    activityThreadManager.doTask(activityTask);
                    LOGGER.info("启动 " + jbxtActivityDVO.getCode() + " 活动");
                }
            }
        }


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

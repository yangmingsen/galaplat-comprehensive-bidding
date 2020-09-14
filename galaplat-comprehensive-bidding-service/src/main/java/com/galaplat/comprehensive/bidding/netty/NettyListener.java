package com.galaplat.comprehensive.bidding.netty;

import com.galaplat.comprehensive.bidding.activity.ActivityInfoMap;
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
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebServerApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.List;

/**
 * 不再使用该类进行Netty启动, 从2.1.1后使用{@link MyApplicationRunner}启动
 *
 * @since 2.1.1
 */
//@Component
@Deprecated
public class NettyListener implements ApplicationListener<ContextRefreshedEvent> {


    private final Logger LOGGER = LoggerFactory.getLogger(NettyListener.class);

    @Autowired
    private WebSocketServer websocketServer;

    @Autowired
    private ActivityInfoMap activityInfoMap;

    @Autowired
    private ActivityThreadManager activityThreadManager;

    private boolean isInit = false;

    //@Autowired
    private  SpringUtil springUtil;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        System.out.println(event);
        boolean isTimeToRun = false;
        if (event.getApplicationContext().getClass().equals(AnnotationConfigServletWebServerApplicationContext.class)) {
            isTimeToRun = true;
        }
        if (!isInit && isTimeToRun) {
            websocketServer.start();
            LOGGER.info("onApplicationEvent(msg): WebSocket started");
            final ActivityService activityServiceBeans = springUtil.getBean(ActivityService.class);
            final ActivityThreadManager activityMap = springUtil.getBean(ActivityThreadManager.class);
            final List<ActivityDVO> lists = activityServiceBeans.findAll();
            for (ActivityDVO jbxtActivityDVO : lists) {
                if (jbxtActivityDVO.getStatus() == 3) {
                    final GoodsService goodsService = springUtil.getBean(GoodsService.class);
                    final GoodsDO activeGoods = goodsService.selectActiveGoods(jbxtActivityDVO.getCode());
                    if (activeGoods != null) {
                        //final ActivityThread currentActivity = new ActivityThread(jbxtActivityDVO.getCode(), activeGoods.getGoodsId().toString(), activeGoods.getTimeNum() * 60, 1);
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
//                        currentActivity.start();
                        LOGGER.info("启动 " + jbxtActivityDVO.getCode() + " 活动");
                    }

                }
            }

            QueueHandlerThreadSingleton queueHandlerThreadSingleton = QueueHandlerThreadSingleton.getInstance();
            queueHandlerThreadSingleton.start();

            this.isInit = true;
        }

    }
}

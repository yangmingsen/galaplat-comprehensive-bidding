package com.galaplat.comprehensive.bidding.netty;

import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.QueueHandler;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class NettyListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private WebSocketServer websocketServer;

    private boolean isInit = false;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (!isInit) {
            websocketServer.start();

//            IJbxtActivityService iJbxtActivityServiceBeans = SpringUtil.getBean(IJbxtActivityService.class);
//            List<JbxtActivityDVO> lists = iJbxtActivityServiceBeans.findAll();
//            for (int i = 0; i < lists.size(); i++) {
//                JbxtActivityDVO jbxtActivityDVO = lists.get(i);
//                if (jbxtActivityDVO.getStatus() == 3) {
//                    //执行线程
//                    new CurrentActivity(jbxtActivityDVO.getCode(),"g1",300,1);
//                }
//            }
            ActivityMap am = SpringUtil.getBean(ActivityMap.class);
            CurrentActivity tca = new CurrentActivity("1275271189644222464","11",600,1);
            am.put("1275271189644222464",tca);
            tca.start();

            QueueHandler queueHandler = new QueueHandler();
            queueHandler.start();

            this.isInit = true;
        }

        if(event.getApplicationContext().getParent() == null) {
            try {
                System.out.println("ws.start");
               //websocketServer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

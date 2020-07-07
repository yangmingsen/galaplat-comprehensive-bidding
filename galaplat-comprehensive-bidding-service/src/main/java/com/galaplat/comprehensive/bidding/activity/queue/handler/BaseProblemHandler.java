package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.AdminChannelMap;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;

public abstract class BaseProblemHandler implements ProblemHandler {

    protected UserChannelMap userChannelMap;
    protected AdminChannelMap adminChannel;
    protected PushQueue pushQueue;
    protected IJbxtGoodsService iJbxtGoodsService; //竞品服务
    protected ActivityMap activityMap;
    protected IJbxtUserService iJbxtUserService;
    protected IJbxtBiddingService iJbxtBiddingService;

    public BaseProblemHandler() {
        this.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
        this.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
        this.pushQueue = SpringUtil.getBean(PushQueue.class);
        this.iJbxtGoodsService = SpringUtil.getBean(IJbxtGoodsService.class);
        this.activityMap = SpringUtil.getBean(ActivityMap.class);
        this.iJbxtUserService = SpringUtil.getBean(IJbxtUserService.class);
        this.iJbxtBiddingService = SpringUtil.getBean(IJbxtBiddingService.class);
    }

    @Override
    public void problem(int type, QueueMessage queuemsg) {
        this.handlerProblem(type,queuemsg);
    }

    public abstract void handlerProblem(int type, QueueMessage queuemsg);

}

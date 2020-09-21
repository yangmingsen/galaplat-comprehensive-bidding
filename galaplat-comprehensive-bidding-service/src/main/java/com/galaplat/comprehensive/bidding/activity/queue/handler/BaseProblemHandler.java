package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityThreadManager;
import com.galaplat.comprehensive.bidding.netty.channel.AdminChannelMap;
import com.galaplat.comprehensive.bidding.netty.channel.AdminInfo;
import com.galaplat.comprehensive.bidding.activity.queue.MessageQueue;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.netty.channel.ChannelComposite;
import com.galaplat.comprehensive.bidding.netty.channel.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.ResponseMessage;
import com.galaplat.comprehensive.bidding.service.ActivityService;
import com.galaplat.comprehensive.bidding.service.BiddingService;
import com.galaplat.comprehensive.bidding.service.GoodsService;
import com.galaplat.comprehensive.bidding.service.UserService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <h2>抽象问题处理者</h2>
 * <p>在这个类中,我们定义好问题处理这的大致骨架。</p>
 * <p>关于处理问题的的细节我们交给它的子类,例如 {@link SupplierOutProblemHandler}</p>
 * <p>子类需要实现{@link BaseProblemHandler#handlerProblem(int, QueueMessage)}. 这是一个模板方法</p>
 */
public abstract class BaseProblemHandler implements ProblemHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(BaseProblemHandler.class);
    protected final UserChannelMap userChannelMap;
    protected final AdminChannelMap adminChannel;
    protected final MessageQueue messageQueue;
    protected final GoodsService goodsService; //竞品服务
    protected final ActivityService activityService;
    protected final ActivityThreadManager activityManager;
    protected final UserService userService;
    protected final BiddingService biddingService;
    protected final ChannelComposite channelComposite;

    public BaseProblemHandler() {
        this.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
        this.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
        this.messageQueue = SpringUtil.getBean(MessageQueue.class);
        this.goodsService = SpringUtil.getBean(GoodsService.class);
        this.activityManager = SpringUtil.getBean(ActivityThreadManager.class);
        this.userService = SpringUtil.getBean(UserService.class);
        this.biddingService = SpringUtil.getBean(BiddingService.class);
        this.activityService = SpringUtil.getBean(ActivityService.class);
        this.channelComposite = SpringUtil.getBean(ChannelComposite.class);
    }

    @Override
    public final void problem(int type, QueueMessage queueMessage) {
        beforeDealWith(type, queueMessage);
        this.handlerProblem(type, queueMessage);
        afterDealWith(type, queueMessage);
    }

    /***
     * 由子类去实现如何处理
     * @param type
     * @param queueMessage
     */
    public abstract void handlerProblem(int type, QueueMessage queueMessage);

    /**
     * 你可以提前做一些事情
     * @param type
     * @param queueMessage
     */
    protected void beforeDealWith(int type, QueueMessage queueMessage) {
        //Let's hand it over to the subclass
    }

    /**
     * 你可以之后做一些事情
     * @param type
     * @param queueMessage
     */
    protected void afterDealWith(int type, QueueMessage queueMessage) {
        //Let's hand it over to the subclass
    }


    /***
     *  推数据流到所有管理端
     * @param message
     * @param activityCode
     */
    protected void notifyAllAdmin(ResponseMessage message, String activityCode) {
        channelComposite.notifyAllAdmin(message,activityCode);
    }

    /***
     * 推送数据到指定管理端
     * @param message
     * @param activityCode
     * @param adminCode
     */
    protected void notifyOptionAdmin(ResponseMessage message, String activityCode, String adminCode) {
        channelComposite.notifyOptionAdmin(message,adminCode);
    }


    /***
     *  推数据流到所有供应商端（适用于所有供应商发同一个数据）
     * @param message
     * @param activityCode
     */
    protected void notifyAllSupplier(ResponseMessage message, String activityCode) {
        channelComposite.notifyAllSupplier(message,activityCode);
    }

    /***
     * 推送数据到指定供应商端
     * @param message
     * @param activityCode
     * @param userCode
     */
    protected void notifyOptionSupplier(ResponseMessage message, String activityCode, String userCode) {
       channelComposite.notifyAllSupplier(message,userCode);
    }

}

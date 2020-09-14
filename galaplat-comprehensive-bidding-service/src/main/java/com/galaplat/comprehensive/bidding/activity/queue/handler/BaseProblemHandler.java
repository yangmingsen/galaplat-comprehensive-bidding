package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityThreadManager;
import com.galaplat.comprehensive.bidding.netty.channel.AdminChannelMap;
import com.galaplat.comprehensive.bidding.netty.channel.AdminInfo;
import com.galaplat.comprehensive.bidding.activity.queue.MessageQueue;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.netty.channel.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.ResponseMessage;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
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
    protected final IJbxtGoodsService goodsService; //竞品服务
    protected final ActivityThreadManager activityManager;
    protected final IJbxtUserService userService;
    protected final IJbxtBiddingService biddingService;

    public BaseProblemHandler() {
        this.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
        this.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
        this.messageQueue = SpringUtil.getBean(MessageQueue.class);
        this.goodsService = SpringUtil.getBean(IJbxtGoodsService.class);
        this.activityManager = SpringUtil.getBean(ActivityThreadManager.class);
        this.userService = SpringUtil.getBean(IJbxtUserService.class);
        this.biddingService = SpringUtil.getBean(IJbxtBiddingService.class);
    }

    @Override
    public final void problem(int type, QueueMessage queueMsg) {
        this.handlerProblem(type,queueMsg);
    }

    /***
     * 由子类去实现如何处理
     * @param type
     * @param queuemsg
     */
    public abstract void handlerProblem(int type, QueueMessage queuemsg);


    /***
     *  推数据流到所有管理端
     * @param message
     * @param activityCode
     */
    protected void notifyAllAdmin(ResponseMessage message, String activityCode) {
        adminChannel.getAllAdmin().forEach(adminCode -> notifyOptionAdmin(message, activityCode, adminCode));
    }

    /***
     * 推送数据到指定管理端
     * @param message
     * @param activityCode
     * @param adminCode
     */
    protected void notifyOptionAdmin(ResponseMessage message, String activityCode, String adminCode) {
        LOGGER.info("notifyOptionAdmin(msg): activityCode="+activityCode+" adminCode="+adminCode+" message="+message);
        AdminInfo adminInfo = adminChannel.get(adminCode);
        if (adminInfo.getFocusActivity().equals(activityCode)) {
            //推数据到管理端
            adminInfo.getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }


    /***
     *  推数据流到所有供应商端（适用于所有供应商发同一个数据）
     * @param message
     * @param activityCode
     */
    protected void notifyAllSupplier(ResponseMessage message, String activityCode) {
        userChannelMap.getAllUser().forEach(supplier -> notifyOptionSupplier(message, activityCode, supplier));
    }

    /***
     * 推送数据到指定供应商端
     * @param message
     * @param activityCode
     * @param userCode
     */
    protected void notifyOptionSupplier(ResponseMessage message, String activityCode, String userCode) {
        LOGGER.info("notifyOptionAdmin(msg): activityCode="+activityCode+" userCode="+userCode+" message="+message);
        if (userChannelMap.getUserFocusActivity(userCode).equals(activityCode)) {
            userChannelMap.get(userCode).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }

}

package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.AdminChannelMap;
import com.galaplat.comprehensive.bidding.activity.AdminInfo;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

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
    public void problem(int type, QueueMessage queueMsg) {
        this.handlerProblem(type,queueMsg);
    }

    public abstract void handlerProblem(int type, QueueMessage queuemsg);


    /***
     *  推数据流到所有管理端
     * @param message
     * @param activityCode
     */
    protected void notifyAllAdmin(Message message, String activityCode) {
        adminChannel.getAllAdmin().forEach(adminCode -> notifyOptionAdmin(message, activityCode, adminCode));
    }

    /***
     * 推送数据到指定管理端
     * @param message
     * @param activityCode
     * @param adminCode
     */
    protected void notifyOptionAdmin(Message message, String activityCode, String adminCode) {
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
    protected void notifyAllSupplier(Message message, String activityCode) {
        userChannelMap.getAllUser().forEach(supplier -> notifyOptionSupplier(message, activityCode, supplier));
    }

    /***
     * 推送数据到指定供应商端
     * @param message
     * @param activityCode
     * @param userCode
     */
    protected void notifyOptionSupplier(Message message, String activityCode, String userCode) {
        if (userChannelMap.getUserFocusActivity(userCode).equals(activityCode)) {
            userChannelMap.get(userCode).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }

}

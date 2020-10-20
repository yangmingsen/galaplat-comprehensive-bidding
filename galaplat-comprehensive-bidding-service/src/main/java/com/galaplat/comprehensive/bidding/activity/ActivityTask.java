package com.galaplat.comprehensive.bidding.activity;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.queue.MessageQueue;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.BiddingDO;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.BiddingDVO;
import com.galaplat.comprehensive.bidding.netty.channel.AdminChannelMap;
import com.galaplat.comprehensive.bidding.netty.channel.AdminInfo;
import com.galaplat.comprehensive.bidding.netty.channel.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.ResponseMessage;
import com.galaplat.comprehensive.bidding.service.ActivityService;
import com.galaplat.comprehensive.bidding.service.BiddingService;
import com.galaplat.comprehensive.bidding.service.GoodsService;
import com.galaplat.comprehensive.bidding.service.impl.JbxtBiddingServiceImpl;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <h3>一些简介和建议</h3>
 * <p>1.这是竞品线程，用于控制竞品状态，剩余时间，排名，延时通知，榜首更新等...</p>
 * <p>2.它使用构造模式进行实例创建，不可直接使用new来创建实例，这是必须要注意的</p>
 * <p>3.这里面逻辑比较复杂的便是{@link ActivityTask#handleRank()} 这个方法，规则很多，需要着重理解</p>
 * <p>4.你需要明白竞品线程有4个状态{@link ActivityTask#getStatus()}</p>
 * <p>5.控制线程暂停采用的是锁机制，你可以看{@link java.util.concurrent.ArrayBlockingQueue}的offer 和 take 方法的源码来理解，就是这样的原理</p>
 *
 */
public class ActivityTask implements Runnable {

    //---------v2.0
    private Logger LOGGER;
    private ResponseMessage remainingTimeMessage;
    private UserChannelMap userChannelMap;
    private AdminChannelMap adminChannel;
    private GoodsService goodsService;
    private BiddingService biddingService;
    private ReentrantLock lock;
    private Condition continueRun;

    private MessageQueue messageQueue;
    private String currentActivityCode;
    private Integer currentGoodsId;
    private int initTime; //初始化时间 秒
    /**
     * 1 进行 2暂停  3//重置 4 结束
     */
    private volatile int status; //默认为进行中
    private int remainingTime;  //剩余时间 秒
    private Map<String, String> t_map;


    //-----------v2.1.1
    private int supplierNum;
    //排名Map
    private volatile Map<String, Integer> lastRankInfoMap ; // supplier -> rank
    private volatile Map<String, Double> lastBidPriceMap; // supplierCode -> BidPrice
    //竞价并列表Map

    private int disappearTime; //过去了多长时间 秒
    private int delayedCondition; //延时条件 秒
    private int allowDelayedLength; //单次延时长度 秒
    private int allowDelayedTime; //允许延迟次数 初始化时默认与initAllowDelayedTime相等 之后的过程中如果发送延时会自动递减
    private boolean thisGoodsAllowDelayed; //这个竞品是否允许延时 默认允许
    private int initAllowDelayedTime; //初始化允许的延迟次数
    private volatile boolean remainingTimeType; //现在是否是延时时间 默认为false标识不是延时时间
    private volatile int lastRemainingTime;// 记录上一次发生延迟的时间
    private int bidType; //竞价方式 1为数值竞价 2为幅度竞价

    public int getDelayedCondition() {
        return delayedCondition;
    }

    /**
     * 单次延时长度 秒
     *
     * @return
     */
    public int getAllowDelayedLength() {
        return allowDelayedLength;
    }

    /**
     * 获取目前还剩余延迟的次数
     *
     * @return
     */
    public int getAllowDelayedTime() {
        return allowDelayedTime;
    }

    /**
     * 初始化允许的延迟次数
     *
     * @return
     */
    public int getInitAllowDelayedTime() {
        return initAllowDelayedTime;
    }

    public Integer getSupplierNum() {
        return supplierNum;
    }

    public int getBidType() {
        return bidType;
    }

    /**
     * 设置当前活动剩余时长（秒）
     *
     * @param remainingTime
     */
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }


    public Integer getCurrentGoodsId() {
        return currentGoodsId;
    }

    /**
     * 获取剩余时间 （秒）
     *
     * @return
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    /**
     * 获取当前延时时间 str
     * <p> 当现在处于延时时间时，调用该方法</p>
     *
     * @return
     */
    public String getDelayRemainingTimeString() {
        final boolean haveRemainingTime = isDelayedTime(); //如果消失的时间大于等于初始化时间，那么意味着可以使用延迟时间了
        final int delayTime = haveRemainingTime ? this.computeCurrentDelayTime() : this.initTime - this.disappearTime;

        return this.doTransferTimeStr(delayTime);
    }

    /**
     * true 为表示进入了延迟时间
     * false表示为正常时间
     * @return
     */
    public boolean isDelayedTime() {
        return this.disappearTime > this.initTime;
    }


    /**
     * 获取当前剩余时间（字符串方式，例如 "15:34"）
     *
     * @return
     */
    public String getRemainingTimeString() {
        int ttime = this.remainingTime;
        return this.doTransferTimeStr(ttime);
    }

    /**
     * 业务启动入口
     */
    public void run() {
        try {
            this.startRemainingTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前剩余时间（秒）
     *
     * @return
     */
    public int getInitTime() {
        return initTime;
    }

    /**
     * //1 进行 2暂停  3//重置 4 结束
     *
     * @return
     */
    public int getStatus() {
        return status;
    }



    /**
     * 设置 当前竞品活动状态
     *
     * @param status 1 进行 2暂停  3重置  4结束(不可手动设置)
     */
    public void setStatus(int status) {
        if (this.status == status) return; //处理相同status设置

        this.status = status;
        if (this.status != 2) {

            final ReentrantLock lock = this.lock;
            try {
                lock.lock();
                this.continueRun.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            if (this.status == 3) {
                this.resetActivity();
                LOGGER.info("setStatus(INFO): 已重置活动【"+currentActivityCode+"】数据");
            }

            //通知供应商端 继续
            final Map<String, String> map = new HashMap<>();
            map.put("activityCode", this.currentActivityCode);
            map.put("goodsId", this.currentGoodsId.toString());
            map.put("status", "1");
            final QueueMessage queueMessage = new QueueMessage(215, map);
            messageQueue.offer(queueMessage);
        }
    }


    /**
     * 判断是否发生了延迟
     * 根据初始化延迟时间和已经消耗的次数来判断
     *
     * @return
     */
    public Boolean isTriggerDelayed() {
        return this.initAllowDelayedTime != this.allowDelayedTime;
    }

    /**
     * 获取剩余时间类型：
     * true: 延迟时间
     * false: 正常时间
     *
     * @return
     */
    public Boolean isRealAccessDealyedTime() {
        return remainingTimeType;
    }

    /**
     * 内部构建器
     */
    public static class Builder {
        private final ActivityTask activityTask = new ActivityTask();

        /**
         * 初始化所有活动线程数据
         */
        public Builder() {
            activityTask.LOGGER = LoggerFactory.getLogger(ActivityTask.class);
            activityTask.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
            activityTask.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
            activityTask.goodsService = SpringUtil.getBean(GoodsService.class);
            activityTask.messageQueue = SpringUtil.getBean(MessageQueue.class);
            activityTask.biddingService = SpringUtil.getBean(JbxtBiddingServiceImpl.class);
            activityTask.remainingTimeMessage = new ResponseMessage(100, null);
            activityTask.t_map = new HashMap<>();
            activityTask.disappearTime = 0;
            activityTask.bidType = 1;
            activityTask.status = 1;
            activityTask.lock = new ReentrantLock();
            activityTask.continueRun = activityTask.lock.newCondition();
            activityTask.lastBidPriceMap = new HashMap<>();
            activityTask.lastRankInfoMap = new HashMap<>();
            activityTask.remainingTimeType = false;
            activityTask.lastRemainingTime = 0;
        }

        /**
         * 设置当前活动供应商数
         *
         * @param num
         * @return
         */
        public Builder supplierNum(int num) {
            this.activityTask.supplierNum = num;
            return this;
        }

        public Builder activityCode(String activityCode) {
            this.activityTask.currentActivityCode = activityCode;
            return this;
        }

        /**
         * 竞价方式 1为数值竞价 2为幅度竞价
         * @param bidType
         * @return
         */
        public Builder bidType(Integer bidType) {
            this.activityTask.bidType = bidType;
            return this;
        }

        /**
         * 设置当前goodsId
         *
         * @param goodsId
         * @return
         */
        public Builder goodsId(Integer goodsId) {
            this.activityTask.currentGoodsId = goodsId;
            return this;
        }

        /**
         * 设置活动初始化时间
         *
         * @param initTime
         * @return
         */
        public Builder initTime(Integer initTime) {
            this.activityTask.initTime = initTime;
            this.activityTask.remainingTime = initTime;
            return this;
        }

        /**
         * 设置延迟条件
         *
         * @param delayedCondition
         * @return
         */
        public Builder delayedCondition(Integer delayedCondition) {
            if (delayedCondition < 1) {
                this.activityTask.thisGoodsAllowDelayed = false;
            }
            this.activityTask.delayedCondition = delayedCondition;
            return this;
        }

        /**
         * 设置延迟时长
         *
         * @param allowDelayedLength
         * @return
         */
        public Builder allowDelayedLength(Integer allowDelayedLength) {
            if (allowDelayedLength < 1) {
                this.activityTask.thisGoodsAllowDelayed = false;
            }
            this.activityTask.allowDelayedLength = allowDelayedLength;
            this.activityTask.thisGoodsAllowDelayed = true;
            return this;
        }

        /**
         * 设置延迟次数
         *
         * @param allowDelayedTime
         * @return
         */
        public Builder allowDelayedTime(Integer allowDelayedTime) {
            if (allowDelayedTime < 1) {
                this.activityTask.thisGoodsAllowDelayed = false;
            }

            this.activityTask.allowDelayedTime = allowDelayedTime;
            this.activityTask.initAllowDelayedTime = allowDelayedTime;
            return this;
        }

        public ActivityTask build() {
            return this.activityTask;
        }

    }

    /**
     * 处理排名
     */
    public void handleRank() {
        boolean needDeedDelayed = false;
        boolean needNotifyTopUpdate = false;
        Map<Double, Integer> tmpRankMap = new HashMap<>();
        List<BiddingDVO> minBidList = biddingService.selectMinBidTableBy(currentGoodsId, currentActivityCode);
        int len = minBidList.size();
        Map<String, Integer> tmpRankInfoMap = new HashMap<>();


        //计算排名
        for (int i = 0; i < len; i++) { //计算排名 价格=>排名
            BiddingDVO oneBid = minBidList.get(i);
            Double bidPrice = oneBid.getBid().doubleValue();

            final int idx = i;
            tmpRankMap.computeIfAbsent(bidPrice, k -> idx + 1);
        }

        //{bidPrice -> { supplierCode -> rankInfo}}
        Map<Double, Map<String, RankInfo>> rankInfoMap = new HashMap<>();
        ////判断是否需要延时 //更新top棒
        for (BiddingDVO oneBid : minBidList) {
            String supplierCode = oneBid.getUserCode();
            Double bidPrice = oneBid.getBid().doubleValue();

            Integer lastRankPosition = lastRankInfoMap.get(supplierCode);
            Integer newRankPostion = tmpRankMap.get(bidPrice);
            if (lastRankPosition == null) { //判断当前供应商是不是第一次竞价
                //如果是第一次竞价
                if (newRankPostion >= 1 && newRankPostion <= 3) {
                    if (newRankPostion == 1) {
                        needNotifyTopUpdate = true;
                    }
                    if (thisGoodsAllowDelayed) {
                        needDeedDelayed = decideDelayed(needDeedDelayed);
                    }
                }
            } else {
                if (lastRankPosition > 1 && newRankPostion == 1) { //可以通知榜首更新
                    needNotifyTopUpdate = true;
                }

                if (bidPrice.compareTo(lastBidPriceMap.get(supplierCode)) < 0) {
                    //上一把自己和其他人是第一名 然后 想争第一 ， 当他变为新第一名，其他人的排名往后挪时。需要通知榜首更新
                    if (isParataxis(supplierCode)) {
                        if (lastRankInfoMap.get(supplierCode) == 1) {
                            needNotifyTopUpdate = true;
                        }
                    }
                }

                if (thisGoodsAllowDelayed) {
                    if (lastRankPosition >= 1 && lastRankPosition <= 3) { //上一次位置在(1,3]; 加 ‘=’ 是为了处理多个第一名并列情况，然后其中某个人重新提交了最低价情况
                        //判断当前报价是否与上次一样 如果一样不需要计算
                        //如果不一样且比上一次小
                        //判断上一次报价排名和当前报价排名是否一样
                        //如果一样
                        //第一种可能是第一名自己重新提交了最低价；
                        //还有种可能是上次排名中当前供应商处于并列状态，那么此时如果提交了最新报价那么会使它进入新的排名
                        //这两种情况都是需要判断是否延时
                        if (bidPrice.compareTo(lastBidPriceMap.get(supplierCode)) < 0) {
                            if (newRankPostion < lastRankPosition) {
                                needDeedDelayed = decideDelayed(needDeedDelayed);
                            }

                            if (isParataxis(supplierCode)) {
                                needDeedDelayed = decideDelayed(needDeedDelayed);
                            }

                        }

                    } else if (lastRankPosition > 3) {// 上一次位置在 (3: +..)
                        if (newRankPostion <= 3) {
                            needDeedDelayed = decideDelayed(needDeedDelayed);
                        }
                    }
                }

            }
            tmpRankInfoMap.put(supplierCode, newRankPostion);
            lastBidPriceMap.put(supplierCode, bidPrice);

            //封装各个供应商排名信息
            if (rankInfoMap.get(bidPrice) != null) {
                RankInfo rankInfo = new RankInfo(supplierCode, bidPrice, newRankPostion);
                rankInfoMap.get(bidPrice).put(supplierCode, rankInfo);
            } else {
                Map<String, RankInfo> newRankMap = new HashMap<>();
                RankInfo rankInfo = new RankInfo(supplierCode, bidPrice, newRankPostion);
                newRankMap.put(supplierCode, rankInfo);
                rankInfoMap.put(bidPrice, newRankMap);
            }

        }
        //更新
        //this.bidParaxtisInfoMap = rankInfoMap;
        this.copyLastRankInfo(tmpRankInfoMap, lastRankInfoMap);

        //是否通知延时
        if (needDeedDelayed) {
            //do other after needDeedDelayed
            final Integer delayedLength = this.allowDelayedLength;
            final Integer remainingTime = this.lastRemainingTime;//this.doTransferTimeStr(this.lastRemainingTime);
            final Integer allowDelayedTime = this.initAllowDelayedTime;
            ResponseMessage responseMessage = new ResponseMessage(120, new HashMap<String, Object>() {{
                put("delayedLength", delayedLength);
                put("remainingTime", remainingTime);
                put("allowDelayedTime", allowDelayedTime);
            }});
            notifyAllSupplier(responseMessage, this.currentActivityCode);
            notifyAllAdmin(responseMessage, this.currentActivityCode);
        }

        //是否通知榜首个更新
        if (needNotifyTopUpdate) {
            // do needNotifyTopUpdate
            final String activityCode = this.currentActivityCode;
            final String goodsId = this.currentGoodsId.toString();
            ResponseMessage responseMessage = new ResponseMessage(111, new HashMap<String, Object>() {{
                put("activityCode", activityCode);
                put("goodsId", goodsId);
            }});
            notifyAllSupplier(responseMessage, activityCode);
        }


        //推送各个供应商排名信息
        for (Map.Entry<Double, Map<String, RankInfo>> priceGroup : rankInfoMap.entrySet()) {
            Map<String, RankInfo> supplierRankMap = priceGroup.getValue();
            boolean isParataxis = supplierRankMap.size() > 1;
            for (Map.Entry<String, RankInfo> supplierRank : supplierRankMap.entrySet()) {
                RankInfo rankInfo = supplierRank.getValue();
                final String supplierCode = supplierRank.getKey();
                final Double goodsPrice = rankInfo.getBidPrice();
                final String goodsId = this.currentGoodsId.toString();
                final String activityCode = this.currentActivityCode;
                final Integer userRank = rankInfo.getRank();
                final Boolean parataxis = isParataxis ? Boolean.TRUE : Boolean.FALSE;
                final boolean needSendBidPercent = this.bidType == 2;
                ResponseMessage responseMessage = new ResponseMessage(200, new HashMap<String, Object>() {{
                    put("goodsPrice", goodsPrice);
                    put("goodsId", goodsId);
                    put("userRank", userRank);
                    put("parataxis", parataxis.toString());
                    if (needSendBidPercent) {
                        BiddingDO minBidRecord = biddingService.selectMinBidTableBy(supplierCode, new Integer(goodsId), activityCode);
                        put("bidPercent", minBidRecord.getBidPercent());
                    }
                }});


                notifyOptionSupplier(responseMessage, activityCode, supplierCode);
            }
        }

    }

    /**
     * 计算当前的延时时间
     * <p>注意: 该方法应当在活动进入延时时间时调用</p>
     *
     * @return
     */
    private int computeCurrentDelayTime() {
        final boolean remainingTimeType = this.remainingTimeType;
        if (remainingTimeType) {
            return this.disappearTime - this.initTime;
        }
        return 0;
    }

    private ActivityTask() {
    }

    /**
     * 时间转换
     * <p>such as: 秒 -> 14:04</p>
     *
     * @param sec
     * @return
     */
    private String doTransferTimeStr(int sec) {
        int minute = sec / 60;
        int second = sec % 60;
        StringBuilder stringBuilder = new StringBuilder();
        if (minute < 10) {
            stringBuilder.append("0").append(minute);
        } else {
            stringBuilder.append(minute);
        }
        stringBuilder.append(":");
        if (second < 10) {
            stringBuilder.append("0").append(second);
        } else {
            stringBuilder.append(second);
        }

        return stringBuilder.toString();
    }

    /**
     * 重置活动
     */
    private void resetActivity() {

        this.status = 1; //重置状态
        this.remainingTime = this.initTime; //重置剩余时间
        this.disappearTime = 0; //重置总消耗时长为0
        this.remainingTimeType = false;//重置剩余时间为false
        this.allowDelayedTime = this.initAllowDelayedTime; //重置允许的延迟次数
        this.lastRankInfoMap = new HashMap<>();//重置历史排名榜
        this.lastRemainingTime = 0;
        this.thisGoodsAllowDelayed = true;

        final Integer goodsId = this.currentGoodsId;
        final String activityCode = this.currentActivityCode;

        //重置db当前竞品的延迟过了次数
        JbxtGoodsVO newGoodsVO = new JbxtGoodsVO();
        newGoodsVO.setGoodsId(goodsId);
        newGoodsVO.setAddDelayTimes(0);
        goodsService.updateJbxtGoods(newGoodsVO);

        //删除所有用户的历史竞价数据(min_bid表和bidding表)
        biddingService.deleteByGoodsIdAndActivityCode(goodsId, activityCode);
        biddingService.deleteMinbidTableByGoodsIdAndActivityCode(goodsId, activityCode);

        //向所有供应商端发送重置通知
        //同步数据（管理端 N，供应商端 Y）
        final Map<String, String> map = new HashMap<>();
        map.put("activityCode", activityCode);
        map.put("goodsId", goodsId.toString());
        final QueueMessage queueMessage = new QueueMessage(212, map);
        messageQueue.offer(queueMessage);
    }

    /**
     * 启动剩余时长计算
     *
     * @throws InterruptedException
     */
    private void startRemainingTime() throws InterruptedException {
        this.syncInfoToAll();
        while (remainingTime > -1) {
            if (status == 2) {

                //通知供应商端 暂停
                Map<String, String> map = new HashMap<>();
                map.put("activityCode", this.currentActivityCode);
                map.put("goodsId", this.currentGoodsId.toString());
                map.put("status", "3");
                QueueMessage queueMessage = new QueueMessage(215, map);
                messageQueue.offer(queueMessage);

                this.whenAcitvityPause();

                final ReentrantLock lock = this.lock;
                try {
                    lock.lockInterruptibly();
                    continueRun.await();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
            this.sendRemainingTimeToAll();
            Thread.sleep(1000);
            remainingTime--; //剩余时间减少一秒
            this.disappearTime++; //过去了多长时间

            //判断现在是否为延迟时间
            this.remainingTimeType = this.disappearTime > this.initTime;
        }
//        if (isfinallyGoods()) {
//            endTheCurrentGoodsActivity();
//            autoCloseCurrentActivity();
//        }
    }

    /**
     * 发送剩余时长到所有通道（supplier,admin）
     */
    private void sendRemainingTimeToAll() {
        final String remainingTimeString = getRemainingTimeString();
        t_map.put("remainingTime", remainingTimeString);
        remainingTimeMessage.setData(t_map);

        final String activityCode = this.currentActivityCode;
        notifyAllSupplier(remainingTimeMessage, activityCode);

        if (this.isTriggerDelayed()) { //当处于延迟时间时
            String delayTimeString = this.getDelayRemainingTimeString();
            int delay = this.disappearTime > this.initTime ? 2 : 1;
            t_map.put("delay", Integer.toString(delay));
            // t_map.put("remainingTime", delayTimeString);
            //remainingTimeMessage.setData(t_map);
        }
        notifyAllAdmin(remainingTimeMessage, activityCode);
    }

    /**
     * 推数据流到所有管理端
     *
     * @param message
     * @param activityCode
     */
    private void notifyAllAdmin(ResponseMessage message, String activityCode) {
        adminChannel.getAllAdmin().forEach(adminCode -> notifyOptionAdmin(message, activityCode, adminCode));
    }

    /**
     * 推送数据到指定管理端
     *
     * @param message
     * @param activityCode
     * @param adminCode
     */
    private void notifyOptionAdmin(ResponseMessage message, String activityCode, String adminCode) {
        AdminInfo adminInfo = adminChannel.get(adminCode);
        if (adminInfo.getFocusActivity().equals(activityCode)) {
            //推数据到管理端
            adminInfo.getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }

    private void notifyAllSupplier(ResponseMessage message, String activityCode) {
        userChannelMap.getAllUser().forEach(supplier -> notifyOptionSupplier(message, activityCode, supplier));
    }

    private void notifyOptionSupplier(ResponseMessage message, String activityCode, String userCode) {
        if (userChannelMap.getUserFocusActivity(userCode).equals(activityCode)) {
            userChannelMap.get(userCode).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
        }
    }

    /**
     * 自动结束当前活动
     */
    private void endTheCurrentGoodsActivity() {
        final GoodsDO jbxtGoodsDO = goodsService.selectByGoodsId(currentGoodsId);
        if (jbxtGoodsDO != null) {
            JbxtGoodsVO jbxtgoodsVO = new JbxtGoodsVO();
            jbxtgoodsVO.setGoodsId(currentGoodsId);
            jbxtgoodsVO.setStatus("2");

            try {
                goodsService.updateJbxtGoods(jbxtgoodsVO);
                LOGGER.info("endTheCurrentGoodsActivity(msg): 活动: " + currentActivityCode + " 竞品id: " + currentGoodsId + " 结束");
            } catch (Exception e) {
                LOGGER.info("endTheCurrentGoodsActivity(ERROR): " + e.getMessage());
            }
//            this.status = 4;
        }
    }

    /**
     * 判断当前竞品是否为当前活动的最后一个竞品
     *
     * @return
     */
    private boolean isfinallyGoods() {
        final String activitCode = this.currentActivityCode;
        final Integer goodsId = this.currentGoodsId;
        final List<SimpleGoodsVO> goods = goodsService.findAll(activitCode);
        final int goodsLength = goods.size();
        if (goodsLength > 0) {
            final int endGoodsIdx = goodsLength - 1;
            if (goodsId == goods.get(endGoodsIdx).getGoodsId()) {
                return true;
            }
        }

        return false;
    }

    private void autoCloseCurrentActivity() {
        final ActivityService activityService = SpringUtil.getBean(ActivityService.class);
        final String activityCode = this.currentActivityCode;
        final ActivityDO activity = activityService.findOneByCode(activityCode);

        if (activity != null) {
            if (activity.getStatus() == 3) {
                final ActivityDO tActivity = new ActivityDO();
                tActivity.setCode(activityCode);
                tActivity.setStatus(4);
                boolean updateActivityStatus = false;

                try {
                    activityService.updateByPrimaryKeySelective(tActivity);
                    updateActivityStatus = true;
                } catch (Exception e) {
                    LOGGER.info("switchActivityGoods(ERROR): 更新活动(" + activityCode + ")结束结束失败");
                }

                if (updateActivityStatus) {
                    //通知所有供应商端 退出登录
                    notify216Event(activityCode);
                }
            }
        }
    }

    private void notify216Event(String activityCode) {
        final Map<String, String> map216 = new HashMap();
        map216.put("activityCode", activityCode);
        messageQueue.offer(new QueueMessage(216, map216));
    }

    /**
     * 同步数据给所有供应商端
     */
    private void syncInfoToAll() {
        //{type: 200, data: {goodsId: 23423, activityCode: 23423345}}
        final Map<String, String> supplierInfo = new HashMap<>();
        supplierInfo.put("goodsId", this.currentGoodsId.toString());
        supplierInfo.put("activityCode", this.currentActivityCode);
        //同步数据 给 供应商
        final QueueMessage queueMessage = new QueueMessage(200, supplierInfo);
        messageQueue.offer(queueMessage);

    }

    /**
     * 当活动处于暂停时 同步暂停后最后一次时间
     */
    private void whenAcitvityPause() {
        this.sendRemainingTimeToAll();
    }


    /**
     * 决策是否延迟
     *
     * @return 是否延迟结果
     */
    private boolean decideDelayed(Boolean continueCompute) {
        if (continueCompute) return true;

        final int remainingTime = this.remainingTime;
        final int delayedCondition = this.delayedCondition;
        final int allowDelayedTime = this.allowDelayedTime;
        if (remainingTime < delayedCondition && allowDelayedTime > 0) {
            final int allowDelayedLength = this.allowDelayedLength;
            this.lastRemainingTime = this.remainingTime;
            this.remainingTime += allowDelayedLength;
            this.allowDelayedTime--;

            final int delayedNum = this.initAllowDelayedTime - this.allowDelayedTime;
            //保存状态到数据库中
            JbxtGoodsVO newGoodsVO = new JbxtGoodsVO();
            newGoodsVO.setGoodsId(this.currentGoodsId);
            newGoodsVO.setAddDelayTimes(delayedNum);
            goodsService.updateJbxtGoods(newGoodsVO);

            //判断如果当前是暂停状态 那么同步最后剩余时间
            if (this.status == 2) {
                this.whenAcitvityPause();
            }

            return true;
        }

        return false;
    }


    /**
     * 排名信息类
     */
    private static class RankInfo {
        private String id; //供应商id
        private Double bidPrice; //供应商竞价
        private Integer rank; //排名

        public RankInfo(String id, Double bidPrice, Integer rank) {
            this.id = id;
            this.bidPrice = bidPrice;
            this.rank = rank;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Double getBidPrice() {
            return bidPrice;
        }

        public void setBidPrice(Double bidPrice) {
            this.bidPrice = bidPrice;
        }

        public Integer getRank() {
            return rank;
        }

        public void setRank(Integer rank) {
            this.rank = rank;
        }
    }

    /**
     * 判断当前供应商是否具有并列排名
     *
     * @param supplierCode
     * @return
     */
    public Boolean isParataxis(String supplierCode) {
        final Map<String, Integer> map = this.lastRankInfoMap;
        Integer curSupplierrank = map.get(supplierCode);
        if (curSupplierrank == null) return false;

        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            //如果当前排名相同但是账号不同就表明有并列
            if (entry.getValue().equals(curSupplierrank) && !(entry.getKey().equals(supplierCode))) {
                return true;
            }
        }

        return false;
    }

    private void copyLastRankInfo(Map<String, Integer> originRankMap, Map<String, Integer> targetRankMap) {
        for (Map.Entry<String, Integer> entry : originRankMap.entrySet()) {
            targetRankMap.put(entry.getKey(), entry.getValue());
        }
    }


}

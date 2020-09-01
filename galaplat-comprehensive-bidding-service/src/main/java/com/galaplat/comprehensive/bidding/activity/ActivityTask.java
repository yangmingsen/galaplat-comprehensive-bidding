package com.galaplat.comprehensive.bidding.activity;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.queue.MessageQueue;
import com.galaplat.comprehensive.bidding.activity.queue.msg.ObjectQueueMessage;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.netty.channel.AdminChannelMap;
import com.galaplat.comprehensive.bidding.netty.channel.AdminInfo;
import com.galaplat.comprehensive.bidding.netty.channel.UserChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.ResponseMessage;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ActivityTask implements Runnable {

    private Logger LOGGER;
    private ResponseMessage remainingTimeMessage;
    private UserChannelMap userChannelMap;
    private AdminChannelMap adminChannel;
    private IJbxtGoodsService iJbxtGoodsService;
    private ReentrantLock lock;
    private Condition continueRun;
    private MessageQueue messageQueue;
    private String currentActivityCode;
    private Integer currentGoodsId;
    private int initTime; //初始化时间 秒
    private int status;//1 进行 2暂停  3//重置 4 结束
    private int remainingTime;  //剩余时间 秒
    private int disappearTime; //过去了多长时间 秒
    private int delayedCondition; //延时条件 秒
    private int allowDelayedLength; //单次延时长度 秒
    private int allowDelayedTime; //允许延迟次数
    private int initAllowDelayedTime; //
    private boolean remainingTimeType = false; //现在是否是延时时间 默认为false标识不是延时时间
    private Map<String, String> t_map;
    private BigDecimal minBid;
    private boolean haveMinBid;
    private Map<String, BigDecimal> minSubmitMap;
    //private List<RankInfo> rankInfos; //所有的供应商排名信息
    //private Map<String, Integer> rankInfoMap; //供应商当前排名位置
    private DynamicRankTable dynamicRankTable;
    private Integer supplierNum;


    /**
     * <h2>动态排名表</h2>
     *<table border="1">
     *     <tr>
     *         <td>标识Id</td>
     *         <td>价格</td>
     *         <td>排名</td>
     *     </tr>
     *     <tr>
     *         <td>yangmingsen</td>
     *         <td>1.234</td>
     *         <td>1</td>
     *     </tr>
     *     <tr>
     *         <td>helloWrld</td>
     *         <td>1.234</td>
     *         <td>1</td>
     *     </tr>
     *     <tr>
     *         <td>eric</td>
     *         <td>2.234</td>
     *         <td>3</td>
     *     </tr>
     *     <tr>
     *         <td>mingsen</td>
     *         <td>3.234</td>
     *         <td>4</td>
     *     </tr>
     *
     *</table>
     *
     */
    private static class DynamicRankTable {
        private List<Map<String, RankInfo>> rankInfos; //所有的供应商排名信息
        private Map<String, Integer> rankInfoMap = new HashMap<>(); //供应商当前排名位置

        public DynamicRankTable(int num) {
            rankInfos = new ArrayList<>();
            for (int i = 0; i < num; i++) {
                rankInfos.add(i,null);
            }
        }

        public List<Map<String, RankInfo>> getRankInfos() {
            return rankInfos;
        }

        public Map<String, RankInfo> getRanInfoMapByIndex(int index) {
            return this.rankInfos.get(index);
        }

        public Integer getRankById(String id) {
            return this.rankInfoMap.get(id);
        }

        public Map<String, Integer> getRankInfoMap() {
            return rankInfoMap;
        }

        public void addFixIndex(RankInfo rankInfo, int index) { //增加指定位置
            Map<String, RankInfo> newMap = new HashMap<>();
            newMap.put(rankInfo.getId(), rankInfo);
            newMap.put(index+"", rankInfo);
            this.rankInfos.add(index, newMap);
        }

        public void remove(int index, String removeId) {
            Map<String, RankInfo> rankInfoMap = this.rankInfos.get(index);
            rankInfoMap.remove(removeId);

            if (rankInfoMap.size() == 1) {
                if (rankInfoMap.get(index+"") != null) {
                    this.rankInfos.set(index, null);
                }
            }

        }

    }

    public int getDelayedCondition() {
        return delayedCondition;
    }

    public int getAllowDelayedLength() {
        return allowDelayedLength;
    }

    public int getAllowDelayedTime() {
        return allowDelayedTime;
    }

    public int getInitAllowDelayedTime() {
        return initAllowDelayedTime;
    }

    public Integer getSupplierNum() {
        return supplierNum;
    }

    private ActivityTask() {
    }

    /**
     * 设置当前活动剩余时长（秒）
     *
     * @param remainingTime
     */
    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getCurrentActivityCode() {
        return currentActivityCode;
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
     * 获取当前剩余时间（字符串方式，例如 "15:34"）
     *
     * @return
     */
    public String getRemainingTimeString() {
        int ttime = this.remainingTime;
        int minute = ttime / 60;

        int second = ttime % 60;

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
     * 设置榜首信息
     *
     * @param theTopBids 处于第一名的竞价列表
     */
    public void updateTopMinBid(List<JbxtBiddingDVO> theTopBids) {
        if (theTopBids.size() == 0) return;

        if (haveMinBid) {
            if (this.minBid.compareTo(theTopBids.get(0).getBid()) > 0) {
                //处理第一名提交竞价后还是第一名的情况
                for (JbxtBiddingDVO tbid : theTopBids) {
                    BigDecimal ttbidPrice = minSubmitMap.get(tbid.getUserCode());
                    if (ttbidPrice != null) {
                        return;
                    }
                }
                updateMinSubmitInfo(theTopBids);
                return;
            }
            if (this.minSubmitMap.size() < theTopBids.size()) {
                updateMinSubmitInfo(theTopBids);
            }

        } else {
            //首次 top
            updateMinSubmitInfo(theTopBids);
            this.haveMinBid = true;
        }
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
            lock.lock();
            try {
                this.continueRun.signal();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

            if (this.status == 3) {
                //同步数据（管理端 N，供应商端 Y）
                final Map<String, String> map = new HashMap<>();
                map.put("activityCode", this.currentActivityCode);
                map.put("goodsId", this.currentGoodsId.toString());

                final QueueMessage queueMessage = new QueueMessage(212, map);
                messageQueue.offer(queueMessage);

                this.status = 1;
                this.remainingTime = this.initTime;
                this.resetTopInfo();
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

    private void resetActivity() {
        //同步数据（管理端 N，供应商端 Y）
        final Map<String, String> map = new HashMap<>();
        map.put("activityCode", this.currentActivityCode);
        map.put("goodsId", this.currentGoodsId.toString());

        final QueueMessage queueMessage = new QueueMessage(212, map);
        messageQueue.offer(queueMessage);

        this.status = 1;
        this.remainingTime = this.initTime;
        //this.resetTopInfo();
        this.dynamicRankTable = new DynamicRankTable(this.supplierNum);
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
            Thread.sleep(1 * 1000);
            remainingTime--;
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
//           System.out.println("currentActivityCode="+currentActivityCode+" goodsId="+currentGoodsId+" 剩余时间:"+remainingTimeString);
        t_map.put("remainingTime", remainingTimeString);
        remainingTimeMessage.setData(t_map);

        userChannelMap.getAllUser().forEach(supplier -> {
            if (userChannelMap.getUserFocusActivity(supplier).equals(this.currentActivityCode)) {
                userChannelMap.get(supplier).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(remainingTimeMessage)));
            }
        });

        adminChannel.getAllAdmin().forEach(admin -> {
            if (adminChannel.get(admin).getFocusActivity().equals(this.currentActivityCode)) {
                adminChannel.get(admin).getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(remainingTimeMessage)));
            }
        });
    }


    /**
     *  推数据流到所有管理端
     * @param message
     * @param activityCode
     */
    private void notifyAllAdmin(ResponseMessage message, String activityCode) {
        adminChannel.getAllAdmin().forEach(adminCode -> notifyOptionAdmin(message, activityCode, adminCode));
    }

    /**
     * 推送数据到指定管理端
     * @param message
     * @param activityCode
     * @param adminCode
     */
    private void notifyOptionAdmin(ResponseMessage message, String activityCode, String adminCode) {
        LOGGER.info("notifyOptionAdmin(msg): activityCode="+activityCode+" adminCode="+adminCode+" message="+message);
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
        final JbxtGoodsDO jbxtGoodsDO = iJbxtGoodsService.selectByGoodsId(currentGoodsId);
        if (jbxtGoodsDO != null) {
            JbxtGoodsVO jbxtgoodsVO = new JbxtGoodsVO();
            jbxtgoodsVO.setGoodsId(currentGoodsId);
            jbxtgoodsVO.setStatus("2");

            try {
                iJbxtGoodsService.updateJbxtGoods(jbxtgoodsVO);
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
        final List<SimpleGoodsVO> goods = iJbxtGoodsService.findAll(activitCode);
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
        final IJbxtActivityService iJbxtActivityService = SpringUtil.getBean(IJbxtActivityService.class);
        final String activityCode = this.currentActivityCode;
        final JbxtActivityDO activity = iJbxtActivityService.findOneByCode(activityCode);

        if (activity != null) {
            if (activity.getStatus() == 3) {
                final JbxtActivityDO tActivity = new JbxtActivityDO();
                tActivity.setCode(activityCode);
                tActivity.setStatus(4);
                boolean updateActivityStatus = false;

                try {
                    iJbxtActivityService.updateByPrimaryKeySelective(tActivity);
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
     * 清除榜首信息
     */
    private void resetTopInfo() {
        minBid = new BigDecimal("0.0");
        haveMinBid = false;
        minSubmitMap.clear();
    }

    /**
     * 更新榜首 提示
     *
     * @param theTopBids
     */
    private void updateMinSubmitInfo(List<JbxtBiddingDVO> theTopBids) {
        this.minSubmitMap.clear();
        for (int i = 0; i < theTopBids.size(); i++) {
            final JbxtBiddingDVO ttBid = theTopBids.get(i);
            this.minSubmitMap.put(ttBid.getUserCode(), ttBid.getBid());
        }
        this.minBid = theTopBids.get(0).getBid();

        //notity all supplier the top updated
        final Map<String, String> map = new HashMap<>();
        map.put("activityCode", this.currentActivityCode);
        map.put("goodsId", this.currentGoodsId.toString());

        final QueueMessage queueMessage = new QueueMessage(111, map);
        messageQueue.offer(queueMessage);
    }

    /**
     * 外部传入的竞价信息
     *
     * @param queueMessage ObjectQueueMessage
     */
    public void recvBidMessage(QueueMessage queueMessage) {
        ObjectQueueMessage messge = (ObjectQueueMessage) queueMessage;
        String userCode = (String) messge.getObjData().get("userCode");
        BigDecimal bidPrice = (BigDecimal) messge.getObjData().get("bidPrice");
        RankInfo rankInfo = new RankInfo(userCode, bidPrice);
        DynamicRankTable dynamicRankTable = this.dynamicRankTable;

        Integer lastRankIdx = dynamicRankTable.getRankById(userCode);
        final List<Map<String, RankInfo>> rankInfos = dynamicRankTable.getRankInfos();

        Boolean needDeedDelayed = false;
        Boolean needNotifyTopUpdate = false;
        Integer newRankIdx = -1;

        if (lastRankIdx != null) { // when 当前供应商存在历史报价
            Boolean needAddToList = false; //是否需要增加到List中 否则添加到Map中
            int rankInfoLen = rankInfos.size();
            for (int i = 0; i < rankInfoLen; i++) {
                Map<String, RankInfo> tmpRankMap = rankInfos.get(i);
                if (tmpRankMap == null) continue;

                RankInfo tmpRankInfo = tmpRankMap.get(i + "");
                int compareRes = bidPrice.compareTo(tmpRankInfo.getBidPrice());
                if (compareRes == -1) { // 0.1 < 0.2的情况
                    newRankIdx = i;
                    needAddToList = true; //需要添加到元素之前
                    break;
                } else if (compareRes == 0) { //相等的情况
                    newRankIdx = i;
                    break;
                }
            }

            if (newRankIdx > -1) {
                //判断是否需要通知榜首更新
                if (lastRankIdx != 0 && newRankIdx == 0) {
                    needNotifyTopUpdate = true;
                }

                //判断是否需要延时
                if (lastRankIdx >= 0 && lastRankIdx <= 2) { //前3
                    if (newRankIdx > lastRankIdx) {
                        needDeedDelayed = decideDelayed();
                    }
                } else if (lastRankIdx > 2) { //前3之后
                    if (newRankIdx <= 2) {
                        needDeedDelayed = decideDelayed();
                    }
                } else {
                    LOGGER.info("recvBidMessage(msg): lastRank数据错误=" + lastRankIdx);
                }

                //更新动态排名表
                Map<String, Integer> rankInfoMap = dynamicRankTable.getRankInfoMap();
                if (needAddToList) {
                    dynamicRankTable.addFixIndex( rankInfo,newRankIdx);
                } else {
                    dynamicRankTable.getRanInfoMapByIndex(newRankIdx).put(rankInfo.getId(), rankInfo);
                }
                rankInfoMap.put(rankInfo.getId(),  newRankIdx); //更新Map中的排名

                if (newRankIdx != lastRankIdx) { //做更新
                    dynamicRankTable.remove(lastRankIdx, rankInfo.id);
                }

            } else {
                LOGGER.info("recvBidMessage(ERROR): 无法匹配newRankIdx 位置");
            }

        } else { // Not exist
            int rankInfoLen = rankInfos.size();
            int i = 0;
            Boolean needAddToList = false;
            int addIndx = 0;
            for ( ; i < rankInfoLen; i++) {
                Map<String, RankInfo> tmpRankMap = rankInfos.get(i);
                if (tmpRankMap == null) continue;

                RankInfo tmpRankInfo = tmpRankMap.get(i + "");
                int compareRes = bidPrice.compareTo(tmpRankInfo.getBidPrice());
                if (compareRes == -1) {
                    newRankIdx = i;
                    needAddToList = true;
                    break;
                } else if (compareRes == 0) {
                    newRankIdx = i;
                    break;
                }
                int crMapSize = tmpRankMap.size() -1;
                addIndx+=crMapSize;
            }

            if (i == rankInfoLen) { //处理插入数据在尾部情况
                newRankIdx = -2;
            } else if(newRankIdx == -1) { //处理在头部情况
                newRankIdx = 0;
            }
            //判断是否需要延时 //判断是否需要通知榜首更新
            if (newRankIdx == 0) {
                needNotifyTopUpdate = true;
                needDeedDelayed = decideDelayed();
            } else if( newRankIdx>0 && newRankIdx <=2) {
                needDeedDelayed = decideDelayed();
            }

            //更新动态排名表
            Map<String, Integer> rankInfoMap = dynamicRankTable.getRankInfoMap();
            if (newRankIdx == -2) { //处理尾部
                dynamicRankTable.addFixIndex(rankInfo, addIndx);
                rankInfoMap.put(rankInfo.getId(), addIndx);
            } else if(newRankIdx == 0) { //头部
                if (needAddToList) {
                    dynamicRankTable.addFixIndex(rankInfo, addIndx);
                    rankInfoMap.put(rankInfo.getId(), addIndx);
                } else {
                    dynamicRankTable.getRanInfoMapByIndex(newRankIdx).put(rankInfo.getId(),rankInfo);
                    rankInfoMap.put(rankInfo.getId(), newRankIdx);
                }

            } else { //中间部分
                if (needAddToList) {
                    dynamicRankTable.addFixIndex(rankInfo, addIndx);
                    rankInfoMap.put(rankInfo.getId(), addIndx);
                } else {
                    dynamicRankTable.getRanInfoMapByIndex(newRankIdx).put(rankInfo.getId(),rankInfo);
                    rankInfoMap.put(rankInfo.getId(), newRankIdx);
                }
            }
        }

        if (needDeedDelayed) {
            //do other after needDeedDelayed
            final Integer delayedLength = this.allowDelayedLength;
            ResponseMessage responseMessage = new ResponseMessage(120,new HashMap<String, Object>(){{
                put("delayedLength",delayedLength);
            }});
            notifyAllSupplier(responseMessage, this.currentActivityCode);
            notifyAllAdmin(responseMessage, this.currentActivityCode);
        }

        if (needNotifyTopUpdate) {
            // do needNotifyTopUpdate
            final String activityCode = this.currentActivityCode;
            final String goodsId = this.currentGoodsId.toString();
            ResponseMessage responseMessage = new ResponseMessage(120,new HashMap<String, Object>(){{
                put("activityCode",activityCode);
                put("goodsId",goodsId);
            }});
            notifyAllSupplier(responseMessage, activityCode);
        }

        //推送最新排名到所有的供应商
        //do sendSupplierRank
        doSendSupplierRank(dynamicRankTable);
    }

    /**
     * 将最新排名信息推送至各个客户端
     * @param dynamicRankTable
     */
    private void doSendSupplierRank(DynamicRankTable dynamicRankTable) {
        List<Map<String, RankInfo>> rankInfoList = dynamicRankTable.getRankInfos();
        Integer listSize = rankInfoList.size();
        for(int i=0; i< listSize; i++) {
            Map<String, RankInfo> curRankInfoMap = rankInfoList.get(i);
            if (curRankInfoMap == null ) continue;

            Set<Map.Entry<String, RankInfo>> entries = curRankInfoMap.entrySet();
            for(Map.Entry<String, RankInfo> entry : entries) {
                String supplierCode = entry.getKey();
                RankInfo rankInfo = entry.getValue();
                final BigDecimal goodsPrice = rankInfo.getBidPrice();
                final String goodsId = this.currentGoodsId.toString();
                final Integer userRank = rankInfo.getRank();
                ResponseMessage responseMessage = new ResponseMessage(200, new HashMap<String, Object>(){{
                    put("goodsPrice", goodsPrice);
                    put("goodsId", goodsId);
                    put("userRank", userRank);
                }});
                final String activityCode = this.currentActivityCode;
                notifyOptionSupplier(responseMessage, activityCode, supplierCode);
            }

        }

    }

    /**
     * 决策是否延迟
     * @return 是否延迟结果
     */
    private boolean decideDelayed() {
        final int remainingTime = this.remainingTime;
        final int delayedCondition = this.delayedCondition;
        final int allowDelayedTime = this.allowDelayedTime;
        if (remainingTime < delayedCondition && allowDelayedTime > 0) {
            final int allowDelayedLength = this.allowDelayedLength;
            this.remainingTime += allowDelayedLength;
            this.allowDelayedTime--;

            return true;
        }

        return false;
    }

    /**
     * 排名信息类
     */
    private static class RankInfo {
        private String id; //供应商id
        private BigDecimal bidPrice; //供应商竞价
        private Integer rank; //排名

        public RankInfo(String id, BigDecimal bidPrice) {
            this.id = id;
            this.bidPrice = bidPrice;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public BigDecimal getBidPrice() {
            return bidPrice;
        }

        public void setBidPrice(BigDecimal bidPrice) {
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
     * 内部构建器
     */
    public static class Builder {
        private final ActivityTask activityTask = new ActivityTask();

        public Builder() {
            activityTask.status = 1;
            activityTask.LOGGER = LoggerFactory.getLogger(ActivityTask.class);
            activityTask.remainingTimeMessage = new ResponseMessage(100, null);
            activityTask.userChannelMap = SpringUtil.getBean(UserChannelMap.class);
            activityTask.adminChannel = SpringUtil.getBean(AdminChannelMap.class);
            activityTask.iJbxtGoodsService = SpringUtil.getBean(IJbxtGoodsService.class);
            activityTask.lock = new ReentrantLock();
            activityTask.continueRun = activityTask.lock.newCondition();
            activityTask.messageQueue = SpringUtil.getBean(MessageQueue.class);
            activityTask.t_map = new HashMap<>();
            activityTask.minBid = new BigDecimal("0.0");
            activityTask.haveMinBid = false;
            activityTask.minSubmitMap = new HashMap<>();
            activityTask.disappearTime = 0;
        }

        /**
         * 设置当前活动供应商数
         * @param num
         * @return
         */
        public Builder supplierNum(int num) {
            this.activityTask.dynamicRankTable = new DynamicRankTable(num);
            return this;
        }

        public Builder activityCode(String activityCode) {
            this.activityTask.currentActivityCode = activityCode;
            return this;
        }

        /**
         * 设置当前goodsId
         * @param goodsId
         * @return
         */
        public Builder goodsId(Integer goodsId) {
            this.activityTask.currentGoodsId = goodsId;
            return this;
        }

        /**
         * 设置活动初始化时间
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
         * @param delayedCondition
         * @return
         */
        public Builder delayedCondition(Integer delayedCondition) {
            this.activityTask.delayedCondition = delayedCondition;
            return this;
        }

        /**
         * 设置延迟时长
         * @param allowDelayedLength
         * @return
         */
        public Builder allowDelayedLength(Integer allowDelayedLength) {
            this.activityTask.allowDelayedLength = allowDelayedLength;
            return this;
        }

        /**
         * 设置延迟次数
         * @param allowDelayedTime
         * @return
         */
        public Builder allowDelayedTime(Integer allowDelayedTime) {
            this.activityTask.allowDelayedTime = allowDelayedTime;
            this.activityTask.initAllowDelayedTime = allowDelayedTime;
            return this;
        }

        public ActivityTask build() {
            return this.activityTask;
        }
    }

}

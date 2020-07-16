package com.galaplat.comprehensive.bidding.activity;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.service.impl.JbxtGoodsServiceImpl;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class CurrentActivity extends Thread {

    private Logger LOGGER = LoggerFactory.getLogger(CurrentActivity.class);
    private String currentActivityCode;
    private String currentGoodsId;
    private int initTime; //秒
    private int status;//1 进行 2暂停  3//重置
    private int remainingTime;  //秒
    private UserChannelMap userChannelMap = SpringUtil.getBean(UserChannelMap.class);
    private AdminChannelMap adminChannel = SpringUtil.getBean(AdminChannelMap.class);
    private ReentrantLock lock =  new ReentrantLock();
    private Condition continueRun = lock.newCondition();
    private PushQueue pushQueue = SpringUtil.getBean(PushQueue.class);
    private Map<String, String> t_map = new HashMap<>();
    private Message message = new Message(100,null);
    private BigDecimal minBid = new BigDecimal("0.0");
    private boolean haveMinBid = false;
    private Map<String, BigDecimal> minSubmitMap = new HashMap<>();

    private void reInitTopInfo() {
        minBid = new BigDecimal("0.0");
        haveMinBid = false;
        minSubmitMap.clear();
    }

    public void updateTopMinBid(List<JbxtBiddingDVO> theTopBids) {
        if (theTopBids.size() == 0 ) return;

        if (haveMinBid) {
            if (this.minBid.compareTo(theTopBids.get(0).getBid()) == 1) {
                //处理第一名提交竞价后还是第一名的情况
                for (int i = 0; i < theTopBids.size(); i++) {
                    JbxtBiddingDVO tbid = theTopBids.get(i);
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

    private void updateMinSubmitInfo(List<JbxtBiddingDVO> theTopBids) {
        this.minSubmitMap.clear();
        for (int i = 0; i < theTopBids.size(); i++) {
            JbxtBiddingDVO ttBid = theTopBids.get(i);
            this.minSubmitMap.put(ttBid.getUserCode(), ttBid.getBid());
        }
        this.minBid = theTopBids.get(0).getBid();

        //notity all supplier the top updated
        Map<String, String> map = new HashMap<>();
        map.put("activityCode",this.currentActivityCode);
        map.put("goodsId", this.currentGoodsId);
        QueueMessage queueMessage = new QueueMessage(111, map);

        pushQueue.offer(queueMessage);
    }

    public void setRemainingTime(int remainingTime) {
        this.remainingTime = remainingTime;
    }

    public String getCurrentActivityCode() {
        return currentActivityCode;
    }

    public String getCurrentGoodsId() {
        return currentGoodsId;
    }

    /**
     * 获取剩余时间 （秒）
     * @return
     */
    public int getRemainingTime() {
        return remainingTime;
    }

    public String getRemainingTimeString() {
        int ttime = this.remainingTime;
        int minute = ttime/60;

        int second = ttime%60;

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

    public int getInitTime() {
        return initTime;
    }

    /***
     * //1 进行 2暂停  3//重置
     * @return
     */
    public int getStatus() {
        return status;
    }

    /***
     *
     * @param currentActivityCode
     * @param currentGoodsId
     * @param initTime 秒
     * @param status 1 进行 2暂停  3重置
     */
    public CurrentActivity(String currentActivityCode, String currentGoodsId,
                           int initTime, int status) {
        this.currentActivityCode = currentActivityCode;
        this.currentGoodsId = currentGoodsId;
        this.initTime = initTime;
        this.status = status;
        this.remainingTime = initTime;
    }

    public CurrentActivity(String currentActivityCode, String currentGoodsId, int initTime) {
        this(currentActivityCode,currentGoodsId,initTime,1);
    }

    public void setStatus(int status) {
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
                IJbxtBiddingService iJbxtBiddingService = SpringUtil.getBean(IJbxtBiddingService.class);
                try {
                    iJbxtBiddingService.deleteByGoodsIdAndActivityCode(Integer.parseInt(this.getCurrentGoodsId()), this.currentActivityCode);
                    iJbxtBiddingService.deleteMinbidTableByGoodsIdAndActivityCode(Integer.parseInt(this.getCurrentGoodsId()), this.currentActivityCode);

                    //同步数据（管理端 N，供应商端 Y）
                    Map<String, String> map = new HashMap<>();
                    map.put("activityCode",this.currentActivityCode);
                    map.put("goodsId", this.currentGoodsId);
                    QueueMessage queueMessage = new QueueMessage(212, map);

                    pushQueue.offer(queueMessage);

                } catch (Exception e) {
                    e.printStackTrace();
                    LOGGER.info("setStatusExction(ERROR): "+e.getMessage());
                } finally {
                    this.status = 1;
                    this.remainingTime = this.initTime;
                    this.reInitTopInfo();
                }
            }

            //通知供应商端 继续
            Map<String, String> map = new HashMap<>();
            map.put("activityCode",this.currentActivityCode);
            map.put("goodsId", this.currentGoodsId);
            map.put("status", "1");
            QueueMessage queueMessage = new QueueMessage(215, map);

            pushQueue.offer(queueMessage);

        }


    }

    private void startRemainingTime() throws InterruptedException{
        while (remainingTime > -1) {
            if (status == 2) {

                //通知供应商端 暂停
                Map<String, String> map = new HashMap<>();
                map.put("activityCode",this.currentActivityCode);
                map.put("goodsId", this.currentGoodsId);
                map.put("status", "3");
                QueueMessage queueMessage = new QueueMessage(215, map);

                pushQueue.offer(queueMessage);

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

            String remainingTimeString = getRemainingTimeString();
//           System.out.println("currentActivityCode="+currentActivityCode+" goodsId="+currentGoodsId+" 剩余时间:"+remainingTimeString);
            t_map.put("remainingTime",remainingTimeString);
            message.setData(t_map);

            userChannelMap.getAllUser().forEach(supplier -> {
                if (userChannelMap.getUserFocusActivity(supplier).equals(this.currentActivityCode)) {
                    userChannelMap.get(supplier).writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
            });

            adminChannel.getAllAdmin().forEach(admin -> {
                if (adminChannel.get(admin).getFocusActivity().equals(this.currentActivityCode)) {
                    adminChannel.get(admin).getChannel().writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
                }
            });

            Thread.sleep(1*1000);
            remainingTime --;
        }

    }


    public void run() {
        try {
            startRemainingTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}

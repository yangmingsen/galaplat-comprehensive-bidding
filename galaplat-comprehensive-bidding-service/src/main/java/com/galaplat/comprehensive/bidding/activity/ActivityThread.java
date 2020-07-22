package com.galaplat.comprehensive.bidding.activity;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.queue.MessageQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.netty.AdminChannelMap;
import com.galaplat.comprehensive.bidding.netty.pojo.Message;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.utils.SpringUtil;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;


public class ActivityThread extends Thread {

    private final Logger LOGGER = LoggerFactory.getLogger(ActivityThread.class);
    private final String currentActivityCode;
    private final String currentGoodsId;
    private final Message remainingTimeMessage = new Message(100,null);
    private final UserChannelMap userChannelMap = SpringUtil.getBean(UserChannelMap.class);
    private final AdminChannelMap adminChannel = SpringUtil.getBean(AdminChannelMap.class);
    private final IJbxtGoodsService iJbxtGoodsService = SpringUtil.getBean(IJbxtGoodsService.class);
    private final ReentrantLock lock =  new ReentrantLock();
    private final Condition continueRun = lock.newCondition();
    private final MessageQueue messageQueue = SpringUtil.getBean(MessageQueue.class);
    private int initTime; //秒
    private int status;//1 进行 2暂停  3//重置 4 结束
    private int remainingTime;  //秒
    private Map<String, String> t_map = new HashMap<>();
    private BigDecimal minBid = new BigDecimal("0.0");
    private boolean haveMinBid = false;
    private Map<String, BigDecimal> minSubmitMap = new HashMap<>();

    /***
     * 设置当前活动剩余时长（秒）
     * @param remainingTime
     */
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

    /***
     * 获取当前剩余时间（字符串方式，例如 "15:34"）
     * @return
     */
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

    /***
     * 获取当前剩余时间（秒）
     * @return
     */
    public int getInitTime() {
        return initTime;
    }

    /***
     * //1 进行 2暂停  3//重置 4 结束
     * @return
     */
    public int getStatus() {
        return status;
    }

    /***
     *设置活动线程构造参数
     * @param currentActivityCode
     * @param currentGoodsId
     * @param initTime 秒
     * @param status 1 进行 2暂停  3重置 4结束。必须是1
     */
    public ActivityThread(String currentActivityCode, String currentGoodsId,
                          int initTime, int status) {
        this.currentActivityCode = currentActivityCode;
        this.currentGoodsId = currentGoodsId;
        this.initTime = initTime;
        this.status = status;
        this.remainingTime = initTime;
    }

    /***
     * 设置活动线程构造参数
     * @param currentActivityCode 活动code
     * @param currentGoodsId  竞品id
     * @param initTime 初始化时间：以秒计算
     */
    public ActivityThread(String currentActivityCode, String currentGoodsId, int initTime) {
        this(currentActivityCode,currentGoodsId,initTime,1);
    }

    /***
     * 设置榜首信息
     * @param theTopBids 处于第一名的竞价列表
     */
    public void updateTopMinBid(List<JbxtBiddingDVO> theTopBids) {
        if (theTopBids.size() == 0 ) return;

        if (haveMinBid) {
            if (this.minBid.compareTo(theTopBids.get(0).getBid()) == 1) {
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

    /***
     * 设置 当前竞品活动状态
     * @param status 1 进行 2暂停  3重置  4结束(不可手动设置)
     */
    public void setStatus(int status) {
        LOGGER.info("currentStatus="+status+" newStatus="+status);
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
                map.put("activityCode",this.currentActivityCode);
                map.put("goodsId", this.currentGoodsId);

                final QueueMessage queueMessage = new QueueMessage(212, map);
                messageQueue.offer(queueMessage);

                this.status = 1;
                this.remainingTime = this.initTime;
                this.resetTopInfo();
            }

            //通知供应商端 继续
            final Map<String, String> map = new HashMap<>();
            map.put("activityCode",this.currentActivityCode);
            map.put("goodsId", this.currentGoodsId);
            map.put("status", "1");

            final QueueMessage queueMessage = new QueueMessage(215, map);
            messageQueue.offer(queueMessage);
        }
    }

    /***
     * 业务启动入口
     */
    public void run() {
        try {
            this.startRemainingTime();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /***
     * 启动剩余时长计算
     * @throws InterruptedException
     */
    private void startRemainingTime() throws InterruptedException{
        this.syncInfoToAll();
        while (remainingTime > -1) {
            if (status == 2) {

                //通知供应商端 暂停
                Map<String, String> map = new HashMap<>();
                map.put("activityCode",this.currentActivityCode);
                map.put("goodsId", this.currentGoodsId);
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
            Thread.sleep(1*1000);
            remainingTime --;
        }
//        if (isfinallyGoods()) {
//            endTheCurrentGoodsActivity();
//            autoCloseCurrentActivity();
//        }
    }

    /***
     * 发送剩余时长到所有通道（supplier,admin）
     */
    private void sendRemainingTimeToAll() {
        final String remainingTimeString = getRemainingTimeString();
//           System.out.println("currentActivityCode="+currentActivityCode+" goodsId="+currentGoodsId+" 剩余时间:"+remainingTimeString);
        t_map.put("remainingTime",remainingTimeString);
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

    /***
     * 自动结束当前活动
     *
     */
    private void endTheCurrentGoodsActivity() {
        final JbxtGoodsDO jbxtGoodsDO = iJbxtGoodsService.selectByGoodsId(Integer.parseInt(currentGoodsId));
        if (jbxtGoodsDO != null) {
            JbxtGoodsVO jbxtgoodsVO = new JbxtGoodsVO();
            jbxtgoodsVO.setGoodsId(Integer.parseInt(currentGoodsId));
            jbxtgoodsVO.setStatus("2");

            try {
                iJbxtGoodsService.updateJbxtGoods(jbxtgoodsVO);
                LOGGER.info("endTheCurrentGoodsActivity(msg): 活动: "+currentActivityCode+" 竞品id: "+currentGoodsId+" 结束");
            } catch (Exception e) {
                LOGGER.info("endTheCurrentGoodsActivity(ERROR): "+e.getMessage());
            }
//            this.status = 4;
        }
    }

    /***
     * 判断当前竞品是否为当前活动的最后一个竞品
     * @return
     */
    private boolean isfinallyGoods() {
        final String activitCode = this.currentActivityCode;
        final Integer goodsId = Integer.parseInt(this.currentGoodsId);
        final List<SimpleGoodsVO> goods = iJbxtGoodsService.findAll(activitCode);
        final int goodsLength = goods.size();
        if (goodsLength > 0) {
            final int endGoodsIdx =goodsLength-1;
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
                    LOGGER.info("switchActivityGoods(ERROR): 更新活动("+activityCode+")结束结束失败");
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


    /***
     * 同步数据给所有供应商端
     */
    private void syncInfoToAll() {
        //{type: 200, data: {goodsId: 23423, activityCode: 23423345}}
        final Map<String, String> supplierInfo = new HashMap<>();
        supplierInfo.put("goodsId", this.currentGoodsId);
        supplierInfo.put("activityCode", this.currentActivityCode);
        //同步数据 给 供应商
        final QueueMessage queueMessage = new QueueMessage(200,supplierInfo);
        messageQueue.offer(queueMessage);

    }

    /***
     * 当活动处于暂停时 同步暂停后最后一次时间
     */
    private void whenAcitvityPause() {
        this.sendRemainingTimeToAll();
    }

    /***
     *  清除榜首信息
     */
    private void resetTopInfo() {
        minBid = new BigDecimal("0.0");
        haveMinBid = false;
        minSubmitMap.clear();
    }

    /**
     * 更新榜首 提示
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
        map.put("activityCode",this.currentActivityCode);
        map.put("goodsId", this.currentGoodsId);

        final QueueMessage queueMessage = new QueueMessage(111, map);
        messageQueue.offer(queueMessage);
    }

}

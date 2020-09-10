package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.galaplat.comprehensive.bidding.activity.ActivityTask;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SupplierInProblemHandler extends BaseProblemHandler {
    protected Logger LOGGER = LoggerFactory.getLogger(SupplierInProblemHandler.class);

    @Override
    public void handlerProblem(int type, QueueMessage queuemsg) {
        switch (type) {
            case 213: {
                handler213Problem(queuemsg);
            }
            break;
        }
    }


    private void handler213Problem(QueueMessage takeQueuemsg) {
        final String bidPriceStr = takeQueuemsg.getData().get("bidPrice");
        final String userCode = takeQueuemsg.getData().get("userCode");
        final String activityCode = takeQueuemsg.getData().get("activityCode");
        final String goodsIdStr = takeQueuemsg.getData().get("goodsId");
        //处理提交是否过时问题(V2.0添加)
        final ActivityTask currentActivity = activityManager.get(activityCode);

        if (currentActivity == null) {
            LOGGER.info("handler213Problem: 当前活动不存在");return;
        } else {
            String currentGoodsId = currentActivity.getCurrentGoodsId().toString();
            if (!currentGoodsId.equals(goodsIdStr)) {
                LOGGER.info("handler213Problem: 当前竞品竞价已经结束或者未开始");return;
            }
            if (currentActivity.getRemainingTime() < 1) {
                LOGGER.info("handler213Problem: 当前竞品竞价已经结束");return;
            }
        }


        // business start
        final Integer goodsId = Integer.parseInt(goodsIdStr);
        final BigDecimal bid = new BigDecimal(bidPriceStr);
        final JbxtBiddingDO curBidInfo =
                iJbxtBiddingService. //获取当前用户最小竞价
                        selectMinBidTableBy(userCode, goodsId, activityCode);
        String percentStr = takeQueuemsg.getData().get("bidPercent");
        Integer bidPercent = percentStr == null ? 0 : Integer.parseInt(percentStr);
        if (curBidInfo != null) {
            if (bid.compareTo(curBidInfo.getBid()) < 0) { //如果1 < 2 => -1
                //处理提交
               saveBidDataToDB(activityCode, userCode, bid, goodsId, 2, bidPercent);
            }
        }  else { //如果没有最低报价(意味着数据库中没有该竞品的提交数据) 那么直接插入
            saveBidDataToDB(activityCode, userCode, bid, goodsId,1, bidPercent);
        }

    }

    @Transactional( rollbackFor = Exception.class) //#issue
    void saveBidDataToDB(String activityCode, String userCode, BigDecimal bid, Integer goodsId, int status, Integer bidPercent) {

        final ActivityTask currentActivity = activityManager.get(activityCode);

        final boolean timeType = currentActivity.isTriggerDelayed();

        //获取剩余时间类型
        final String bidTime = !timeType ? currentActivity.getRemainingTimeString() : currentActivity.getDelayRemainingTimeString();

        final JbxtBiddingVO newBidVO = new JbxtBiddingVO();
        newBidVO.setBid(bid);
        newBidVO.setUserCode(userCode);
        newBidVO.setGoodsId(goodsId);
        newBidVO.setActivityCode(activityCode); //设置当前活动id
        newBidVO.setBidTime(bidTime);
        newBidVO.setBidPercent(bidPercent);
        if (!timeType) {
            newBidVO.setIsdelay(1);
        } else {
            newBidVO.setIsdelay(2);
        }


        try {
            //add to db
            iJbxtBiddingService.insertJbxtBidding(newBidVO);
            if (status == 1) { //插入
                iJbxtBiddingService.insertMinBidTableSelective(newBidVO);
            } else if (status == 2) { //更新
                final JbxtBiddingDO minbidE = iJbxtBiddingService.selectMinBidTableBy(userCode, goodsId, activityCode);

                final JbxtBiddingVO updateBidVO = new JbxtBiddingVO();
                updateBidVO.setCode(minbidE.getCode());
                updateBidVO.setBid(bid);
                updateBidVO.setUpdatedTime(new Date());
                updateBidVO.setBidTime(bidTime);
                updateBidVO.setBidPercent(bidPercent);
                if (!timeType) {
                    updateBidVO.setIsdelay(1);
                } else {
                    updateBidVO.setIsdelay(2);
                }
                iJbxtBiddingService.updateMinBidTableByPrimaryKeySelective(updateBidVO);
            }
        }catch (Exception e) {
            LOGGER.info("saveBidDataToDB(ERROR): 更新竞价数据失败-"+e.getMessage());
            return;
        }

        Map<String, String> map301 = new HashMap();
        map301.put("activityCode", activityCode);
        map301.put("userCode", userCode);
        map301.put("goodsId", goodsId.toString());
        messageQueue.offer(new QueueMessage(301, map301));

//        final Map<String, Object> map200 = new HashMap();
//        map200.put("activityCode", activityCode);
//        map200.put("userCode", userCode);
//        map200.put("goodsId", goodsId.toString());
//        map200.put("bidPrice", bid);
//        ObjectQueueMessage msg = new ObjectQueueMessage(200, map200);

        currentActivity.handleRank();

       // currentActivity.recvBidMessage(msg);


/*
        //
        final Map<String, String> map200 = new HashMap();
        map200.put("activityCode", activityCode);
        map200.put("goodsId", goodsId.toString());
        messageQueue.offer(new QueueMessage(200,map200));

        //检查是否更新top提示
        final List<JbxtBiddingDVO> theTopBids = iJbxtBiddingService.getTheTopBids(goodsId, activityCode);
        activityManager.get(activityCode).updateTopMinBid(theTopBids);*/
    }

}

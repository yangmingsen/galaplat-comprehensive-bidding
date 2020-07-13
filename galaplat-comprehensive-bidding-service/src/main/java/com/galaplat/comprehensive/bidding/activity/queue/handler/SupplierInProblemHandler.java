package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        String bidPriceStr = takeQueuemsg.getData().get("bidPrice");
        String userCode = takeQueuemsg.getData().get("userCode");
        String activityCode = takeQueuemsg.getData().get("activityCode");
        String goodsIdStr = takeQueuemsg.getData().get("goodsId");
        //处理提交是否过时问题(V2.0添加)
        CurrentActivity currentActivity = activityMap.get(activityCode);
        if (currentActivity == null) {
            LOGGER.info("SupplierInProblemHandler(handler213Problem): 当前活动不存在");return;
        } else {
            String currentGoodsId = currentActivity.getCurrentGoodsId();
            if (!currentGoodsId.equals(goodsIdStr)) {
                LOGGER.info("SupplierInProblemHandler(handler213Problem): 当前竞品竞价已经结束或者未开始");return;
            }
            if (currentActivity.getRemainingTime() < 1) {
                LOGGER.info("SupplierInProblemHandler(handler213Problem): 当前竞品竞价已经结束");return;
            }
        }


        // business start
        Integer goodsId = Integer.parseInt(goodsIdStr);
        BigDecimal bid = new BigDecimal(bidPriceStr);
        JbxtBiddingDO curBidInfo =
                iJbxtBiddingService. //获取当前用户最小竞价
                        selectMinBidTableBy(userCode, goodsId, activityCode);
        if (curBidInfo != null) {
            if (bid.compareTo(curBidInfo.getBid()) == -1) { //如果1 < 2 => -1
                //处理提交
               saveBidDataToDB(activityCode, userCode, bid, goodsId, 2);
            } else {
                LOGGER.info("SupplierInProblemHandler(handler213Problem): 提交竞价低于之前提交"); return;
            }
        }  else { //如果没有最低报价(意味着数据库中没有该竞品的提交数据) 那么直接插入
            saveBidDataToDB(activityCode, userCode, bid, goodsId,1);
        }

    }

    private void saveBidDataToDB(String activityCode, String userCode, BigDecimal bid, Integer goodsId, int status) {
        try {
            CurrentActivity currentActivity = activityMap.get(activityCode);
            int se = currentActivity.getRemainingTime();
            String bidTime = (se/60)+":"+(se%60);

            JbxtBiddingVO jbv = new JbxtBiddingVO();
            jbv.setBid(bid);
            jbv.setUserCode(userCode);
            jbv.setGoodsId(goodsId);
            jbv.setActivityCode(activityCode); //设置当前活动id
            jbv.setBidTime(bidTime);

            //add to db
            iJbxtBiddingService.insertJbxtBidding(jbv);

            if (status == 1) { //插入
                iJbxtBiddingService.insertMinBidTableSelective(jbv);
            } else if (status == 2) { //更新
                JbxtBiddingDO minbidE = iJbxtBiddingService.selectMinBidTableBy(userCode, goodsId, activityCode);

                JbxtBiddingVO var1 = new JbxtBiddingVO();
                var1.setCode(minbidE.getCode());
                var1.setBid(bid);
                var1.setUpdatedTime(new Date());
                var1.setBidTime(bidTime);
                iJbxtBiddingService.updateMinBidTableByPrimaryKeySelective(var1);
            }
            //
            Map<String, String> map301 = new HashMap();
            map301.put("bidTime",bidTime);
            map301.put("bid",bid.toString());
            map301.put("activityCode", activityCode);
            JbxtUserDO jbxtUserDO = iJbxtUserService.selectByuserCodeAndActivityCode(userCode, activityCode);
            map301.put("supplierCode",jbxtUserDO.getCode());
            map301.put("CodeName", jbxtUserDO.getCodeName());
            map301.put("supplierName", jbxtUserDO.getSupplierName());
            pushQueue.offer(new QueueMessage(301, map301));

            //
            Map<String, String> map200 = new HashMap();
            map200.put("activityCode", activityCode);
            map200.put("goodsId", goodsId.toString());
            pushQueue.offer(new QueueMessage(200,map200));

            //检查是否更新top提示
            List<JbxtBiddingDVO> theTopBids = iJbxtBiddingService.getTheTopBids(goodsId, activityCode);
            activityMap.get(activityCode).updateTopMinBid(theTopBids);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

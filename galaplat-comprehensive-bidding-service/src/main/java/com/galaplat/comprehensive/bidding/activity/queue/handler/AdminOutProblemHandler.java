package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.alibaba.fastjson.JSON;
import com.galaplat.comprehensive.bidding.activity.ActivityTask;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.BiddingDO;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.BiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.GoodsDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.UserDVO;
import com.galaplat.comprehensive.bidding.netty.channel.AdminInfo;
import com.galaplat.comprehensive.bidding.netty.pojo.ResponseMessage;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t1;
import com.galaplat.comprehensive.bidding.netty.pojo.res.Res300t2;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


public class AdminOutProblemHandler extends BaseProblemHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(AdminOutProblemHandler.class);

    @Override
    public void handlerProblem(int type, QueueMessage queueMessage) {
        switch (type) {
            case 300: //当某个管理端中途加入（或掉线从新加入） 同步数据  //同步
                handler300Problem(queueMessage);
                break;

            case 301: //推数据给管理端（同步一些数据给管理端）
                handler301Problem(queueMessage);
                break;

            case 302: //处理管理端主动请求获取某个竞品数据时
                handler302Problem(queueMessage);
                break;
        }
    }

    private void handler302Problem(QueueMessage queuemsg) {
        this.handler300Problem(queuemsg);
    }

    /**
     * 这里会处理300和302问题
     *
     * @param takeQueuemsg
     */
    private void handler300Problem(QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");
        if (activityCode == null) {
            LOGGER.info("handler300Problem(ERROR): 无法获取activityCode");
            return;
        }
        final String adminCode = takeQueuemsg.getData().get("adminCode");
        final AdminInfo adminInfo = adminChannel.get(adminCode);
        if (adminInfo == null) {
            LOGGER.info("handler300Problem(ERROR): 无法获取当前管理员的消息");
            return;
        }

        final Channel caChannel = adminInfo.getChannel();
        final String goodsIdStr = takeQueuemsg.getData().get("goodsId");
        final Integer goodsId;
        final ActivityDO activityDO = activityService.findOneByCode(activityCode);
        //获取当前竞价类型
        final Integer bidType = activityDO.getBidingType();
        if (bidType == null) {
            LOGGER.info("handler300Problem(ERROR): bidType数据为null，请检查数据activity表的bidType字段");
            return;
        }

        if (goodsIdStr == null) { //如果传入的goodsId为 null 意味着是300问题
            ActivityTask currentActivity = activityManager.get(activityCode); //#issue3001 如果存在未开始情况如何处理
            if (currentActivity == null) { //处理 issue3001
                List<GoodsDVO> tmpGoodsList = goodsService.findAllByActivityCode(activityCode);
                if (tmpGoodsList.size() > 0) {
                    GoodsDVO tmpGoodsDVO = tmpGoodsList.get(0);
                    goodsId = tmpGoodsDVO.getGoodsId();
                } else {
                    LOGGER.info("handler300Problem(ERROR): 当前活动" + activityCode + "不存在竞品列表");
                    return;
                }
            } else {
                goodsId = currentActivity.getCurrentGoodsId();
            }
        } else { //不为null 意味着302问题
            try {
                goodsId = Integer.parseInt(goodsIdStr);
            } catch (NumberFormatException e) {
                LOGGER.info("handler300Problem(ERROR): 解析goodsId为整形时报错-----" + e.getMessage());
                return;
            }

        }


        //查找所有的供应商,用于循环查找他们的竞价信息
        final List<UserDVO> suppliers = userService.findAllByActivityCode(activityCode);
        final List<Res300t1> supplierBidInfos = new ArrayList<>();
        for (final UserDVO supplier : suppliers) {
            final Res300t1 res300t1 = new Res300t1();
            res300t1.setSupplierName(supplier.getSupplierName());
            res300t1.setCodeName(supplier.getCodeName());
            res300t1.setSupplierCode(supplier.getCode());

            //从min_bid表中查找他的历史最小竞价记录
            final BiddingDO currentSupplierMinBid = biddingService.selectMinBidTableBy(supplier.getCode(), goodsId, activityCode);
            if (currentSupplierMinBid != null) {
                if (bidType == 1) {
                    res300t1.setMinBid(currentSupplierMinBid.getBid());
                } else if(bidType == 2) {
                    res300t1.setMinBid(currentSupplierMinBid.getBidPercent());
                }
            } else {
                if (bidType == 1) {
                    res300t1.setMinBid(new BigDecimal("0.000"));
                } else if (bidType == 2) {
                    res300t1.setMinBid(new BigDecimal("0.00"));
                }
            }

            //从bidding表查找当前供应商的所有竞价记录
            final List<BiddingDVO> currentSupplierBidHistory = biddingService.
                    findAllByUserCodeAndGooodsIdAndActivityCode(supplier.getCode(), goodsId, activityCode);
            final List<Res300t2> supplierBids = new ArrayList<>();
            if (currentSupplierBidHistory.size() > 0) {

                //遍历当前供应商所有的历史竞价记录 添加的返回对象中
                for (final BiddingDVO bidRecord : currentSupplierBidHistory) {
                    final Res300t2 supplierBidRecord = new Res300t2();
                    if (bidType == 1) {
                        supplierBidRecord.setBid(bidRecord.getBid());
                    } else if (bidType == 2) {
                        supplierBidRecord.setBid(bidRecord.getBidPercent());
                    }
                    supplierBidRecord.setBidTime(bidRecord.getBidTime());
                    supplierBidRecord.setIsDelay(bidRecord.getIsdelay());

                    supplierBids.add(supplierBidRecord);
                }
            }
            res300t1.setBids(supplierBids);

            supplierBidInfos.add(res300t1);
        }

        //根据每个供应商的最小竞价 排序
        List<Res300t1> newSortSupplierBidInfos = null;
        if (bidType == 1) {// ASC
            newSortSupplierBidInfos =  supplierBidInfos.stream().sorted(Comparator.comparing(Res300t1::getMinBid)).collect(Collectors.toList());
        } else  if (bidType == 2) {//DESC
            newSortSupplierBidInfos =  supplierBidInfos.stream().sorted(Comparator.comparing(Res300t1::getMinBid).reversed()).collect(Collectors.toList());
        }

        //300返回对象
        final Res300 res300 = new Res300();
        res300.setGoodsId(goodsId);

        GoodsDO currentGoods = goodsService.selectByGoodsId(goodsId); //获取当前竞品信息
        final int delayedNum = currentGoods.getAddDelayTimes(); //获取当前竞品出现过的延迟次数
        if (delayedNum > 0) {
            res300.setDelay(true); //设置是否出现延时判断字段
            res300.setDelayedTime(currentGoods.getAddDelayTimes()); //设置出现过的延迟次数
            res300.setDelayedLength(currentGoods.getPerDelayTime()); //设置每次延时时长
        } else {
            res300.setDelay(false);
        }

        BigDecimal minPrice = null;

        if (newSortSupplierBidInfos.size() > 0) {
            minPrice = newSortSupplierBidInfos.get(0).getMinBid();
        } else {
            if (bidType == 1) {
                minPrice = new BigDecimal("0.000");
            } else if (bidType == 2) {
                minPrice = new BigDecimal("0.00");
            }
        }

        res300.setMinPrice(minPrice);
        res300.setList(newSortSupplierBidInfos);
        res300.setBidingType(bidType); //默认为数值竞价

        //处理返回数据
        final ResponseMessage tmsg = new ResponseMessage(300, res300);
        LOGGER.info("handler300Problem(msg): 往管理员" + adminCode + " send msg=" + tmsg.toString());
        caChannel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(tmsg)));


        //处理当管理端点击暂停后无故刷新控制台界面 时间同步问题
        //传递当前活动剩余时长
        final ActivityTask currentActivity = activityManager.get(activityCode);
        if (currentActivity != null) {
            if (currentActivity.getStatus() == 2) { //如果暂停为暂停状态
                final Map<String, String> t_map = new HashMap<>();
                t_map.put("remainingTime", currentActivity.getRemainingTimeString());
                final ResponseMessage remainingTimeMessage = new ResponseMessage(100, t_map);
                notifyOptionAdmin(remainingTimeMessage, activityCode, adminCode);
            }
        }

    }

    private void handler301Problem(QueueMessage takeQueuemsg) {
        final String activityCode = takeQueuemsg.getData().get("activityCode");
        final String userCode = takeQueuemsg.getData().get("userCode");
        final String goodsIdStr = takeQueuemsg.getData().get("goodsId");
        String bidTypeStr = takeQueuemsg.getData().get("bidType");
        int bidType = Integer.parseInt(bidTypeStr);
        final Integer goodsId = Integer.parseInt(goodsIdStr);
        final BiddingDO minbidInfo = biddingService.selectMinBidTableBy(userCode, goodsId, activityCode);
        final UserDO userInfo = userService.selectByuserCodeAndActivityCode(userCode, activityCode);

        final Map<String, String> res301 = new HashMap<>();
        if (minbidInfo != null && userInfo != null) {
            res301.put("bidTime", minbidInfo.getBidTime());
            if (bidType == 1) {
                res301.put("bid", minbidInfo.getBid().toString());
            } else if( bidType == 2) {
                res301.put("bid", minbidInfo.getBidPercent().toString());
            }
            res301.put("activityCode", activityCode);
            res301.put("supplierCode", userInfo.getCode());
            res301.put("CodeName", userInfo.getCodeName());
//            res301.put("supplierName", userInfo.getSupplierName());
//            if (bidPercent != null) {
//                res301.put("bidPercent", bidPercent);
//            }

            final ActivityTask activityTask = this.activityManager.get(activityCode);
            if (activityTask != null) {
                res301.put("remainingTimeType", activityTask.isRealAccessDealyedTime().toString());
            } else {
                res301.put("remainingTimeType", "false");
            }

        } else {
            return;
        }
        //推数据给管理端
        final ResponseMessage message = new ResponseMessage(301, res301);
        notifyAllAdmin(message, activityCode);
    }

}

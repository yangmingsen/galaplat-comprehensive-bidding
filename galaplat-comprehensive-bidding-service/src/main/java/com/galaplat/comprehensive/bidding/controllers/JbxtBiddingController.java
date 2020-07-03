package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import com.galaplat.comprehensive.bidding.vos.pojo.MyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.baseplatform.permissions.controllers.BaseController;
import com.github.pagehelper.PageInfo;
import com.galaplat.comprehensive.bidding.querys.JbxtBiddingQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


/**
 * 竞价表Controller
 *
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/jbxt/bid")
public class JbxtBiddingController {


    @Autowired
    IJbxtBiddingService jbxtbiddingService;

    @Autowired
    IJbxtGoodsService jbxtGoodsService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsController.class);

    @Autowired
    private ActivityMap activityMap;

    public MyResult checkSubmit(BigDecimal bid, Integer goodsId, String activityCode) {
        if (bid == null) {
            return new MyResult(false, "提交失败: bid不能为空哦^_^", null);
        }
        if (goodsId == null) {
            return new MyResult(false, "提交失败: goodsId不能为空哦^_^", null);
        }
        if (activityCode == null || activityCode.equals("")) {
            return new MyResult(false, "提交失败: activityCode不能为空哦^_^", null);
        }

        //处理提交是否过时问题(V2.0添加)
        CurrentActivity currentActivity = activityMap.get(activityCode);
        if (currentActivity == null) {
            return new MyResult(false, "提交失败: 当前活动不存在哦^_^", null);
        } else {
            String currentGoodsId = currentActivity.getCurrentGoodsId();
            if (!currentGoodsId.equals(goodsId)) {
                return new MyResult(false, "提交失败: 当前竞品竞价已经结束或者未开始哦^_^", null);
            }

            if (currentActivity.getRemainingTime() < 1) {
                return new MyResult(false, "提交失败: 当前竞品竞价已经结束哦^_^", null);
            }
        }

        return new MyResult(true,"",null);
    }

    @PostMapping("/submit")
    @RestfulResult
    @Transactional(rollbackFor = Exception.class)
    public Object submit(BigDecimal bid, Integer goodsId, String activityCode) {
        //业务描述：
        //1. 要求3个传入参数不能为空
        //2. 检查数据库中当前用户提交的最低价(db_bid)
        //3. 如果传入的bid大于数据库中db_bid 那么返回告诉用户 竞价失败....
        //4. 如果3不成立 走6
        //5. 如果2 返回 null 走6
        //6. 那么直接insert 到db

        MyResult myResult = checkSubmit(bid, goodsId, activityCode);
        if (myResult.isSuccess() == false) {
            return myResult;
        }

        JbxtUserDO userInfo = (JbxtUserDO) httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);
        //处理用户提交的bid

        JbxtBiddingDO curBidInfo = jbxtbiddingService.selectMinBidTableBy(userInfo.getCode(), goodsId, activityCode);  //获取当前用户对该竞品的最低报价(V2.0)
                //jbxtbiddingService.getCurrentGoodsMinSubmitPrice(userInfo.getCode(), goodsId, activityCode); //获取当前用户对该竞品的最低报价(V1.0)

        if (curBidInfo != null) {
            if (bid.compareTo(curBidInfo.getBid()) == -1) { //如果1 < 2 => -1
                return handlerBidDataToDB(activityCode, userInfo.getCode(), bid, goodsId, 2, curBidInfo.getCode());
            } else {
                Map<String, String> map = new HashMap();
                map.put("curMinPrice", curBidInfo.getBid().toString());
                return new MyResult(false, "提交失败: 您当前提交的竞价高于您之前提交的竞价(" + curBidInfo.getBid().toString() + ")哦^_^", map);
            }
        } else { //如果没有最低报价(意味着数据库中没有该竞品的提交数据) 那么直接插入
            return handlerBidDataToDB(activityCode, userInfo.getCode(), bid, goodsId,1);
        }
    }


    private Object handlerBidDataToDB(String activityCode, String userCode, BigDecimal bid, Integer goodsId, int status) {
        return handlerBidDataToDB(activityCode,userCode,bid,goodsId,status,null);
    }


    @Autowired
    private PushQueue pushQueue;

    /**
     * 保存数据到 db
     *
     * @param userCode
     * @param bid
     * @param goodsId
     * @param status 1表示插入minbidTable；2表示更新minBidTable
     * @return
     */
    private Object handlerBidDataToDB(String activityCode, String userCode, BigDecimal bid, Integer goodsId, int status, String minbidTableCode) {

        try {
            JbxtBiddingVO jbv = new JbxtBiddingVO();
            jbv.setBid(bid);
            jbv.setUserCode(userCode);
            jbv.setGoodsId(goodsId);
            jbv.setActivityCode(activityCode); //设置当前活动id

            //add to db
            jbxtbiddingService.insertJbxtBidding(jbv);

            if (status == 1) {
                jbxtbiddingService.insertMinBidTableSelective(jbv);
            } else if (status == 2) {
                jbv.setCode(minbidTableCode);
                jbxtbiddingService.updateMinBidTableByPrimaryKeySelective(jbv);
            }


            //获取当前用户的当前竞品的最新排名
            CustomBidVO cbv = jbxtGoodsService.handlerFindCustomBidVO(userCode, goodsId, activityCode);
            Map<String, String> map100 = new HashMap();
            map100.put("userRank", cbv.getUserRank().toString());
            map100.put("goodsPrice", cbv.getGoodsPrice().toString());
            map100.put("activityCode", activityCode);
            int gId1 = cbv.getGoodsId();
            Integer tgid = gId1;
            map100.put("goodsId", tgid.toString());

            //V2.0
            pushQueue.offer(new QueueMessage(100,map100));



            //业务处理
            return new MyResult(true, "提交成功", map100);
        } catch (Exception e) {
            return new MyResult(false, "提交失败: 保存失败", null);
        }
    }

}
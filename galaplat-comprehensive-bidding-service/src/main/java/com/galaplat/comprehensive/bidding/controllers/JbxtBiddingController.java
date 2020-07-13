package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.netty.UserChannelMap;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;


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
    IJbxtUserService iJbxtUserService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsController.class);

    @Autowired
    private ActivityMap activityMap;

    @Autowired
    private PushQueue pushQueue;



    @PostMapping("/submit")
    @RestfulResult
    public Object submit(BigDecimal bid, Integer goodsId, String activityCode) {
        MyResult myResult = checkSubmit(bid, goodsId, activityCode);
        if (myResult.isSuccess() == false) {
            return myResult;
        }

        JbxtUserDO userInfo = (JbxtUserDO) httpServletRequest.
                getSession().
                getAttribute(SessionConstant.SESSION_USER);

        //处理用户提交的bid
        JbxtBiddingDO curBidInfo =
                jbxtbiddingService.
                        selectMinBidTableBy(userInfo.getCode(), goodsId, activityCode);  //获取当前用户对该竞品的最低报价(V2.0)

        if (curBidInfo != null) {
            if (bid.compareTo(curBidInfo.getBid()) == -1) { //如果1 < 2 => -1
                //处理提交
                return saveBidDataToDB(activityCode, userInfo.getCode(), bid, goodsId, 2);
            } else {
                Map<String, String> map = new HashMap();
                map.put("curMinPrice", curBidInfo.getBid().toString());
                return new MyResult(false, "提交失败: 您当前提交的竞价高于您之前提交的竞价(" + curBidInfo.getBid().toString() + ")哦^_^", map);
            }
        }  else { //如果没有最低报价(意味着数据库中没有该竞品的提交数据) 那么直接插入
           return saveBidDataToDB(activityCode, userInfo.getCode(), bid, goodsId,1);
        }

    }

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
            if (!currentGoodsId.equals(goodsId.toString())) {
                return new MyResult(false, "提交失败: 当前竞品竞价已经结束或者未开始哦^_^", null);
            }

            if (currentActivity.getRemainingTime() < 1) {
                return new MyResult(false, "提交失败: 当前竞品竞价已经结束哦^_^", null);
            }
        }


        Map<String, String> map = new HashMap<>();
        String remainingTimeString = currentActivity.getRemainingTimeString();
        map.put("bidTime",remainingTimeString);

        return new MyResult(true,"",map);
    }

    private Object saveBidDataToDB(String activityCode, String userCode, BigDecimal bid, Integer goodsId, int status) {

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
            jbxtbiddingService.insertJbxtBidding(jbv);

            if (status == 1) { //插入
                jbxtbiddingService.insertMinBidTableSelective(jbv);
            } else if (status == 2) { //更新
                JbxtBiddingDO minbidE = jbxtbiddingService.selectMinBidTableBy(userCode, goodsId, activityCode);

                JbxtBiddingVO var1 = new JbxtBiddingVO();
                var1.setCode(minbidE.getCode());
                var1.setBid(bid);
                var1.setUpdatedTime(new Date());
                var1.setBidTime(bidTime);
                jbxtbiddingService.updateMinBidTableByPrimaryKeySelective(var1);
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


            //业务处理
            return new MyResult(true, "提交成功", null);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return new MyResult(false, "提交失败: 保存失败", null);
    }


    @GetMapping("/test")
    @RestfulResult
    public void test() {
        //
        Map<String, String> map200 = new HashMap();
        map200.put("activityCode", "1275271189644222464");
        map200.put("goodsId", "15");
        pushQueue.offer(new QueueMessage(200,map200));
    }

}
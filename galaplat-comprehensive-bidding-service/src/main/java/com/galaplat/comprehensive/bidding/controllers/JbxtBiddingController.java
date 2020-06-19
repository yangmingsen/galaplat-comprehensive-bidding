package com.galaplat.comprehensive.bidding.controllers;

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


    @PostMapping("/submit")
    @RestfulResult
    public Object submit(BigDecimal bid, Integer goodsId, String activityCode) {
        //业务描述：
        //1. 要求3个传入参数不能为空
        //2. 检查数据库中当前用户提交的最低价(db_bid)
        //3. 如果传入的bid大于数据库中db_bid 那么返回告诉用户 竞价失败....
        //4. 如果3不成立 走6
        //5. 如果2 返回 null 走6
        //6. 那么直接insert 到db
        if (bid == null) {
            return new MyResult(false, "提交失败: bid不能为空哦^_^", null);
        }
        if (goodsId == null) {
            return new MyResult(false, "提交失败: goodsId不能为空哦^_^", null);
        }
        if (activityCode == null || activityCode.equals("")) {
            return new MyResult(false, "提交失败: activityCode不能为空哦^_^", null);
        }

        JbxtUserDO userInfo = (JbxtUserDO) httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);

        //处理用户提交的bid
        JbxtBiddingDVO curBidInfo = jbxtbiddingService.getCurrentGoodsMinSubmitPrice(userInfo.getCode(), goodsId, activityCode); //获取当前用户对该竞品的最低报价
        if (curBidInfo != null) {
            if (bid.compareTo(curBidInfo.getBid()) == -1) {
                return handlerBidDataToDB(activityCode, userInfo.getCode(), bid, goodsId);
            } else {
                Map<String, String> map = new HashMap();
                map.put("curMinPrice", curBidInfo.getBid().toString());
                return new MyResult(false, "提交失败: 您当前提交的竞价高于您之前提交的竞价(" + curBidInfo.getBid().toString() + ")哦^_^", map);
            }
        } else { //如果没有最低报价(意味着数据库中没有该竞品的提交数据) 那么直接插入
            return handlerBidDataToDB(activityCode, userInfo.getCode(), bid, goodsId);
        }
    }


    /**
     * 保存数据到 db
     *
     * @param userCode
     * @param bid
     * @param goodsId
     * @return
     */
    private Object handlerBidDataToDB(String activityCode, String userCode, BigDecimal bid, Integer goodsId) {

        try {
            JbxtBiddingVO jbv = new JbxtBiddingVO();
            jbv.setBid(bid);
            jbv.setUserCode(userCode);
            jbv.setGoodsId(goodsId);
            jbv.setActivityCode(activityCode); //设置当前活动id

            //add to db
            jbxtbiddingService.insertJbxtBidding(jbv);

            //获取当前用户的当前竞品的最新排名
            CustomBidVO cbv = jbxtGoodsService.handlerFindCustomBidVO(userCode, goodsId, activityCode);
            Map<String, String> map = new HashMap();
            map.put("userRank", cbv.getUserRank().toString());

            //业务处理
            return new MyResult(true, "提交成功", map);
        } catch (Exception e) {
            return new MyResult(false, "提交失败: 保存失败", null);
        }
    }

}
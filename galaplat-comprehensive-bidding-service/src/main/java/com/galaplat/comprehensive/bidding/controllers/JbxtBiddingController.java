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
@RequestMapping("/bid")
public class JbxtBiddingController {


    @Autowired
    IJbxtBiddingService jbxtbiddingService;

    @Autowired
    IJbxtGoodsService jbxtGoodsService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Value("${my.activityCode}")
    private String activityCode;

    Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsController.class);



    @PostMapping("/submit")
    @RestfulResult
    public Object submit(BigDecimal bid, Integer goodsId, String activityCode) {
        LOGGER.info("JbxtBiddingController(submit): bid=" + bid + " goodsId=" + goodsId);

        if (bid != null && goodsId != null && activityCode != null) {

            JbxtUserDO userInfo = (JbxtUserDO) httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);

            JbxtBiddingDVO curBidInfo = jbxtbiddingService.getCurrentGoodsMinSubmitPrice(goodsId, activityCode);
            if (bid.compareTo(curBidInfo.getBid()) == -1) {
                try {
					JbxtBiddingVO jbv = new JbxtBiddingVO();
					jbv.setBid(bid);
					jbv.setUserCode(userInfo.getCode());
					jbv.setGoodsId(goodsId);
					jbv.setActivityCode(activityCode); //设置当前活动id

                    //add to db
                    jbxtbiddingService.insertJbxtBidding(jbv);

                    //获取当前用户的当前竞品的最新排名
                    CustomBidVO cbv = jbxtGoodsService.handlerFindCustomBidVO(userInfo.getCode(), goodsId, activityCode);
                    Map<String, String> map = new HashMap();
                    map.put("userRank", cbv.getUserRank().toString());

                    //业务处理
                    return new MyResult(true, "提交成功", map);
                } catch (Exception e) {
                    return new MyResult(false, "提交失败", null);
                }
            } else {

                Map<String, String> map = new HashMap();
                map.put("curMinPrice",curBidInfo.getBid().toString());

                return new MyResult(false, "提交失败: 您当前提交的竞价高于最低价", map);
            }

        }
        return new MyResult(false, "提交失败", null);
    }


    /**
     * 分页获取竞价表列表
     *
     * @param jbxtbiddingQuery
     * @return
     */
    @GetMapping("/list")
    @RestfulResult
    public Object getJbxtBiddingPage(JbxtBiddingQuery jbxtbiddingQuery) throws BaseException {

        return jbxtbiddingService.getJbxtBiddingPage(jbxtbiddingQuery);

    }


    /**
     * 新增竞价表
     *
     * @param jbxtbiddingVO
     * @return
     */
    @PostMapping
    @RestfulResult
    public Object insertJbxtBidding(JbxtBiddingVO jbxtbiddingVO) throws BaseException {

        return jbxtbiddingService.insertJbxtBidding(jbxtbiddingVO);
    }

    /**
     * 修改竞价表
     *
     * @param jbxtbiddingVO
     * @return
     */
    @PutMapping
    @RestfulResult
    public Object updateJbxtBidding(JbxtBiddingVO jbxtbiddingVO) throws BaseException {

        return jbxtbiddingService.updateJbxtBidding(jbxtbiddingVO);
    }


}
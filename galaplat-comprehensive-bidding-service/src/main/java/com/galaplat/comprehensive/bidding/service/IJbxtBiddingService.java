package com.galaplat.comprehensive.bidding.service;

import java.io.Serializable;
import java.util.List;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.querys.JbxtBiddingQuery;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;
import com.github.pagehelper.PageInfo;

 /**
 * 竞价表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtBiddingService{


    /**
	 * 添加竞价表
	 */
	int insertJbxtBidding(JbxtBiddingVO jbxtbiddingVO);

	/**
	 * 更新竞价表信息
	 */
	int updateJbxtBidding(JbxtBiddingVO jbxtbiddingVO);


	 public JbxtBiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode);


	 /**
	  * 获取当前竞品的最小提交价
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 public JbxtBiddingDVO getCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode);

}
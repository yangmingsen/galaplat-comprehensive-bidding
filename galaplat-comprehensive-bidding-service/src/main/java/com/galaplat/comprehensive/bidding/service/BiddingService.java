package com.galaplat.comprehensive.bidding.service;

import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dvos.BiddingDVO;
import com.galaplat.comprehensive.bidding.dao.dos.BiddingDO;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;

/**
 * 竞价表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface BiddingService {

	//---------------------
	 /***
	  * insert
	  * @param record
	  * @return
	  */
	 int insertMinBidTableSelective(JbxtBiddingVO record);

	 /***
	  * 获取当前用户最小竞价
	  * @param userCode
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 BiddingDO selectMinBidTableBy(String userCode, Integer goodsId, String activityCode);

	 /***
	  * 获取当前竞品所有用户最小竞价
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 List<BiddingDVO> selectMinBidTableBy(Integer goodsId, String activityCode);

	 /***
	  * update
	  * @param record
	  * @return
	  */
	 int updateMinBidTableByPrimaryKeySelective(JbxtBiddingVO record);

	 public int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

	//----------minBidTble opreation end-----------


	 List<BiddingDVO> getTheTopBids(Integer goodsId, String activityCode);


	 int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

    /**
	 * 添加竞价表
	 */
	int insertJbxtBidding(JbxtBiddingVO jbxtbiddingVO);

	/**
	 * 更新竞价表信息
	 */
	int updateJbxtBidding(JbxtBiddingVO jbxtbiddingVO);


	 public BiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode);


	 /**
	  * 获取当前竞品的最小提交价
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 public BiddingDVO getCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode);


	 /***
	  * 根据userCode和activityCode获取对应的竞价信息(也就是该用户的竞价记录) 根据竞价ASC
	  * @param userCode
	  * @param activityCode
	  * @return
	  */
	 public List<BiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode);

	 List<BiddingDVO> findAllByUserCodeAndGooodsIdAndActivityCode(String userCode, Integer goodsId, String activityCode);

}
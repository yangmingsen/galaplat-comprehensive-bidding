package com.galaplat.comprehensive.bidding.dao;

import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.BiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.BidDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.BiddingDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import com.github.pagehelper.PageInfo;

/**
 * 竞价表Dao
 * @author esr
 * @date: 2020年06月17日
 */
public interface BiddingDao {

	//---------v2.0 最小竞价表API-------

	 /***
	  * insert
	  * @param record
	  * @return
	  */
	 int insertMinBidTableSelective(BiddingDO record);

	 /***
	  * 获取当前用户对该竞品的最小竞价
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
	 int updateMinBidTableByPrimaryKeySelective(BiddingDO record);

	 public int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

	 //-------------------

	 public int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

    /**
	 * 添加竞价表
	 */
	int insertJbxtBidding(BiddingDO entity);

	/**
	 * 更新竞价表信息
	 */
	int updateJbxtBidding(BiddingDO entity);

	/**
	 * 分页获取竞价表列表
	 *
	 */
	public PageInfo<BiddingDVO> getJbxtBiddingPage(JbxtBiddingParam jbxtbiddingParam) throws BaseException;
	
    
    BiddingDO getJbxtBidding(JbxtBiddingParam jbxtbiddingParam);

	 /***
	  * 根据goodsId 获取所有相关联的竞价数据
	  * @param goodsId
	  * @return
	  */
	 public List<BiddingDVO> getJbxtListBiddingByGoodsId(Integer goodsId, String activityCode);


	 /***
	  * 根据userCode和activityCode获取对应的竞价信息(也就是该用户的竞价记录)
	  * @param userCode
	  * @param activityCode
	  * @return
	  */
	 public List<BiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode);


	/***
	 * 根据userCode和goodsId and activityCode获取对应的竞价信息(也就是该用户的竞价记录)
	 * @param userCode
	 * @param goodsId
	 * @param activityCode
	 * @return
	 */
	List<BiddingDVO> findAllByUserCodeAndGooodsIdAndActivityCode(String userCode, Integer goodsId, String activityCode);

	 /***
	  * 获取当前用户当前竞品最小竞价
	  * @param userCode
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 BiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode);

	 /**
	  * 获取当前用户竞品的最小提交价
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 BiddingDVO gerCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode);

	 /**
	  * 获取竞标活动的供应商
	  * @param biddingParam
	  * @return
	  */
	 List<String> listBidActivityUsers(JbxtBiddingParam biddingParam);

	/**
	 * 获取竞价活动
	 * @param biddingParam
	 * @return
	 */
	BidDVO getBidActivity(JbxtBiddingParam biddingParam);

	/**
	 * 获取一个供应商在一个竞标活动中的某个竞品的所有价格
	 * @param biddingParam
	 * @return
	 */
	List<BidDVO> getOneSupplierBidPriceDeatil(JbxtBiddingParam biddingParam);

	/**
	 * 删除竞价
	 * @param biddingParam
	 * @return
	 */
	int  deleteBidding(JbxtBiddingParam biddingParam);
}
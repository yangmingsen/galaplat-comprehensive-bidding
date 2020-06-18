package com.galaplat.comprehensive.bidding.dao;

import java.io.Serializable;
import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import com.github.pagehelper.PageInfo;

 /**
 * 竞价表Dao
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtBiddingDao{


    /**
	 * 添加竞价表
	 */
	int insertJbxtBidding(JbxtBiddingDO entity);

	/**
	 * 更新竞价表信息
	 */
	int updateJbxtBidding(JbxtBiddingDO entity);

	/**
	 * 分页获取竞价表列表
	 *
	 */
	public PageInfo<JbxtBiddingDVO> getJbxtBiddingPage(JbxtBiddingParam jbxtbiddingParam) throws BaseException;
	
    
    JbxtBiddingDO getJbxtBidding(JbxtBiddingParam jbxtbiddingParam);

	 /***
	  * 根据goodsId 获取所有相关联的竞价数据
	  * @param goodsId
	  * @return
	  */
	 public List<JbxtBiddingDVO> getJbxtListBiddingByGoodsId( String userCode, Integer goodsId, String activityCode);




	 /***
	  * 获取当前用户当前竞品最小竞价
	  * @param userCode
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 JbxtBiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode);

	 /**
	  * 获取当前竞品的最小提交价
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 JbxtBiddingDVO gerCurrentGoodsMinSubmitPrice( Integer goodsId, String activityCode);

}
package com.galaplat.comprehensive.bidding.service;

import java.io.Serializable;
import java.util.List;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.querys.JbxtGoodsQuery;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import com.github.pagehelper.PageInfo;

 /**
 * 竞品表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtGoodsService{


	 /***
	  * 给客户端
	  * @param activityCode
	  * @return
	  */
	 public List<CustomGoodsVO> findAllByActivityCode(String activityCode);

	 /**
	  * 给管理端使用
	  * @param activityCode
	  * @return
	  */
	 public List<SimpleGoodsVO> findAll(String activityCode);



	 public CustomBidVO findBidVOByGoodsId(Integer goodsId);


    /**
	 * 添加竞品表
	 */
	int insertJbxtGoods(JbxtGoodsVO jbxtgoodsVO);

	/**
	 * 更新竞品表信息
	 */
	int updateJbxtGoods(JbxtGoodsVO jbxtgoodsVO);

	/**
	 * 分页获取竞品表列表
	 *
	 */
	public PageInfo<JbxtGoodsDVO> getJbxtGoodsPage(JbxtGoodsQuery jbxtgoodsQuery) throws BaseException;
	
    /**
	 * 获取竞品表详情
	 *
	 */
    JbxtGoodsDO getJbxtGoods(JbxtGoodsQuery jbxtgoodsQuery);


}
package com.galaplat.comprehensive.bidding.service;

import java.io.Serializable;
import java.util.List;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
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
	  * @return  List CustomGoodsVO
	  */
	 public List<CustomGoodsVO> findAllByActivityCode(String activityCode);

	 /**
	  * 给管理端使用
	  * @param activityCode
	  * @return List SimpleGoodsVO
	  */
	 public List<SimpleGoodsVO> findAll(String activityCode);



	 /***
	  * 获取所有的GoodsDVO
	  * @param activityCode
	  * @return List JbxtGoodsDVO
	  */
	 public List<JbxtGoodsDVO> getListJbxtGoodsByActivityCode(String activityCode);


	 /***
	  * 通用获取当前用户排名情况方法
	  * @param userCode
	  * @param goodsId
	  * @return
	  */
	 public CustomBidVO handlerFindCustomBidVO(String userCode, Integer goodsId, String activityCode);


	 /**
	 * 添加竞品表
	 */
	int insertJbxtGoods(JbxtGoodsVO jbxtgoodsVO);

	/**
	 * 更新竞品表信息
	 */
	int updateJbxtGoods(JbxtGoodsVO jbxtgoodsVO);


	 public JbxtGoodsDO selectActiveGoods(String activityCode);


	 /***
	  * v2.0 获取当前用户对当前竞品的排名
	  * @param goodsId
	  * @param userCode
	  * @param activityCode
	  * @return
	  */
	 public CustomBidVO getUserBidRankInfoByUserCodeAndActivity(Integer goodsId, String userCode, String activityCode);

 }
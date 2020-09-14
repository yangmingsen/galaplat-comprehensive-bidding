package com.galaplat.comprehensive.bidding.service;

import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dvos.GoodsDVO;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;

/**
 * 竞品表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface GoodsService {



	 /**
	  * 给管理端使用
	  * @param activityCode
	  * @return List SimpleGoodsVO
	  */
	 public List<SimpleGoodsVO> findAll(String activityCode);




	 /***
	  * 获取所有的GoodsDVO
	  * @param activityCode
	  * @return List GoodsDVO
	  */
	 public List<GoodsDVO> getListJbxtGoodsByActivityCode(String activityCode);



	 /**
	 * 添加竞品表
	 */
	int insertJbxtGoods(JbxtGoodsVO jbxtgoodsVO);

	/**
	 * 更新竞品表信息
	 */
	int updateJbxtGoods(JbxtGoodsVO jbxtgoodsVO);


	 public GoodsDO selectActiveGoods(String activityCode);

	 public GoodsDO selectByGoodsId(Integer goodsId);


	 /***
	  * v2.0 获取当前用户对当前竞品的排名
	  * @param goodsId
	  * @param userCode
	  * @param activityCode
	  * @return
	  */
	 public CustomBidVO getUserBidRankInfoByUserCodeAndActivity(Integer goodsId, String userCode, String activityCode);

 }
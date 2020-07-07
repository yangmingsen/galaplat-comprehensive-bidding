package com.galaplat.comprehensive.bidding.dao.impl;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtBiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtBiddingDOMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

 /**
 * 竞价表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Repository
public   class JbxtBiddingDaoImpl implements IJbxtBiddingDao  {
	 @Override
	 public int insertMinBidTableSelective(JbxtBiddingDO record) {
		 return mapper.insertMinBidTableSelective(record);
	 }

	 @Override
	 public JbxtBiddingDO selectMinBidTableBy(String userCode, Integer goodsId, String activityCode) {
		 return mapper.selectMinBidTableByOne(userCode,goodsId,activityCode);
	 }

	 @Override
	 public List<JbxtBiddingDVO> selectMinBidTableBy(Integer goodsId, String activityCode) {
		 return mapper.selectMinBidTableByList(goodsId,activityCode);
	 }

	 @Override
	 public int updateMinBidTableByPrimaryKeySelective(JbxtBiddingDO record) {
		 return mapper.updateMinBidTableByPrimaryKeySelective(record);
	 }


	 public int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
	 	return mapper.deleteMinbidTableByGoodsIdAndActivityCode(goodsId,activityCode);
	 }

	 //--------------------------

	@Autowired
	private JbxtBiddingDOMapper mapper;

	 public int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
		 return mapper.deleteByGoodsIdAndActivityCode(goodsId,activityCode);
	 }

    @Override
	public int insertJbxtBidding(JbxtBiddingDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtBidding(JbxtBiddingDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public PageInfo<JbxtBiddingDVO> getJbxtBiddingPage(JbxtBiddingParam jbxtbiddingParam) throws BaseException{
	       PageHelper.startPage(jbxtbiddingParam.getPn(), jbxtbiddingParam.getPs());
		   return new PageInfo<JbxtBiddingDVO>(mapper.getJbxtBiddingList(jbxtbiddingParam));
	
	}
	
    @Override
    public JbxtBiddingDO getJbxtBidding(JbxtBiddingParam jbxtbiddingParam){
		return mapper.selectByPrimaryKey(jbxtbiddingParam.getCode());
    }

	 /***
	  *
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 @Override
	 public List<JbxtBiddingDVO> getJbxtListBiddingByGoodsId(Integer goodsId, String activityCode) {

		 List<JbxtBiddingDVO> list = mapper.getAllBidUserInfo(goodsId, activityCode); //获取所有正在竞价的用户
		Map<String, String> map = new HashMap<>();
		list.stream().forEach(x -> {
			map.put(x.getUserCode(),"1");
		});
		List<JbxtBiddingDVO> res = new ArrayList<>();
		 for(String key:map.keySet()) {
			 res.add(getUserMinBid(key, goodsId, activityCode)); //
		 }

		 return res.stream().sorted(Comparator.comparing(JbxtBiddingDVO::getBid)).collect(Collectors.toList());
	 }


	 /**
	  * 获取当前用户最小竞价
	  * @param userCode
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 public JbxtBiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode) {
    	return mapper.getUserMinBid(userCode,goodsId,activityCode);
	 }

	 public JbxtBiddingDVO gerCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode) {
    	return mapper.gerCurrentGoodsMinSubmitPrice(userCode,goodsId,activityCode);
	 }

	 public List<JbxtBiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode) {
	 	return mapper.findAllByUserCodeAndActivityCode(userCode,activityCode);
	 }


}
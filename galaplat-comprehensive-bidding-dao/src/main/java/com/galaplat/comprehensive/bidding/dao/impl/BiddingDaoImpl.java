package com.galaplat.comprehensive.bidding.dao.impl;

import java.util.*;
import java.util.stream.Collectors;

import com.galaplat.comprehensive.bidding.dao.dvos.BidDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.BiddingDVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.BiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.BiddingDO;
import com.galaplat.comprehensive.bidding.dao.mappers.custs.JbxtBiddingCustMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

 /**
 * 竞价表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Repository
public   class BiddingDaoImpl implements BiddingDao {
	 @Override
	 public int insertMinBidTableSelective(BiddingDO record) {
		 return mapper.insertMinBidTableSelective(record);
	 }

	 @Override
	 public BiddingDO selectMinBidTableBy(String userCode, Integer goodsId, String activityCode) {
		 return mapper.selectMinBidTableByOne(userCode,goodsId,activityCode);
	 }

	 @Override
	 public List<BiddingDVO> selectMinBidTableBy(Integer goodsId, String activityCode) {
		 return mapper.selectMinBidTableByList(goodsId,activityCode);
	 }

	 @Override
	 public int updateMinBidTableByPrimaryKeySelective(BiddingDO record) {
		 return mapper.updateMinBidTableByPrimaryKeySelective(record);
	 }


	 public int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
	 	return mapper.deleteMinbidTableByGoodsIdAndActivityCode(goodsId,activityCode);
	 }

	 //--------------------------

	@Autowired
	private JbxtBiddingCustMapper mapper;

	 public int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
		 return mapper.deleteByGoodsIdAndActivityCode(goodsId,activityCode);
	 }

    @Override
	public int insertJbxtBidding(BiddingDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtBidding(BiddingDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public PageInfo<BiddingDVO> getJbxtBiddingPage(JbxtBiddingParam jbxtbiddingParam) throws BaseException{
	       PageHelper.startPage(jbxtbiddingParam.getPn(), jbxtbiddingParam.getPs());
		   return new PageInfo<BiddingDVO>(mapper.getJbxtBiddingList(jbxtbiddingParam));

	}

    @Override
    public BiddingDO getJbxtBidding(JbxtBiddingParam jbxtbiddingParam){
		return mapper.selectByPrimaryKey(jbxtbiddingParam.getCode());
    }

	 /***
	  *
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 @Override
	 public List<BiddingDVO> getJbxtListBiddingByGoodsId(Integer goodsId, String activityCode) {

		 List<BiddingDVO> list = mapper.getAllBidUserInfo(goodsId, activityCode); //获取所有正在竞价的用户
		Map<String, String> map = new HashMap<>();
		list.stream().forEach(x -> {
			map.put(x.getUserCode(),"1");
		});
		List<BiddingDVO> res = new ArrayList<>();
		 for(String key:map.keySet()) {
			 res.add(getUserMinBid(key, goodsId, activityCode));
		 }

		 return res.stream().sorted(Comparator.comparing(BiddingDVO::getBid)).collect(Collectors.toList());
	 }


	 /**
	  * 获取当前用户最小竞价
	  * @param userCode
	  * @param goodsId
	  * @param activityCode
	  * @return
	  */
	 @Override
	 public BiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode) {
    	return mapper.getUserMinBid(userCode,goodsId,activityCode);
	 }

	 @Override
	 public BiddingDVO gerCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode) {
    	return mapper.gerCurrentGoodsMinSubmitPrice(userCode,goodsId,activityCode);
	 }

	 @Override
	 public List<BiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode) {
	 	return mapper.findAllByUserCodeAndActivityCode(userCode,activityCode);
	 }

	 public  List<BiddingDVO> findAllByUserCodeAndGooodsIdAndActivityCode(String userCode, Integer goodsId, String activityCode) {
	 	return mapper.findAllByUserCodeAndGooodsIdAndActivityCode(userCode, goodsId, activityCode);
	 }


	 @Override
	 public List<String> listBidActivityUsers(JbxtBiddingParam biddingParam) {
		 return mapper.listBidActivityUsers(biddingParam);
	 }

	 @Override
	 public BidDVO getBidActivity(JbxtBiddingParam biddingParam) {
		 return mapper.getBidActivity(biddingParam);
	 }

	 @Override
	 public  List<BidDVO> getOneSupplierBidPriceDeatil(JbxtBiddingParam biddingParam) {
		 return mapper.getOneSupplierBidPriceDeatil(biddingParam);
	 }

	 @Override
	 public int deleteBidding(JbxtBiddingParam biddingParam) {
		 return mapper.deleteBidding(biddingParam);
	 }

 }

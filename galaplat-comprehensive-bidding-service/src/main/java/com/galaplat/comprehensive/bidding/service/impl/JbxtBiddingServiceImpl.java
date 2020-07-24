package com.galaplat.comprehensive.bidding.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.utils.IdWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.BeanCopyUtils;
import com.galaplat.base.core.common.utils.HttpResultUtils;


import com.galaplat.comprehensive.bidding.dao.IJbxtBiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import com.galaplat.comprehensive.bidding.querys.JbxtBiddingQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * 竞价表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtBiddingServiceImpl implements IJbxtBiddingService  {

 	//--------------------------CRUD------------
 	@Override
	public int insertMinBidTableSelective(JbxtBiddingVO record) {
		record.setCode(idWorker.nextId());

		record.setCreatedTime(new Date());
		record.setUpdatedTime(new Date());

		JbxtBiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(JbxtBiddingDO.class, record);

		return jbxtbiddingDao.insertMinBidTableSelective(jbxtbiddingDO);
	}

	@Override
	public JbxtBiddingDO selectMinBidTableBy(String userCode, Integer goodsId, String activityCode) {
		return jbxtbiddingDao.selectMinBidTableBy(userCode,goodsId,activityCode);
	}

	@Override
	public List<JbxtBiddingDVO> selectMinBidTableBy(Integer goodsId, String activityCode) {
		return jbxtbiddingDao.selectMinBidTableBy(goodsId,activityCode);

	}

	@Override
	public int updateMinBidTableByPrimaryKeySelective(JbxtBiddingVO record) {
		JbxtBiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(JbxtBiddingDO.class, record);
		return jbxtbiddingDao.updateMinBidTableByPrimaryKeySelective(jbxtbiddingDO);
	}

	@Override
	public int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
		return jbxtbiddingDao.deleteMinbidTableByGoodsIdAndActivityCode(goodsId,activityCode);
	}

	//--------------------


	public List<JbxtBiddingDVO> getTheTopBids(Integer goodsId, String activityCode) {
		List<JbxtBiddingDVO> allUserMinBid = jbxtbiddingDao.selectMinBidTableBy(goodsId, activityCode);
		if (allUserMinBid.size() > 0) {
			BigDecimal bid = allUserMinBid.get(0).getBid();
			int lastIdx = -1;
			for (int i = 1; i < allUserMinBid.size(); i++) {
				JbxtBiddingDVO tMinBid = allUserMinBid.get(i);
				if (tMinBid.getBid().compareTo(bid) == 1) {
					lastIdx = i;break;
				}
			}
			if (lastIdx != -1) {
				return allUserMinBid.subList(0,lastIdx);
			} else {
				return allUserMinBid.subList(0,1);
			}
		}
		return allUserMinBid;
	}


	@Autowired
	IJbxtBiddingDao jbxtbiddingDao;

	@Override
	public int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
		return jbxtbiddingDao.deleteByGoodsIdAndActivityCode(goodsId,activityCode);
	}

	@Autowired
	private HttpServletRequest httpServletRequest;

	 @Autowired
	 private IdWorker idWorker;
	
    @Override
	@Transactional(rollbackFor = Exception.class)
	public int insertJbxtBidding(JbxtBiddingVO jbxtbiddingVO){


    		//设置code userCode activityCode   createdTime updateTime
			jbxtbiddingVO.setCode(idWorker.nextId());

			jbxtbiddingVO.setCreatedTime(new Date());
			jbxtbiddingVO.setUpdatedTime(new Date());


	       JbxtBiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(JbxtBiddingDO.class, jbxtbiddingVO);
	       return jbxtbiddingDao.insertJbxtBidding(jbxtbiddingDO );
	}



	public JbxtBiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode) {
		return jbxtbiddingDao.getUserMinBid(userCode,goodsId,activityCode);
	}


	@Override
	public int updateJbxtBidding(JbxtBiddingVO jbxtbiddingVO){
	      JbxtBiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(JbxtBiddingDO.class,jbxtbiddingVO);
		  jbxtbiddingDO.setUpdatedTime(new Date());
		  return jbxtbiddingDao.updateJbxtBidding(jbxtbiddingDO);
	}


	public JbxtBiddingDVO getCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode) {
    	return jbxtbiddingDao.gerCurrentGoodsMinSubmitPrice(userCode, goodsId,activityCode);
	}

	@Override
	public List<JbxtBiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode) {
		return jbxtbiddingDao.findAllByUserCodeAndActivityCode(userCode,activityCode);
	}

	public List<JbxtBiddingDVO> findAllByUserCodeAndGooodsIdAndActivityCode(String userCode, Integer goodsId, String activityCode) {
    	return jbxtbiddingDao.findAllByUserCodeAndGooodsIdAndActivityCode(userCode, goodsId, activityCode);
	}


}
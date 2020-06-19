package com.galaplat.comprehensive.bidding.service.impl;

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


	@Autowired
	IJbxtBiddingDao jbxtbiddingDao;

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

	@Override
	public PageInfo<JbxtBiddingDVO> getJbxtBiddingPage(JbxtBiddingQuery jbxtbiddingQuery) throws BaseException{
	        JbxtBiddingParam jbxtbiddingParam = BeanCopyUtils.copyProperties(JbxtBiddingParam.class, jbxtbiddingQuery);
			jbxtbiddingParam.setCompanyCode(jbxtbiddingQuery.getCompanyCode());
			jbxtbiddingParam.setSysCode(jbxtbiddingQuery.getSysCode());
		  return jbxtbiddingDao.getJbxtBiddingPage(jbxtbiddingParam);
	
	}
	
    @Override
    public JbxtBiddingDO getJbxtBidding(JbxtBiddingQuery jbxtbiddingQuery){
       JbxtBiddingParam jbxtbiddingParam = BeanCopyUtils.copyProperties(JbxtBiddingParam.class, jbxtbiddingQuery);
		return jbxtbiddingDao.getJbxtBidding(jbxtbiddingParam);
    }

	public JbxtBiddingDVO getCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode) {
    	return jbxtbiddingDao.gerCurrentGoodsMinSubmitPrice(userCode, goodsId,activityCode);
	}


}
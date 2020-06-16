package com.galaplat.comprehensive.bidding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

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

 /**
 * 竞价表ServiceImpl
 * @author esr
 * @date: 2020年06月16日
 */
 @Service
public  class JbxtBiddingServiceImpl implements IJbxtBiddingService  {


	@Autowired
	IJbxtBiddingDao jbxtbiddingDao;
	
    @Override
	public int insertJbxtBidding(JbxtBiddingVO jbxtbiddingVO){
	       JbxtBiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(JbxtBiddingDO.class, jbxtbiddingVO);
	       return jbxtbiddingDao.insertJbxtBidding(jbxtbiddingDO );
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
}
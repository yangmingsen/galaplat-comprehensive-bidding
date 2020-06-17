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


import com.galaplat.comprehensive.bidding.dao.IJbxtActivityDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.querys.JbxtActivityQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.vos.JbxtActivityVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

 /**
 * 活动表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtActivityServiceImpl implements IJbxtActivityService  {


	@Autowired
	IJbxtActivityDao jbxtactivityDao;
	
    @Override
	public int insertJbxtActivity(JbxtActivityVO jbxtactivityVO){
	       JbxtActivityDO jbxtactivityDO = BeanCopyUtils.copyProperties(JbxtActivityDO.class, jbxtactivityVO);
	       return jbxtactivityDao.insertJbxtActivity(jbxtactivityDO );
	}

	@Override
	public int updateJbxtActivity(JbxtActivityVO jbxtactivityVO){
	      JbxtActivityDO jbxtactivityDO = BeanCopyUtils.copyProperties(JbxtActivityDO.class,jbxtactivityVO);
		  jbxtactivityDO.setUpdatedTime(new Date());
		  return jbxtactivityDao.updateJbxtActivity(jbxtactivityDO);
	}

	@Override
	public PageInfo<JbxtActivityDVO> getJbxtActivityPage(JbxtActivityQuery jbxtactivityQuery) throws BaseException{
	        JbxtActivityParam jbxtactivityParam = BeanCopyUtils.copyProperties(JbxtActivityParam.class, jbxtactivityQuery);
			jbxtactivityParam.setCompanyCode(jbxtactivityQuery.getCompanyCode());
			jbxtactivityParam.setSysCode(jbxtactivityQuery.getSysCode());
		  return jbxtactivityDao.getJbxtActivityPage(jbxtactivityParam);
	
	}
	
    @Override
    public JbxtActivityDO getJbxtActivity(JbxtActivityQuery jbxtactivityQuery){
       JbxtActivityParam jbxtactivityParam = BeanCopyUtils.copyProperties(JbxtActivityParam.class, jbxtactivityQuery);
		return jbxtactivityDao.getJbxtActivity(jbxtactivityParam);
    }
}
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


import com.galaplat.comprehensive.bidding.dao.IJbxtAtivityDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtAtivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtAtivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtAtivityParam;
import com.galaplat.comprehensive.bidding.querys.JbxtAtivityQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtAtivityService;
import com.galaplat.comprehensive.bidding.vos.JbxtAtivityVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

 /**
 * 活动表ServiceImpl
 * @author esr
 * @date: 2020年06月16日
 */
 @Service
public  class JbxtAtivityServiceImpl implements IJbxtAtivityService  {


	@Autowired
	IJbxtAtivityDao jbxtativityDao;
	
    @Override
	public int insertJbxtAtivity(JbxtAtivityVO jbxtativityVO){
	       JbxtAtivityDO jbxtativityDO = BeanCopyUtils.copyProperties(JbxtAtivityDO.class, jbxtativityVO);
	       return jbxtativityDao.insertJbxtAtivity(jbxtativityDO );
	}

	@Override
	public int updateJbxtAtivity(JbxtAtivityVO jbxtativityVO){
	      JbxtAtivityDO jbxtativityDO = BeanCopyUtils.copyProperties(JbxtAtivityDO.class,jbxtativityVO);
		  jbxtativityDO.setUpdatedTime(new Date());
		  return jbxtativityDao.updateJbxtAtivity(jbxtativityDO);
	}

	@Override
	public PageInfo<JbxtAtivityDVO> getJbxtAtivityPage(JbxtAtivityQuery jbxtativityQuery) throws BaseException{
	        JbxtAtivityParam jbxtativityParam = BeanCopyUtils.copyProperties(JbxtAtivityParam.class, jbxtativityQuery);
			jbxtativityParam.setCompanyCode(jbxtativityQuery.getCompanyCode());
			jbxtativityParam.setSysCode(jbxtativityQuery.getSysCode());
		  return jbxtativityDao.getJbxtAtivityPage(jbxtativityParam);
	
	}
	
    @Override
    public JbxtAtivityDO getJbxtAtivity(JbxtAtivityQuery jbxtativityQuery){
       JbxtAtivityParam jbxtativityParam = BeanCopyUtils.copyProperties(JbxtAtivityParam.class, jbxtativityQuery);
		return jbxtativityDao.getJbxtAtivity(jbxtativityParam);
    }
}
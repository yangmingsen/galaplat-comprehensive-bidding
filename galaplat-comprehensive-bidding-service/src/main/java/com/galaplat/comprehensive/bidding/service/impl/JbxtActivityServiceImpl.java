package com.galaplat.comprehensive.bidding.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaplat.base.core.common.utils.BeanCopyUtils;


import com.galaplat.comprehensive.bidding.dao.ActivityDao;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.ActivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.querys.JbxtActivityQuery;
import com.galaplat.comprehensive.bidding.service.ActivityService;
import com.galaplat.comprehensive.bidding.vos.JbxtActivityVO;
import org.springframework.transaction.annotation.Transactional;

/**
 * 活动表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtActivityServiceImpl implements ActivityService {


	@Autowired
	ActivityDao activityDao;
	
    @Override
	public int insertJbxtActivity(JbxtActivityVO jbxtactivityVO){
	       ActivityDO jbxtactivityDO = BeanCopyUtils.copyProperties(ActivityDO.class, jbxtactivityVO);
	       return activityDao.insertJbxtActivity(jbxtactivityDO );
	}

	@Override
	public int updateJbxtActivity(JbxtActivityVO jbxtactivityVO){
	      ActivityDO jbxtactivityDO = BeanCopyUtils.copyProperties(ActivityDO.class,jbxtactivityVO);
		  jbxtactivityDO.setUpdatedTime(new Date());
		  return activityDao.updateJbxtActivity(jbxtactivityDO);
	}

	@Transactional(rollbackFor = Exception.class)
	public int updateByPrimaryKeySelective(ActivityDO entity) {
    	return activityDao.updateByPrimaryKeySelective(entity);
	}

	public ActivityDO findOneByCode(String code) {
    	return activityDao.findOneByCode(code);
	}




	 public List<ActivityDVO> findAll() {
    	return activityDao.findAll();
	 }
	
    @Override
    public ActivityDO getJbxtActivity(JbxtActivityQuery jbxtactivityQuery){
       JbxtActivityParam jbxtactivityParam = BeanCopyUtils.copyProperties(JbxtActivityParam.class, jbxtactivityQuery);
		return activityDao.getJbxtActivity(jbxtactivityParam);
    }

	public List<ActivityDVO> findAllByStatus(Integer status) {
    	return activityDao.findAllByStatus(status);
	}
}
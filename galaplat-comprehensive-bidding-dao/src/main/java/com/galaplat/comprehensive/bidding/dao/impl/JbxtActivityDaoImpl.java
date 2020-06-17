package com.galaplat.comprehensive.bidding.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtActivityDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtActivityDOMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

 /**
 * 活动表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Repository
public   class JbxtActivityDaoImpl implements IJbxtActivityDao  {

	@Autowired
	private JbxtActivityDOMapper mapper;
	
    @Override
	public int insertJbxtActivity(JbxtActivityDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtActivity(JbxtActivityDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public PageInfo<JbxtActivityDVO> getJbxtActivityPage(JbxtActivityParam jbxtactivityParam) throws BaseException{
	       PageHelper.startPage(jbxtactivityParam.getPn(), jbxtactivityParam.getPs());
		   return new PageInfo<JbxtActivityDVO>(mapper.getJbxtActivityList(jbxtactivityParam));
	
	}
	
    @Override
    public JbxtActivityDO getJbxtActivity(JbxtActivityParam jbxtactivityParam){
		return mapper.selectByPrimaryKey(jbxtactivityParam);
    }
}
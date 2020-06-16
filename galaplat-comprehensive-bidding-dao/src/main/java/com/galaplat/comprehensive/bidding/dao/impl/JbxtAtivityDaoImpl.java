package com.galaplat.comprehensive.bidding.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtAtivityDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtAtivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtAtivityDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtAtivityDOMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtAtivityParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

 /**
 * 活动表DaoImpl
 * @author esr
 * @date: 2020年06月16日
 */
 @Repository
public   class JbxtAtivityDaoImpl implements IJbxtAtivityDao  {

	@Autowired
	private JbxtAtivityDOMapper mapper;
	
    @Override
	public int insertJbxtAtivity(JbxtAtivityDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtAtivity(JbxtAtivityDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public PageInfo<JbxtAtivityDVO> getJbxtAtivityPage(JbxtAtivityParam jbxtativityParam) throws BaseException{
	       PageHelper.startPage(jbxtativityParam.getPn(), jbxtativityParam.getPs());
		  return new PageInfo<JbxtAtivityDVO>(mapper.getJbxtAtivityList(jbxtativityParam));
	
	}
	
    @Override
    public JbxtAtivityDO getJbxtAtivity(JbxtAtivityParam jbxtativityParam){
		return mapper.selectByPrimaryKey(jbxtativityParam);
    }
}
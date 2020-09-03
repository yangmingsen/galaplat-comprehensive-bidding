package com.galaplat.comprehensive.bidding.dao.impl;

import com.galaplat.comprehensive.bidding.dao.dvos.CompetitiveListDVO;
import com.galaplat.comprehensive.bidding.dao.params.CompetitiveListParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtActivityDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.custs.JbxtActivityCustMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 活动表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Repository
public   class JbxtActivityDaoImpl implements IJbxtActivityDao  {

	@Autowired
	private JbxtActivityCustMapper mapper;

    @Override
	public int insertJbxtActivity(JbxtActivityDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtActivity(JbxtActivityDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	public int updateByPrimaryKeySelective(JbxtActivityDO entity) {
    	return mapper.updateByPrimaryKeySelective(entity);
	}

	public JbxtActivityDO findOneByCode(String code) {
    	return mapper.selectByPrimaryKey(code);
	}


	@Override
	public PageInfo<JbxtActivityDVO> getJbxtActivityPage(JbxtActivityParam jbxtactivityParam) throws BaseException{
	       PageHelper.startPage(jbxtactivityParam.getPn(), jbxtactivityParam.getPs());
		   return new PageInfo<JbxtActivityDVO>(mapper.getJbxtActivityList(jbxtactivityParam));

	}

	@Override
	public List<JbxtActivityDVO> findAll(){
		return mapper.selectAll();
	}

    @Override
    public JbxtActivityDO getJbxtActivity(JbxtActivityParam jbxtactivityParam){
		return mapper.selectByPrimaryKey(jbxtactivityParam.getCode());
    }

	@Override
	public PageInfo listCompetitiveListPage(CompetitiveListParam param) {
		PageHelper.startPage(param.getPn(), param.getPs());
		List<CompetitiveListDVO>  list  = mapper.listCompetitiveListPage(param);
		PageInfo<CompetitiveListDVO> info = new PageInfo<>(list);
		return info;
	}

	@Override
	public int insertBidActivity(JbxtActivityDO entity){
		return mapper.insertSelective(entity);
	}

	@Override
	public int updateBidActivity(JbxtActivityDO entity) {
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int delete(String[] activityCodes) {
		return 0;
	}


}

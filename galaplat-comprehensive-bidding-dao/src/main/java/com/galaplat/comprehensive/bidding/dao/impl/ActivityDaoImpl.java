package com.galaplat.comprehensive.bidding.dao.impl;

import com.galaplat.comprehensive.bidding.dao.dvos.CompetitiveListDVO;
import com.galaplat.comprehensive.bidding.dao.params.CompetitiveListParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.galaplat.comprehensive.bidding.dao.ActivityDao;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.ActivityDVO;
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
public   class ActivityDaoImpl implements ActivityDao {

	@Autowired
	private JbxtActivityCustMapper mapper;

    @Override
	public int insertJbxtActivity(ActivityDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtActivity(ActivityDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	public int updateByPrimaryKeySelective(ActivityDO entity) {
    	return mapper.updateByPrimaryKeySelective(entity);
	}

	public ActivityDO findOneByCode(String code) {
    	return mapper.selectByPrimaryKey(code);
	}

	@Override
	public List<ActivityDVO> findAllByStatus(Integer status) {
		return mapper.selectAllByStatus(status);
	}

	@Override
	public List<ActivityDVO> findAll(){
		return mapper.selectAll();
	}

    @Override
    public ActivityDO getJbxtActivity(JbxtActivityParam jbxtactivityParam){
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
	public int insertBidActivity(ActivityDO entity){
		return mapper.insertSelective(entity);
	}

	@Override
	public int updateBidActivity(ActivityDO entity) {
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int delete(String[] activityCodes) {
		return 0;
	}


}

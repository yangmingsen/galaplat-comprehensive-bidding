package com.galaplat.comprehensive.bidding.service;

import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dvos.ActivityDVO;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.querys.JbxtActivityQuery;
import com.galaplat.comprehensive.bidding.vos.JbxtActivityVO;

/**
 * 活动表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface ActivityService {


    /**
	 * 添加活动表
	 */
	int insertJbxtActivity(JbxtActivityVO jbxtactivityVO);

	/**
	 * 更新活动表信息
	 */
	int updateJbxtActivity(JbxtActivityVO jbxtactivityVO);

	 int updateByPrimaryKeySelective(ActivityDO entity);

	 ActivityDO findOneByCode(String code);


    /**
	 * 获取活动表详情
	 *
	 */
    ActivityDO getJbxtActivity(JbxtActivityQuery jbxtactivityQuery);

    public List<ActivityDVO> findAll();

	List<ActivityDVO> findAllByStatus(Integer status);


}
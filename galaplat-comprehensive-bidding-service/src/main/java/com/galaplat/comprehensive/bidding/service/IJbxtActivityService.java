package com.galaplat.comprehensive.bidding.service;

import java.io.Serializable;
import java.util.List;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.querys.JbxtActivityQuery;
import com.galaplat.comprehensive.bidding.vos.JbxtActivityVO;
import com.github.pagehelper.PageInfo;

 /**
 * 活动表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtActivityService{


    /**
	 * 添加活动表
	 */
	int insertJbxtActivity(JbxtActivityVO jbxtactivityVO);

	/**
	 * 更新活动表信息
	 */
	int updateJbxtActivity(JbxtActivityVO jbxtactivityVO);

	/**
	 * 分页获取活动表列表
	 *
	 */
	public PageInfo<JbxtActivityDVO> getJbxtActivityPage(JbxtActivityQuery jbxtactivityQuery) throws BaseException;
	
    /**
	 * 获取活动表详情
	 *
	 */
    JbxtActivityDO getJbxtActivity(JbxtActivityQuery jbxtactivityQuery);
}
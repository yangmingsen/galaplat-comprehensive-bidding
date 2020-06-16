package com.galaplat.comprehensive.bidding.service;

import java.io.Serializable;
import java.util.List;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtAtivityDVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtAtivityDO;
import com.galaplat.comprehensive.bidding.querys.JbxtAtivityQuery;
import com.galaplat.comprehensive.bidding.vos.JbxtAtivityVO;
import com.github.pagehelper.PageInfo;

 /**
 * 活动表Service
 * @author esr
 * @date: 2020年06月16日
 */
public interface IJbxtAtivityService{


    /**
	 * 添加活动表
	 */
	int insertJbxtAtivity(JbxtAtivityVO jbxtativityVO);

	/**
	 * 更新活动表信息
	 */
	int updateJbxtAtivity(JbxtAtivityVO jbxtativityVO);

	/**
	 * 分页获取活动表列表
	 *
	 */
	public PageInfo<JbxtAtivityDVO> getJbxtAtivityPage(JbxtAtivityQuery jbxtativityQuery) throws BaseException;
	
    /**
	 * 获取活动表详情
	 *
	 */
    JbxtAtivityDO getJbxtAtivity(JbxtAtivityQuery jbxtativityQuery);
}
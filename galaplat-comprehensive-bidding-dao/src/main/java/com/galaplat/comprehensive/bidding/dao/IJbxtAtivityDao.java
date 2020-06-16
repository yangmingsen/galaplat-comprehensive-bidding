package com.galaplat.comprehensive.bidding.dao;

import java.io.Serializable;
import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtAtivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtAtivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtAtivityParam;
import com.github.pagehelper.PageInfo;

 /**
 * 活动表Dao
 * @author esr
 * @date: 2020年06月16日
 */
public interface IJbxtAtivityDao{


    /**
	 * 添加活动表
	 */
	int insertJbxtAtivity(JbxtAtivityDO entity);

	/**
	 * 更新活动表信息
	 */
	int updateJbxtAtivity(JbxtAtivityDO entity);

	/**
	 * 分页获取活动表列表
	 *
	 */
	public PageInfo<JbxtAtivityDVO> getJbxtAtivityPage(JbxtAtivityParam jbxtativityParam) throws BaseException;
	
    
    JbxtAtivityDO getJbxtAtivity(JbxtAtivityParam jbxtativityParam);
}
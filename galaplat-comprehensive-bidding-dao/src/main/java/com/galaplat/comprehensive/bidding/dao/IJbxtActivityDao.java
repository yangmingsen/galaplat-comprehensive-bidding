package com.galaplat.comprehensive.bidding.dao;

import java.io.Serializable;
import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.github.pagehelper.PageInfo;

 /**
 * 活动表Dao
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtActivityDao{


    /**
	 * 添加活动表
	 */
	int insertJbxtActivity(JbxtActivityDO entity);

	/**
	 * 更新活动表信息
	 */
	int updateJbxtActivity(JbxtActivityDO entity);

	/**
	 * 分页获取活动表列表
	 *
	 */
	public PageInfo<JbxtActivityDVO> getJbxtActivityPage(JbxtActivityParam jbxtactivityParam) throws BaseException;
	
    
    JbxtActivityDO getJbxtActivity(JbxtActivityParam jbxtactivityParam);
}
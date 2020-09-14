package com.galaplat.comprehensive.bidding.dao;

import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.CompetitiveListDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.ActivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.CompetitiveListParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.github.pagehelper.PageInfo;

 /**
 * 活动表Dao
 * @author esr
 * @date: 2020年06月17日
 */
public interface ActivityDao {


    /**
	 * 添加活动表
	 */
	int insertJbxtActivity(ActivityDO entity);

	 public List<ActivityDVO> findAll();

	/**
	 * 更新活动表信息
	 */
	int updateJbxtActivity(ActivityDO entity);

	int updateByPrimaryKeySelective(ActivityDO entity);

	 ActivityDO findOneByCode(String code);

	List<ActivityDVO> findAllByStatus(Integer status);


    ActivityDO getJbxtActivity(JbxtActivityParam jbxtactivityParam);

	 /**
	  * 竞标单管理列表查询
	  * @param param
	  * @return
	  */
	 PageInfo<CompetitiveListDVO> listCompetitiveListPage(CompetitiveListParam param);

    /**
	  * 竞标单删除
	  * @param activityCodes
	  * @return
	  */
   int delete(String[] activityCodes);

	 /**
	  * 新增竞标单
	  * @param entity
	  * @return
	  */
   int insertBidActivity(ActivityDO entity);

	 /**
	  * 更新竞标活动
	  */
	int updateBidActivity(ActivityDO entity);

}
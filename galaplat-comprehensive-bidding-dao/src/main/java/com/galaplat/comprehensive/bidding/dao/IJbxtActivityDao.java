package com.galaplat.comprehensive.bidding.dao;

import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.CompetitiveListDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.CompetitiveListParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 活动表Dao
 *
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtActivityDao {


	/**
	 * 添加活动表
	 */
	int insertJbxtActivity(JbxtActivityDO entity);

	public List<JbxtActivityDVO> findAll();

	/**
	 * 更新活动表信息
	 */
	int updateJbxtActivity(JbxtActivityDO entity);

	int updateByPrimaryKeySelective(JbxtActivityDO entity);

	JbxtActivityDO findOneByCode(String code);

	/**
	 * 分页获取活动表列表
	 */
	public PageInfo<JbxtActivityDVO> getJbxtActivityPage(JbxtActivityParam jbxtactivityParam) throws BaseException;


	JbxtActivityDO getJbxtActivity(JbxtActivityParam jbxtactivityParam);

	/**
	 * 竞标单管理列表查询
	 *
	 * @param param
	 * @return
	 */
	PageInfo<CompetitiveListDVO> listCompetitiveListPage(CompetitiveListParam param);

	/**
	 * 竞标单删除
	 *
	 * @param activityCodes
	 * @return
	 */
	int delete(String[] activityCodes);

	/**
	 * 新增竞标单
	 *
	 * @param entity
	 * @return
	 */
	int insertBidActivity(JbxtActivityDO entity);

	/**
	 * 更新竞标活动
	 */
	int updateBidActivity(JbxtActivityDO entity);

	 /**
	  *
	  * @return
	  */
	JbxtActivityDO getJbxtActivityByParam(JbxtActivityParam param);

	/***
	 * 修改竞标活动部门内容
	 * @param updateParam
	 * @param conditionParam
	 * @return
	 */
	int updateJbxtActivityBySomeParam(JbxtActivityParam updateParam, JbxtActivityParam conditionParam);

}

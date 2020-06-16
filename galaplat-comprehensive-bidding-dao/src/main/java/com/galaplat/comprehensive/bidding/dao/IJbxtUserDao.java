package com.galaplat.comprehensive.bidding.dao;

import java.io.Serializable;
import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.github.pagehelper.PageInfo;

 /**
 * 用户表Dao
 * @author esr
 * @date: 2020年06月16日
 */
public interface IJbxtUserDao{


    /**
	 * 添加用户表
	 */
	int insertJbxtUser(JbxtUserDO entity);

	/**
	 * 更新用户表信息
	 */
	int updateJbxtUser(JbxtUserDO entity);

	/**
	 * 分页获取用户表列表
	 *
	 */
	public PageInfo<JbxtUserDVO> getJbxtUserPage(JbxtUserParam jbxtuserParam) throws BaseException;
	
    
    JbxtUserDO getJbxtUser(JbxtUserParam jbxtuserParam);
}
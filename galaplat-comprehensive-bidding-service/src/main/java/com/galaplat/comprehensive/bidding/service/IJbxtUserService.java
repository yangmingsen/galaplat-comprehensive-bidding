package com.galaplat.comprehensive.bidding.service;

import java.io.Serializable;
import java.util.List;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.querys.JbxtUserQuery;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;
import com.github.pagehelper.PageInfo;

 /**
 * 用户表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtUserService{


	boolean handlerLogin(String username, String password);

	 JbxtUserDO getJbxtUserByUsername(String  username);

	 List<JbxtUserDVO> findAllByActivityCode(String activityCode);

	 public JbxtUserDO selectByuserCodeAndActivityCode(String userCode, String activityCode);

	 JbxtUserDO selectByUsernameAndActivityCode(String username, String activityCode);

    /**
	 * 添加用户表
	 */
	int insertJbxtUser(JbxtUserVO jbxtuserVO);

	/**
	 * 更新用户表信息
	 */
	int updateJbxtUser(JbxtUserVO jbxtuserVO);

	/**
	 * 分页获取用户表列表
	 *
	 */
	public PageInfo<JbxtUserDVO> getJbxtUserPage(JbxtUserQuery jbxtuserQuery) throws BaseException;
	
    /**
	 * 获取用户表详情
	 *
	 */
    JbxtUserDO getJbxtUser(JbxtUserQuery jbxtuserQuery);
}
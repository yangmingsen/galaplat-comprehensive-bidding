package com.galaplat.comprehensive.bidding.service;

import java.util.List;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.UserDVO;
import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import com.galaplat.comprehensive.bidding.querys.JbxtUserQuery;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;
import com.github.pagehelper.PageInfo;

 /**
 * 用户表Service
 * @author esr
 * @date: 2020年06月17日
 */
public interface UserService {

	 UserDO getJbxtUserByUsername(String  username);

	 List<UserDVO> findAllByActivityCode(String activityCode);

	 public UserDO selectByuserCodeAndActivityCode(String userCode, String activityCode);

	 UserDO selectByUsernameAndActivityCode(String username, String activityCode);

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
	public PageInfo<UserDVO> getJbxtUserPage(JbxtUserQuery jbxtuserQuery) throws BaseException;
	
    /**
	 * 获取用户表详情
	 *
	 */
    UserDO getJbxtUser(JbxtUserQuery jbxtuserQuery);
}
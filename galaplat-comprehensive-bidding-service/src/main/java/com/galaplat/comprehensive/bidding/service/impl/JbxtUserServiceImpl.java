package com.galaplat.comprehensive.bidding.service.impl;

import java.util.Date;
import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.BeanCopyUtils;


import com.galaplat.comprehensive.bidding.dao.UserDao;
import com.galaplat.comprehensive.bidding.dao.dvos.UserDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.galaplat.comprehensive.bidding.querys.JbxtUserQuery;
import com.galaplat.comprehensive.bidding.service.UserService;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;
import com.github.pagehelper.PageInfo;

/**
 * 用户表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtUserServiceImpl implements UserService {

 	private final Logger LOGGER = LoggerFactory.getLogger(JbxtUserServiceImpl.class);

	@Autowired
	private UserDao jbxtuserDao;

	public UserDO selectByuserCodeAndActivityCode(String userCode, String activityCode) {
	 	return jbxtuserDao.selectByuserCodeAndActivityCode(userCode,activityCode);
	}

	public UserDO selectByUsernameAndActivityCode(String username, String activityCode) {
	 	return jbxtuserDao.selectByUsernameAndActivityCode(username,activityCode);
	}


	@Override
	public UserDO getJbxtUserByUsername(String username) {
		return jbxtuserDao.getJbxtUserByUsername(username);
	}

	public List<UserDVO> findAllByActivityCode(String activityCode) {
	 	return jbxtuserDao.findAllByActivityCode(activityCode);
	}


	@Override
	public int insertJbxtUser(JbxtUserVO jbxtuserVO){
	       UserDO jbxtuserDO = BeanCopyUtils.copyProperties(UserDO.class, jbxtuserVO);
	       return jbxtuserDao.insertJbxtUser(jbxtuserDO );
	}

	@Override
	public int updateJbxtUser(JbxtUserVO jbxtuserVO){
	      UserDO jbxtuserDO = BeanCopyUtils.copyProperties(UserDO.class,jbxtuserVO);
		  jbxtuserDO.setUpdatedTime(new Date());
		  return jbxtuserDao.updateJbxtUser(jbxtuserDO);
	}

	@Override
	public PageInfo<UserDVO> getJbxtUserPage(JbxtUserQuery jbxtuserQuery) throws BaseException{
	        JbxtUserParam jbxtuserParam = BeanCopyUtils.copyProperties(JbxtUserParam.class, jbxtuserQuery);
			jbxtuserParam.setCompanyCode(jbxtuserQuery.getCompanyCode());
			jbxtuserParam.setSysCode(jbxtuserQuery.getSysCode());
		  return jbxtuserDao.getJbxtUserPage(jbxtuserParam);
	
	}
	
    @Override
    public UserDO getJbxtUser(JbxtUserQuery jbxtuserQuery){
       JbxtUserParam jbxtuserParam = BeanCopyUtils.copyProperties(JbxtUserParam.class, jbxtuserQuery);
		return jbxtuserDao.getJbxtUser(jbxtuserParam);
    }


}
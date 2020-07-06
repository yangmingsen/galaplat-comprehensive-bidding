package com.galaplat.comprehensive.bidding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.BeanCopyUtils;


import com.galaplat.comprehensive.bidding.dao.IJbxtUserDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.galaplat.comprehensive.bidding.querys.JbxtUserQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtUserServiceImpl implements IJbxtUserService  {

 	Logger LOGGER = LoggerFactory.getLogger(JbxtUserServiceImpl.class);

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	IJbxtUserDao jbxtuserDao;

	 @Override
	 public boolean handlerLogin(String username, String password) {

		 JbxtUserDO jud = jbxtuserDao.getJbxtUserByUsername(username);
		 if (jud != null) {
		 	if (jud.getPassword().equals(password)) {
		 		LOGGER.info("LoginInfo: "+username+" login");
				//存入session中
				httpServletRequest.getSession().setAttribute(SessionConstant.SESSION_USER,jud);

		 		return true;
			}
		 }
		 return false;
	 }

	public JbxtUserDO selectByuserCodeAndActivityCode(String userCode, String activityCode) {
	 	return jbxtuserDao.selectByuserCodeAndActivityCode(userCode,activityCode);
	}


	@Override
	public JbxtUserDO getJbxtUserByUsername(String username) {
		return jbxtuserDao.getJbxtUserByUsername(username);
	}

	public List<JbxtUserDVO> findAllByActivityCode(String activityCode) {
	 	return jbxtuserDao.findAllByActivityCode(activityCode);
	}


	@Override
	public int insertJbxtUser(JbxtUserVO jbxtuserVO){
	       JbxtUserDO jbxtuserDO = BeanCopyUtils.copyProperties(JbxtUserDO.class, jbxtuserVO);
	       return jbxtuserDao.insertJbxtUser(jbxtuserDO );
	}

	@Override
	public int updateJbxtUser(JbxtUserVO jbxtuserVO){
	      JbxtUserDO jbxtuserDO = BeanCopyUtils.copyProperties(JbxtUserDO.class,jbxtuserVO);
		  jbxtuserDO.setUpdatedTime(new Date());
		  return jbxtuserDao.updateJbxtUser(jbxtuserDO);
	}

	@Override
	public PageInfo<JbxtUserDVO> getJbxtUserPage(JbxtUserQuery jbxtuserQuery) throws BaseException{
	        JbxtUserParam jbxtuserParam = BeanCopyUtils.copyProperties(JbxtUserParam.class, jbxtuserQuery);
			jbxtuserParam.setCompanyCode(jbxtuserQuery.getCompanyCode());
			jbxtuserParam.setSysCode(jbxtuserQuery.getSysCode());
		  return jbxtuserDao.getJbxtUserPage(jbxtuserParam);
	
	}
	
    @Override
    public JbxtUserDO getJbxtUser(JbxtUserQuery jbxtuserQuery){
       JbxtUserParam jbxtuserParam = BeanCopyUtils.copyProperties(JbxtUserParam.class, jbxtuserQuery);
		return jbxtuserDao.getJbxtUser(jbxtuserParam);
    }


}
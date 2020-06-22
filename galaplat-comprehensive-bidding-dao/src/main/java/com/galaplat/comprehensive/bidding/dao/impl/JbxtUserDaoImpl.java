package com.galaplat.comprehensive.bidding.dao.impl;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtUserDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtUserDOMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

 /**
 * 用户表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Repository
public   class JbxtUserDaoImpl implements IJbxtUserDao  {

	@Autowired
	private JbxtUserDOMapper mapper;
	
    @Override
	public int insertJbxtUser(JbxtUserDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtUser(JbxtUserDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public PageInfo<JbxtUserDVO> getJbxtUserPage(JbxtUserParam jbxtuserParam) throws BaseException{
	       PageHelper.startPage(jbxtuserParam.getPn(), jbxtuserParam.getPs());
		   return new PageInfo<JbxtUserDVO>(mapper.getJbxtUserList(jbxtuserParam));
	
	}
	
    @Override
    public JbxtUserDO getJbxtUser(JbxtUserParam jbxtuserParam){
		return mapper.selectByPrimaryKey(jbxtuserParam.getCode());
    }


	 public JbxtUserDO getJbxtUserByUsername(String username){
		 return mapper.selectByUsernameKey(username);
	 }
}
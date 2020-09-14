package com.galaplat.comprehensive.bidding.dao.impl;

import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.SupplierAccountExportDVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.UserDao;
import com.galaplat.comprehensive.bidding.dao.dvos.UserDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.custs.JbxtUserCustMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 用户表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
@Repository
public   class UserDaoImpl implements UserDao {

	@Autowired
	private JbxtUserCustMapper mapper;

	@Override
	public List<UserDVO> findAllByActivityCode(String activityCode) {
		return mapper.findAllByActivityCode(activityCode);
	}

	@Override
	public int insertJbxtUser(UserDO entity){
		return mapper.insert(entity);
	}

	@Override
	public int updateJbxtUser(UserDO entity){
		return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public PageInfo<UserDVO> getJbxtUserPage(JbxtUserParam jbxtuserParam) throws BaseException{
		PageHelper.startPage(jbxtuserParam.getPn(), jbxtuserParam.getPs());
		return new PageInfo<UserDVO>(mapper.getJbxtUserList(jbxtuserParam));

	}

	@Override
	public UserDO getJbxtUser(JbxtUserParam jbxtuserParam){
		return mapper.selectByPrimaryKey(jbxtuserParam.getCode());
	}

	@Override
	public UserDO getJbxtUserByUsername(String username){
		return mapper.selectByUsernameKey(username);
	}

	@Override
	public UserDO selectByuserCodeAndActivityCode(String userCode, String activityCode) {
		return mapper.selectByuserCodeAndActivityCode(userCode,activityCode);
	}

	@Override
	public UserDO selectByUsernameAndActivityCode(String username, String activityCode) {
		return mapper.selectByUsernameAndActivityCode(username,activityCode);
	}

	@Override
	public int btachInsertAndUpdate(List<JbxtUserParam> userParams) {
		return mapper.btachInsertAndUpdate(userParams);
	}

	@Override
	public List<UserDO> getUser(JbxtUserParam userParam) {
		return mapper.getUser(userParam);
	}

	@Override
	public List<SupplierAccountExportDVO> getAccountByActivityCode(JbxtUserParam userParam) {
		return mapper.getAccountByActivityCode(userParam);
	}

	@Override
	public int deleteUser(JbxtUserParam userParam) {
		return mapper.deleteUser(userParam);
	}

	@Override
	public int batchDeleteUser(List<String> userCodes, String activityCode) {
		return mapper.batchDeleteUser(userCodes, activityCode);
	}


}

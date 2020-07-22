package com.galaplat.comprehensive.bidding.dao;

import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.SupplierAccountExportDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.github.pagehelper.PageInfo;

/**
 * 用户表Dao
 * @author esr
 * @date: 2020年06月17日
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


	JbxtUserDO getJbxtUserByUsername(String  username);


	JbxtUserDO selectByuserCodeAndActivityCode(String userCode, String activityCode);

	JbxtUserDO selectByUsernameAndActivityCode(String username, String activityCode);

	List<JbxtUserDVO> findAllByActivityCode(String activityCode);

	/**
	 *  批量新增或者更新供应商信息
	 * @param userParams
	 * @return
	 */
	int btachInsertAndUpdate(List<JbxtUserParam> userParams);

	/**
	 * 获取用户信息
	 * @param userParam
	 * @return
	 */
	List<JbxtUserDO> getUser(JbxtUserParam userParam);

	/**
	 * 获取供应商站账号
	 * @param userParam
	 * @return
	 */
	List<SupplierAccountExportDVO> getAccountByActivityCode(JbxtUserParam userParam);

	/**
	 * 删除供应商
	 * @param userParam
	 * @return
	 */
	int deleteUser(JbxtUserParam userParam);

	/**
	 * 批量删除供应商
	 * @param userCodes
	 * @param activityCode
	 * @return
	 */
	int batchDeleteUser(List<String> userCodes,String activityCode);
}
package com.galaplat.comprehensive.bidding.dao;

import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.UserDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.SupplierAccountExportDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.BidSupplierDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

/**
 * 用户表Dao
 * @author esr
 * @date: 2020年06月17日
 */
public interface UserDao {


	/**
	 * 添加用户表
	 */
	int insertJbxtUser(UserDO entity);

	/**
	 * 更新用户表信息
	 */
	int updateJbxtUser(UserDO entity);

	/**
	 * 分页获取用户表列表
	 *
	 */
	public PageInfo<UserDVO> getJbxtUserPage(JbxtUserParam jbxtuserParam) throws BaseException;


	UserDO getJbxtUser(JbxtUserParam jbxtuserParam);


	UserDO getJbxtUserByUsername(String  username);


	UserDO selectByuserCodeAndActivityCode(String userCode, String activityCode);

	UserDO selectByUsernameAndActivityCode(String username, String activityCode);

	List<UserDVO> findAllByActivityCode(String activityCode);

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
	List<UserDO> getUser(JbxtUserParam userParam);

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

	/**
	 * 查询供应商
	 * @param param
	 * @return
	 */
	List<UserDO> listJbxtUser(@Param("param") JbxtUserParam param);

	/**
	 * 修改
	 * @param updateParam
	 * @param conditionParam
	 * @return
	 */
	int updateBySomeParam(JbxtUserParam updateParam,  JbxtUserParam conditionParam);



	/**
	 * 查询供应商
	 * @param bidActivityCode
	 * @return
	 */
	List<BidSupplierDVO> listSupplierInfo(String bidActivityCode);

	/***
	 * 获取供应商
	 * @param param
	 * @return
	 */
	UserDO getUserByParam( JbxtUserParam param);
}

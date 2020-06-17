package com.galaplat.comprehensive.bidding.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.baseplatform.permissions.controllers.BaseController;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.querys.JbxtUserQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtUserService;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;


 /**
 * 用户表Controller
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/jbxtuser")
public  class JbxtUserController extends BaseController {


	@Autowired
	IJbxtUserService jbxtuserService;
	
	
	/**
	 * 分页获取用户表列表
	 * @param jbxtuserQuery
	 * @return
	 */
	@GetMapping("/list")
	@RestfulResult
	public Object getJbxtUserPage(JbxtUserQuery jbxtuserQuery) throws BaseException{

		  return jbxtuserService.getJbxtUserPage( jbxtuserQuery);
	
	}
	
	
	/**
	 * 新增用户表
	 * @param jbxtuserVO
	 * @return
	 */
   	@PostMapping
	@RestfulResult
	public Object insertJbxtUser(JbxtUserVO jbxtuserVO) throws BaseException {

	   return jbxtuserService.insertJbxtUser(jbxtuserVO);
	}

	/**
	 * 修改用户表
	 * @param jbxtuserVO
	 * @return
	 */
	@PutMapping
	@RestfulResult
	public Object updateJbxtUser(JbxtUserVO jbxtuserVO) throws BaseException {

		return jbxtuserService.updateJbxtUser(jbxtuserVO);
	}

    /**
	 * 获取上架单详情
	 * @param jbxtuserQuery
	 * @return
	 */
	@GetMapping()
	@RestfulResult
    public JbxtUserDO getJbxtUser(JbxtUserQuery jbxtuserQuery) throws BaseException {
    
		return jbxtuserService.getJbxtUser(jbxtuserQuery);
    }
}
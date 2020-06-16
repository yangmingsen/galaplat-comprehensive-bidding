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
import com.github.pagehelper.PageInfo;
import com.galaplat.comprehensive.bidding.querys.JbxtAtivityQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtAtivityService;
import com.galaplat.comprehensive.bidding.vos.JbxtAtivityVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtAtivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtAtivityDVO;


 /**
 * 活动表Controller
 * @author esr
 * @date: 2020年06月16日
 */
@RestController
@RequestMapping("/jbxtativity")
public  class JbxtAtivityController extends BaseController {


	@Autowired
	IJbxtAtivityService jbxtativityService;
	
	
	/**
	 * 分页获取活动表列表
	 * @param jbxtativityQuery
	 * @return
	 */
	@GetMapping("/list")
	@RestfulResult
	public Object getJbxtAtivityPage(JbxtAtivityQuery jbxtativityQuery) throws BaseException{
		  return jbxtativityService.getJbxtAtivityPage( jbxtativityQuery);
	
	}
	
	
	/**
	 * 新增活动表
	 * @param jbxtativityVO
	 * @return
	 */
   	@PostMapping
	@RestfulResult
	public Object insertJbxtAtivity(JbxtAtivityVO jbxtativityVO) throws BaseException {
	   return jbxtativityService.insertJbxtAtivity(jbxtativityVO);
	}

	/**
	 * 修改活动表
	 * @param jbxtativityVO
	 * @return
	 */
	@PutMapping
	@RestfulResult
	public Object updateJbxtAtivity(JbxtAtivityVO jbxtativityVO) throws BaseException {
	
		return jbxtativityService.updateJbxtAtivity(jbxtativityVO);
	}

    /**
	 * 获取上架单详情
	 * @param jbxtativityQuery
	 * @return
	 */
	@GetMapping()
	@RestfulResult
    public JbxtAtivityDO getJbxtAtivity(JbxtAtivityQuery jbxtativityQuery) throws BaseException {
    
		return jbxtativityService.getJbxtAtivity(jbxtativityQuery);
    }
}
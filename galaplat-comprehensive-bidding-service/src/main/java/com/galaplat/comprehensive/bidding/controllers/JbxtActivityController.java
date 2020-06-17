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
import com.galaplat.comprehensive.bidding.querys.JbxtActivityQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.vos.JbxtActivityVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;


 /**
 * 活动表Controller
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/jbxtactivity")
public  class JbxtActivityController extends BaseController {


	@Autowired
	IJbxtActivityService jbxtactivityService;
	
	
	/**
	 * 分页获取活动表列表
	 * @param jbxtactivityQuery
	 * @return
	 */
	@GetMapping("/list")
	@RestfulResult
	public Object getJbxtActivityPage(JbxtActivityQuery jbxtactivityQuery) throws BaseException{

		  return jbxtactivityService.getJbxtActivityPage( jbxtactivityQuery);
	
	}
	
	
	/**
	 * 新增活动表
	 * @param jbxtactivityVO
	 * @return
	 */
   	@PostMapping
	@RestfulResult
	public Object insertJbxtActivity(JbxtActivityVO jbxtactivityVO) throws BaseException {
	
	   return jbxtactivityService.insertJbxtActivity(jbxtactivityVO);
	}

	/**
	 * 修改活动表
	 * @param jbxtactivityVO
	 * @return
	 */
	@PutMapping
	@RestfulResult
	public Object updateJbxtActivity(JbxtActivityVO jbxtactivityVO) throws BaseException {
	
		return jbxtactivityService.updateJbxtActivity(jbxtactivityVO);
	}

    /**
	 * 获取上架单详情
	 * @param jbxtactivityQuery
	 * @return
	 */
	@GetMapping()
	@RestfulResult
    public JbxtActivityDO getJbxtActivity(JbxtActivityQuery jbxtactivityQuery) throws BaseException {

		return jbxtactivityService.getJbxtActivity(jbxtactivityQuery);
    }
}
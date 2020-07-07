package com.galaplat.comprehensive.bidding.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.comprehensive.bidding.querys.JbxtActivityQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.vos.JbxtActivityVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;


/**
 * 活动表Controller
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/jbxtactivity")
public  class JbxtActivityController  {


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

}
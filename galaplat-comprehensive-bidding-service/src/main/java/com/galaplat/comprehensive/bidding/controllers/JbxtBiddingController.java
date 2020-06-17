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
import com.galaplat.comprehensive.bidding.querys.JbxtBiddingQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;


 /**
 * 竞价表Controller
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/jbxtbidding")
public  class JbxtBiddingController extends BaseController {


	@Autowired
	IJbxtBiddingService jbxtbiddingService;
	
	
	/**
	 * 分页获取竞价表列表
	 * @param jbxtbiddingQuery
	 * @return
	 */
	@GetMapping("/list")
	@RestfulResult
	public Object getJbxtBiddingPage(JbxtBiddingQuery jbxtbiddingQuery) throws BaseException{
	
		  return jbxtbiddingService.getJbxtBiddingPage( jbxtbiddingQuery);
	
	}
	
	
	/**
	 * 新增竞价表
	 * @param jbxtbiddingVO
	 * @return
	 */
   	@PostMapping
	@RestfulResult
	public Object insertJbxtBidding(JbxtBiddingVO jbxtbiddingVO) throws BaseException {
	
	   return jbxtbiddingService.insertJbxtBidding(jbxtbiddingVO);
	}

	/**
	 * 修改竞价表
	 * @param jbxtbiddingVO
	 * @return
	 */
	@PutMapping
	@RestfulResult
	public Object updateJbxtBidding(JbxtBiddingVO jbxtbiddingVO) throws BaseException {
	
		return jbxtbiddingService.updateJbxtBidding(jbxtbiddingVO);
	}

    /**
	 * 获取上架单详情
	 * @param jbxtbiddingQuery
	 * @return
	 */
	@GetMapping()
	@RestfulResult
    public JbxtBiddingDO getJbxtBidding(JbxtBiddingQuery jbxtbiddingQuery) throws BaseException {
    
		jbxtbiddingQuery.setCompanyCode(getCompanyCode() == null ? "" : getCompanyCode());
		jbxtbiddingQuery.setSysCode(getSysCode() == null ? "" : getSysCode());
		return jbxtbiddingService.getJbxtBidding(jbxtbiddingQuery);
    }
}
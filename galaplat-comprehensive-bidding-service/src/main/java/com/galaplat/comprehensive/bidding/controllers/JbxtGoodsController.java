package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.comprehensive.bidding.service.impl.JbxtGoodsServiceImpl;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.MyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.galaplat.comprehensive.bidding.querys.JbxtGoodsQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;

import java.util.List;


/**
 * 竞品表Controller
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/goods")
public  class JbxtGoodsController extends BaseController {


	@Autowired
	IJbxtGoodsService jbxtgoodsService;

	Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsController.class);


	 /**
	  * 获取竞品表列表
	  * @param activityCode
	  * @return
	  */
	 @GetMapping("/findAll")
	 @RestfulResult
	 public Object findAll(String activityCode) throws BaseException{
	 	LOGGER.info("JbxtGoodsController(findAll): activityCode="+activityCode);
	 	if (activityCode != null && (!activityCode.equals(""))) {
			return new MyResult(true,"获取data成功", jbxtgoodsService.findAllByActivityCode(activityCode));
		}
		 return new MyResult(true,"获取data失败", null);
	 }

	/**
	 * 获取某个竞品的竞价信息
	 * @param goodsId
	 * @return
	 * @throws BaseException
	 */
	@GetMapping("/findOne")
	@RestfulResult
	public Object findOne(Integer goodsId) throws BaseException{
		LOGGER.info("JbxtGoodsController(findOne): goodsId="+goodsId);

		if (goodsId != null ) {
			return new MyResult(true,"获取data成功", jbxtgoodsService.findBidVOByGoodsId(goodsId));
		}
		return new MyResult(true,"获取data失败", null);
	}


	/**
	 * 分页获取竞品表列表
	 * @param jbxtgoodsQuery
	 * @return
	 */
	@GetMapping("/list")
	@RestfulResult
	public Object getJbxtGoodsPage(JbxtGoodsQuery jbxtgoodsQuery) throws BaseException{
		  return jbxtgoodsService.getJbxtGoodsPage( jbxtgoodsQuery);
	}


	
	/**
	 * 新增竞品表
	 * @param jbxtgoodsVO
	 * @return
	 */
   	@PostMapping
	@RestfulResult
	public Object insertJbxtGoods(JbxtGoodsVO jbxtgoodsVO) throws BaseException {
	
	   return jbxtgoodsService.insertJbxtGoods(jbxtgoodsVO);
	}

	/**
	 * 修改竞品表
	 * @param jbxtgoodsVO
	 * @return
	 */
	@PutMapping
	@RestfulResult
	public Object updateJbxtGoods(JbxtGoodsVO jbxtgoodsVO) throws BaseException {

		return jbxtgoodsService.updateJbxtGoods(jbxtgoodsVO);
	}

    /**
	 * 获取上架单详情
	 * @param jbxtgoodsQuery
	 * @return
	 */
	@GetMapping()
	@RestfulResult
    public JbxtGoodsDO getJbxtGoods(JbxtGoodsQuery jbxtgoodsQuery) throws BaseException {
    
		return jbxtgoodsService.getJbxtGoods(jbxtgoodsQuery);
    }
}
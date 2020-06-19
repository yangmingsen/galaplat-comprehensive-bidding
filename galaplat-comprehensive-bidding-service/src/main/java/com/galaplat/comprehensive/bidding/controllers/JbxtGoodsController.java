package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.service.impl.JbxtGoodsServiceImpl;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.MyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 竞品表Controller
 * @author esr
 * @date: 2020年06月17日
 */
@RestController
@RequestMapping("/jbxt/goods")
public  class JbxtGoodsController extends BaseController {


	@Autowired
	IJbxtGoodsService jbxtgoodsService;

	Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsController.class);


	@Autowired
	private HttpServletRequest httpServletRequest;

	@Value("${my.activityCode}")
	private String activityCode;

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
		} else {
			return new MyResult(true,"出错: activityCode不能为空哦(*￣︶￣)", null);
		}

	 }

	/**
	 * 获取某个竞品的竞价信息
	 * @param goodsId
	 * @return
	 * @throws BaseException
	 */
	@GetMapping("/findOne")
	@RestfulResult
	public Object findOne(Integer goodsId, String activityCode) throws BaseException{
		LOGGER.info("JbxtGoodsController(findOne): goodsId="+goodsId);
		if(activityCode == null || activityCode.equals("")) {
			return new MyResult(false, "获取数据出错: activityCode不能为空哦^_^", null);
		}


		//1 判断你是否有goodsId
		//2 如果没有走3
		//3 查看当前是否有正在进行的竞品活动
		//4 如果存在 查看当前用户的排名

		JbxtUserDO userInfo = (JbxtUserDO)httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);
		String userCode = userInfo.getCode();

		if (goodsId != null) {
			return new MyResult(true, "获取data成功",jbxtgoodsService.handlerFindCustomBidVO(userCode, goodsId, activityCode));
		} else {
			JbxtGoodsDO curActiveGoods = jbxtgoodsService.selectActiveGoods(activityCode);
			if (curActiveGoods != null) { //如果存在正在进行的竞品
				return new MyResult(true,"获取data成功", jbxtgoodsService.handlerFindCustomBidVO(userCode, curActiveGoods.getGoodsId(), activityCode));
			} else { //不存在正在进行的竞品，意味着还未开始竞价活动
				Map<String, String> map = new HashMap<>();
				map.put("goodsId","-1");
				return new MyResult(true,"当前还未开始活动或者已经结束了活动", map);
			}
		}
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
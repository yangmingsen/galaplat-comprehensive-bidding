package com.galaplat.comprehensive.bidding.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.IJbxtBiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.BeanCopyUtils;
import com.galaplat.base.core.common.utils.HttpResultUtils;


import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.galaplat.comprehensive.bidding.querys.JbxtGoodsQuery;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;

import javax.servlet.http.HttpServletRequest;

/**
 * 竞品表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtGoodsServiceImpl implements IJbxtGoodsService  {


	Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsServiceImpl.class);

	@Autowired
	private HttpServletRequest httpServletRequest;

	@Autowired
	IJbxtGoodsDao jbxtgoodsDao;

	@Autowired
	IJbxtBiddingDao jbxtBiddingDao;

	/***
	 * 业务处理：
	 *  <p>1.获取当前用户信息</p>
	 * 	<p>2.根据activityCode获取所有竞品信息</p>
	 * 	<p>3.根据每一个竞品信息的goodsId获取每个竞品的相应竞价信息</p>
	 * 	<p>4.根据3返回的竞价List,查到当前用户的位置</p>
	 * 	<p>5.返回List CustomGoodsVO </p>
	 * @param activityCode
	 * @return
	 */
	 public List<CustomGoodsVO> findAllByActivityCode(String activityCode) {
	 	LOGGER.info("goodsFindAll: activityCode="+activityCode);

		 JbxtUserDO userInfo = (JbxtUserDO)httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);
	 	LOGGER.info("userInfo(userCode): "+userInfo.getCode());

	 	List<JbxtGoodsDVO> listGoods = jbxtgoodsDao.getListJbxtGoodsByActivityCode(activityCode);
	 	LOGGER.info("listGoodsSize: "+listGoods.size());

		 ArrayList<CustomGoodsVO> res = new ArrayList<>();

		 String userCode = userInfo.getCode();
		 listGoods.stream().forEach( x -> {
			 List<JbxtBiddingDVO> bidList = jbxtBiddingDao.getJbxtListBiddingByGoodsId(x.getGoodsId());
			 int idx = -1;
			 for (int i = 0; i < bidList.size(); i++) {
				 JbxtBiddingDVO tbd = bidList.get(i);


				 //如果存在竞价信息
				 if (userCode.equals(tbd.getUserCode())) {
					 CustomGoodsVO cgv = new CustomGoodsVO();
					 cgv.setGoodsId(x.getGoodsId());
					 cgv.setGoodsCode(x.getCode());
					 cgv.setGoodsNum(x.getNum());
					 cgv.setGoodsName(x.getName());
					 cgv.setGoodsPrice(tbd.getBid());
					 cgv.setUserRank(i+1);
					 cgv.setIsActive(x.getStatus());

					 res.add(cgv);

					 idx = 1; //标记存在
					 break;
				 }
			 }

			 if (idx == -1) { //如果不存在竞价信息
				 CustomGoodsVO cgv = new CustomGoodsVO();
				 cgv.setGoodsId(x.getGoodsId());
				 cgv.setGoodsCode(x.getCode());
				 cgv.setGoodsNum(x.getNum());
				 cgv.setGoodsPrice(new BigDecimal("0.000"));
				 cgv.setUserRank(-1);
				 cgv.setIsActive(x.getStatus());

				 res.add(cgv);

			 }

		 });

		 return res;
	 }


	public List<SimpleGoodsVO> findAll(String activityCode) {

		List<JbxtGoodsDVO> jgdList = jbxtgoodsDao.getListJbxtGoodsByActivityCode(activityCode);
		List<SimpleGoodsVO> sgvs = new ArrayList<>();
		jgdList.stream().forEach(x -> {
			SimpleGoodsVO sgv = new SimpleGoodsVO();
			sgv.setGoodsId(x.getGoodsId());
			sgv.setGoodsCode(x.getCode());
			sgv.setGoodsName(x.getName());
			sgv.setIsActive(x.getStatus());
			sgvs.add(sgv);
		});

		return sgvs;
	}

	/***
	 * 业务流程：
	 * <p>1.获取最新目前正在进行竞拍的竞品</p>
	 * @param goodsId
	 * @return
	 */
	@Override
	public CustomBidVO findBidVOByGoodsId(Integer goodsId) {

		JbxtUserDO userInfo = (JbxtUserDO)httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);
		LOGGER.info("userInfo: "+userInfo);

		JbxtGoodsDO relaseActiveGoods = jbxtgoodsDao.selectActiveGoods();
		if (goodsId != relaseActiveGoods.getGoodsId()) { //如果与传入的goodsId不一致，说明要更新了
			goodsId = relaseActiveGoods.getGoodsId();
			LOGGER.info("relaseActiveGoodsId: "+goodsId);
		}

		List<JbxtBiddingDVO> jbgs = jbxtBiddingDao.getJbxtListBiddingByGoodsId(goodsId);

		String userCode = userInfo.getCode();
		CustomBidVO cbv = new CustomBidVO();
		int idx = -1;
		for (int i = 0; i < jbgs.size(); i++) {
			JbxtBiddingDVO tjb = jbgs.get(i);

			if (tjb.getUserCode().equals(userCode)) {
				cbv.setGoodsId(goodsId);
				cbv.setGoodsPrice(tjb.getBid());
				cbv.setUserRank(i+1);

				idx = 1;
				break;
			}
		}
		if (idx == -1) {
			cbv.setGoodsId(goodsId);
			cbv.setUserRank(-1);
		}

		return cbv;
	}

	@Override
	public int insertJbxtGoods(JbxtGoodsVO jbxtgoodsVO){
	       JbxtGoodsDO jbxtgoodsDO = BeanCopyUtils.copyProperties(JbxtGoodsDO.class, jbxtgoodsVO);
	       return jbxtgoodsDao.insertJbxtGoods(jbxtgoodsDO );
	}

	@Override
	public int updateJbxtGoods(JbxtGoodsVO jbxtgoodsVO){
	      JbxtGoodsDO jbxtgoodsDO = BeanCopyUtils.copyProperties(JbxtGoodsDO.class,jbxtgoodsVO);
		  jbxtgoodsDO.setUpdatedTime(new Date());
		  return jbxtgoodsDao.updateJbxtGoods(jbxtgoodsDO);
	}

	@Override
	public PageInfo<JbxtGoodsDVO> getJbxtGoodsPage(JbxtGoodsQuery jbxtgoodsQuery) throws BaseException{
	        JbxtGoodsParam jbxtgoodsParam = BeanCopyUtils.copyProperties(JbxtGoodsParam.class, jbxtgoodsQuery);
			jbxtgoodsParam.setCompanyCode(jbxtgoodsQuery.getCompanyCode());
			jbxtgoodsParam.setSysCode(jbxtgoodsQuery.getSysCode());
		  return jbxtgoodsDao.getJbxtGoodsPage(jbxtgoodsParam);
	
	}
	
    @Override
    public JbxtGoodsDO getJbxtGoods(JbxtGoodsQuery jbxtgoodsQuery){
       JbxtGoodsParam jbxtgoodsParam = BeanCopyUtils.copyProperties(JbxtGoodsParam.class, jbxtgoodsQuery);
		return jbxtgoodsDao.getJbxtGoods(jbxtgoodsParam);
    }
}
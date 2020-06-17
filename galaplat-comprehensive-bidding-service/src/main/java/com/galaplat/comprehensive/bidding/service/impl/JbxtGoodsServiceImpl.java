package com.galaplat.comprehensive.bidding.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

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

 /**
 * 竞品表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtGoodsServiceImpl implements IJbxtGoodsService  {


	@Autowired
	IJbxtGoodsDao jbxtgoodsDao;
	
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
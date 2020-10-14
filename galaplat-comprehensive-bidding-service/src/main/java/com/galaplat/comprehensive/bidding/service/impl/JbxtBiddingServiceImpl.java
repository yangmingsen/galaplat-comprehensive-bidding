package com.galaplat.comprehensive.bidding.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dvos.BiddingDVO;
import com.galaplat.comprehensive.bidding.utils.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaplat.base.core.common.utils.BeanCopyUtils;


import com.galaplat.comprehensive.bidding.dao.BiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.BiddingDO;
import com.galaplat.comprehensive.bidding.service.BiddingService;
import com.galaplat.comprehensive.bidding.vos.JbxtBiddingVO;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

/**
 * 竞价表ServiceImpl
 * @author esr
 * @date: 2020年06月17日
 */
 @Service
public  class JbxtBiddingServiceImpl implements BiddingService {

 	//--------------------------CRUD------------
 	@Override
	public int insertMinBidTableSelective(JbxtBiddingVO record) {
		record.setCode(idWorker.nextId());

		record.setCreatedTime(new Date());
		record.setUpdatedTime(new Date());

		BiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(BiddingDO.class, record);

		return jbxtbiddingDao.insertMinBidTableSelective(jbxtbiddingDO);
	}

	@Override
	public BiddingDO selectMinBidTableBy(String userCode, Integer goodsId, String activityCode) {
		return jbxtbiddingDao.selectMinBidTableBy(userCode,goodsId,activityCode);
	}

	@Override
	public List<BiddingDVO> selectMinBidTableBy(Integer goodsId, String activityCode) {
		return jbxtbiddingDao.selectMinBidTableBy(goodsId,activityCode);

	}

	@Override
	public int updateMinBidTableByPrimaryKeySelective(JbxtBiddingVO record) {
		BiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(BiddingDO.class, record);
		return jbxtbiddingDao.updateMinBidTableByPrimaryKeySelective(jbxtbiddingDO);
	}

	@Override
	public int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
		return jbxtbiddingDao.deleteMinbidTableByGoodsIdAndActivityCode(goodsId,activityCode);
	}

	//--------------------


	public List<BiddingDVO> getTheTopBids(Integer goodsId, String activityCode) {
		List<BiddingDVO> allUserMinBid = jbxtbiddingDao.selectMinBidTableBy(goodsId, activityCode);
		if (allUserMinBid.size() > 0) {
			BigDecimal bid = allUserMinBid.get(0).getBid();
			int lastIdx = -1;
			for (int i = 1; i < allUserMinBid.size(); i++) {
				BiddingDVO tMinBid = allUserMinBid.get(i);
				if (tMinBid.getBid().compareTo(bid) == 1) {
					lastIdx = i;break;
				}
			}
			if (lastIdx != -1) {
				return allUserMinBid.subList(0,lastIdx);
			} else {
				return allUserMinBid.subList(0,1);
			}
		}
		return allUserMinBid;
	}


	@Autowired
	BiddingDao jbxtbiddingDao;

	@Override
	public int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode) {
		return jbxtbiddingDao.deleteByGoodsIdAndActivityCode(goodsId,activityCode);
	}

	@Autowired
	private HttpServletRequest httpServletRequest;

	 @Autowired
	 private IdWorker idWorker;
	
    @Override
	@Transactional(rollbackFor = Exception.class)
	public int insertJbxtBidding(JbxtBiddingVO jbxtbiddingVO){


    		//设置code userCode activityCode   createdTime updateTime
			jbxtbiddingVO.setCode(idWorker.nextId());

			jbxtbiddingVO.setCreatedTime(new Date());
			jbxtbiddingVO.setUpdatedTime(new Date());


	       BiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(BiddingDO.class, jbxtbiddingVO);
	       return jbxtbiddingDao.insertJbxtBidding(jbxtbiddingDO );
	}



	public BiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode) {
		return jbxtbiddingDao.getUserMinBid(userCode,goodsId,activityCode);
	}


	@Override
	public int updateJbxtBidding(JbxtBiddingVO jbxtbiddingVO){
	      BiddingDO jbxtbiddingDO = BeanCopyUtils.copyProperties(BiddingDO.class,jbxtbiddingVO);
		  jbxtbiddingDO.setUpdatedTime(new Date());
		  return jbxtbiddingDao.updateJbxtBidding(jbxtbiddingDO);
	}


	public BiddingDVO getCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode) {
    	return jbxtbiddingDao.gerCurrentGoodsMinSubmitPrice(userCode, goodsId,activityCode);
	}

	@Override
	public List<BiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode) {
		return jbxtbiddingDao.findAllByUserCodeAndActivityCode(userCode,activityCode);
	}

	public List<BiddingDVO> findAllByUserCodeAndGooodsIdAndActivityCode(String userCode, Integer goodsId, String activityCode) {
    	return jbxtbiddingDao.findAllByUserCodeAndGooodsIdAndActivityCode(userCode, goodsId, activityCode);
	}


}
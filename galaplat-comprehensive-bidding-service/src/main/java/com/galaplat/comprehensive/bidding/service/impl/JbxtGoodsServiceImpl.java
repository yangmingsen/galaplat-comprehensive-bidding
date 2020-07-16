package com.galaplat.comprehensive.bidding.service.impl;

import java.math.BigDecimal;
import java.util.*;

import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.IJbxtBiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;

import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.galaplat.base.core.common.utils.BeanCopyUtils;


import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;

import javax.servlet.http.HttpServletRequest;

/**
 * 竞品表ServiceImpl
 *
 * @author esr
 * @date: 2020年06月17日
 */
@Service
public class JbxtGoodsServiceImpl implements IJbxtGoodsService {

    Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsServiceImpl.class);

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
	private IJbxtGoodsDao jbxtgoodsDao;

    @Autowired
	private IJbxtBiddingDao jbxtBiddingDao;

	@Autowired
	private ActivityMap activityMap;






    public List<SimpleGoodsVO> findAll(String activityCode) {

        List<JbxtGoodsDVO> jgdList = jbxtgoodsDao.getListJbxtGoodsByActivityCode(activityCode);
        List<SimpleGoodsVO> sgvs = new ArrayList<>();
		CurrentActivity currentActivity = activityMap.get(activityCode);
		jgdList.stream().forEach(goods -> {
            SimpleGoodsVO sgv = new SimpleGoodsVO();
            sgv.setGoodsId(goods.getGoodsId());
            sgv.setGoodsCode(goods.getCode());
            sgv.setGoodsNum(goods.getNum());
            sgv.setGoodsName(goods.getName());
			sgv.setFirstPrice(goods.getFirstPrice());
			sgv.setTimeNum(goods.getTimeNum());

			if (currentActivity != null) {
				if (currentActivity.getCurrentGoodsId().equals(goods.getGoodsId().toString())) {
					int status = currentActivity.getStatus();
					if (status == 2) {
						sgv.setIsActive("3");
					} else {
						sgv.setIsActive(goods.getStatus());
					}
				}
			} else {
				sgv.setIsActive(goods.getStatus());
			}
			
            sgvs.add(sgv);
        });

        return sgvs;
    }


	static class ComputedRes{
    	private BigDecimal bid;
    	private Integer rank;

		public BigDecimal getBid() {
			return bid;
		}

		public Integer getRank() {
			return rank;
		}

		public ComputedRes(BigDecimal bid, Integer rank) {
			this.bid = bid;
			this.rank = rank;
		}
	}


	/***
	 * v2.0 计算当前用户的竞品的排名
	 * @return
	 */
	private ComputedRes computedUserBidRankInfoByUserCodeAndActivity(String userCode, Integer goodsId, String activityCode) {
		List<JbxtBiddingDVO> bidList = jbxtBiddingDao.selectMinBidTableBy(goodsId,activityCode);

		Map<BigDecimal, Integer> map = new HashMap<>(); //bid->idx
		BigDecimal curUserBid = new BigDecimal("0.000"); //记录当前用户的竞价
		Integer rank = -1; //价格排名
		boolean exsitUserRank = false;
		for (int i = 0; i < bidList.size(); i++) {
			JbxtBiddingDVO t1 = bidList.get(i);
			if (t1.getUserCode().equals(userCode)) {
				curUserBid = t1.getBid(); //获得当前竞价
				exsitUserRank = true;
			}

			Integer tIdx = map.get(t1.getBid());
			if (tIdx == null) { //存在
				map.put(t1.getBid(), i+1); //存入当前价格的索引idx
			}
		}
		if (exsitUserRank) {
			rank = map.get(curUserBid);
		}

		return new ComputedRes(curUserBid, rank);
	}

	/***
	 * v2.0
	 * @param goodsId
	 * @param userCode
	 * @param activityCode
	 * @return
	 */
	public CustomBidVO getUserBidRankInfoByUserCodeAndActivity(Integer goodsId,String userCode, String activityCode) {
		ComputedRes cr = computedUserBidRankInfoByUserCodeAndActivity( userCode,  goodsId, activityCode);
		CustomBidVO cbv = new CustomBidVO();
		cbv.setUserRank(cr.getRank());
		cbv.setGoodsPrice(cr.getBid());
		cbv.setGoodsId(goodsId);
		return cbv;
	}



    public JbxtGoodsDO selectActiveGoods(String activityCode) {
        return jbxtgoodsDao.selectActiveGoods(activityCode);
    }


	public JbxtGoodsDO selectByGoodsId(Integer goodsId) {
		return jbxtgoodsDao.selectByGoodsId(goodsId);
	}

    public List<JbxtGoodsDVO> getListJbxtGoodsByActivityCode(String activityCode) {
        return jbxtgoodsDao.getListJbxtGoodsByActivityCode(activityCode);
    }


    @Override
    public int insertJbxtGoods(JbxtGoodsVO jbxtgoodsVO) {
        JbxtGoodsDO jbxtgoodsDO = BeanCopyUtils.copyProperties(JbxtGoodsDO.class, jbxtgoodsVO);
        return jbxtgoodsDao.insertJbxtGoods(jbxtgoodsDO);
    }

    @Override
    public int updateJbxtGoods(JbxtGoodsVO jbxtgoodsVO) {
        JbxtGoodsDO jbxtgoodsDO = BeanCopyUtils.copyProperties(JbxtGoodsDO.class, jbxtgoodsVO);
        jbxtgoodsDO.setUpdatedTime(new Date());
        return jbxtgoodsDao.updateJbxtGoods(jbxtgoodsDO);
    }

}
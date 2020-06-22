package com.galaplat.comprehensive.bidding.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.galaplat.comprehensive.bidding.constants.SessionConstant;
import com.galaplat.comprehensive.bidding.dao.IJbxtBiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.vos.JbxtUserVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomBidVO;
import com.galaplat.comprehensive.bidding.vos.pojo.CustomGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.SimpleGoodsVO;
import io.swagger.models.auth.In;
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
    IJbxtGoodsDao jbxtgoodsDao;

    @Autowired
    IJbxtBiddingDao jbxtBiddingDao;



    public List<SimpleGoodsVO> findAll(String activityCode) {

        List<JbxtGoodsDVO> jgdList = jbxtgoodsDao.getListJbxtGoodsByActivityCode(activityCode);
        List<SimpleGoodsVO> sgvs = new ArrayList<>();
        jgdList.stream().forEach(x -> {
            SimpleGoodsVO sgv = new SimpleGoodsVO();
            sgv.setGoodsId(x.getGoodsId());
            sgv.setGoodsCode(x.getCode());
            sgv.setGoodsNum(x.getNum());
            sgv.setGoodsName(x.getName());
            sgv.setIsActive(x.getStatus());
            sgvs.add(sgv);
        });

        return sgvs;
    }


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
		JbxtUserDO userInfo = (JbxtUserDO) httpServletRequest.getSession().getAttribute(SessionConstant.SESSION_USER);

		List<JbxtGoodsDVO> listGoods = jbxtgoodsDao.getListJbxtGoodsByActivityCode(activityCode);
		ArrayList<CustomGoodsVO> res = new ArrayList<>();

		String userCode = userInfo.getCode();
		listGoods.stream().forEach(x -> {

			ComputedRes cr = computedCurrentUserSpecificGoodsRankInfo(userCode, x.getGoodsId(), activityCode);

			CustomGoodsVO cgv = new CustomGoodsVO();
			cgv.setGoodsId(x.getGoodsId());
			cgv.setGoodsCode(x.getCode());
			cgv.setGoodsNum(x.getNum());
			cgv.setGoodsName(x.getName());
			cgv.setGoodsPrice(cr.getBid());
			cgv.setUserRank(cr.getRank());
			cgv.setIsActive(x.getStatus());

			res.add(cgv);
		});

		return res;
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
	 * 计算当前用户的当前竞品的排名
	 * @param userCode
	 * @param goodsId
	 * @param activityCode
	 * @return
	 */
    private ComputedRes computedCurrentUserSpecificGoodsRankInfo(String userCode, Integer goodsId, String activityCode) {
        //根据goods_id 返回正在竞价该竞品的用户排名信息
        List<JbxtBiddingDVO> bidList = jbxtBiddingDao.getJbxtListBiddingByGoodsId(goodsId, activityCode);


        Map<BigDecimal, String> map = new HashMap<>();
        BigDecimal curUserBid = new BigDecimal("0.000"); //记录当前用户的竞价
		Integer rank = -1;
		boolean exsitUserRank = false;

        for (int i = 0; i < bidList.size(); i++) {
            JbxtBiddingDVO t1 = bidList.get(i);
            if (t1.getUserCode().equals(userCode)) {
                curUserBid = t1.getBid(); //获得当前竞价
                exsitUserRank = true;
            }
            map.put(t1.getBid(), "");
        }
        if (exsitUserRank) {
			List<BigDecimal> list = new ArrayList<>();
			for (BigDecimal key : map.keySet()) {
				list.add(key);
			}
			List<BigDecimal> sortList = list.stream().sorted(BigDecimal::compareTo).collect(Collectors.toList());

			for (int i = 0; i < sortList.size(); i++) {
				BigDecimal bd1 = sortList.get(i);
				if (bd1.compareTo(curUserBid) == 0) {
					rank = i + 1; //获得排名
					break;
				}
			}
		}

        return new ComputedRes(curUserBid, rank);
    }


	public CustomBidVO handlerFindCustomBidVO(String userCode, Integer goodsId, String activityCode) {
		ComputedRes cr = computedCurrentUserSpecificGoodsRankInfo(userCode, goodsId, activityCode);

		CustomBidVO cbv = new CustomBidVO();
		cbv.setGoodsId(goodsId);
		cbv.setUserRank(cr.getRank());

		return cbv;
	}


    public JbxtGoodsDO selectActiveGoods(String activityCode) {
        return jbxtgoodsDao.selectActiveGoods(activityCode);
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
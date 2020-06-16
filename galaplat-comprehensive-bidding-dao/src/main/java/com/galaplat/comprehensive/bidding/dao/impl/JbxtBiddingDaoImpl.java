package com.galaplat.comprehensive.bidding.dao.impl;

import java.io.Serializable;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtBiddingDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtBiddingDOMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

 /**
 * 竞价表DaoImpl
 * @author esr
 * @date: 2020年06月16日
 */
 @Repository
public   class JbxtBiddingDaoImpl implements IJbxtBiddingDao  {

	@Autowired
	private JbxtBiddingDOMapper mapper;
	
    @Override
	public int insertJbxtBidding(JbxtBiddingDO entity){
	       return mapper.insert(entity);
	}

	@Override
	public int updateJbxtBidding(JbxtBiddingDO entity){
	        return mapper.updateByPrimaryKey(entity);
	}

	@Override
	public PageInfo<JbxtBiddingDVO> getJbxtBiddingPage(JbxtBiddingParam jbxtbiddingParam) throws BaseException{
	       PageHelper.startPage(jbxtbiddingParam.getPn(), jbxtbiddingParam.getPs());
		   return new PageInfo<JbxtBiddingDVO>(mapper.getJbxtBiddingList(jbxtbiddingParam));
	
	}
	
    @Override
    public JbxtBiddingDO getJbxtBidding(JbxtBiddingParam jbxtbiddingParam){
		return mapper.selectByPrimaryKey(jbxtbiddingParam);
    }
}
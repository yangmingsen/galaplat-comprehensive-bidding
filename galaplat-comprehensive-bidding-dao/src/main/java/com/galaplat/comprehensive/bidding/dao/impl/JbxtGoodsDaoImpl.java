package com.galaplat.comprehensive.bidding.dao.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtGoodsDOMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 竞品表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
@Repository
public   class JbxtGoodsDaoImpl implements IJbxtGoodsDao  {

	@Autowired
	private JbxtGoodsDOMapper mapper;

	@Override
	public int insertJbxtGoods(JbxtGoodsDO entity){
		return mapper.insert(entity);
	}

	@Override
	public int updateJbxtGoods(JbxtGoodsDO entity){
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public PageInfo<JbxtGoodsDVO> getJbxtGoodsPage(JbxtGoodsParam jbxtgoodsParam) throws BaseException{
		PageHelper.startPage(jbxtgoodsParam.getPn(), jbxtgoodsParam.getPs());
		return new PageInfo<JbxtGoodsDVO>(mapper.getJbxtGoodsList(jbxtgoodsParam));

	}

	@Override
	public JbxtGoodsDO getJbxtGoods(JbxtGoodsParam jbxtgoodsParam){
		return mapper.selectByPrimaryKey(jbxtgoodsParam.getGoodsId());
	}

	//---------------------------my--------------------
	@Override
	public JbxtGoodsDO selectByGoodsId(Integer goodsId){
		return mapper.selectByPrimaryKey(goodsId);
	}

	@Override
	public List<JbxtGoodsDVO> getListJbxtGoodsByActivityCode(String activityCode) {
		return mapper.getListJbxtGoodsByActivityCode(activityCode);
	}

	@Override
	public JbxtGoodsDO selectActiveGoods(String activityCode) {
		return mapper.selectActiveGoods(activityCode);
	}

	@Override
	public int batchInsert(List<JbxtGoodsParam>  goodsParams) {
		return mapper.batchInsert(goodsParams);
	}

	@Override
	public List<JbxtGoodsDO> listGoods(JbxtGoodsParam jbxtgoodsParam) {
		return mapper.listGoods(jbxtgoodsParam);
	}

	@Override
	public int delete(JbxtGoodsParam jbxtgoodsParam) {
		return  mapper.delete(jbxtgoodsParam);
	}
}
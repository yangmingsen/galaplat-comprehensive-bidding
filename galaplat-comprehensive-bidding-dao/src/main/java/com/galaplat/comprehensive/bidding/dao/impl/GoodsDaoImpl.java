package com.galaplat.comprehensive.bidding.dao.impl;

import java.util.List;

import com.galaplat.comprehensive.bidding.dao.dvos.GoodsDVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.GoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.mappers.custs.JbxtGoodsCustMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

/**
 * 竞品表DaoImpl
 * @author esr
 * @date: 2020年06月17日
 */
@Repository
public   class GoodsDaoImpl implements GoodsDao {

	@Autowired
	private JbxtGoodsCustMapper mapper;

	@Override
	public int insertJbxtGoods(GoodsDO entity){
		return mapper.insert(entity);
	}

	@Override
	public int updateJbxtGoods(GoodsDO entity){
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public PageInfo<GoodsDVO> getJbxtGoodsPage(JbxtGoodsParam jbxtgoodsParam) throws BaseException{
		PageHelper.startPage(jbxtgoodsParam.getPn(), jbxtgoodsParam.getPs());
		return new PageInfo<GoodsDVO>(mapper.getJbxtGoodsList(jbxtgoodsParam));

	}

	@Override
	public GoodsDO getJbxtGoods(JbxtGoodsParam jbxtgoodsParam){
		return mapper.selectByPrimaryKey(jbxtgoodsParam.getGoodsId());
	}

	//---------------------------my--------------------
	@Override
	public GoodsDO selectByGoodsId(Integer goodsId){
		return mapper.selectByPrimaryKey(goodsId);
	}

	@Override
	public List<GoodsDVO> getListJbxtGoodsByActivityCode(String activityCode) {
		return mapper.getListJbxtGoodsByActivityCode(activityCode);
	}

	@Override
	public GoodsDO selectActiveGoods(String activityCode) {
		return mapper.selectActiveGoods(activityCode);
	}

	@Override
	public int batchInsert(List<JbxtGoodsParam>  goodsParams) {
		return mapper.batchInsert(goodsParams);
	}

	@Override
	public List<GoodsDO> listGoods(JbxtGoodsParam jbxtgoodsParam) {
		return mapper.listGoods(jbxtgoodsParam);
	}

	@Override
	public int delete(JbxtGoodsParam jbxtgoodsParam) {
		return  mapper.delete(jbxtgoodsParam);
	}
}

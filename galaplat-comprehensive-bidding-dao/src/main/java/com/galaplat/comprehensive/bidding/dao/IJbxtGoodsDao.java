package com.galaplat.comprehensive.bidding.dao;

import java.io.Serializable;
import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.github.pagehelper.PageInfo;

 /**
 * 竞品表Dao
 * @author esr
 * @date: 2020年06月17日
 */
public interface IJbxtGoodsDao{


    /**
	 * 添加竞品表
	 */
	int insertJbxtGoods(JbxtGoodsDO entity);

	/**
	 * 更新竞品表信息
	 */
	int updateJbxtGoods(JbxtGoodsDO entity);

	/**
	 * 分页获取竞品表列表
	 *
	 */
	public PageInfo<JbxtGoodsDVO> getJbxtGoodsPage(JbxtGoodsParam jbxtgoodsParam) throws BaseException;
	
    
    JbxtGoodsDO getJbxtGoods(JbxtGoodsParam jbxtgoodsParam);
}
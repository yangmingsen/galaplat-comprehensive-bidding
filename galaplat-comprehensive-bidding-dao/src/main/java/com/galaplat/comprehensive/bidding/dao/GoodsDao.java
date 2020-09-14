package com.galaplat.comprehensive.bidding.dao;

import java.util.List;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.GoodsDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.github.pagehelper.PageInfo;

/**
 * 竞品表Dao
 * @author esr
 * @date: 2020年06月17日
 */
public interface GoodsDao {


	/**
	 * 添加竞品表
	 */
	int insertJbxtGoods(GoodsDO entity);

	/**
	 * 更新竞品表信息
	 */
	int updateJbxtGoods(GoodsDO entity);

	/**
	 * 分页获取竞品表列表
	 *
	 */
	public PageInfo<GoodsDVO> getJbxtGoodsPage(JbxtGoodsParam jbxtgoodsParam) throws BaseException;


	GoodsDO getJbxtGoods(JbxtGoodsParam jbxtgoodsParam);


	/**
	 * 根据活动Code 获取所有的竞品
	 * @param activityCode
	 * @return res => ASC（id）
	 */
	List<GoodsDVO> getListJbxtGoodsByActivityCode(String activityCode);

	public GoodsDO selectActiveGoods(String activityCode);




	/**
	 * 根据 goodsId获取一个竞品信息
	 * @param goodsId
	 * @return
	 */
	public GoodsDO selectByGoodsId(Integer goodsId);


	/**
	 * 批量新增或更新
	 * @param jbxtgoodsParam
	 * @return
	 */
	int batchInsert(List<JbxtGoodsParam> jbxtgoodsParam);

	/**
	 * 查询竞品
	 * @param jbxtgoodsParam
	 * @return
	 */
	List<GoodsDO> listGoods(JbxtGoodsParam jbxtgoodsParam);

	/**
	 * 删除竞品
	 * @param jbxtgoodsParam 竞品信息
	 * @return
	 */
	int delete(JbxtGoodsParam jbxtgoodsParam);

}
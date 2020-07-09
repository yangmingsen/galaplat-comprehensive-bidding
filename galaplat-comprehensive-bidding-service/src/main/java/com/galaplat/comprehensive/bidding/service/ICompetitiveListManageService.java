package com.galaplat.comprehensive.bidding.service;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.CompetitiveListDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.querys.CompetitiveListQuery;
import com.galaplat.comprehensive.bidding.vos.SupplierAccountVO;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Description:  竞标单管理
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 11:11
 */
public interface ICompetitiveListManageService {

    /**
     * 竞标单列表查询
     * @param query 查询条件
     * @return
     * @throws BaseException
     */
    PageInfo<CompetitiveListDVO>  listCompetitiveListPage(CompetitiveListQuery query ) throws BaseException;

    /**
     * 竞标单新增和删除
     * @param activityParam 供应商信息和代号
     * @param type 操作类型 ， add- 新增， update-修改
     * @param bidActivityCode
     * @return
     * @throws BaseException
     */
    String   addAndUpdate(JbxtActivityParam activityParam, String type, String bidActivityCode) throws BaseException;


    /**
     * 获取代号
     * @param num 获取个数
     * @return
     * @throws BaseException
     */
    List<String>   listReplaceCode(int num) throws BaseException;

    /**
     * 获取供应商的账号信息
     * @param bidActivityCode
     * @return
     * @throws BaseException
     */
    List<SupplierAccountVO> listSupplierAccount(String bidActivityCode) throws BaseException;

    /**
     * 删除竞标单
     * @param activityCodes
     * @return
     * @throws BaseException
     */
    int delete(String[] activityCodes) throws BaseException;

    /**导出竞标单 ，供应商竞标排名和供应商历史竞标价格信息
     * @param bidActivityCode 竞标单号
     * @return
     * @throws BaseException
     */
    String exportBidRankAndBidPrice(String bidActivityCode) throws BaseException;
}

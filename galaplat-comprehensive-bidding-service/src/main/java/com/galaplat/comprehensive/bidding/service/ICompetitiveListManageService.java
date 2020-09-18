package com.galaplat.comprehensive.bidding.service;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.dvos.*;
import com.galaplat.comprehensive.bidding.dao.params.BidActivityInfoParam;
import com.galaplat.comprehensive.bidding.querys.CompetitiveListQuery;
import com.galaplat.comprehensive.bidding.utils.Tuple3;
import com.galaplat.comprehensive.bidding.vos.BidCodeVO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
     * 删除竞标单
     * @param activityCodes
     * @return
     * @throws BaseException
     */
    int delete(String[] activityCodes) throws BaseException;

    /**导出竞标单 ，供应商竞标排名和供应商历史竞标价格信息
     * @param bidActivityCode 竞标单号
     * @param response
     * @param request
     * @return
     * @throws BaseException
     */
    String exportBidRankAndBidPrice(String bidActivityCode,HttpServletResponse response, HttpServletRequest request) throws BaseException;

    /**
     * 新增竞标活动时获取活动编码
     * @param userName
     * @return
     */
    BidCodeVO getBidcode(String userName);

    /**
     * 新增或者编辑竞标活动基本信息
     * @param infoParam
     * @param userName
     * @return
     */
    String saveBidActivityBasicInfo(BidActivityInfoParam infoParam, String userName) throws Exception;

    /**
     * 查询带有竞品和供应商的竞标活动信息
     * @param bidActivityCode
     * @return
     * @throws Exception
     */
    BidActivityDVO getBidActivityWithGoodsAndSupplier(String bidActivityCode) throws Exception;

    /**
     * 获取供应商的账号
     * @return
     */
    String getUserName();

    /**
     * 获取供应商账号密码
     * @return
     */
    String getPassword();

    /**
     * 检查竞标活动的状态是否是未导入数据，并且所有的数据已经新增
     * @param bidActivityCode
     * @return
     */
    boolean checkActivityInfoComplete(String bidActivityCode);

    /**
     * 发送短信与邮件
     *
     * 发送全部，传bidActivityCode，emailFile，type="all";
     * 单个发送传短信：传bidActivityCode，phone，supplierCode, type="phone";
     * 单个发送邮件：传bidActivityCode，emailAdrress，supplierCode，type="email";
     *
     * @param bidActivityCode
     * @param phone
     * @param emailAdrress
     * @param type
     * @param supplierCode
     * @return
     */
    MessageAndEmialDVO sendMsgAndMail(String bidActivityCode, String phone, String emailAdrress, String type, String supplierCode) throws BaseException;

    /**
     * 查询供应商
     * @param bidActivityCode
     * @return
     */
    List<BidSupplierDVO> listSupplierInfo(String bidActivityCode);

    /**
     * 查询竞品
     * @param bidActivityCode
     * @return
     */
    List<BidGoodsDVO> listGoods(String bidActivityCode);

    /***
     * 保存承诺函
     * @param bidActivityCode
     * @param promiseTitle
     * @param promiseText
     * @return
     */
    int savePromiseText(String bidActivityCode, String promiseTitle, String promiseText) throws BaseException;

    /**
     * 上传附件
     * @param bidActivityCode
     * @param rarFile
     * @return
     */
    String fileUpload(String bidActivityCode, MultipartFile rarFile);

    /**
     * 获取文件
     * @param bidActivityCode
     * @return
     * @throws BaseException
     */
    MultipartFile getfile(String bidActivityCode)throws BaseException;

    /**
     *  获取文件的文件名
     * @param bidActivityCode
     * @return
     */
    Tuple3<String, String, String> getFileAllName(String bidActivityCode);

    /**
     *  获取短信日志
     * @param type
     * @param msgCode
     * @return
     */
    String getMsgLog(String type, String msgCode);
}

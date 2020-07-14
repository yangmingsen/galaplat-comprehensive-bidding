package com.galaplat.comprehensive.bidding.service.impl;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtActivityDao;
import com.galaplat.comprehensive.bidding.dao.IJbxtBiddingDao;
import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.IJbxtUserDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.BidDVO;
import com.galaplat.comprehensive.bidding.dao.params.*;
import com.galaplat.comprehensive.bidding.dao.params.validate.InsertParam;
import com.galaplat.comprehensive.bidding.enums.ActivityStatusEnum;
import com.galaplat.comprehensive.bidding.enums.CodeNameEnum;
import com.galaplat.comprehensive.bidding.querys.CompetitiveListQuery;
import com.galaplat.comprehensive.bidding.service.ICompetitiveListManageService;
import com.galaplat.comprehensive.bidding.utils.BeanValidateUtils;
import com.galaplat.comprehensive.bidding.utils.IdWorker;
import com.galaplat.comprehensive.bidding.utils.SpellingUtil;
import com.galaplat.comprehensive.bidding.vos.RankInfoVO;
import com.galaplat.comprehensive.bidding.vos.SupplierAccountVO;
import com.galaplat.platformdocking.base.core.utils.CopyUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 竞标单管理
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 14:35
 */
@Service
public class CompetitiveListManageServiceImpl implements ICompetitiveListManageService {

    private static final Logger log = LoggerFactory.getLogger(CompetitiveListManageServiceImpl.class);

    @Autowired
    private IJbxtActivityDao activityDao;

    @Autowired
    private IJbxtUserDao userDao;

    @Autowired
    private IJbxtGoodsDao goodsDdao;

    @Autowired
    private IJbxtBiddingDao biddingDao;

    @Autowired
    private IdWorker worker;

    /* 新增操作类型*/
    private static final String OPRATETYPE_ADD = "add";

    /* 修改操作类型*/
    private static final String OPRATETYPE_UPDATE = "update";

    @Override
    public PageInfo listCompetitiveListPage(CompetitiveListQuery query) throws BaseException {
        CompetitiveListParam param = new CompetitiveListParam();
        if (null != query && StringUtils.isNotEmpty(query.getBidActivityCode())) {
            query.getBidActivityCodeList(query.getBidActivityCode());
        }
        CopyUtil.copyPropertiesExceptEmpty(query, param);

        if (StringUtils.isAllEmpty(param.getEndTime(), param.getEndTime(), param.getActivityStatus(), param.getBidActivityCode())) {
            param.setStartTime(LocalDate.now().toString());
            param.setEndTime(LocalDate.now().toString());
        } else {
            String startTime = param.getStartTime();
            String endTime = param.getEndTime();
            if (StringUtils.isNotEmpty(startTime)) {
                startTime = startTime.substring(0, 4) + "-" + startTime.substring(4, 6) + "-01";
            }
            if (StringUtils.isNotEmpty(endTime)) {
                endTime = endTime.substring(0, 4) + "-" + endTime.substring(4, 6) + "-01";
            }
            param.setStartTime(startTime);
            param.setEndTime(endTime);
        }
        return activityDao.listCompetitiveListPage(param);
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public String addAndUpdate(JbxtActivityParam activityParam, String type, String bidActivityCode) throws BaseException {
        String activityCode = bidActivityCode;
        List<SupplierAccountParam> supplierAccountParamList = activityParam.getSupplierAccountParams();
        Map<String, List<SupplierAccountParam>> supplierAccountParamMap = supplierAccountParamList.stream().collect(Collectors.groupingBy(o -> o.getCodeName()));
        for (Map.Entry<String, List<SupplierAccountParam>> m : supplierAccountParamMap.entrySet()) {
            List<SupplierAccountParam> paramList = m.getValue();
            if (paramList.size() > 1) {
                throw new BaseException(m.getKey() + "代号重复,请重新填写!", m.getKey() + "代号重复，请重新填写!");
            }
        }
        // 校验为空
        for (SupplierAccountParam accountParam : supplierAccountParamList) {
            try {
                BeanValidateUtils.validateErrorThenThrowException(accountParam, InsertParam.class);
            } catch (BaseException baseException) {
                log.error("操作异常{},{}", baseException.getMessage(), baseException);
            }
            if (accountParam.getSupplierName().length() > 30) {
                throw new BaseException(accountParam.getSupplierName() + "太长已超过30个字符！", accountParam.getSupplierName() + "太长已超过30个字符！");
            }
        }

        if (StringUtils.equals(type, OPRATETYPE_ADD) && StringUtils.isEmpty(bidActivityCode)) {
            activityCode = worker.nextId();
        } else if (StringUtils.equals(type, OPRATETYPE_UPDATE) && StringUtils.isNotEmpty(bidActivityCode)) {
            JbxtActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(bidActivityCode).build());
            if (null != activityDO && activityDO.getStatus().equals(ActivityStatusEnum.FINISH.getCode())) {
                throw new BaseException("该竞标活动已结束不允许编辑！", "该竞标活动已结束不允许编辑！");
            }
        }
        JbxtActivityDO activityDO = JbxtActivityDO.builder()
                .code(activityCode)
                .companyCode(StringUtils.isEmpty(bidActivityCode) ? activityParam.getCompanyCode() : null)
                .sysCode(StringUtils.isEmpty(bidActivityCode) ? activityParam.getSysCode() : null)
                .createdTime(StringUtils.isEmpty(bidActivityCode) ? new Date() : null)
                .updatedTime(new Date())
                .updator(activityParam.getCreator())
                .creator(StringUtils.isEmpty(bidActivityCode) ? activityParam.getCreator() : null)
                .supplierNum(activityParam.getSupplierAccountParams().size())
                .recordStatus(1)
                .status(StringUtils.isEmpty(bidActivityCode) ? ActivityStatusEnum.UNEXPORT.getCode() : null)
                .build();
        if (StringUtils.isEmpty(bidActivityCode)) {
            activityDao.insertBidActivity(activityDO);
        } else {
            activityDao.updateBidActivity(activityDO);
        }
        activityParam.setCode(activityCode);
        batchInsertOrUpdate(activityParam, supplierAccountParamList, bidActivityCode);
        return activityCode;
    }

    @Override
    public Set<String> listReplaceCode(Integer num) throws BaseException {
        num = null == num ? 20 : num;
        if (num > 20) {
            throw new BaseException("获取代号个数不能超过20个", "获取代号个数不能超过20个");
        }
        Set<String> numSet = new HashSet<>();
        for (int i = 1; i <= num; i++) {
            numSet.add(CodeNameEnum.findByCode(i) + LocalDate.now().toString().replace("-", ""));
        }
        return numSet;
    }

    @Override
    public List<SupplierAccountVO> listSupplierAccount(String bidActivityCode) throws BaseException {
        List<SupplierAccountVO> accountVOS = Lists.newArrayList();
        List<JbxtUserDO> userDOList = userDao.getUser(JbxtUserParam.builder().activityCode(bidActivityCode).build());
        userDOList.stream().forEach(e -> {
            SupplierAccountVO accountVO = new SupplierAccountVO();
            CopyUtil.copyPropertiesExceptEmpty(e, accountVO);
            accountVO.setSupplierAccount(e.getUsername());
            accountVOS.add(accountVO);
        });
        return accountVOS;
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public int delete(String[] activityCodes) throws BaseException {
        return activityDao.delete(activityCodes);
    }

    @Override
    public String exportBidRankAndBidPrice(String bidActivityCode, HttpServletResponse response, HttpServletRequest request) throws BaseException {
        if (StringUtils.isEmpty(bidActivityCode)) {
            throw new BaseException("竞标编码不能为空！", "竞标编码不能为空！");
        }
        int maxBidNum = 0;
        List<JbxtGoodsDO> goodsList = goodsDdao.getGoods(JbxtGoodsParam.builder().activityCode(bidActivityCode).build());
        List<List<String>> bidPriceRankData = new ArrayList<>();
        List<List<String>> bidPriceDdetailData = new ArrayList<>();

        for (JbxtGoodsDO goods : goodsList) {
            List<String> allBidUserCodes = biddingDao.listBidActivityUsers(JbxtBiddingParam.builder().goodsId(goods.getGoodsId())
                    .activityCode(goods.getActivityCode()).build());

            Map<String,Object>  map = getBidPriceFinalRank(goods, allBidUserCodes);
            int size  = (int) map.get("size");
            if (maxBidNum <  size) {
                maxBidNum = size;
            }
            bidPriceRankData.add((List<String>)map.get("list"));

            for (int i = 0; i < allBidUserCodes.size(); i++) {
                String userCode = allBidUserCodes.get(i);
                bidPriceDdetailData.add(getBidPriceDetail(userCode, goods, bidActivityCode));
            }
        }

        // 获取表头信息
        List<String> bidPriceHeadInfo = new ArrayList<>();
        bidPriceHeadInfo.add("序号");
        bidPriceHeadInfo.add("产品编码");
        bidPriceHeadInfo.add("名称");
        bidPriceHeadInfo.add("年采购量");

        for (int i = 0; i < maxBidNum; i++) {
            bidPriceHeadInfo.add("第" + (i + 1) + "名");
            bidPriceHeadInfo.add("第" + (i + 1) + "报价");
        }
        bidPriceRankData.add(0, bidPriceHeadInfo);


        //处理头部
        List<String> headList = new ArrayList<>();
        headList.add("产品编码");
        headList.add("名称");
        headList.add("供应商");
        bidPriceDdetailData.add(0,headList);

        Map<String, List<List<String>>> sheetDataMap = new HashMap<>();
        sheetDataMap.put("竞价排名",bidPriceRankData);
        sheetDataMap.put("竞价详情",bidPriceDdetailData);
        try {
            exportBidPriceRankAndDetail(createMultiSheet(sheetDataMap), response,bidActivityCode );
        } catch (Exception e) {
            log.error("导出异常【{}】,【{}】",e.getMessage(),e );
        }
        return "导出成功";
    }

    public  Workbook createMultiSheet(Map<String, List<List<String>>> sheetData)  throws Exception {
        final  String excelType = "org.apache.poi.xssf.usermodel.XSSFWorkbook";
        //根据 type 参数生成工作簿实例对象
        Workbook workbook = (Workbook) Class.forName(excelType).newInstance();

        for (Map.Entry<String, List<List<String>>> entry: sheetData.entrySet()) {
            Sheet sheet = workbook.createSheet(entry.getKey());
            int rowLen = entry.getValue().size();
            for (int i = 0; i < rowLen; i++) {
                Row row = sheet.createRow(i);

                List<String> colsData = entry.getValue().get(i);
                int colLen = colsData.size();
                for (int j = 0; j < colLen; j++) {
                    Cell cell = row.createCell(j);

                    String value = colsData.get(j);
                    cell.setCellValue(value);
                }
            }
        }
        return workbook;
    }

    private void exportBidPriceRankAndDetail(Workbook workbook, HttpServletResponse response,  String bidActivityCode) {
        String fileName = bidActivityCode + LocalDate.now().toString();
        try {
            response.setContentType("application/vnd.ms-excel;charset=UTF-8");
            response.setHeader("Content-disposition",
                    "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            // 设置浏览器可访问的响应头
            response.setHeader("Access-Control-Expose-Headers", "Content-disposition");
            response.flushBuffer();
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (IOException e) {
            log.error("导出异常【{}】,【{}】",e.getMessage(),e );
        }
    }

    /***
     * 获取当前竞品 所有用户的最低报价
     * @param goods
     * @return
     */
    private Map<String,Object> getMinBidList(JbxtGoodsDO goods,  List<String> allBidUserCodes) {

        List<BidDVO> minBidList = new ArrayList<>();
        for (int i = 0; i < allBidUserCodes.size(); i++) {
            String curUserCode = allBidUserCodes.get(i);
            BidDVO minBid = biddingDao.getBidActivity(JbxtBiddingParam.builder().userCode(curUserCode).goodsId(goods.getGoodsId())
                    .activityCode(goods.getActivityCode()).build());
            if (minBid != null) {
                minBidList.add(minBid);
            }
        }
        List<BidDVO> bidList =  minBidList.stream().sorted(Comparator.comparing(BidDVO::getBid)).collect(Collectors.toList());
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("size",bidList.size());
        resultMap.put("list",bidList);
        return resultMap;
    }

    /***
     * 供应商竞价排序
     * @param bids
     * @return
     */
    private List<RankInfoVO> getRankInfos(List<BidDVO> bids) {

        Map<BigDecimal, RankInfoVO> map = new HashMap<>();
        for (int i = 0; i < bids.size(); i++) {
            BidDVO bid = bids.get(i);
            RankInfoVO rankInfo = map.get(bid.getBid());
            JbxtUserDO userDO = userDao.getJbxtUser(JbxtUserParam.builder().code(bid.getUserCode()).build());
            String userName = userDO.getUsername() != null ? userDO.getUsername() : userDO.getCode();
            if (rankInfo == null) {
                RankInfoVO newRankInfo = new RankInfoVO();
                newRankInfo.setBid(bid.getBid());
                newRankInfo.setNames(userName);
                newRankInfo.setRank(i + 1);
                map.put(bid.getBid(), newRankInfo);
            } else {
                rankInfo.setNames(rankInfo.getNames() + "-" + userName);
                map.put(bid.getBid(), rankInfo);

            }
        }
        List<RankInfoVO> res = new ArrayList<>();
        for (BigDecimal bid : map.keySet()) {
            res.add(map.get(bid));
        }
        return res.stream().sorted(Comparator.comparing(RankInfoVO::getRank)).collect(Collectors.toList());
    }


    /**
     * 组装竞价排名
     *
     * @param goods
     * @param allBidUserCodes
     * @return
     */
    private Map<String,Object> getBidPriceFinalRank(JbxtGoodsDO goods, List<String> allBidUserCodes) {
        Map<String,Object> resultMap = new HashMap<>();

        List<String> res = new ArrayList<>();
        res.add(goods.getGoodsId().toString());
        res.add(goods.getCode());
        res.add(goods.getName());
        res.add(goods.getNum().toString());

        Map<String,Object> rankBidMap = getMinBidList(goods, allBidUserCodes);
        List<BidDVO> bidDVOList = (List<BidDVO>)rankBidMap.get("list");
        List<RankInfoVO> rankInfos = getRankInfos(bidDVOList);

        if (CollectionUtils.isEmpty(rankInfos)) {
            resultMap.put("size",0);
            resultMap.put("list",res);
            return resultMap;
        }
        int idx = 0;
        int endRankNum = rankInfos.get(rankInfos.size() - 1).getRank();
        for (int i = 1; i <= endRankNum; i++) {
            RankInfoVO rankInfo = rankInfos.get(idx);
            if (rankInfo.getRank() == i) {
                res.add(rankInfo.getNames());
                res.add(rankInfo.getBid().toString());
                idx++;
            } else {
                res.add(" ");
                res.add(" ");
            }
        }
        // size用于计算excel显示的最大名次标题，应该取未整合前价格排序
        resultMap.put("size",rankBidMap.get("size"));
        resultMap.put("list",res);
        return  resultMap;
    }

    public  List<String> getBidPriceDetail(String userCode, JbxtGoodsDO goods, String activityCode) {
        List<BidDVO> bids = biddingDao.getOneSupplierBidPriceDeatil(JbxtBiddingParam.builder()
                .userCode(userCode).goodsId(goods.getGoodsId()).activityCode(activityCode).build());
        List<String> res = new ArrayList<>();
        res.add(goods.getCode());
        res.add(goods.getName());

        JbxtUserDO userDO = userDao.getJbxtUser(JbxtUserParam.builder().code(userCode).build());
        String userName = userDO.getUsername() != null ? userDO.getUsername() : userDO.getCode();
        res.add((userName));

        bids.forEach(bid -> {
            String dateStr = bid.getCreatedTime();
            dateStr = dateStr.replace("-", "/").substring(0,16);
            res.add(dateStr+"-"+bid.getBid());
        });

        return res;
    }


    /**
     * 批量现新增或删除供应商信息
     *
     * @param supplierAccountParamList
     * @param bidActivityCode
     * @return
     */
    private int batchInsertOrUpdate(JbxtActivityParam activityParam, List<SupplierAccountParam> supplierAccountParamList, String bidActivityCode) {
        List<JbxtUserParam> userParamList = Lists.newArrayList();
        supplierAccountParamList.forEach(e -> {
            JbxtUserDO userDO = null;
            List<JbxtUserDO> userDOList = userDao.getUser(JbxtUserParam.builder().codeName(e.getCodeName())
                    .username(e.getSupplierAccount()).activityCode(bidActivityCode).build());
            if (CollectionUtils.isNotEmpty(userDOList)) {
                userDO = userDOList.get(0);
            }
            if (null != userDO) {
                if (!StringUtils.equals(userDO.getSupplierName(), e.getSupplierName())) {
                    userDO.setSupplierName(e.getSupplierName());
                    userDO.setUsername(getUserName(e.getSupplierName()));
                    userDO.setPassword(getPassword(e.getSupplierName()));
                    userDO.setUpdatedTime(new Date());
                    userDO.setUpdator(activityParam.getCreator());
                    JbxtUserParam userParam = new JbxtUserParam();
                    CopyUtil.copyPropertiesExceptEmpty(userDO, userParam);
                    userParamList.add(userParam);
                }
            } else {
                JbxtUserParam userParam = JbxtUserParam.builder()
                        .code(worker.nextId())
                        .admin("0")
                        .companyCode(activityParam.getCompanyCode())
                        .sysCode(activityParam.getSysCode())
                        .createdTime(new Date())
                        .creator(activityParam.getCreator())
                        .updatedTime(new Date())
                        .updator(activityParam.getCreator())
                        .username(getUserName(e.getSupplierName()))
                        .password(getPassword(e.getSupplierName()))
                        .supplierName(e.getSupplierName())
                        .codeName(e.getCodeName())
                        .activityCode(activityParam.getCode())
                        .build();
                userParamList.add(userParam);
            }
        });
        int count = 0;
        if (CollectionUtils.isNotEmpty(userParamList)) {
            count = userDao.btachInsertAndUpdate(userParamList);
        }
        return count;
    }


    /**
     * 获取账号
     * 账号格式：供应商名称拼音首字母大写 + 四位自增数字
     *
     * @param supplierName 供应商名称
     * @return
     */
    private String getUserName(String supplierName) {
        String nameWords = SpellingUtil.chineseToFirstChar(supplierName).toUpperCase();
        String userName = nameWords + getShortCode();
        List<JbxtUserDO> userDOList = userDao.getUser(JbxtUserParam.builder().username(userName).build());
        while (CollectionUtils.isNotEmpty(userDOList)) {
            userName = nameWords + getShortCode();
            userDOList = userDao.getUser(JbxtUserParam.builder().username(userName).build());
        }
        return userName;
    }

    /**
     * 获取供应商密码
     * 密码格式 ：
     * （1）如果供应商名称的汉字字数为偶数个---供应商名称拼音首字母大写 + 日期（形如20200709） + 四位自增数字
     * （2）如果供应商名称的汉字字数为奇数个---供应商名称拼音首字母小写 + 日期（形如20200709） + 四位自增数字
     *
     * @param supplierName
     * @return
     */
    private String getPassword(String supplierName) {
        String today = LocalDate.now().toString().replace("-", "");
        String nameWords = SpellingUtil.chineseToFirstChar(supplierName);
        if (StringUtils.isNotEmpty(nameWords) && nameWords.length() % 2 == 0) {
            nameWords = nameWords.toUpperCase();
        }
        return nameWords + today + getShortCode();
    }

    /**
     * 获取四位自增数字
     *
     * @return
     */
    private String getShortCode() {
        String code = worker.nextId();
        return code.substring(code.length() - 4, code.length());
    }

}

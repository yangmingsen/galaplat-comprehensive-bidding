package com.galaplat.comprehensive.bidding.service.impl;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.*;
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
import com.galaplat.comprehensive.bidding.vos.RankInfoVO;
import com.galaplat.comprehensive.bidding.vos.SupplierAccountVO;
import com.galaplat.platformdocking.base.core.utils.CopyUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
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
import java.text.MessageFormat;
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

    /* 新增操作类型*/
    private static final String OPRATETYPE_ADD = "add";

    /* 修改操作类型*/
    private static final String OPRATETYPE_UPDATE = "update";

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

    @Autowired
    private IJbxtMinbidDao minbidDao;

    @Override
    public PageInfo listCompetitiveListPage(CompetitiveListQuery query) throws BaseException {
        CompetitiveListParam param = new CompetitiveListParam();
        if (null != query) {
            if (StringUtils.isNotEmpty(query.getBidActivityCode())) {
                query.getBidActivityCodeList(query.getBidActivityCode());
            }
            if (StringUtils.isNotEmpty(query.getActivityStatus())) {
                query.getActivityStatusList(query.getActivityStatus());
            }
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
    public String addAndUpdate(JbxtActivityParam activityParam, String type, String bidActivityCode) throws Exception {
        String activityCode = bidActivityCode;
        List<SupplierAccountParam> supplierAccountParamList = activityParam.getSupplierAccountParams();
        // 校验代号重复
        Map<String, List<SupplierAccountParam>> supplierAccountParamMap = supplierAccountParamList.stream().collect(Collectors.groupingBy(o -> o.getCodeName()));
        for (Map.Entry<String, List<SupplierAccountParam>> m : supplierAccountParamMap.entrySet()) {
            List<SupplierAccountParam> paramList = m.getValue();
            if (paramList.size() > 1) {
                throw new BaseException("【" + m.getKey() + "】代号重复,请重新填写!", "【" + m.getKey() + "】代号重复,请重新填写!");
            }
        }

        // 校验供应商重复
        Map<String, List<SupplierAccountParam>> supplierNameMap = supplierAccountParamList.stream().collect(Collectors.groupingBy(o -> o.getSupplierName()));
        for (Map.Entry<String, List<SupplierAccountParam>> m : supplierNameMap.entrySet()) {
            List<SupplierAccountParam> paramList = m.getValue();
            if (paramList.size() > 1) {
                throw new BaseException("供应商【" + m.getKey() + "】重复,请重新填写!","供应商【" + m.getKey() + "】重复,请重新填写!");
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
            if (null != activityDO) {
                activityParam.setStatus(activityDO.getStatus());
                if (activityDO.getStatus().equals(ActivityStatusEnum.FINISH.getCode())) {
                    throw new BaseException("该竞标活动已结束不允许编辑！", "该竞标活动已结束不允许编辑！");
                }
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
        // 保存或更新
        activityParam.setCode(activityCode);
        batchInsertOrUpdate(activityParam, supplierAccountParamList, bidActivityCode);
        if (StringUtils.equals(type, OPRATETYPE_UPDATE) && StringUtils.isNotEmpty(bidActivityCode)) {
            // 删除多余的供应商
            batchDeleteUser(activityParam, supplierAccountParamList, bidActivityCode);
        }
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
        List<JbxtGoodsDO> goodsList = goodsDdao.listGoods(JbxtGoodsParam.builder().activityCode(bidActivityCode).build());
        List<List<String>> bidPriceRankData = new ArrayList<>();
        List<List<String>> bidPriceDdetailData = new ArrayList<>();
        int index = 1;
        for (JbxtGoodsDO goods : goodsList) {
            goods.setGoodsId(index);
            // 查询某竞标活动某竞品的所有用户
            List<String> allBidUserCodes = biddingDao.listBidActivityUsers(JbxtBiddingParam.builder().goodsId(goods.getGoodsId())
                    .activityCode(goods.getActivityCode()).build());

            // 竞价等级
            Map<String,Object>  map = getBidPriceFinalRank(goods, allBidUserCodes);
            int size  = (int) map.get("size");
            if (maxBidNum <  size) {
                maxBidNum = size;
            }
            bidPriceRankData.add((List<String>)map.get("list"));

            // 竞价详情获取
            for (int i = 0; i < allBidUserCodes.size(); i++) {
                String userCode = allBidUserCodes.get(i);
                bidPriceDdetailData.add(getBidPriceDetail(userCode, goods, bidActivityCode));
            }
            index ++;
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

    public  Workbook createMultiSheet(Map<String, List<List<String>>> sheetData)  {
        //根据 type 参数生成工作簿实例对象
        Workbook workbook = new XSSFWorkbook();

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
        String fileName = MessageFormat.format("[{0}]-{1}{2}", bidActivityCode, LocalDate.now().toString(), ".xls");
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
            String userName = null != userDO && userDO.getSupplierName() != null ? userDO.getSupplierName() : bid.getUserCode();
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
        String userName = null != userDO && userDO.getSupplierName() != null ? userDO.getSupplierName()  : userCode;
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
        List<JbxtUserParam> saveUserParamList = Lists.newArrayList();
        supplierAccountParamList.forEach(e -> {
            List<JbxtUserDO> userDOList = null;
            // 修改的的时候
            if (StringUtils.isNotEmpty(bidActivityCode)) {
                 userDOList = userDao.getUser(JbxtUserParam.builder().codeName(e.getCodeName())
                        .supplierName(e.getSupplierName()).activityCode(bidActivityCode).build());
            }
            // 新增时userDOList必为空，修改时 userDOList可能为空
            if(CollectionUtils.isEmpty(userDOList)) {
                JbxtUserParam userParam = JbxtUserParam.builder()
                        .code(worker.nextId())
                        .admin("0")
                        .companyCode(activityParam.getCompanyCode())
                        .sysCode(activityParam.getSysCode())
                        .createdTime(new Date())
                        .creator(activityParam.getCreator())
                        .updatedTime(new Date())
                        .updator(activityParam.getCreator())
                        .username(getUserName())
                        .password(getPassword())
                        .supplierName(e.getSupplierName())
                        .codeName(e.getCodeName())
                        .activityCode(activityParam.getCode())
                        .build();
                saveUserParamList.add(userParam);
            }
        });

        int insertCount = 0;
        if (CollectionUtils.isNotEmpty(saveUserParamList)) {
            insertCount = userDao.btachInsertAndUpdate(saveUserParamList);
        }
        return insertCount;
    }

    /**
     * 批量删除供应商
     * @param activityParam 竞标活动
     * @param supplierAccountParamList 供应商信息
     * @param bidActivityCode 竞标活动编码
     * @return
     * @throws Exception
     */
    private int batchDeleteUser(JbxtActivityParam activityParam, List<SupplierAccountParam> supplierAccountParamList, String bidActivityCode) throws  Exception {
        List<String> deleteUserList = Lists.newArrayList();
        List<JbxtUserDO> currentUserDOList = null;
        currentUserDOList = userDao.getUser(JbxtUserParam.builder().activityCode(bidActivityCode).build());
        currentUserDOList.stream().forEach(e->{
            int time = 0;
            boolean exists = false;
            for (SupplierAccountParam accountParam : supplierAccountParamList) {
                time ++ ;
                if (StringUtils.equals(e.getSupplierName(), accountParam.getSupplierName())
                        && StringUtils.equals(e.getCodeName(), accountParam.getCodeName())) {
                    exists = true;
                    break;
                }
            }
            if (time == supplierAccountParamList.size() && !exists) {
                deleteUserList.add(e.getCode());
            }
        });

        int deleteCount = 0;

        if ( (activityParam.getStatus().equals(ActivityStatusEnum.BIDING.getCode())
                || activityParam.getStatus().equals(ActivityStatusEnum.FINISH.getCode()))
        && CollectionUtils.isNotEmpty(deleteUserList)) {
            throw  new BaseException("竞标中或者竞标结束的活动不允许删减供应商！","竞标中或者竞标结束的活动不允许删减供应商！");
        }

        if (CollectionUtils.isNotEmpty(deleteUserList)) {
            deleteCount = userDao.batchDeleteUser(deleteUserList, bidActivityCode);
            // 删除竞价中的供应商的信息
          /*   biddingDao.deleteBidding(JbxtBiddingParam.builder().activityCode(bidActivityCode).userCodeList(deleteUserList).build());
             minbidDao.deleteBidding(JbxtMinbidParam.builder().activityCode(bidActivityCode).userCodeList(deleteUserList).build());*/
        }
        return deleteCount;
    }

    /**
     * 获取账号
     * 账号格式：供应商名称拼音首字母大写取前两个字母 + 四位自增数字
     *
     * @return
     */
    private String getUserName() {
        String  words = "abcdefghijklmnopqrstuvwxyz";
        String userName = RandomStringUtils.random(2,words) + getShortCode(3)
                + RandomStringUtils.random(1,words) ;
        List<JbxtUserDO> userDOList = userDao.getUser(JbxtUserParam.builder().username(userName).build());
        while (CollectionUtils.isNotEmpty(userDOList)) {
            userName = RandomStringUtils.random(2,words) + getShortCode(3)
                    + RandomStringUtils.random(1,words) ;
            userDOList = userDao.getUser(JbxtUserParam.builder().username(userName).build());
        }
        return userName;
    }

    /**
     * 获取供应商密码
     * 密码格式 ：供应商名称拼音首字母 + 两位随机数 + 供应商名称拼音首字母/2+1的字母 +  两位随机数 + 供应商名称拼音首字母最后两个字母
     *
     * @return
     */
    private String getPassword() {
        String  words = "abcdefghijklmnopqrstuvwxyz";
        String password = RandomStringUtils.random(2,words) + getShortCode(3)
                + RandomStringUtils.random(2,words) + getShortCode(1);
        List<JbxtUserDO> userDOList = userDao.getUser(JbxtUserParam.builder().password(password).build());
        while (CollectionUtils.isNotEmpty(userDOList)) {
            password =  RandomStringUtils.random(2,words) + getShortCode(3)
                    + RandomStringUtils.random(2,words) + getShortCode(1);
            userDOList = userDao.getUser(JbxtUserParam.builder().password(password).build());
        }
        return password;
    }

    /**
     * 获取四位自增数字
     *
     * @return
     * @param index
     */
    private String getShortCode(int index) {
        String code = worker.nextId();
        return code.substring(code.length() - index, code.length());
    }

}

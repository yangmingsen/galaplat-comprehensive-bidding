package com.galaplat.comprehensive.bidding.service.impl;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.baseplatform.messaging.plugin.feign.IFeiginMessageClient;
import com.galaplat.comprehensive.bidding.dao.*;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.*;
import com.galaplat.comprehensive.bidding.dao.params.*;
import com.galaplat.comprehensive.bidding.enums.ActivityStatusEnum;
import com.galaplat.comprehensive.bidding.enums.PhoneTempleteEnum;
import com.galaplat.comprehensive.bidding.querys.CompetitiveListQuery;
import com.galaplat.comprehensive.bidding.service.ICompetitiveListManageService;
import com.galaplat.comprehensive.bidding.utils.IdWorker;
import com.galaplat.comprehensive.bidding.utils.Tuple3;
import com.galaplat.comprehensive.bidding.vos.BidCodeVO;
import com.galaplat.comprehensive.bidding.vos.RankInfoVO;
import com.galaplat.platformdocking.base.core.utils.CopyUtil;
import com.github.pagehelper.PageInfo;
import feign.Response;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.galaplat.baseplatform.file.plugin.feign.IFileFeignClient;
import org.galaplat.baseplatform.file.plugin.feign.vos.ImageVO;
import org.galaplat.baseplatform.file.upload.untils.MyMultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
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
    private ActivityDao activityDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private GoodsDao goodsDdao;

    @Autowired
    private BiddingDao biddingDao;

    @Autowired
    private IdWorker worker;

    @Autowired
    private IJbxtMinbidDao minbidDao;

    @Autowired
    private  IFileFeignClient fileFeignClient;

    @Autowired
    private IFeiginMessageClient messageClient;

    @Autowired
    IFeiginMessageClient fmi;

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
    public int delete(String[] activityCodes) throws BaseException {
        return activityDao.delete(activityCodes);
    }

    @Override
    public String exportBidRankAndBidPrice(String bidActivityCode, HttpServletResponse response, HttpServletRequest request) throws BaseException {
        if (StringUtils.isEmpty(bidActivityCode)) {
            throw new BaseException("竞标编码不能为空！", "竞标编码不能为空！");
        }
        int maxBidNum = 0;
        List<GoodsDO> goodsList = goodsDdao.listGoods(JbxtGoodsParam.builder().activityCode(bidActivityCode).build());
        List<List<String>> bidPriceRankData = new ArrayList<>();
        List<List<String>> bidPriceDdetailData = new ArrayList<>();
        int index = 1;
        for (GoodsDO goods : goodsList) {
            // 查询某竞标活动某竞品的所有用户
            List<String> allBidUserCodes = biddingDao.listBidActivityUsers(JbxtBiddingParam.builder().goodsId(goods.getGoodsId())
                    .activityCode(goods.getActivityCode()).build());

            // 竞价等级
            Map<String,Object>  map = getBidPriceFinalRank(goods, allBidUserCodes, index);
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
    private Map<String,Object> getMinBidList(GoodsDO goods, List<String> allBidUserCodes) {

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
            UserDO userDO = userDao.getJbxtUser(JbxtUserParam.builder().code(bid.getUserCode()).build());
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
     * @param index
     * @return
     */
    private Map<String,Object> getBidPriceFinalRank(GoodsDO goods, List<String> allBidUserCodes, int index) {
        Map<String,Object> resultMap = new HashMap<>();

        List<String> res = new ArrayList<>();
        res.add(index+"");
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

    public  List<String> getBidPriceDetail(String userCode, GoodsDO goods, String activityCode) {
        List<BidDVO> bids = biddingDao.getOneSupplierBidPriceDeatil(JbxtBiddingParam.builder()
                .userCode(userCode).goodsId(goods.getGoodsId()).activityCode(activityCode).build());
        List<String> res = new ArrayList<>();
        res.add(goods.getCode());
        res.add(goods.getName());

        UserDO userDO = userDao.getJbxtUser(JbxtUserParam.builder().code(userCode).build());
        String userName = null != userDO && userDO.getSupplierName() != null ? userDO.getSupplierName()  : userCode;
        res.add((userName));

        bids.forEach(bid -> {
            String dateStr = bid.getCreatedTime();
            dateStr = dateStr.replace("-", "/").substring(0,16);
            res.add(dateStr+"-"+bid.getBid());
        });

        return res;
    }


    @Override
    public BidCodeVO getBidcode(String userName) {
        return  BidCodeVO.builder().bidActivityCode(worker.nextId()).creator(userName).createdDate(DateFormatUtils.format(new Date(), "yyyy-MM-dd")).build();
    }

    @Override
    public  String saveBidActivityBasicInfo(BidActivityInfoParam infoParam, String userName) throws Exception {
        int count = 0;

        String bidActivityCode = infoParam.getBidActivityCode();
        Integer bidingType = infoParam.getBidingType();
        String predictBidDateTime = infoParam.getPredictBidDateTime();
        String bidActivityInfo =  infoParam.getBidActivityInfo();
        if (StringUtils.isAnyBlank(bidActivityCode, predictBidDateTime) || null == bidingType) {
            throw new BaseException("竞标单编号，出价方式，预计竞标日全部必填！","竞标单编号，出价方式，预计竞标日全部必填！");
        }

        if (bidActivityInfo.length() > 500) {
            throw new BaseException("竞标描述不能超过500个字符！","竞标描述不能超过500个字符！");
        }

        if ( 1 != bidingType && 2 != bidingType) {
            throw new BaseException("出价方式方式异常！","出价方式方式异常！");
        }

        ActivityDO activityDO = new ActivityDO();
        activityDO.setCode(bidActivityCode);
        activityDO.setBidingType(infoParam.getBidingType());
        activityDO.setBidActivityInfo(infoParam.getBidActivityInfo());
        Date predictBidDate = DateUtils.parseDate(infoParam.getPredictBidDateTime(),"yyyyMMddHHmmss");
        activityDO.setPredictBidDatetime(predictBidDate);

        if (StringUtils.isNotBlank(infoParam.getType()) && StringUtils.equals(infoParam.getType(), OPRATETYPE_ADD)) {
            ActivityDO sourceActivity = activityDao.getJbxtActivityByParam(JbxtActivityParam.builder().code(bidActivityCode).build());
            if (null != sourceActivity) {
                throw  new BaseException("当前竞标单编号对应的竞标活动已存在，请重新新增竞标单！","当前竞标单编号对应的竞标活动已存在，请重新新增竞标单！");
            }

            activityDO.setCreator(userName);
            activityDO.setCreatedTime(new Date());
            activityDO.setUpdatedTime(new Date());
            activityDO.setUpdator(userName);
            activityDO.setRecordStatus(1);
            activityDO.setStatus(ActivityStatusEnum.UNEXPORT.getCode());
            count = activityDao.insertBidActivity(activityDO);
            if (count == 0) {
                throw  new BaseException("新增竞标活动失败！","新增竞标活动失败！");
            }
        } else if (StringUtils.isNotBlank(infoParam.getType()) && StringUtils.equals(infoParam.getType(),OPRATETYPE_UPDATE)) {
            ActivityDO sourceActivity = activityDao.getJbxtActivityByParam(JbxtActivityParam.builder().code(bidActivityCode).build());
            if (null == sourceActivity ) {
               throw new BaseException("竞标单编码错误，竞标活动不存在！","竞标单编码错误，竞标活动不存在！");
            }

            if (ActivityStatusEnum.BIDING.getCode().equals(sourceActivity.getStatus())
                    || ActivityStatusEnum.FINISH.getCode().equals(sourceActivity.getStatus()) ) {
                throw new BaseException("竞标活动进行中或已结束，不可在编辑竞标基本信息！","竞标活动进行中或已结束，不可在编辑竞标基本信息！");
            }
            activityDO.setUpdator(userName);
            activityDO.setUpdatedTime(new Date());
            count = activityDao.updateBidActivity(activityDO);
            if (count == 0) {
                throw new BaseException("新增竞标活动失败！", "新增竞标活动失败！");
            }

            if (checkActivityInfoComplete(bidActivityCode)) {
                activityDO.setStatus(ActivityStatusEnum.IMPORT_NO_SATRT.getCode());
            }

            // 如果竞标描述和竞标预计时间改变则将所有邮件和短信状态置为未发送
            if (!StringUtils.equals(sourceActivity.getBidActivityInfo(), activityDO.getBidActivityInfo())
            || (sourceActivity.getPredictBidDatetime().compareTo(activityDO.getPredictBidDatetime()) != 0)) {
            userDao.updateBySomeParam(JbxtUserParam.builder().sendMail(0).sendSms(0).activityCode(bidActivityCode).build(),
                  JbxtUserParam.builder().activityCode(bidActivityCode).build());
            }
        } else {
            throw  new BaseException("操作类型错误！","操作类型错误！");
     }
    return bidActivityCode;
    }

    @Override
    public BidActivityDVO getBidActivityWithGoodsAndSupplier(String bidActivityCode) throws Exception {
        ActivityDO activityDO = activityDao.getJbxtActivityByParam(JbxtActivityParam.builder().code(bidActivityCode).build());
        if (null == activityDO) {
        throw new BaseException("竞标编码为" + bidActivityCode + "的竞标活动不存在！","竞标编码为" + bidActivityCode + "的竞标活动不存在！");
        }
        List<GoodsDVO> goodsDVOList = goodsDdao.getListJbxtGoodsByActivityCode(bidActivityCode);
        List<UserDO> userDOList = userDao.listJbxtUser(JbxtUserParam.builder().activityCode(bidActivityCode).build());

        List<BidGoodsDVO> goodsDVOSList = Lists.newArrayList();
        goodsDVOList.stream().forEach(e->{
            BidGoodsDVO dvo = BidGoodsDVO.builder()
                    .code(e.getCode())
                    .goodsName(e.getName())
                    .firstPrice(e.getFirstPrice())
                    .retainPrice(e.getFirstPrice())
                    .yearPurchaseNum(e.getNum())
                    .bidTimeNum(e.getTimeNum())
                    .lastChangTime(e.getLastChangTime())
                    .perDelayTime(e.getPerDelayTime())
                    .delayTimes(e.getDelayTimes())
                    .build();
            goodsDVOSList.add(dvo);
        });

        List<BidSupplierDVO> supplierDVOList = Lists.newArrayList();
        userDOList.stream().forEach(e->{
            BidSupplierDVO dvo = BidSupplierDVO.builder()
                    .code(e.getCode())
                    .codeName(e.getCodeName())
                    .supplierName(e.getSupplierName())
                    .contactPerson(e.getContactPerson())
                    .phone(e.getPhone())
                    .emailAddress(e.getEmailAddress())
                    .loginStatus(e.getLoginStatus())
                    .sendMail(e.getSendMail())
                    .sendSms(e.getSendSms())
                    .build();
            supplierDVOList.add(dvo);
        });

        String filePath = activityDO.getFilePath();
        String fileName = "";
        if (StringUtils.isNotBlank(filePath)) {
            String [] fileInfo = filePath.split("/");
            fileName = fileInfo[fileInfo.length-1];
        }

       return BidActivityDVO.builder()
                .bidActivityCode(bidActivityCode)
                .bidActivityInfo(activityDO.getBidActivityInfo())
                .creator(activityDO.getCreator())
                .createdDate(null != activityDO.getCreatedTime() ? DateFormatUtils.format(activityDO.getCreatedTime(),"yyyy-MM-dd") : "")
                .bidingType(activityDO.getBidingType())
                .predictBidDatetime(null != activityDO.getPredictBidDatetime() ? DateFormatUtils.format(activityDO.getPredictBidDatetime(),"yyyy-MM-dd HH:mm:ss") : "")
                .promiseTitle(activityDO.getPromiseTitle())
                .promiseText(activityDO.getPromiseText())
                .filePath(fileName)
                .activityStatus(activityDO.getStatus())
                .goodsList(goodsDVOSList)
                .supplierList(supplierDVOList)
                .build();
    }



    /**
     * 获取账号
     * 账号格式：供应商名称拼音首字母大写取前两个字母 + 四位自增数字
     *
     * @return
     */
    @Override
    public String getUserName() {
        String  words = "abcdefghijklmnopqrstuvwxyz";
        String userName = RandomStringUtils.random(2,words) + getShortCode(3)
                + RandomStringUtils.random(1,words) ;
        List<UserDO> userDOList = userDao.getUser(JbxtUserParam.builder().username(userName).build());
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
    @Override
    public String getPassword() {
        String  words = "abcdefghijklmnopqrstuvwxyz";
        String password = RandomStringUtils.random(2,words) + getShortCode(3)
                + RandomStringUtils.random(2,words) + getShortCode(1);
        List<UserDO> userDOList = userDao.getUser(JbxtUserParam.builder().password(password).build());
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

    /**
     * 检查竞标活动的竞品是否导入，供应商是否导入，承诺函是否导入
     * @param bidActivityCode
     * @return
     */
    @Override
    public boolean checkActivityInfoComplete(String bidActivityCode) {
        boolean activityUnimportData = false;
        boolean promiseExists = false;
        boolean goodsExists = false;
        boolean supplierExists = false;

        ActivityDO ActivityDO = activityDao.getJbxtActivityByParam(JbxtActivityParam.builder().code(bidActivityCode).build());

        if (ActivityStatusEnum.UNEXPORT.getCode().equals(ActivityDO.getStatus())) {
            activityUnimportData = true;
        }

        if ( null != ActivityDO && StringUtils.isNotBlank(ActivityDO.getPromiseText())) {
            promiseExists = true;
        }

        List<GoodsDO>   goodsDOs  = goodsDdao.listGoods(JbxtGoodsParam.builder().activityCode(bidActivityCode).build());
        if ( CollectionUtils.isNotEmpty(goodsDOs)) {
            goodsExists = true;
        }

        List<UserDVO> users = userDao.findAllByActivityCode(bidActivityCode);
        if ( CollectionUtils.isNotEmpty(users)) {
            supplierExists = true;
        }

        return  supplierExists && goodsExists && promiseExists && activityUnimportData;

    }

    @Override
     public  MessageAndEmialDVO sendMsgAndMail(String bidActivityCode, String phone, String emailAddress, String type, String supplierCode) throws BaseException {
        MessageAndEmialDVO messageAndEmialDVO = new MessageAndEmialDVO();

        if (StringUtils.isAnyBlank(bidActivityCode,type)) {
            throw new BaseException("bidActivityCode 或 type 参数异常！","bidActivityCode 或 type 参数异常！");
        }

        ActivityDO ActivityDO = activityDao.getJbxtActivityByParam(JbxtActivityParam.builder().code(bidActivityCode).build());
        if (ActivityStatusEnum.FINISH.getCode().equals(ActivityDO.getStatus())) {
            throw new BaseException("竞标活动已结束不能发送短信或者发送邮箱！","竞标活动已结束不能发送短信或者发送邮箱！");
        }

        if (StringUtils.equals(type, "all")) {

            if (StringUtils.isAnyBlank(bidActivityCode)) {
                throw new BaseException("参数异常，竞标单编号必传！","参数异常，竞标单编号必传！");
            }

            List<BidSupplierDVO> supplierDVOS = userDao.listSupplierInfo(bidActivityCode);
            List<String> allPhones = supplierDVOS.stream().map(e->e.getPhone()).collect(Collectors.toList());
            List<String> allEmailAddress = supplierDVOS.stream().map(e->e.getEmailAddress()).collect(Collectors.toList());
            Integer sendMailCount = 0;
            Integer sendMessageCount = 0;

            if (CollectionUtils.isNotEmpty(supplierDVOS)) {
                for (BidSupplierDVO supplierDvo: supplierDVOS) {
                    String email;
                    String phoneNumber;
                    String tempSupplierCode = supplierDvo.getCode();
                    if ( StringUtils.isNotBlank(email = supplierDvo.getEmailAddress())) {
                        sendMailCount += sendEmail(bidActivityCode, email,tempSupplierCode );
                    }
                    if ( StringUtils.isNotBlank(phoneNumber = supplierDvo.getPhone())) {
                        sendMessageCount +=  sendPhoneMsg(bidActivityCode, phoneNumber, tempSupplierCode);
                    }
                }
            }

            messageAndEmialDVO.setEmailSeandStatus(CollectionUtils.isNotEmpty(allEmailAddress) && sendMailCount > 0 && sendMailCount == allEmailAddress.size()
                    ? 1 :  (CollectionUtils.isNotEmpty(allEmailAddress) && sendMailCount == 0 ? 0 : 2));

            messageAndEmialDVO.setMessageSendStatus(CollectionUtils.isNotEmpty(allPhones) && sendMessageCount > 0 && sendMessageCount == allPhones.size()
                    ? 1 :  (CollectionUtils.isNotEmpty(allPhones) && sendMessageCount == 0 ? 0 : 2));

        } else if (StringUtils.equals(type, "phone")) {

            // 只发送短信
            if (StringUtils.isAnyBlank(bidActivityCode, phone, supplierCode)) {
                throw new BaseException("参数异常，竞标单编号，手机号，供应商编码必传！","参数异常，竞标单编号，手机号，供应商编码必传！");
            }
            messageAndEmialDVO.setMessageSendStatus(sendPhoneMsg(bidActivityCode,phone,supplierCode));
        } else if (StringUtils.equals(type, "email")) {

            // 只发送邮件
            if (StringUtils.isAnyBlank(bidActivityCode, emailAddress, supplierCode)) {
                throw new BaseException("参数异常，竞标单编号，手机号，供应商编码必传！","参数异常，竞标单编号，手机号，供应商编码必传！");
            }
            messageAndEmialDVO.setEmailSeandStatus(sendEmail(bidActivityCode, emailAddress,supplierCode));
        } else {
            throw new BaseException("type 参数值异常！","type 参数值异常！");
        }
        return messageAndEmialDVO;
    }

    @Override
    public List<BidSupplierDVO> listSupplierInfo(String bidActivityCode) {
        return  userDao.listSupplierInfo(bidActivityCode);
    }

    @Override
    public List<BidGoodsDVO> listGoods(String bidActivityCode) {
        List<GoodsDO> goodsList = goodsDdao.listGoods(JbxtGoodsParam.builder().activityCode(bidActivityCode).build());
        List<BidGoodsDVO> goodsDVOList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(goodsList)) {
            goodsList.stream().forEach(e ->{
                BidGoodsDVO goodsDVO = new BidGoodsDVO();
                CopyUtil.copyPropertiesExceptEmpty(e, goodsDVO);
                goodsDVO.setGoodsName(e.getName());
                goodsDVO.setYearPurchaseNum(e.getNum());
                goodsDVO.setBidTimeNum(e.getTimeNum());
                goodsDVOList.add(goodsDVO);
            });
        }
        return goodsDVOList;
    }

    @Override
    public int  savePromiseText(String bidActivityCode, String promiseTitle, String promiseText) throws  BaseException{

        ActivityDO ActivityDO = activityDao.getJbxtActivityByParam(JbxtActivityParam.builder().code(bidActivityCode).build());
        if (null == ActivityDO) {
           throw  new BaseException("竞标单编号为" + bidActivityCode +"的竞标活动不存在！","竞标单编号为" + bidActivityCode +"的竞标活动不存在！");
        }

        if (ActivityStatusEnum.FINISH.getCode().equals(ActivityDO.getStatus())) {
            throw  new BaseException("竞标活动已结束不允许修改承诺函内容！","竞标活动已结束不允许修改承诺函内容！");
        }

        if (StringUtils.isNotBlank(promiseTitle) && promiseTitle.length() > 20) {
            throw new BaseException("标题字数不能超过20个字！", "标题字数不能超过20个字！");
        }


        if (StringUtils.isNotBlank(promiseText) && promiseText.length() > 3000) {
            throw new BaseException("承诺函内容不能超过3000个字！", "标题字数不能超过3000个字！");
        }
        JbxtActivityParam updateActivityParam = JbxtActivityParam.builder().code(bidActivityCode).promiseTitle(promiseTitle).promiseText(promiseText).build();
        JbxtActivityParam conditionActivityParam = JbxtActivityParam.builder().code(bidActivityCode).build();
        int updateCount = activityDao.updateJbxtActivityBySomeParam(updateActivityParam, conditionActivityParam);

        if (updateCount > 0 && checkActivityInfoComplete(bidActivityCode)) {
            updateActivityParam.setStatus(ActivityStatusEnum.IMPORT_NO_SATRT.getCode());
            updateActivityParam.setPromiseText(null);
            updateActivityParam.setPromiseTitle(null);
            activityDao.updateJbxtActivityBySomeParam(updateActivityParam, conditionActivityParam);
        }
        return updateCount;
    }

    /***
     * 上传附件
     * @param bidActivityCode
     * @param rarFile
     * @return
     */
    @Override
    public String fileUpload(String bidActivityCode, MultipartFile rarFile) {
        ImageVO imageVO =null;

        Tuple3<String, String , String > stringTuple3 = getFileAllName(bidActivityCode);
        String fileName = stringTuple3._1; // 文件名
        String oldOriginFileName = stringTuple3._2; // 原文件名
        String extendName =  stringTuple3._3; // 扩展名
        String newOriginFileName = "";

        if (StringUtils.isNotBlank(fileName) || StringUtils.isNotBlank(oldOriginFileName)|| StringUtils.isNotBlank(extendName)) {
            try {
                fileFeignClient.delete(fileName);
            } catch (BaseException e) {
                log.error("There is an error of deleting file .【{}】,【{}】",e.getMessage(),e );
                e.printStackTrace();
            }
        }

        try {
            byte[]  bytes = rarFile.getBytes();
            newOriginFileName = rarFile.getOriginalFilename();
            MyMultipartFile myMultipartFile = new MyMultipartFile("file", "", "multipart/form-data", bytes);
            imageVO = new ImageVO(bidActivityCode, newOriginFileName , "-", "-", myMultipartFile);
        } catch (IOException e) {
            log.error(" There is an error of uploadinfg file. 【{}】,【{}】", e.getMessage(),e );
            e.printStackTrace();
        }
        try {
            if (null != imageVO) {
                String fileDir = fileFeignClient.addFile(imageVO);
                fileDir = StringUtils.isNotBlank(newOriginFileName) ? fileDir + "/" + newOriginFileName : fileDir;
                activityDao.updateBidActivity(ActivityDO.builder().code(bidActivityCode).filePath(fileDir).build());

                if (StringUtils.isNotBlank(oldOriginFileName)) {
                    oldOriginFileName = oldOriginFileName.split("\\.")[0];
                    if (StringUtils.isNotBlank(newOriginFileName) && StringUtils.isNotBlank(oldOriginFileName) &&
                            !StringUtils.equals(newOriginFileName, oldOriginFileName)) {
                        userDao.updateBySomeParam(JbxtUserParam.builder().sendMail(0).activityCode(bidActivityCode).build(),
                                JbxtUserParam.builder().activityCode(bidActivityCode).build());
                    }
                }

                return newOriginFileName;
            }
        } catch (BaseException e) {
            log.error(" There is an error of uploadinfg file. 【{}】,【{}】", e.getMessage(),e );
            e.printStackTrace();
        }
        return  null;
    }

    /***
     * 获取附件
     * @param bidActivityCode
     * @return
     */
    @Override
    public MultipartFile getfile(String bidActivityCode) throws BaseException{

        MultipartFile cMultiFile = null;

        try {
            Tuple3<String, String , String > stringTuple3 = getFileAllName(bidActivityCode);
            // 文件名
            String fileName = stringTuple3._1;
            // 原文件名
            String originFileName = stringTuple3._2;
            // 扩展名
            String extendName =  stringTuple3._3;

            if (!StringUtils.isAnyBlank(fileName, extendName)) {
                feign.Response response = fileFeignClient.getFile(fileName,extendName);
                Response.Body body = response.body();
                try {

                    InputStream  inputStream = body.asInputStream();
                    MultipartFile multipartFile = new MockMultipartFile(bidActivityCode + "." + extendName,originFileName + "." + extendName,"", inputStream);
                    ApplicationHome h = new ApplicationHome(getClass());
                    String pathName = h.getDir().getAbsolutePath() + "/" + originFileName + "." + extendName;
                    File file = new File(pathName);
                    multipartFile.transferTo(file);
                    DiskFileItem fileItem = new DiskFileItem("file", "multipart/form-data", true, file.getName(), (int) file.length(), file);
                    IOUtils.copy(new FileInputStream(file), fileItem.getOutputStream());
                    cMultiFile = new CommonsMultipartFile(fileItem);

                } catch (IOException e) {
                    log.error(" There is an error of getting file. 【{}】,【{}】", e.getMessage(),e );
                    e.printStackTrace();
                }
            }

        } catch (BaseException e) {
            log.error(" There is an error of getting file. 【{}】,【{}】", e.getMessage(),e );
            e.printStackTrace();
        }
        return  cMultiFile;
    }


    /**
     * 获取文件的各种名称
     * @param bidActivityCode
     * @return
     */
    @Override
    public Tuple3<String, String, String> getFileAllName(String bidActivityCode) {
        Tuple3<String, String, String> stringTuple3  = Tuple3.createTuple(null,null,null);
        ActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(bidActivityCode).build());
        String [] fileInfo = null != activityDO && StringUtils.isNotBlank(activityDO.getFilePath())
                ? activityDO.getFilePath().split("/") : null;

        // 文件名
        String fileName = null;
        // 原文件名
        String originFileName = null;
        // 扩展名
        String extendName =  null;

        if (null != fileInfo) {
            fileName = fileInfo[fileInfo.length - 2];
            originFileName = fileInfo[fileInfo.length - 1];
        }

        if (StringUtils.isNotBlank(originFileName)) {
            String[] originFileNameArray = originFileName.split("\\.");
            originFileName = originFileNameArray[0];
        }

        if (StringUtils.isNotBlank(fileName)) {
            String[] fileNameArray = fileName.split("\\.");
            fileName = fileNameArray[0];
            extendName = fileNameArray[1];
        }
        stringTuple3._1 = fileName;
        stringTuple3._2 = originFileName;
        stringTuple3._3 = extendName;
        return stringTuple3;
    }


    /***
     *  获取邮件内容字符串
     * @param bidActivityCode
     * @param emailAdress
     * @return
     */
    private String getMailContent(String bidActivityCode, String emailAdress, String supplierCode) throws  BaseException{
        ActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(bidActivityCode).build());
        String  bidActivityInfo = activityDO.getBidActivityInfo();
        String  predictBidDatetime = DateFormatUtils.format(activityDO.getPredictBidDatetime(), "yyyy-MM-dd HH:mm:ss");
        UserDO userDO = userDao.getUserByParam(JbxtUserParam.builder().activityCode(bidActivityCode).emailAddress(emailAdress).code(supplierCode).build());
        if (null == userDO) {
         throw  new BaseException("供应商信息获取错误！","供应商信息获取错误！");
        }
        String supplierName = userDO.getSupplierName();
        String username = userDO.getUsername();
        String password =  userDO.getPassword();

        StringBuilder mailContent = new StringBuilder("");
        if (!StringUtils.isAnyBlank(predictBidDatetime, supplierName,username,password )) {
            mailContent.append("致<B>").append(supplierName).append("</B>:</br>").append("您好！");
            mailContent.append("<p>现邀请贵公司参与ESR举行的竞标活动，以下是本次竞标活动相关信息：").append("</br>");
            mailContent.append("竞标单编号：").append(bidActivityCode).append("</br></p>");
            mailContent.append("<p>竞标描述：").append(StringUtils.isNotBlank(bidActivityInfo) ? bidActivityInfo : "").append("</p>");
            mailContent.append("<p>预计竞标日：<B>").append(predictBidDatetime).append("</B></p>");
            mailContent.append("<p>账号：").append(username).append("</br>");
            mailContent.append("密码：").append(password).append("</p>");
            mailContent.append("<p>登录链接：https://www.esrcloud.com/jb/ ").append("（建议使用谷歌、火狐、360浏览器打开）</br>");
            mailContent.append("温馨提示：请提前登录系统，熟悉系统操作与本次竞标的产品，如有疑问可联系对口业务人员；</p>");
            mailContent.append("<p>附件为竞标须知，请查收！</br>");
            mailContent.append(" 祝：竞标顺利！</br>");
            mailContent.append(" ESR</p>");
        }
        return mailContent.toString();
    }

    /***
     *  发送邮件
     * @param bidActivityCode
     * @param emailAddress
     * @param supplierCode
     * @return
     */
    private  Integer sendEmail(String bidActivityCode, String emailAddress, String supplierCode) throws BaseException {
        String cs2 = "\"1\"";
        ActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(bidActivityCode).build());
        if (null == activityDO) {
            throw  new BaseException("竞标活动不存在!","竞标活动不存在!");
        }
        String filePath = activityDO.getFilePath();
        Integer emailSeandStatus;
        String  mailSendResult = null;
        MultipartFile multipartFile = null;
        String emailContent = getMailContent(bidActivityCode, emailAddress, supplierCode);

        if (StringUtils.isNotBlank(filePath)) {
            multipartFile = getfile(bidActivityCode);
        }
        if (null != multipartFile && StringUtils.isNotBlank(emailAddress)) {
            // 发送邮件
            mailSendResult = messageClient.sendfile(emailAddress, "竞标活动通知",emailContent , multipartFile);
        } else if (StringUtils.isBlank(filePath) && StringUtils.isNotBlank(emailAddress)) {
            mailSendResult = messageClient.sendemail(emailAddress, "竞标活动通知",emailContent);
        }


        if (StringUtils.isNotBlank(mailSendResult) && StringUtils.equals(mailSendResult, cs2)) {
            userDao.updateBySomeParam(JbxtUserParam.builder().sendMail(1).activityCode(bidActivityCode).build(),
                    JbxtUserParam.builder().activityCode(bidActivityCode).emailAddress(emailAddress).code(supplierCode).build());
        } else {
            userDao.updateBySomeParam(JbxtUserParam.builder().sendMail(2).activityCode(bidActivityCode).build(),
                    JbxtUserParam.builder().activityCode(bidActivityCode).emailAddress(emailAddress).code(supplierCode).build());
        }

        emailSeandStatus = StringUtils.isNotBlank(mailSendResult) && StringUtils.equals(mailSendResult, cs2) ? 1: 0;

        return emailSeandStatus;
    }


    /**
     * 发送短信
     * @param bidActivityCode
     * @param phoneNumber
     * @param supplierCode
     * @return
     * @throws BaseException
     */
    private  Integer sendPhoneMsg(String bidActivityCode, String phoneNumber, String supplierCode) throws BaseException {

        String supplierName = null;
        String bidActivityInfo = null;
        String bidActivityDateTime = null;
        String bidActivityAccount = null;
        String bidActivityPassword = null;
        String tempcode  = PhoneTempleteEnum.BIDDINGMSG.getCode();

        ActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(bidActivityCode).build());
        if (null != activityDO) {
            bidActivityInfo = activityDO.getBidActivityInfo();
            bidActivityDateTime = DateFormatUtils.format(activityDO.getPredictBidDatetime(), "yyyy-MM-dd HH:mm:ss");
        }

        UserDO userDO = userDao.getUserByParam(JbxtUserParam.builder().activityCode(bidActivityCode).phone(phoneNumber).code(supplierCode).build());
        if (null != userDO) {
            bidActivityAccount = userDO.getUsername();
            bidActivityPassword = userDO.getPassword();
            supplierName = userDO.getSupplierName();
        }

        if (!StringUtils.isAnyBlank(phoneNumber, supplierName, bidActivityCode,
                 bidActivityDateTime, bidActivityAccount, bidActivityPassword, tempcode)) {
            String result = fmi.sendBidMsg(phoneNumber, supplierName, bidActivityCode, bidActivityInfo, bidActivityDateTime,
                    bidActivityAccount, bidActivityPassword, tempcode);
            if (StringUtils.isNotBlank(result) && result.equals("\"1\"")) {
                userDao.updateBySomeParam(JbxtUserParam.builder().sendSms(1).activityCode(bidActivityCode).build(),
                        JbxtUserParam.builder().activityCode(bidActivityCode).phone(phoneNumber).code(supplierCode).build());
                return 1;
            } else {
                userDao.updateBySomeParam(JbxtUserParam.builder().sendSms(2).activityCode(bidActivityCode).build(),
                        JbxtUserParam.builder().activityCode(bidActivityCode).phone(phoneNumber).code(supplierCode).build());
                return 0;
            }
        } else {
            userDao.updateBySomeParam(JbxtUserParam.builder().sendSms(2).activityCode(bidActivityCode).build(),
                    JbxtUserParam.builder().activityCode(bidActivityCode).phone(phoneNumber).code(supplierCode).build());
            return 0;
        }
    }

}

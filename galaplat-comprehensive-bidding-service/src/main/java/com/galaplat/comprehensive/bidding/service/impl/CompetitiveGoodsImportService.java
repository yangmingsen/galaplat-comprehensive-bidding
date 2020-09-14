package com.galaplat.comprehensive.bidding.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.JsonUtils;
import com.galaplat.baseplatform.permissions.feign.IFeignPermissions;
import com.galaplat.comprehensive.bidding.dao.ActivityDao;
import com.galaplat.comprehensive.bidding.dao.GoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.galaplat.comprehensive.bidding.enums.ActivityStatusEnum;
import com.galaplat.comprehensive.bidding.param.JbxtGoodsExcelStrParam;
import com.galaplat.comprehensive.bidding.param.JbxtGoodsExcelParam;
import com.galaplat.comprehensive.bidding.utils.ImportExcelValidateMapUtil;
import com.galaplat.comprehensive.bidding.utils.Tuple;
import com.galaplat.platformdocking.base.core.utils.CopyUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Row;
import org.galaplat.baseplatform.file.upload.service.IImportSubMethodWithParamService;
import org.galaplat.baseplatform.file.upload.vos.ImportVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;

/**@
 * @Description: 竞标活动竞品导入
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/9 20:15
 */
@Service("competitiveGoodsExcelService")
public class CompetitiveGoodsImportService implements IImportSubMethodWithParamService<JbxtGoodsExcelStrParam> {

    private static final Logger log = LoggerFactory.getLogger(CompetitiveGoodsImportService.class);

    @Autowired
    private IFeignPermissions feignPermissions;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private ActivityDao activityDao;


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public List<JbxtGoodsExcelStrParam> insertExcelDate(List<Map<String, Object>> list, ImportVO importVO) {
        if (CollectionUtils.isEmpty(list)) {return Collections.emptyList();}
        List<JbxtGoodsExcelStrParam>  errorList = Lists.newArrayList();
        List<JbxtGoodsExcelParam>  rightList = Lists.newArrayList();
        List<JbxtGoodsParam>  saveList = Lists.newArrayList();

        String creatorName = getCreatorName(importVO.getCreator());
        String paramJson = importVO.getParamJson();
        String activityCode= null;

        if (StringUtils.isNotEmpty(paramJson)) {
            Map<String, Object> mapVO = JSON.parseObject(paramJson, new TypeReference<Map<String, Object>>() {
            });
            activityCode = (String) mapVO.get("bidActivityCode");
        }
        ActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(activityCode).build());
        // 如果竞标活动的状态时竞标中或者已结束时，不允许导入
        if (activityDO.getStatus().equals(ActivityStatusEnum.BIDING.getCode())
                || activityDO.getStatus().equals(ActivityStatusEnum.FINISH.getCode())){
            return Collections.emptyList();
        }

        if (null != activityDO && CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object>  goodsMap: list) {
                JbxtGoodsExcelParam goodsExcelParam = new JbxtGoodsExcelParam();
                JbxtGoodsExcelStrParam goodsExceStrlParam = new JbxtGoodsExcelStrParam();
                StringBuilder errorMsg = new StringBuilder("");
                Tuple<String, JbxtGoodsExcelStrParam> paramTuple =  ImportExcelValidateMapUtil.validateField_1(JbxtGoodsExcelParam.class, goodsExceStrlParam, goodsMap);
                errorMsg.append(paramTuple._1);
                goodsExceStrlParam = paramTuple._2;
                errorMsg.append(validateGoodsInfo(goodsExceStrlParam));
                if (StringUtils.isNotEmpty(errorMsg.toString())) {
                    goodsExceStrlParam.setErrorMsg(errorMsg.toString());
                    errorList.add(goodsExceStrlParam);
                } else {
                    try {
                        goodsExcelParam = JsonUtils.toObject(JsonUtils.toJson(goodsMap), JbxtGoodsExcelParam.class);
                    } catch (Exception e) {
                        log.error(" 竞品导入格式化异常【{}】,【{}】",e.getMessage(), e );
                    }
                        goodsExcelParam.setActivityCode(activityCode);
                        goodsExcelParam.setCreatedTime(new Date());
                        goodsExcelParam.setUpdatedTime(new Date());
                        goodsExcelParam.setCreator(creatorName);
                        goodsExcelParam.setStatus("0");
                        rightList.add(goodsExcelParam);
                        JbxtGoodsParam goodsParam = new JbxtGoodsParam();
                        CopyUtil.copyPropertiesExceptEmpty(goodsExcelParam, goodsParam);
                        goodsParam.setAddDelayTimes(0);// 已延长次数默认为0
                        saveList.add(goodsParam);
                }
            }
            int insertCount = 0;

            if (CollectionUtils.isNotEmpty(saveList) && CollectionUtils.isEmpty(errorList)) {
                goodsDao.delete(JbxtGoodsParam.builder().activityCode(activityCode).build());
                insertCount = goodsDao.batchInsert(saveList);
            }
            List<GoodsDO> activityGoodsDOList = goodsDao.listGoods(JbxtGoodsParam.builder().activityCode(activityCode).build());
            if (activityDO.getStatus().equals(ActivityStatusEnum.UNEXPORT.getCode())
                    && (CollectionUtils.isNotEmpty(activityGoodsDOList) || insertCount > 0)) {
                activityDao.updateBidActivity(ActivityDO.builder().code(activityCode).status(ActivityStatusEnum.EXPORT_NO_SATRT.getCode()).build());
            }
            if (CollectionUtils.isNotEmpty(errorList)) {

                rightList.stream().forEach(e ->{
                    JbxtGoodsExcelStrParam excelStrParam = JbxtGoodsExcelStrParam.builder()
                            .serialNumber(e.getSerialNumber())
                            .code(e.getCode())
                            .name(e.getName())
                            .firstPrice(String.valueOf(e.getFirstPrice()))
                            .retainPrice(String.valueOf(e.getRetainPrice()))
                            .num(String.valueOf(e.getNum()))
                            .timeNum(String.valueOf(e.getTimeNum()))
                            .lastChangTime(String.valueOf(e.getLastChangTime()))
                            .perDelayTime(String.valueOf(e.getPerDelayTime()))
                            .delayTimes(String.valueOf(e.getDelayTimes()))
                            .companyCode(e.getCompanyCode())
                            .sysCode(e.getSysCode())
                            .build();
                    errorList.add(excelStrParam);
                });
            }
        }// if

        return errorList;
    }

    @Override
    public Object getExcelObject() {
        return new JbxtGoodsExcelParam();
    }

    @Override
    public boolean validateExcelRow(Row row, int i) throws BaseException {
        return false;
    }

    private String getCreatorName(String creator) {
        String creatorName = null;
        String result = "result";
                //获取用户名
                try {
                    JsonNode userByCode = feignPermissions.getUserByCode(creator);
                    if (userByCode == null || userByCode.get(result) == null) {
                        throw  new BaseException("创建者信息获取异常！","创建者信息获取异常！");
                    }
                    if (userByCode.get(result).get("name") != null) {
                        creatorName = userByCode.get(result).get("name").asText();
                    }else{
                        creatorName = creator;
                    }

                } catch (BaseException e) {
                    log.error(e.getMessage(),e);
                }
                return creatorName;
    }

    private String validateGoodsInfo(JbxtGoodsExcelStrParam excelParam) {

        StringBuilder error= new StringBuilder("");
        String timeNuimStr = excelParam.getTimeNum();
        String lastChangTimeStr = excelParam.getLastChangTime();
        String perDelayTimeStr = excelParam.getPerDelayTime();
        String delayTimesStr = excelParam.getDelayTimes();

        String regx = "^\\d+$";

        Integer timeNum = StringUtils.isNotBlank(timeNuimStr)&& Pattern.compile(regx).matcher(timeNuimStr).find() ? Integer.parseInt(timeNuimStr) : null;
        Integer lastChangTime = StringUtils.isNotBlank(lastChangTimeStr) && Pattern.compile(regx).matcher(lastChangTimeStr).find() ? Integer.parseInt(lastChangTimeStr) : null;
        Integer perDelayTime = StringUtils.isNotBlank(perDelayTimeStr) && Pattern.compile(regx).matcher(perDelayTimeStr).find()  ? Integer.parseInt(perDelayTimeStr) : null;
        Integer delayTimes = StringUtils.isNotBlank(delayTimesStr) && Pattern.compile(regx).matcher(delayTimesStr).find() ? Integer.parseInt(delayTimesStr) : null;

        if (null != timeNum && timeNum > 60) {
            error.append("竞标时长不能超过60分钟！");
        }
        if (null != lastChangTime && null != timeNum && lastChangTime > timeNum * 60) {
            error.append("延时窗口期不能超过竞标时长！");
        }
        if (null != perDelayTime && perDelayTime > 600) {
            error.append("单次延长不能超过600秒！");
        }
        if (null != delayTimes && delayTimes > 100) {
            error.append("延长次数不能超过100次！");
        }
        return error.toString();
    }

}

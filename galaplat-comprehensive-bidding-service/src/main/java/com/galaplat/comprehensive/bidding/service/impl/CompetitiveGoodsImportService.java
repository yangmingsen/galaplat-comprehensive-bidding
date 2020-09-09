package com.galaplat.comprehensive.bidding.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.JsonUtils;
import com.galaplat.baseplatform.permissions.feign.IFeignPermissions;
import com.galaplat.comprehensive.bidding.dao.IJbxtActivityDao;
import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.galaplat.comprehensive.bidding.enums.ActivityStatusEnum;
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

/**@
 * @Description: 竞标活动竞品导入
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/9 20:15
 */
@Service("competitiveGoodsExcelService")
public class CompetitiveGoodsImportService implements IImportSubMethodWithParamService<JbxtGoodsExcelParam> {

    private static final Logger log = LoggerFactory.getLogger(CompetitiveGoodsImportService.class);

    @Autowired
    private IFeignPermissions feignPermissions;

    @Autowired
    private IJbxtGoodsDao goodsDao;

    @Autowired
    private IJbxtActivityDao  activityDao;


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED,rollbackFor = Exception.class)
    public List<JbxtGoodsExcelParam> insertExcelDate(List<Map<String, Object>> list, ImportVO importVO) {
        if (CollectionUtils.isEmpty(list)) {return Collections.emptyList();}
        List<JbxtGoodsExcelParam>  errorList = Lists.newArrayList();
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
        JbxtActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(activityCode).build());
        // 如果竞标活动的状态时竞标中或者已结束时，不允许导入
        if (activityDO.getStatus().equals(ActivityStatusEnum.BIDING.getCode())
                || activityDO.getStatus().equals(ActivityStatusEnum.FINISH.getCode())){
            return Collections.emptyList();
        }

        if (null != activityDO && CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object>  goodsMap: list) {
                JbxtGoodsExcelParam goodsExcelParam = new JbxtGoodsExcelParam();
                StringBuilder errorMsg = new StringBuilder("");
                Tuple<String,JbxtGoodsExcelParam> paramTuple =  ImportExcelValidateMapUtil.validateField(goodsExcelParam, goodsMap);
                errorMsg.append(paramTuple._1);
                goodsExcelParam = paramTuple._2;
                errorMsg.append(validateGoodsInfo(goodsExcelParam));
                if (StringUtils.isNotEmpty(errorMsg.toString())) {
                    goodsExcelParam.setErrorMsg(errorMsg.toString());
                    errorList.add(goodsExcelParam);
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
            List<JbxtGoodsDO> activityGoodsDOList = goodsDao.listGoods(JbxtGoodsParam.builder().activityCode(activityCode).build());
            if (activityDO.getStatus().equals(ActivityStatusEnum.UNEXPORT.getCode())
                    && (CollectionUtils.isNotEmpty(activityGoodsDOList) || insertCount > 0)) {
                activityDao.updateBidActivity(JbxtActivityDO.builder().code(activityCode).status(ActivityStatusEnum.EXPORT_NO_SATRT.getCode()).build());
            }
            if (CollectionUtils.isNotEmpty(errorList)) {
                errorList.addAll(rightList);
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

    private String validateGoodsInfo(JbxtGoodsExcelParam excelParam) {
        StringBuilder error= new StringBuilder("");
        Integer timeNum = excelParam.getTimeNum();
        Integer lastChangTime = excelParam.getLastChangTime();
        Integer perDelayTime = excelParam.getPerDelayTime();
        Integer delayTimes = excelParam.getDelayTimes();
        if (timeNum > 60) {
            error.append("竞标时长不能超过60分钟！");
        }
        if (lastChangTime > timeNum * 60) {
            error.append("延时窗口期不能超过竞标时长！");
        }
        if (perDelayTime > 600) {
            error.append("单次延长不能超过600秒！");
        }
        if (delayTimes > 100) {
            error.append("延长次数不能超过100次！");
        }
        return error.toString();
    }

}

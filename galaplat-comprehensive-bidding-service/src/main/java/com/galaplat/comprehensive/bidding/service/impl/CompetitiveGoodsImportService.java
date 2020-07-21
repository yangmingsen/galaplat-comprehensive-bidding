package com.galaplat.comprehensive.bidding.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.JsonUtils;
import com.galaplat.baseplatform.permissions.feign.IFeignPermissions;
import com.galaplat.comprehensive.bidding.annotations.AlisaField;
import com.galaplat.comprehensive.bidding.dao.IJbxtActivityDao;
import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import com.galaplat.comprehensive.bidding.enums.ActivityStatusEnum;
import com.galaplat.comprehensive.bidding.param.JbxtGoodsExcelParam;
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

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
        List<JbxtGoodsParam>  saveList = Lists.newArrayList();

        String creatorName = getCreatorName(importVO.getCreator());
        String paramJson = importVO.getParamJson();
        String activityCode= null;

        if (StringUtils.isNotEmpty(paramJson)) {
            Map<String, Object> mapVO = JSONObject.parseObject(paramJson, new TypeReference<Map<String, Object>>() {
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
                StringBuffer errorMsg = new StringBuffer("");
                Tuple<String,JbxtGoodsExcelParam> paramTuple =  validateField(goodsExcelParam, goodsMap);
                errorMsg.append(paramTuple._1);
                if (StringUtils.isNotEmpty(errorMsg.toString())) {
                    goodsExcelParam = paramTuple._2;
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
                        JbxtGoodsParam goodsParam = new JbxtGoodsParam();
                        CopyUtil.copyPropertiesExceptEmpty(goodsExcelParam, goodsParam);
                        saveList.add(goodsParam);
                }
            }
            int insertCount = 0;
            if (CollectionUtils.isNotEmpty(saveList)) {
                goodsDao.delete(JbxtGoodsParam.builder().activityCode(activityCode).build());
                insertCount = goodsDao.batchInsert(saveList);
            }
            List<JbxtGoodsDO> activityGoodsDOList = goodsDao.listGoods(JbxtGoodsParam.builder().activityCode(activityCode).build());
            if (activityDO.getStatus().equals(ActivityStatusEnum.UNEXPORT.getCode())
                    && (CollectionUtils.isNotEmpty(activityGoodsDOList) || insertCount > 0)) {
                activityDao.updateBidActivity(JbxtActivityDO.builder().code(activityCode).status(ActivityStatusEnum.EXPORT_NO_SATRT.getCode()).build());
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


    /**
     * 校验导入数据,返回有异常的行
     * @param param
     * @param excelDataMap
     * @return
     */
    private <T> Tuple<String,T> validateField(T param, Map<String, Object> excelDataMap) {
        Field[] fileds = param.getClass().getDeclaredFields();
        StringBuffer errorMsg = new StringBuffer("");
        for (Field field : fileds) {
            AlisaField  annotation = field.getAnnotation(AlisaField.class);
            NotNull  notNull = field.getAnnotation(NotNull.class);
            String  fieldName  = field.getName();
            String  excelTitle;
            StringBuffer  temErrorMsg  = new StringBuffer("") ;
            if (annotation != null) {
                excelTitle = annotation.value();
                Class superclass = field.getType().getSuperclass();
                Class clazz = field.getType();
                String  fieldValue  = (String) excelDataMap.get(fieldName);
                if (superclass  == Number.class
                        || clazz == int.class
                        || clazz == short.class
                        || clazz == long.class
                        || clazz == double.class
                        || clazz == float.class ) {
                    fieldValue = StringUtils.replace(fieldValue, ".","");
                    if (!StringUtils.isNumeric(fieldValue)) {
                        temErrorMsg.append(excelTitle).append("必须为数字！ ");
                    }
                }
                if (notNull != null && StringUtils.isEmpty(fieldValue)) {
                    temErrorMsg.append(notNull.message());
                }
                    try {
                        String methodName = "set"+ capitalize(fieldName);
                        Method method = param.getClass().getDeclaredMethod(methodName,clazz);
                        if (null != method && StringUtils.isEmpty(temErrorMsg.toString())) {
                            if (superclass == Number.class) {
                                String valurStr = (String) excelDataMap.get(fieldName);
                                method.invoke(param, getNumberValue(valurStr, clazz));
                            } else {
                                method.invoke(param, excelDataMap.get(fieldName));
                            }
                        }
                    } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                        log.error("导入校验异常{}{}",e.getMessage(),e );
                    }
                errorMsg.append(temErrorMsg);
            }// if
        }

        if (StringUtils.isNotEmpty(errorMsg.toString())) {
            try {
                Method method = param.getClass().getDeclaredMethod("setErrorMsg",String.class);
                    method.invoke(param,errorMsg.toString());
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return new Tuple<>(errorMsg.toString(), param);
    }

    private String capitalize(String str) {
        int strLen;
        if (str != null && (strLen = str.length()) != 0) {
            char firstChar = str.charAt(0);
            char newChar = Character.toTitleCase(firstChar);
            if (firstChar == newChar) {
                return str;
            } else {
                char[] newChars = new char[strLen];
                newChars[0] = newChar;
                str.getChars(1, strLen, newChars, 1);
                return String.valueOf(newChars);
            }
        } else {
            return str;
        }
    }

    private Object getNumberValue(String value, Class clazz) {
        if (StringUtils.isNotEmpty(value) && clazz == BigDecimal.class) {
            return new BigDecimal(value);
        } else if (StringUtils.isNotEmpty(value) && clazz == Integer.class || clazz == int.class) {
            return new Integer(value);
        } else if (StringUtils.isNotEmpty(value) && clazz == Double.class || clazz == double.class) {
            return new Double(value);
        } else if (StringUtils.isNotEmpty(value) && clazz == Long.class || clazz == long.class) {
            return new Long(value);
        } else if (StringUtils.isNotEmpty(value) && clazz == Short.class || clazz == short.class) {
            return new Short(value);
        }
        return null;
    }
}

package com.galaplat.comprehensive.bidding.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.common.utils.JsonUtils;
import com.galaplat.base.core.common.utils.RegexUtils;
import com.galaplat.baseplatform.permissions.feign.IFeignPermissions;
import com.galaplat.comprehensive.bidding.dao.ActivityDao;
import com.galaplat.comprehensive.bidding.dao.UserDao;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.galaplat.comprehensive.bidding.enums.ActivityStatusEnum;
import com.galaplat.comprehensive.bidding.enums.CodeNameEnum;
import com.galaplat.comprehensive.bidding.param.JbxtSupplierExcelParam;
import com.galaplat.comprehensive.bidding.service.ICompetitiveListManageService;
import com.galaplat.comprehensive.bidding.utils.IdWorker;
import com.galaplat.comprehensive.bidding.utils.ImportExcelValidateMapUtil;
import com.galaplat.comprehensive.bidding.utils.Tuple;
import com.galaplat.platformdocking.base.core.utils.CopyUtil;
import com.google.common.collect.Maps;
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

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @
 * @Description: 竞标活动竞品导入
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/9 20:15
 */
@Service("competitiveSupplierImportService")
public class CompetitiveSupplierImportService implements IImportSubMethodWithParamService<JbxtSupplierExcelParam> {

    private static final Logger log = LoggerFactory.getLogger(CompetitiveSupplierImportService.class);

    @Autowired
    private IdWorker worker;

    @Autowired
    private IFeignPermissions feignPermissions;

    @Autowired
    private UserDao userDao;

    @Autowired
    private ActivityDao activityDao;

    @Autowired
    private ICompetitiveListManageService manageService;


    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
    public List<JbxtSupplierExcelParam> insertExcelDate(List<Map<String, Object>> list, ImportVO importVO) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        List<JbxtSupplierExcelParam> errorList = Lists.newArrayList();
        List<JbxtUserParam> saveList = Lists.newArrayList();

        String creatorName = getCreatorName(importVO.getCreator());
        String paramJson = importVO.getParamJson();
        String activityCode = null;

        if (StringUtils.isNotEmpty(paramJson)) {
            Map<String, Object> mapVO = JSON.parseObject(paramJson, new TypeReference<Map<String, Object>>() {
            });
            activityCode = (String) mapVO.get("bidActivityCode");
        }
        ActivityDO activityDO = activityDao.getJbxtActivity(JbxtActivityParam.builder().code(activityCode).build());
        // 如果竞标活动的状态是结束时，不允许导入
        if (activityDO.getStatus().equals(ActivityStatusEnum.FINISH.getCode())) {
            return Collections.emptyList();
        }

        //超过20个导入失败
        if ((CollectionUtils.isNotEmpty(list) && list.size() > 20) || CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }

        if (null != activityDO && CollectionUtils.isNotEmpty(list)) {
            for (Map<String, Object> userMap : list) {
                JbxtSupplierExcelParam supplierExcelParam = new JbxtSupplierExcelParam();
                StringBuilder errorMsg = new StringBuilder("");

                Tuple<String, JbxtSupplierExcelParam> paramTuple = ImportExcelValidateMapUtil.validateField(supplierExcelParam, userMap);
                errorMsg.append(paramTuple._1);
                supplierExcelParam = paramTuple._2;
//                errorMsg.append(validateExcelParam(supplierExcelParam));

                if (StringUtils.isNotEmpty(errorMsg.toString())) {
                    supplierExcelParam.setErrorMsg(errorMsg.toString());
                    errorList.add(supplierExcelParam);
                } else {
                    try {
                        supplierExcelParam = JsonUtils.toObject(JsonUtils.toJson(userMap), JbxtSupplierExcelParam.class);
                    } catch (Exception e) {
                        log.error(" 竞品导入格式化异常【{}】,【{}】", e.getMessage(), e);
                    }
                    supplierExcelParam.setActivityCode(activityCode);
                    supplierExcelParam.setCreatedTime(new Date());
                    supplierExcelParam.setUpdatedTime(new Date());
                    supplierExcelParam.setCreator(creatorName);
                    JbxtUserParam userParam = new JbxtUserParam();
                    CopyUtil.copyPropertiesExceptEmpty(supplierExcelParam, userParam);
                    saveList.add(userParam);
                }
            }

            if (CollectionUtils.isNotEmpty(saveList)) {
                List<UserDO> oldSupplierList = userDao.listJbxtUser(JbxtUserParam.builder().activityCode(activityCode).build());
                List<JbxtUserParam> addSuppliers = getAddSupplierList(oldSupplierList, saveList, creatorName, activityCode);
                List<JbxtUserParam> updateSuppliers = getUpdateSupplierList(oldSupplierList, saveList, creatorName, activityCode);
                List<String> deleteCodes = getDeleteSupplierList(oldSupplierList, saveList);

                if (!ActivityStatusEnum.BIDING.getCode().equals(activityDO.getStatus())
                        && !ActivityStatusEnum.FINISH.getCode().equals(activityDO.getStatus())) {
                    addSuppliers.addAll(updateSuppliers);
                    if (CollectionUtils.isNotEmpty(addSuppliers)) {
                        userDao.btachInsertAndUpdate(addSuppliers);
                    }
                    if (CollectionUtils.isNotEmpty(deleteCodes)) {
                        userDao.batchDeleteUser(deleteCodes, activityCode);
                    }

                } else if (ActivityStatusEnum.BIDING.getCode().equals(activityDO.getStatus())) {
                    if (CollectionUtils.isNotEmpty(addSuppliers)) {
                        userDao.btachInsertAndUpdate(addSuppliers);
                    }
                }
            }
            if (manageService.checkActivityInfoComplete(activityCode)) {
                activityDao.updateBidActivity(ActivityDO.builder().code(activityCode).status(ActivityStatusEnum.EXPORT_NO_SATRT.getCode()).build());
            }
        }// if
        return errorList;
    }

    @Override
    public Object getExcelObject() {
        return new JbxtSupplierExcelParam();
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
                throw new BaseException("创建者信息获取异常！", "创建者信息获取异常！");
            }
            if (userByCode.get(result).get("name") != null) {
                creatorName = userByCode.get(result).get("name").asText();
            } else {
                creatorName = creator;
            }

        } catch (BaseException e) {
            log.error(e.getMessage(), e);
        }
        return creatorName;
    }

    /***
     * 校验手机号
     * @param excelParam
     * @return
     */
    private String validateExcelParam(JbxtSupplierExcelParam excelParam) {
        StringBuilder error = new StringBuilder("");
        String phone = excelParam.getPhone();
        String emailAddress = excelParam.getEmailAddress();
        if (RegexUtils.isMobile(phone)) {
            error.append("手机号格式格式错误！");
        }
        if (RegexUtils.isEmail(emailAddress)) {
            error.append("邮箱地址格式错误！");
        }
        return error.toString();
    }

    /**
     * 找出新增的供应商
     *
     * @param oldSupplierList
     * @param excelList
     * @param userName
     * @param bidActivityCode
     * @return
     */
    private List<JbxtUserParam> getAddSupplierList(List<UserDO> oldSupplierList, List<JbxtUserParam> excelList, String userName, String bidActivityCode) {
        if ( CollectionUtils.isEmpty(excelList)) {
            return Collections.emptyList();
        }

        if (CollectionUtils.isEmpty(oldSupplierList)) {
            excelList.stream().forEach(excelSupplier->{
                excelSupplier.setCode(worker.nextId());
                excelSupplier.setCreator(userName);
                excelSupplier.setCreatedTime(new Date());
                excelSupplier.setUpdator(userName);
                excelSupplier.setUpdatedTime(new Date());
                excelSupplier.setUsername(manageService.getUserName());
                excelSupplier.setPassword(manageService.getPassword());
                excelSupplier.setAdmin("0");// 表示普通成员
                excelSupplier.setActivityCode(bidActivityCode);
                excelSupplier.setCodeName(getCodeName(bidActivityCode));
                excelSupplier.setLoginStatus(0);
                excelSupplier.setSendMail(0);
                excelSupplier.setSendSms(0);
            });
            return excelList;
        }
        Map<String, UserDO> oldSupplierMap = Maps.uniqueIndex(oldSupplierList, o -> o.getSupplierName());
        List<JbxtUserParam> addSupplierList = Lists.newArrayList();
        excelList.stream().forEach(excelSupplier -> {
            if (!oldSupplierMap.containsKey(excelSupplier.getSupplierName())) {
                excelSupplier.setCode(worker.nextId());
                excelSupplier.setCreator(userName);
                excelSupplier.setCreatedTime(new Date());
                excelSupplier.setUpdator(userName);
                excelSupplier.setUpdatedTime(new Date());
                excelSupplier.setUsername(manageService.getUserName());
                excelSupplier.setPassword(manageService.getPassword());
                excelSupplier.setAdmin("0");// 表示普通成员
                excelSupplier.setActivityCode(bidActivityCode);
                excelSupplier.setCodeName(getCodeName(bidActivityCode));
                excelSupplier.setLoginStatus(0);
                excelSupplier.setSendMail(0);
                excelSupplier.setSendSms(0);
                addSupplierList.add(excelSupplier);
            }
        });
        return addSupplierList;
    }

    /**
     * 获取更新的供应商信息
     *
     * @param oldSupplierList
     * @param excelList
     * @return
     */
    private List<JbxtUserParam> getUpdateSupplierList(List<UserDO> oldSupplierList, List<JbxtUserParam> excelList, String userName, String bidActivityCode) {
        if (CollectionUtils.isEmpty(oldSupplierList) || CollectionUtils.isEmpty(excelList)) {
            return Collections.emptyList();
        }
        Map<String, UserDO> oldSupplierMap = Maps.uniqueIndex(oldSupplierList, o -> o.getSupplierName());
        List<JbxtUserParam> updateSupplierList = Lists.newArrayList();
        excelList.stream().forEach(excelSupplier -> {
            if (oldSupplierMap.containsKey(excelSupplier.getSupplierName())) {
                UserDO userDO = oldSupplierMap.get(excelSupplier.getSupplierName());
                if (userDO.getSupplierName().equals(excelSupplier.getSupplierName()) && (
                        !userDO.getPhone().equals(excelSupplier.getPhone())
                                || !userDO.getEmailAddress().equals(excelSupplier.getEmailAddress())
                                || !userDO.getContactPerson().equals(excelSupplier.getContactPerson()))) {
                    JbxtUserParam userParam = new JbxtUserParam();
                    CopyUtil.copyPropertiesExceptEmpty(userDO, userParam);
                    userParam.setPhone(excelSupplier.getPhone());
                    userParam.setEmailAddress(excelSupplier.getEmailAddress());
                    userParam.setContactPerson(excelSupplier.getContactPerson());
                    userParam.setSendSms(0);
                    userParam.setSendMail(0);
                    updateSupplierList.add(userParam);
                }
            }
        });
        return updateSupplierList;
    }

    /**
     * 获取删除的供应商信息
     *
     * @param oldSupplierList
     * @param excelList
     * @return
     */
    private List<String> getDeleteSupplierList(List<UserDO> oldSupplierList, List<JbxtUserParam> excelList) {
        if (CollectionUtils.isEmpty(oldSupplierList) || CollectionUtils.isEmpty(excelList)) {
            return Collections.emptyList();
        }
        Map<String, JbxtUserParam> excelSupplierMap = Maps.uniqueIndex(excelList, o -> o.getSupplierName());
        List<String> deleteCodes = Lists.newArrayList();
        oldSupplierList.stream().forEach(oldSupplier -> {
            if (!excelSupplierMap.containsKey(oldSupplier.getSupplierName())) {
                deleteCodes.add(oldSupplier.getCode());
            }
        });
        return deleteCodes;
    }

    /**
     * 获取代号
     *
     * @param bidActivityCode
     * @return
     */
    private String getCodeName(String bidActivityCode) {

        String codeName = null;
        for (int i = 1; i <= 20; i++) {
            try {
                codeName = CodeNameEnum.findByCode(i);
            } catch (BaseException e) {
                log.error("There is an error of getting CodeName【{}】,【{}】", e.getMessage(), e);
                e.printStackTrace();
            }
            List<UserDO> userDOList = userDao.getUser(JbxtUserParam.builder().activityCode(bidActivityCode).codeName(codeName).build());
            if (CollectionUtils.isEmpty(userDOList)) {
                break;
            }
        }
        return codeName;
    }
}



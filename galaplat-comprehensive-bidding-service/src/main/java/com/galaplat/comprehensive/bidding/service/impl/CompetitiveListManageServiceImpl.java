package com.galaplat.comprehensive.bidding.service.impl;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.IJbxtActivityDao;
import com.galaplat.comprehensive.bidding.dao.IJbxtUserDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.params.CompetitiveListParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import com.galaplat.comprehensive.bidding.dao.params.SupplierAccountParam;
import com.galaplat.comprehensive.bidding.dao.params.validate.InsertParam;
import com.galaplat.comprehensive.bidding.enums.ActivityStatusEnum;
import com.galaplat.comprehensive.bidding.enums.CodeNameEnum;
import com.galaplat.comprehensive.bidding.querys.CompetitiveListQuery;
import com.galaplat.comprehensive.bidding.service.ICompetitiveListManageService;
import com.galaplat.comprehensive.bidding.utils.BeanValidateUtils;
import com.galaplat.comprehensive.bidding.utils.IdWorker;
import com.galaplat.comprehensive.bidding.utils.SpellingUtil;
import com.galaplat.comprehensive.bidding.vos.SupplierAccountVO;
import com.galaplat.platformdocking.base.core.utils.CopyUtil;
import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

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
    private IdWorker worker;
    /* 新增或修改操作类型*/
    private static final String OPRATETYPE_ADD = "add";

    @Override
    public PageInfo listCompetitiveListPage(CompetitiveListQuery query) throws BaseException {
        CompetitiveListParam param = new CompetitiveListParam();
        query.getBidActivityCodeList(query.getBidActivityCode());
        CopyUtil.copyPropertiesExceptEmpty(query,param);

        if (StringUtils.isAllEmpty(param.getEndTime(), param.getEndTime(), param.getActivityStatus(), param.getBidActivityCode())) {
            param.setStartTime(LocalDate.now().toString());
            param.setEndTime(LocalDate.now().toString());
        } else {
            String startTime = param.getStartTime();
            String endTime = param.getEndTime();
            if (StringUtils.isNotEmpty(startTime)) {
                startTime = startTime.substring(0,4) + "-" + startTime.substring(4,6) + "-01";
            }
            if (StringUtils.isNotEmpty(endTime)) {
                endTime = endTime.substring(0,4) + "-" + endTime.substring(4,6) + "-01";
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
        List<SupplierAccountParam>  supplierAccountParamList = activityParam.getSupplierAccountParams();
        Map<String, List<SupplierAccountParam>>  supplierAccountParamMap =  supplierAccountParamList.stream().collect(Collectors.groupingBy(o -> o.getCodeName()));
        for (Map.Entry<String,List<SupplierAccountParam>> m : supplierAccountParamMap.entrySet()) {
            List<SupplierAccountParam> paramList = m.getValue();
            if (paramList.size() > 1) {
                throw new BaseException("代号重复,请重新填写!","代号重复，请重新填写!");
            }
        }
        // 校验为空
        supplierAccountParamList.forEach(e->{
            try {
                BeanValidateUtils.validateErrorThenThrowException(e, InsertParam.class);
            } catch (BaseException baseException) {
                log.error("操作异常{},{}",baseException.getMessage(),baseException);
            }
        });
        if (StringUtils.equals(type,OPRATETYPE_ADD)&& StringUtils.isEmpty(bidActivityCode)) {
            activityCode = worker.nextId();
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
        return  activityCode;
    }

    @Override
    public Set<String> listReplaceCode(Integer num) throws BaseException {
        num = null == num ? 20 : num;
        if (num > 20) {
            throw  new BaseException("获取代号个数不能超过20个","获取代号个数不能超过20个");
        }
        Set<String> numSet = new HashSet<>();
        for (int i = 1; i <= num ; i++) {
            numSet.add(CodeNameEnum.findByCode(i) + LocalDate.now().toString().replace("-",""));
        }
        return numSet;
    }

    @Override
    public List<SupplierAccountVO> listSupplierAccount(String bidActivityCode) throws BaseException {
        List<SupplierAccountVO> accountVOS = Lists.newArrayList();
        List<JbxtUserDO> userDOList = userDao.getUser(JbxtUserParam.builder().activityCode(bidActivityCode).build());
        userDOList.stream().forEach(e->{
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
    public String exportBidRankAndBidPrice(String bidActivityCode) throws BaseException {
        return null;
    }

    /**
     *  批量现新增或删除供应商信息
     * @param supplierAccountParamList
     * @param bidActivityCode
     * @return
     */
    private int batchInsertOrUpdate(JbxtActivityParam activityParam, List<SupplierAccountParam>  supplierAccountParamList , String bidActivityCode) {
        List<JbxtUserParam> userParamList = Lists.newArrayList();
        supplierAccountParamList.forEach(e->{
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
     *  账号格式：供应商名称拼音首字母大写 + 四位自增数字
     * @param supplierName 供应商名称
     * @return
     */
    private String getUserName(String supplierName) {
        String nameWords =  SpellingUtil.chineseToFirstChar(supplierName).toUpperCase();
        String userName = nameWords + getShortCode();
        List<JbxtUserDO> userDOList = userDao.getUser(JbxtUserParam.builder().username(userName).build());
        while (CollectionUtils.isNotEmpty(userDOList)) {
            userName =  nameWords + getShortCode();
            userDOList = userDao.getUser(JbxtUserParam.builder().username(userName).build());
        }
        return   userName;
    }

    /**
     * 获取供应商密码
     *  密码格式 ：
     *  （1）如果供应商名称的汉字字数为偶数个---供应商名称拼音首字母大写 + 日期（形如20200709） + 四位自增数字
     *  （2）如果供应商名称的汉字字数为奇数个---供应商名称拼音首字母小写 + 日期（形如20200709） + 四位自增数字
     *
     * @param supplierName
     * @return
     */
    private String getPassword(String supplierName) {
        String today = LocalDate.now().toString().replace("-","");
        String nameWords =  SpellingUtil.chineseToFirstChar(supplierName);
        if (StringUtils.isNotEmpty(nameWords) && nameWords.length() % 2 == 0) {
            nameWords = nameWords.toUpperCase();
        }
        return   nameWords + today + getShortCode();
    }

    /**
     *  获取四位自增数字
     * @return
     */
    private String getShortCode() {
        String code = worker.nextId();
        return code.substring(code.length() - 4,code.length());
    }

}

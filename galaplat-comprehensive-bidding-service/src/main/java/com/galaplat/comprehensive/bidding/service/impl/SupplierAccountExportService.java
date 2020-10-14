package com.galaplat.comprehensive.bidding.service.impl;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.comprehensive.bidding.dao.UserDao;
import com.galaplat.comprehensive.bidding.dao.dvos.SupplierAccountExportDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import org.apache.commons.lang3.StringUtils;
import org.galaplat.baseplatform.file.upload.service.IExportSubMethodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description: 供应商账户（一个竞标活动中的）信息导出
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/10 20:18
 */
@Service("supplierAccountExportService")
public class SupplierAccountExportService implements IExportSubMethodService<SupplierAccountExportDVO> {

    @Autowired
    private UserDao userDao;

    @Override
    public List<SupplierAccountExportDVO> getExportExcelDataList(HttpServletRequest request, String sysCode, String companyCode) throws BaseException {
        JbxtUserParam userParam = new JbxtUserParam();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String bidActivityCode = "bidActivityCode";
        if (parameterMap.get(bidActivityCode).length == 0 || StringUtils.isEmpty(parameterMap.get(bidActivityCode)[0])) {
            throw new BaseException("传参异常","传参异常");
        }
        String activitYCode = parameterMap.get(bidActivityCode)[0];
        if (StringUtils.isNotEmpty(activitYCode)) {
            userParam.setActivityCode(activitYCode);
            return userDao.getAccountByActivityCode(userParam);
        } else {
            return new ArrayList<SupplierAccountExportDVO>();
        }

    }

    @Override
    public String[] getExcelTitles() {
        return new  String[]{ "账号","密码", "供应商", "代号", "竞标单编码", "竞标单创建日期",
                "竞标单导出日期"};
    }

    @Override
    public String getExcelSheetName() {
        return null;
    }

    @Override
    public String[] getExcelFieldNames() {
        return new String[]{"account",
                "password", "supplierName", "codeName", "activityCode", "activityCreateDate", "activityExportDate"};
    }

}

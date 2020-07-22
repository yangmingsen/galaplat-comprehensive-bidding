package com.galaplat.comprehensive.bidding.controllers;

import com.alibaba.fastjson.JSON;
import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.baseplatform.permissions.controllers.BaseController;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import com.galaplat.comprehensive.bidding.dao.params.SupplierAccountParam;
import com.galaplat.comprehensive.bidding.querys.CompetitiveListQuery;
import com.galaplat.comprehensive.bidding.service.ICompetitiveListManageService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Description: 竞标单管理
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 09:48
 */
@RestController
@RequestMapping("/jbxt/admin/bidmamanage")
public class CompetitiveListManageController extends BaseController {

    @Autowired
    private ICompetitiveListManageService manageService;

    @Autowired
    HttpServletResponse response;

    @Autowired
    HttpServletRequest request;

    @PostMapping
    @RestfulResult
    public Object listCompetitiveListPage(CompetitiveListQuery query) throws BaseException {
        return manageService.listCompetitiveListPage(query);
    }

    @PostMapping("/operate")
    @RestfulResult
    public Object addAndUpdate(String activitySupplierAccount, String type, String bidActivityCode) throws Exception {
        List<SupplierAccountParam> supplierAccountParamList =   JSON.parseArray(activitySupplierAccount, SupplierAccountParam.class);
        JbxtActivityParam activityParam = new JbxtActivityParam();
        activityParam.setCreator(getUser().getName());
        activityParam.setCompanyCode(getCompanyCode());
        activityParam.setSysCode(getSysCode());
        activityParam.setSupplierAccountParams(supplierAccountParamList);
        if (CollectionUtils.isEmpty(supplierAccountParamList)) {
            throw new BaseException("供应商信息和代号信息不能为空！","供应商信息和代号信息不能为空！");
        }
        return  manageService.addAndUpdate(activityParam, type, bidActivityCode);
    }

    @GetMapping("/replacecode")
    @RestfulResult
    public Object listReplaceCode(@RequestParam("num") Integer num) throws BaseException {
        return manageService.listReplaceCode(num);
    }

    @GetMapping("/supplierquery")
    @RestfulResult
    public Object listSupplierAccount(@RequestParam("bidActivityCode") String bidActivityCode) throws BaseException {
        return manageService.listSupplierAccount(bidActivityCode);
    }

    @PostMapping("/export")
    @RestfulResult
    public Object exportBidRankAndBidPrice(String bidActivityCode) throws BaseException {
        return manageService.exportBidRankAndBidPrice(bidActivityCode, response, request);
    }

}

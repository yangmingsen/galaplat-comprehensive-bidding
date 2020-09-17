package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.baseplatform.permissions.controllers.BaseController;
import com.galaplat.comprehensive.bidding.dao.params.BidActivityInfoParam;
import com.galaplat.comprehensive.bidding.querys.CompetitiveListQuery;
import com.galaplat.comprehensive.bidding.service.ICompetitiveListManageService;
import com.galaplat.comprehensive.bidding.utils.Tuple3;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

/**
 * @Description: 竞标单管理
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 09:48
 */
@RestController
@RequestMapping("/jbxt/admin/bidmamanage")
@Slf4j
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

    @PostMapping("/export")
    @RestfulResult
    public Object exportBidRankAndBidPrice(String bidActivityCode) throws BaseException {
        return manageService.exportBidRankAndBidPrice(bidActivityCode, response, request);
    }

    @GetMapping("/getbidcode")
    @RestfulResult
    public Object getBidcode() throws Exception {
      return   manageService.getBidcode(getUser().getName());
    }

    @PostMapping("/saveBasicInfo")
    @RestfulResult
    public Object saveBidActivityBasicInfo(BidActivityInfoParam infoParam) throws Exception {
        return manageService.saveBidActivityBasicInfo(infoParam, getUser().getName());
    }

    @GetMapping("/getBidActivity")
    @RestfulResult
    public Object saveBidActivityBasicInfo(@RequestParam(value = "bidActivityCode") String bidActivityCode) throws Exception {
        return manageService.getBidActivityWithGoodsAndSupplier(bidActivityCode);
    }


    @PostMapping("/sendMsgAndMail")
    @RestfulResult
    public Object sendMsgAndMail(@RequestParam("bidActivityCode") String bidActivityCode,@RequestParam(value = "phone",required = false)String phone,
                                 @RequestParam(value = "emailAddress",required = false) String emailAddress,@RequestParam(value = "type") String type
            ,@RequestParam(value = "supplierCode",required = false) String supplierCode) throws BaseException {
        return manageService.sendMsgAndMail( bidActivityCode, phone, emailAddress, type, supplierCode);
    }

    @GetMapping("/getSupplier")
    @RestfulResult
    public Object  listSupplierInfo(@RequestParam(value = "bidActivityCode")String bidActivityCode) {
        return  manageService.listSupplierInfo(bidActivityCode);
    }

    @GetMapping("/getGoods")
    @RestfulResult
    public Object  listGoods(@RequestParam(value = "bidActivityCode")String bidActivityCode) {
        return  manageService.listGoods(bidActivityCode);
    }


    @PostMapping("/savePromiseText")
    @RestfulResult
    public Object  savePromiseText(String bidActivityCode, String promiseTitle, String promiseText) throws BaseException {
        return  manageService.savePromiseText(bidActivityCode, promiseTitle, promiseText);
    }

    @PostMapping("/uploadFile")
    @RestfulResult
    public Object  fileUpload(@RequestParam("bidActivityCode") String bidActivityCode, @RequestPart("rarFile") MultipartFile rarFile) {
        return  manageService.fileUpload(bidActivityCode, rarFile);
    }


    @PostMapping("/downloadFile")
    @RestfulResult
    public void  fileDownload(String bidActivityCode) throws BaseException {
        MultipartFile file = manageService.getfile(bidActivityCode);
        Tuple3<String ,String ,String > tuple3 = manageService.getFileAllName(bidActivityCode);

        if (null == tuple3 || null == file || StringUtils.isBlank(tuple3._2) || StringUtils.isBlank(tuple3._3)) {
            throw  new BaseException("下载文件异常！","下载文件异常！");
        }

        OutputStream os = null;
        try {
            byte[] files = file.getBytes();
            response.setContentType("application/x-msdownload");
            response.setCharacterEncoding("utf-8");
            response.setContentLength(files.length);
            response.setHeader("Content-Disposition", "attachment;filename=" + tuple3._2 + tuple3._3);
            response.flushBuffer();
            os = response.getOutputStream();
            os.write(files);
            os.flush();
        } catch (IOException e) {
            log.error("get file error {},{}", e.getMessage(), e);
            throw new BaseException("下载文件异常！", "下载文件异常！");
        }
    }


}

package com.galaplat.comprehensive.bidding.param;
import com.galaplat.comprehensive.bidding.dao.params.validate.InsertParam;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
* 竞品表Query
* @author esr
* @date: 2020年06月17日
*/
public class JbxtGoodsExcelParam implements Serializable {

   /*唯一编码*/
   @NotNull(message =  "必填 竞品编码",  groups = InsertParam.class)
   private String code;
   /*商品名称*/
   @NotNull(message =  "必填 竞品名称",  groups = InsertParam.class)
   private String name;
    /*起始价*/
    @NotNull(message =  "必填 起拍价",  groups = InsertParam.class)
    private BigDecimal firstPrice;
    /*竞品竞标时长(分)*/
    @NotNull(message =  "必填 竞拍时长",  groups = InsertParam.class)
    private Integer timeNum;

    /*竞标数量*/
    @NotNull(message =  "必填 年采货量",  groups = InsertParam.class)
    private Integer num;

    private String errorMsg;
   /*竞标活动code*/
   private String activityCode;
   /*创建时间*/
   private Date createdTime;
   /*更新时间如果没有默认创建时间，修改数据必须更新*/
   private Date updatedTime;
   /*创建人*/
   private String creator;
    /*更新人*/
    private String updator;
   /*公司唯一编码*/
   private String companyCode;
   /*系统唯一编码*/
   private String sysCode;
   /*商品唯一ID*/
   private Integer goodsId;
   /*是否过期(0表示未过期，1表示为进行中, 2表示过期)*/
   private String status;

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getActivityCode() {
        return activityCode;
    }

    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode == null ? null : activityCode.trim();
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdatedTime() {
        return updatedTime;
    }

    public void setUpdatedTime(Date updatedTime) {
        this.updatedTime = updatedTime;
    }

    public BigDecimal getFirstPrice() {
        return firstPrice;
    }

    public void setFirstPrice(BigDecimal firstPrice) {
        this.firstPrice = firstPrice;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode == null ? null : companyCode.trim();
    }

    public String getSysCode() {
        return sysCode;
    }

    public void setSysCode(String sysCode) {
        this.sysCode = sysCode == null ? null : sysCode.trim();
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Integer getTimeNum() {
        return timeNum;
    }

    public void setTimeNum(Integer timeNum) {
        this.timeNum = timeNum;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

}
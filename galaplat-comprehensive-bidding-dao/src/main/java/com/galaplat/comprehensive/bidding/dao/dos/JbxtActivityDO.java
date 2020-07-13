package com.galaplat.comprehensive.bidding.dao.dos;
import com.galaplat.comprehensive.bidding.dao.params.SupplierAccountParam;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 活动表DO
 * @author esr
 * @date: 2020年06月17日
 */
 @Getter
 @Setter
 @Builder
 @AllArgsConstructor
 @NoArgsConstructor
public class JbxtActivityDO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /*活动名称*/
    /*唯一编码*/
    private String code;
     private String name;
    /*开始时间*/
    private Date startTime;
    /*结束时间*/
    private Date endTime;
    /*创建时间*/
    private Date createdTime;
    /*更新时间如果没有默认创建时间，修改数据必须更新*/
    private Date updatedTime;
    /*更新人*/
    private String updator;
    /*创建人*/
    private String creator;
    /*公司唯一编码*/
    private String companyCode;
    /*系统唯一编码*/
    private String sysCode;

    private Integer supplierNum;

    private Integer status;
     /*记录的状态，1-在用，0-删除*/
     private Integer recordStatus;

     /*删除时间*/
     private Date deleteTime;
     /*删除人*/
     private String deleter;

     public static long getSerialVersionUID() {
         return serialVersionUID;
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

     public Date getStartTime() {
         return startTime;
     }

     public void setStartTime(Date startTime) {
         this.startTime = startTime;
     }

     public Date getEndTime() {
         return endTime;
     }

     public void setEndTime(Date endTime) {
         this.endTime = endTime;
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

     public Date getDeleteTime() {
         return deleteTime;
     }

     public void setDeleteTime(Date deleteTime) {
         this.deleteTime = deleteTime;
     }

     public String getUpdator() {
         return updator;
     }

     public void setUpdator(String updator) {
         this.updator = updator == null ? null : updator.trim();
     }

     public String getCreator() {
         return creator;
     }

     public void setCreator(String creator) {
         this.creator = creator == null ? null : creator.trim();
     }

     public String getDeleter() {
         return deleter;
     }

     public void setDeleter(String deleter) {
         this.deleter = deleter == null ? null : deleter.trim();
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

     public Integer getSupplierNum() {
         return supplierNum;
     }

     public void setSupplierNum(Integer supplierNum) {
         this.supplierNum = supplierNum;
     }

     public Integer getStatus() {
         return status;
     }

     public void setStatus(Integer status) {
         this.status = status;
     }

     public Integer getRecordStatus() {
         return recordStatus;
     }

     public void setRecordStatus(Integer recordStatus) {
         this.recordStatus = recordStatus;
     }
}
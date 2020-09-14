package com.galaplat.comprehensive.bidding.dao.dvos;
import java.io.Serializable;
import java.util.Date;



 /**
 * 活动表DVO
 * @author esr
 * @date: 2020年06月17日
 */
public class ActivityDVO implements Serializable {
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

     /*出价方式(1-数值，2-降幅)*/
     private Integer bidingType;

     /*预计竞标日*/
     private Date predictBidDatetime;

     /*竞标描述*/
     private String bidActivityInfo;

     /*承诺函标题*/
     private String promiseTitle;

     /*附件路径*/
     private String filePath;

     /*承诺函内容*/
     private String promiseText;


     public String getCode() {
         return code;
     }

     public void setCode(String code) {
         this.code = code;
     }

     public String getName() {
         return name;
     }

     public void setName(String name) {
         this.name = name;
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

     public String getUpdator() {
         return updator;
     }

     public void setUpdator(String updator) {
         this.updator = updator;
     }

     public String getCreator() {
         return creator;
     }

     public void setCreator(String creator) {
         this.creator = creator;
     }

     public String getCompanyCode() {
         return companyCode;
     }

     public void setCompanyCode(String companyCode) {
         this.companyCode = companyCode;
     }

     public String getSysCode() {
         return sysCode;
     }

     public void setSysCode(String sysCode) {
         this.sysCode = sysCode;
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

     public Date getDeleteTime() {
         return deleteTime;
     }

     public void setDeleteTime(Date deleteTime) {
         this.deleteTime = deleteTime;
     }

     public String getDeleter() {
         return deleter;
     }

     public void setDeleter(String deleter) {
         this.deleter = deleter;
     }

     public Integer getBidingType() {
         return bidingType;
     }

     public void setBidingType(Integer bidingType) {
         this.bidingType = bidingType;
     }

     public Date getPredictBidDatetime() {
         return predictBidDatetime;
     }

     public void setPredictBidDatetime(Date predictBidDatetime) {
         this.predictBidDatetime = predictBidDatetime;
     }

     public String getBidActivityInfo() {
         return bidActivityInfo;
     }

     public void setBidActivityInfo(String bidActivityInfo) {
         this.bidActivityInfo = bidActivityInfo;
     }

     public String getPromiseTitle() {
         return promiseTitle;
     }

     public void setPromiseTitle(String promiseTitle) {
         this.promiseTitle = promiseTitle;
     }

     public String getFilePath() {
         return filePath;
     }

     public void setFilePath(String filePath) {
         this.filePath = filePath;
     }

     public String getPromiseText() {
         return promiseText;
     }

     public void setPromiseText(String promiseText) {
         this.promiseText = promiseText;
     }
 }

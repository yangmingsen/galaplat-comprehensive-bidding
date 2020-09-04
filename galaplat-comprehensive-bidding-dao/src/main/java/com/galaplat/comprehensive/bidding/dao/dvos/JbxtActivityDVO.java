package com.galaplat.comprehensive.bidding.dao.dvos;
import java.io.Serializable;
import java.util.Date;



 /**
 * 活动表DVO
 * @author esr
 * @date: 2020年06月17日
 */
public class JbxtActivityDVO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*唯一编码*/
    private String code;
    /*活动名称*/
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

     /*供应商个数*/
    private Integer supplierNum;
     /*竞标单状态*/
    private Integer status;

     @Override
     public String toString() {
         return "JbxtActivityDVO{" +
                 "code='" + code + '\'' +
                 ", name='" + name + '\'' +
                 ", startTime=" + startTime +
                 ", endTime=" + endTime +
                 ", createdTime=" + createdTime +
                 ", updatedTime=" + updatedTime +
                 ", updator='" + updator + '\'' +
                 ", creator='" + creator + '\'' +
                 ", companyCode='" + companyCode + '\'' +
                 ", sysCode='" + sysCode + '\'' +
                 ", supplierNum=" + supplierNum +
                 ", status=" + status +
                 '}';
     }

     public static long getSerialVersionUID() {
         return serialVersionUID;
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


}

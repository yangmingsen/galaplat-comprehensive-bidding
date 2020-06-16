package com.galaplat.comprehensive.bidding.vos;
import java.io.Serializable;
import java.util.Date;


 /**
 * 竞品表VO
 * @author esr
 * @date: 2020年06月16日
 */
public class JbxtGoodsVO implements Serializable {
    private static final long serialVersionUID = 1L;
    
    /*唯一编码*/
    private String code;
    /*商品名称*/
    private String name;
    /*竞标数量*/
    private Integer num;
    /*竞标活动code*/
    private String activityCode;
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
        this.activityCode = activityCode;
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
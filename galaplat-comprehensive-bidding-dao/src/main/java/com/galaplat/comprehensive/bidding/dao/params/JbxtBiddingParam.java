package com.galaplat.comprehensive.bidding.dao.params;
import com.galaplat.base.core.common.params.Param;
import java.math.BigDecimal;
import java.util.Date;



 /**
 * 竞价表Param
 * @author esr
 * @date: 2020年06月16日
 */
public class JbxtBiddingParam extends Param {
    private static final long serialVersionUID = 1L;
    
    /*唯一编码*/
    private String code;
    /*竞品code*/
    private String goodsCode;
    /*用户code*/
    private Integer userCode;
    /*竞标活动code*/
    private String activityCode;
    /*用户出价(保留2位小数)*/
    private BigDecimal bid;
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

    public String getGoodsCode() {
        return goodsCode;
    }
    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }

    public Integer getUserCode() {
        return userCode;
    }
    public void setUserCode(Integer userCode) {
        this.userCode = userCode;
    }

    public String getActivityCode() {
        return activityCode;
    }
    public void setActivityCode(String activityCode) {
        this.activityCode = activityCode;
    }

    public BigDecimal getBid() {
        return bid;
    }
    public void setBid(BigDecimal bid) {
        this.bid = bid;
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
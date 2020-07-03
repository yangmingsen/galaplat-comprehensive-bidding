package com.galaplat.comprehensive.bidding.dao.dos;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;



 /**
 * 竞品表DO
 * @author esr
 * @date: 2020年06月17日
 */
public class JbxtGoodsDO implements Serializable {
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

    private BigDecimal firstPrice;
    /*创建人*/
    private String creator;
    /*公司唯一编码*/
    private String companyCode;
    /*系统唯一编码*/
    private String sysCode;
    /*商品唯一ID*/
    private Integer goodsId;
    /*是否过期(0表示未过期，1表示为进行中, 2表示过期)*/
    private String status;

    private Integer timeNum;

     public Integer getTimeNum() {
         return timeNum;
     }

     public void setTimeNum(Integer timeNum) {
         this.timeNum = timeNum;
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

    public Integer getGoodsId() {
        return goodsId;
    }
    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }


}
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

    /*竞标时长*/
    private Integer timeNum;

    /*需要延长竞标时长的最后的时间点，比如最后10秒，排名发生变化需要延长*/
    private Integer lastChangTime;

    /*每次延长的时长*/
    private Integer perDelayTime;

    /*延长次数*/
    private Integer delayTimes;

    /*已经延长的次数*/
    private Integer addDelayTimes;

    /*保留价*/
    private BigDecimal retainPrice;

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

    public String getUpdator() {
        return updator;
    }

    public void setUpdator(String updator) {
        this.updator = updator == null ? null : updator.trim();
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

    public Integer getLastChangTime() {
        return lastChangTime;
    }

    public void setLastChangTime(Integer lastChangTime) {
        this.lastChangTime = lastChangTime;
    }

    public Integer getPerDelayTime() {
        return perDelayTime;
    }

    public void setPerDelayTime(Integer perDelayTime) {
        this.perDelayTime = perDelayTime;
    }

    public Integer getDelayTimes() {
        return delayTimes;
    }

    public void setDelayTimes(Integer delayTimes) {
        this.delayTimes = delayTimes;
    }

    /**
     * 获取已经延迟的次数
     * @return
     */
    public Integer getAddDelayTimes() {
        return addDelayTimes;
    }

    public void setAddDelayTimes(Integer addDelayTimes) {
        this.addDelayTimes = addDelayTimes;
    }

    public BigDecimal getRetainPrice() {
        return retainPrice;
    }

    public void setRetainPrice(BigDecimal retainPrice) {
        this.retainPrice = retainPrice;
    }

}

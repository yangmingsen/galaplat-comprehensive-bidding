package com.galaplat.comprehensive.bidding.dao.dvos;

import java.math.BigDecimal;

public class BidDVO {
    private String code;
    /*竞品id*/
    private Integer goodsId;
    /*用户code*/
    private String userCode;
    /*竞标活动code*/
    private String activityCode;
    /*用户出价(保留2位小数)*/
    private BigDecimal bid;

    private String createdTime;

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "code='" + code + '\'' +
                ", goodsId=" + goodsId +
                ", userCode='" + userCode + '\'' +
                ", activityCode='" + activityCode + '\'' +
                ", bid=" + bid +
                ", createdTime='" + createdTime + '\'' +
                '}';
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getUserCode() {
        return userCode;
    }

    public void setUserCode(String userCode) {
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
}

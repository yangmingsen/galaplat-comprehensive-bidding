package com.galaplat.comprehensive.bidding.netty.pojo.res;

import java.math.BigDecimal;

public class Res300t2 {
    private BigDecimal bid;
    private String bidTime;
    private BigDecimal bidPercent;
    private Integer isDelay;

    public BigDecimal getBidPercent() {
        return bidPercent;
    }

    public void setBidPercent(BigDecimal bidPercent) {
        this.bidPercent = bidPercent;
    }

    public Integer getIsDelay() {
        return isDelay;
    }

    public void setIsDelay(Integer isDelay) {
        this.isDelay = isDelay;
    }

    public BigDecimal getBid() {
        return bid;
    }

    public void setBid(BigDecimal bid) {
        this.bid = bid;
    }

    public String getBidTime() {
        return bidTime;
    }

    public void setBidTime(String bidTime) {
        this.bidTime = bidTime;
    }
}

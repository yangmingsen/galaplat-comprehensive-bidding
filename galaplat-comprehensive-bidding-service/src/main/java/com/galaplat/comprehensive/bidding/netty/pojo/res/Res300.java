package com.galaplat.comprehensive.bidding.netty.pojo.res;

import java.math.BigDecimal;
import java.util.List;

public class Res300 {
    private BigDecimal minPrice;
    private Integer goodsId;
    private List<Res300t1> list;
    private Boolean isDelay;
    private Integer delayedTime; //已经延迟次数
    private Integer delayedLength; //每次延时时长 秒

    public Boolean getDelay() {
        return isDelay;
    }

    public void setDelay(Boolean delay) {
        isDelay = delay;
    }

    public Integer getDelayedTime() {
        return delayedTime;
    }

    public void setDelayedTime(Integer delayedTime) {
        this.delayedTime = delayedTime;
    }

    public Integer getDelayedLength() {
        return delayedLength;
    }

    public void setDelayedLength(Integer delayedLength) {
        this.delayedLength = delayedLength;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public List<Res300t1> getList() {
        return list;
    }

    public void setList(List<Res300t1> list) {
        this.list = list;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }
}

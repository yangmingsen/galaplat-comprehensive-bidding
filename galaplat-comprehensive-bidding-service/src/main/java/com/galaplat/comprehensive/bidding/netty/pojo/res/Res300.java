package com.galaplat.comprehensive.bidding.netty.pojo.res;

import java.math.BigDecimal;
import java.util.List;

public class Res300 {
    private BigDecimal minPrice;
    private Integer goodsId;
    private List<Res300t1> list;

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
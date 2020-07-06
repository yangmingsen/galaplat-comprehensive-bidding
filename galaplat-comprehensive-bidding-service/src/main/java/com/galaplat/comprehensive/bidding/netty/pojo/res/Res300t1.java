package com.galaplat.comprehensive.bidding.netty.pojo.res;

import java.math.BigDecimal;
import java.util.List;

public class Res300t1 {
    private String supplierName;
    private String CodeName;
    private String supplierCode;
    private BigDecimal minBid;
    private Integer rank;
    private List<Res300t2> bids;

    public BigDecimal getMinBid() {
        return minBid;
    }

    public void setMinBid(BigDecimal minBid) {
        this.minBid = minBid;
    }

    public List<Res300t2> getBids() {
        return bids;
    }

    public void setBids(List<Res300t2> bids) {
        this.bids = bids;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getCodeName() {
        return CodeName;
    }

    public void setCodeName(String codeName) {
        CodeName = codeName;
    }

    public String getSupplierCode() {
        return supplierCode;
    }

    public void setSupplierCode(String supplierCode) {
        this.supplierCode = supplierCode;
    }
}

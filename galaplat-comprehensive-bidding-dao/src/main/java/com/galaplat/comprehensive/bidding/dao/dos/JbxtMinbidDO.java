package com.galaplat.comprehensive.bidding.dao.dos;

import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JbxtMinbidDO {
    private String code;

    private Integer goodsId;

    private String userCode;

    private String activityCode;

    private BigDecimal bid;

    private Date createdTime;

    private Date updatedTime;

    private String updator;

    private String creator;

    private String companyCode;

    private String sysCode;

    private String bidTime;

}
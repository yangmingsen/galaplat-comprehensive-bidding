package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

}

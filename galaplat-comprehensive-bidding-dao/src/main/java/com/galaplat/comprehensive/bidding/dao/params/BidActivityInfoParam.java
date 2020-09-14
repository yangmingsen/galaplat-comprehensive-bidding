package com.galaplat.comprehensive.bidding.dao.params;

import lombok.*;

import java.util.Date;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 16:36
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidActivityInfoParam {

    /*竞标单编号*/
    private String bidActivityCode;
    /*出价方式(1-数值，2-降幅)*/
    private Integer bidingType;
    /*预计竞日(格式：yyyyMMddHHmmss)*/
    private String predictBidDateTime;
    /*竞标描述*/
    private String bidActivityInfo;
    /*保存类型（ add-新增，update-编辑）*/
    private String type;

}

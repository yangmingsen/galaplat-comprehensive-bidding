package com.galaplat.comprehensive.bidding.vos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 16:15
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidCodeVO {

    /*竞标活动编号*/
    private String bidActivityCode;

    /*创建人*/
    private String creator;

    /*创建时间*/
    private String createdDate;

}

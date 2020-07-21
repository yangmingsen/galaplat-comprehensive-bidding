package com.galaplat.comprehensive.bidding.dao.params;

import com.galaplat.base.core.common.params.Param;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/21 16:29
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JbxtMinbidParam extends Param {

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

    private List<String> userCodeList;

}

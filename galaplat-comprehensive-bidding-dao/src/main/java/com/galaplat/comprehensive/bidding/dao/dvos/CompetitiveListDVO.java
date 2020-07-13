package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @Description: 竞标单
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 15:05
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitiveListDVO {

    private static final Logger log = LoggerFactory.getLogger(CompetitiveListDVO.class);

    /*竞标单状态*/
    private String activityStatus;

    /*竞标单编号*/
    private String activityCode;

    /*创建人*/
    private String creator;

    /*创建时间*/
    private String createdDate;

    /*竞标产品编码*/
    private String bidProductCode;

    /*参与供应商人数*/
    private Integer joinSupplierNum;


}

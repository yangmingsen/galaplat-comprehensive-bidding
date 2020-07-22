package com.galaplat.comprehensive.bidding.dao.params;

import lombok.*;

import java.util.List;

/**
 * @Description: 竞标单查询Query
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 13:57
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompetitiveListParam extends BaseParam {

    /* 开始时间(创建时间) */
    private String startTime;

    /* 开始时间(创建时间) */
    private String endTime;

    /*  活动状态 */
    private String activityStatus;

    /* 需求单编号 （竞标单编码） */
    private String bidActivityCode;

    /* 需求单编号 （竞标单编码） */
    private List<String> bidActivityCodeList;

    /* 活动状态  */
    private List<String> activityStatusList;

}

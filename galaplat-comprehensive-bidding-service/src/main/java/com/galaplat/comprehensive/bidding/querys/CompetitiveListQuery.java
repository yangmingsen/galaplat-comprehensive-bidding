package com.galaplat.comprehensive.bidding.querys;

import com.google.common.base.Splitter;
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
public class CompetitiveListQuery {

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


    public List<String> getBidActivityCodeList(String bidActivityCode) {
        bidActivityCodeList = strings2List(bidActivityCode);
        return bidActivityCodeList;
    }

    /**
     * 将 字符串 转换成为list ,规则： 按照 逗号， 分割
     *
     * @param str
     * @return
     */
    public static List<String> strings2List(String str) {
        str = org.apache.commons.lang3.StringUtils.trimToEmpty(str).replace("，", ",");
        return Splitter.on(",").omitEmptyStrings().splitToList(str);
    }
}

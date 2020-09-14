package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/14 18:12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BidActivityDVO {

    private static final Logger log = LoggerFactory.getLogger(BidActivityDVO.class);

    /*唯一编码*/
    private String bidActivityCode;

    /*创建时间*/
    private String createdDate;

    /*创建人*/
    private String creator;

    /*出价方式(1-数值，2-降幅)*/
    private Integer bidingType;

    /*预计竞标日*/
    private String predictBidDatetime;

    /*竞标描述*/
    private String bidActivityInfo;

    /*承诺函标题*/
    private String promiseTitle;

    /*承诺函内容*/
    private String promiseText;

    /*附件路径*/
    private String filePath;

    /*竞标活动状态*/
    private Integer activityStatus;

    /*竞品信息*/
    private List<BidGoodsDVO> goodsList;

    /*供应商信息*/
    private List<BidSupplierDVO> supplierList;

}

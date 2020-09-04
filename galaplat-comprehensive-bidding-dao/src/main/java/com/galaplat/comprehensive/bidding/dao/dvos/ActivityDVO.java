package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 18:36
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ActivityDVO {

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

    /*附件路径*/
    private String filePath;

    /*承诺函内容*/
    private String promiseText;

    /*竞品信息*/
    private List<GoodsDVO> goodsList;

    /*供应商信息*/
    private List<SupplierDVO> supplierList;

}

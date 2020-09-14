package com.galaplat.comprehensive.bidding.dao.dos;
import com.galaplat.comprehensive.bidding.dao.params.SupplierAccountParam;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;


/**
 * 活动表DO
 * @author esr
 * @date: 2020年06月17日
 */
 @Getter
 @Setter
 @Builder
 @AllArgsConstructor
 @NoArgsConstructor
public class ActivityDO implements Serializable {
    private static final long serialVersionUID = 1L;

    /*活动名称*/
    /*唯一编码*/
    private String code;
     private String name;
    /*开始时间*/
    private Date startTime;
    /*结束时间*/
    private Date endTime;
    /*创建时间*/
    private Date createdTime;
    /*更新时间如果没有默认创建时间，修改数据必须更新*/
    private Date updatedTime;
    /*更新人*/
    private String updator;
    /*创建人*/
    private String creator;
    /*公司唯一编码*/
    private String companyCode;
    /*系统唯一编码*/
    private String sysCode;

    private Integer supplierNum;

    private Integer status;
     /*记录的状态，1-在用，0-删除*/
     private Integer recordStatus;

     /*删除时间*/
     private Date deleteTime;
     /*删除人*/
     private String deleter;

    /*出价方式(1-数值，2-降幅)*/
    private Integer bidingType;

    /*预计竞标日*/
    private Date predictBidDatetime;

    /*竞标描述*/
    private String bidActivityInfo;

    /*承诺函标题*/
    private String promiseTitle;

    /*附件路径*/
    private String filePath;

    /*承诺函内容*/
    private String promiseText;

     public static long getSerialVersionUID() {
         return serialVersionUID;
     }

}

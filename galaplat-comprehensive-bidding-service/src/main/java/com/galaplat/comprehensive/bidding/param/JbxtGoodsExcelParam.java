package com.galaplat.comprehensive.bidding.param;
import com.galaplat.comprehensive.bidding.annotations.AlisaField;
import com.galaplat.comprehensive.bidding.dao.params.validate.InsertParam;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
* 竞品表Query
* @author esr
* @date: 2020年06月17日
*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JbxtGoodsExcelParam implements Serializable {

   /*唯一编码*/
   @NotNull(message =  "必填 竞品编码",  groups = InsertParam.class)
   @AlisaField("PN")
   private String code;
   /*商品名称*/
   @NotNull(message =  "必填 竞品名称",  groups = InsertParam.class)
   @AlisaField("名称")
   private String name;
    /*起始价*/
    @NotNull(message =  "必填 起拍价",  groups = InsertParam.class)
    @AlisaField("起拍价")
    private BigDecimal firstPrice;

    /*竞品竞标时长(分)*/
    @NotNull(message =  "必填 竞拍时长",  groups = InsertParam.class)
    @AlisaField("竞标时长")
    private Integer timeNum;

    /*竞标数量*/
    @NotNull(message =  "必填 年采货量",  groups = InsertParam.class)
    @AlisaField("年采货量")
    private Integer num;

    private String errorMsg;
   /*竞标活动code*/
   private String activityCode;
   /*创建时间*/
   private Date createdTime;
   /*更新时间如果没有默认创建时间，修改数据必须更新*/
   private Date updatedTime;
   /*创建人*/
   private String creator;
    /*更新人*/
    private String updator;
   /*公司唯一编码*/
   private String companyCode;
   /*系统唯一编码*/
   private String sysCode;
   /*商品唯一ID*/
   private Integer goodsId;
   /*是否过期(0表示未过期，1表示为进行中, 2表示过期)*/
   private String status;
}
package com.galaplat.comprehensive.bidding.param;
import com.galaplat.comprehensive.bidding.dao.params.validate.InsertParam;
import com.galaplat.comprehensive.bidding.utils.ImportExcelValidateMapUtil;
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

   /*序号*/
   @ImportExcelValidateMapUtil.AlisaField("序号")
   private String serialNumber;

   /*唯一编码*/
   @NotNull(message =  "必填 PN",  groups = InsertParam.class)
   @ImportExcelValidateMapUtil.AlisaField("PN")
   private String code;

   /*商品名称*/
   @NotNull(message =  "必填 名称",  groups = InsertParam.class)
   @ImportExcelValidateMapUtil.AlisaField("名称")
   private String name;

    /*起始价*/
    @NotNull(message =  "必填 起拍价",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("起拍价")
    private BigDecimal firstPrice;

    /*保留价*/
    @NotNull(message =  "必填 保留价",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("保留价")
    private BigDecimal retainPrice;

    /*竞标数量*/
    @NotNull(message =  "必填 年采货量",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("年采货量")
    private Integer num;

    /*竞品竞标时长(分)*/
    @NotNull(message =  "必填 竞拍时长",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("竞标时长")
    private Integer timeNum;

    /*延时窗口期*/
    @NotNull(message =  "必填 延时窗口期",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("延时窗口期")
    private Integer lastChangTime;

    /*单次延时时长*/
    @NotNull(message =  "必填 单次延时",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("单次延时")
    private Integer perDelayTime;

    /*延时次数*/
    @NotNull(message =  "必填 延时次数",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("延时次数")
    private Integer delayTimes;

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

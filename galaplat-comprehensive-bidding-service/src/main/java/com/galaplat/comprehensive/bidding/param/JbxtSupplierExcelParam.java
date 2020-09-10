package com.galaplat.comprehensive.bidding.param;
import com.galaplat.comprehensive.bidding.dao.params.validate.InsertParam;
import com.galaplat.comprehensive.bidding.utils.ImportExcelValidateMapUtil;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
* 供应商表Query
* @author esr
* @date: 2020年06月17日
*/
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JbxtSupplierExcelParam implements Serializable {

   /*代号*/
   private Integer serialNumber;

   /*供应商*/
   @NotNull(message =  "必填 供应商",  groups = InsertParam.class)
   @ImportExcelValidateMapUtil.AlisaField("供应商")
   private String supplierName;

   /*联系人*/
   @NotNull(message =  "必填 联系人",  groups = InsertParam.class)
   @ImportExcelValidateMapUtil.AlisaField("联系人")
   private String contactPerson;

    /*手机*/
    @NotNull(message =  "必填 手机",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("手机")
    private String  phone;

    /*邮箱*/
    @NotNull(message =  "必填 邮箱",  groups = InsertParam.class)
    @ImportExcelValidateMapUtil.AlisaField("邮箱")
    private String  emailAddress;

    private String errorMsg;
   /*竞标活动code*/
   private String activityCode;
   /*创建时间*/
   private Date createdTime;
   /*更新时间*/
   private Date updatedTime;
   /*创建人*/
   private String creator;
    /*更新人*/
    private String updator;
   /*公司唯一编码*/
   private String companyCode;
   /*系统唯一编码*/
   private String sysCode;
}

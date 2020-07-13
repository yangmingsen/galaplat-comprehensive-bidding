package com.galaplat.comprehensive.bidding.dao.params;


import com.galaplat.comprehensive.bidding.dao.params.validate.InsertParam;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Description: 竞标单param
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 14:02
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierAccountParam extends BaseParam  {

    /* 代号 */
    @NotNull(message =  "必填 代号",  groups = InsertParam.class)
    private String codeName ;

    /* 供应商名称 */
    @NotNull(message =  "必填 供应商名称",  groups = InsertParam.class)
    private String supplierName ;

    /* 供应商账号 */
    private String supplierAccount ;

}

package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.*;

import java.io.Serializable;

/**
 * @Description: 供应商账户
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/10 20:22
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierAccountExportDVO  implements Serializable {

    private String account;

    private String password;

    private String supplierName;

    private String codeName;

    private String activityCode;

    private String activityCreateDate;

    private String activityExportDate;

}

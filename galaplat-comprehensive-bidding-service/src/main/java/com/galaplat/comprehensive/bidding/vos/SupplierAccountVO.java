package com.galaplat.comprehensive.bidding.vos;

import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Description: 供应商信息
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 14:21
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierAccountVO {

    /*  代号 */
    private String codeName ;

    /* 供应商名称 */
    private String supplierName ;

    /* 供应商账号 */
    private String supplierAccount ;
}

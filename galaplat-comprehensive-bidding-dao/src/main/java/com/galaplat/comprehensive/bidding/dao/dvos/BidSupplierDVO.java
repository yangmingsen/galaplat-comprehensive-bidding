package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 20:04
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BidSupplierDVO {
    /*代号*/
   private String codeName;
    /*供应商*/
   private String supplierName;
    /*联系人*/
   private String contactPerson;
    /*手机号*/
   private String phone;
    /*邮箱地址*/
   private String emailAddress;
    /*是否登录系统（0-从未登录，1-登录过）*/
   private Integer loginStatus;
    /* 短信发送状态（0-未发送，1-已发送，2-发送失败）*/
   private Integer sendSms;
    /* 邮箱发送状态（0-未发送，1-已发送，2-发送失败）*/
   private Integer sendMail;
}

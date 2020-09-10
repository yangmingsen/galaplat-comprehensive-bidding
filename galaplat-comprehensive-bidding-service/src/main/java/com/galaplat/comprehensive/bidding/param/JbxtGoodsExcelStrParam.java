package com.galaplat.comprehensive.bidding.param;
import lombok.*;
import java.io.Serializable;


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
public class JbxtGoodsExcelStrParam implements Serializable {

   /*序号*/
   private String serialNumber;

   /*唯一编码*/
   private String code;

   /*商品名称*/
   private String name;

    /*起始价*/
    private String firstPrice;

    /*保留价*/
    private String retainPrice;

    /*竞标数量*/
    private String num;

    /*竞品竞标时长(分)*/
    private String timeNum;

    /*延时窗口期*/
    private String lastChangTime;

    /*单次延时时长*/
    private String perDelayTime;

    /*延时次数*/
    private String delayTimes;

    private String errorMsg;

   /*公司唯一编码*/
   private String companyCode;

   /*系统唯一编码*/
   private String sysCode;
}

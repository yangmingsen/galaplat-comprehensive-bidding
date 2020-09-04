package com.galaplat.comprehensive.bidding.dao.dvos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 18:42
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoodsDVO {

    /*唯一编码*/
    private String code;
    /*商品名称*/
    private String goodsName;
    /*起拍价*/
    private BigDecimal firstPrice;

    /*保留价*/
    private BigDecimal retainPrice;

    /*年采货量*/
    private Integer yearPurchaseNum;

    /*竞标时长*/
    private Integer bidTimeNum;

    /*需要延长竞标时长的最后的时间点，比如最后10秒，排名发生变化需要延长*/
    private Integer lastChangTime;

    /*每次延长的时长*/
    private Integer perDelayTime;

    /*延长次数*/
    private Integer delayTimes;



}


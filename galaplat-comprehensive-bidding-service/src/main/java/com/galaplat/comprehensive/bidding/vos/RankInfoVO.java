package com.galaplat.comprehensive.bidding.vos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/12 18:34
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RankInfoVO {

    private BigDecimal bid;
    private Integer rank;
    private String names; //使用 ‘-’ 分隔

}

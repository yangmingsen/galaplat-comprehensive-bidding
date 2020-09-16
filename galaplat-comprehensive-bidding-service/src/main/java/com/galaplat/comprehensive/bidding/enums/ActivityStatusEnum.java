package com.galaplat.comprehensive.bidding.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 20:37
 */
@Getter
@AllArgsConstructor
public enum ActivityStatusEnum {

    UNEXPORT(1,"未导入数据"),
    IMPORT_NO_SATRT(2,"已导入数据未开始"),
    BIDING(3,"竞标中"),
    FINISH(4,"已结束");

    private Integer code;
    private String status;

}

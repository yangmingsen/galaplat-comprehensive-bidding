package com.galaplat.comprehensive.bidding.enums;

import com.galaplat.base.core.common.exception.BaseException;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: 竞标单代号昵称
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/8 20:37
 */
@Getter
@AllArgsConstructor
public enum CodeNameEnum {

    LITTEL_RED(1,"小红"),
    LITTEL_ORANGE(2,"小橙"),
    LITTEL_YELLOW(3,"小黄"),
    LITTEL_GREEN(4,"小绿"),
    LITTEL_CYAN(5,"小青"),
    LITTEL_BLUE(6,"小蓝"),
    LITTEL_PURPLE(7,"小紫"),
    MIDDEL_RED(8,"中红"),
    MIDDEL_ORANGE(9,"中橙"),
    MIDDEL_YELLOW(10,"中黄"),
    MIDDEL_GREEN(11,"中绿"),
    MIDDEL_CYAN(12,"中青"),
    MIDDEL_BLUE(13,"中蓝"),
    MIDDEL_PURPLE(14,"中紫"),
    BIG_RED(15,"大红"),
    BIG_ORANGE(16,"大橙"),
    BIG_YELLOW(17,"大黄"),
    BIG_GREEN(18,"大绿"),
    BIG_CYAN(19,"大青"),
    BIG_BLUE(20,"大蓝");

    /*序号*/
    private  int code;
    /*代号昵称*/
    private String name;

    public static String findByCode(int code) throws BaseException {

        for (CodeNameEnum value : CodeNameEnum.values()) {
            if (value.getCode() == code) {
                return value.name;
            }
        }
        throw new BaseException(code + "不存在的代号个数",code + "不存在的代号个数");
    }

}

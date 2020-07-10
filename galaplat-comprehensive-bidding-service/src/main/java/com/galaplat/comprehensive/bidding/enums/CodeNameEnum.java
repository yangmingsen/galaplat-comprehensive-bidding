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

    WYF(1,"吴亦凡"),
    YY(2,"杨洋"),
    WLH(3,"王力宏"),
    LJJ(4,"林俊杰"),
    ZJ(5,"张杰"),
    WF(6,"王菲"),
    DZQ(7,"邓紫棋"),
    YCY(8,"杨超越"),
    YM(9,"杨幂"),
    DLRB(10,"迪丽热巴"),
    ZYS(11,"张一山"),
    YZ(12,"杨紫"),
    ZLY(13,"赵丽颖"),
    LYF(14,"李易峰"),
    DL(15,"邓伦"),
    LZL(16,"林志玲"),
    FBB(17,"范冰冰"),
    LBB(18,"李冰冰"),
    ZZY(19,"章子怡"),
    MY(20,"马云");

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

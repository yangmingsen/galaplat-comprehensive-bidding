package com.galaplat.comprehensive.bidding.dao;

import com.galaplat.comprehensive.bidding.dao.params.JbxtMinbidParam;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/21 16:35
 */
public interface IJbxtMinbidDao {

    /**
     * 删除竞价
     * @param minbidParam
     * @return
     */
    int deleteBidding(JbxtMinbidParam minbidParam);
}

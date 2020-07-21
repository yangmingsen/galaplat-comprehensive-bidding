package com.galaplat.comprehensive.bidding.dao.impl;

import com.galaplat.comprehensive.bidding.dao.IJbxtMinbidDao;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtMinbidDOMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtMinbidParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/7/21 16:38
 */
@Repository
public class JbxtMinbidDaoImpl implements IJbxtMinbidDao {

    @Autowired
    private JbxtMinbidDOMapper mapper;

    @Override
    public int deleteBidding(JbxtMinbidParam minbidParam) {
        return mapper.deleteBidding(minbidParam);
    }
}

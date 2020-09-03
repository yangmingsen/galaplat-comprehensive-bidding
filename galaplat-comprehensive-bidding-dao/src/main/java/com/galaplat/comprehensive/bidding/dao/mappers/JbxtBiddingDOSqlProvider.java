package com.galaplat.comprehensive.bidding.dao.mappers;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;

public class JbxtBiddingDOSqlProvider {

    public String insertMinBidTableSelective(JbxtBiddingDO record) {
        BEGIN();
        INSERT_INTO("t_jbxt_minbid");

        if (record.getCode() != null) {
            VALUES("code", "#{code,jdbcType=VARCHAR}");
        }

        if (record.getIsdelay() != null) {
            VALUES("isdelay", "#{isdelay,jdbcType=INTEGER}");
        }

        if (record.getGoodsId() != null) {
            VALUES("goods_id", "#{goodsId,jdbcType=INTEGER}");
        }

        if (record.getUserCode() != null) {
            VALUES("user_code", "#{userCode,jdbcType=VARCHAR}");
        }

        if (record.getBidTime() != null) {
            VALUES("bid_time", "#{bidTime,jdbcType=VARCHAR}");
        }

        if (record.getActivityCode() != null) {
            VALUES("activity_code", "#{activityCode,jdbcType=VARCHAR}");
        }

        if (record.getBid() != null) {
            VALUES("bid", "#{bid,jdbcType=DECIMAL}");
        }

        if (record.getCreatedTime() != null) {
            VALUES("created_time", "#{createdTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdatedTime() != null) {
            VALUES("updated_time", "#{updatedTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdator() != null) {
            VALUES("updator", "#{updator,jdbcType=VARCHAR}");
        }

        if (record.getCreator() != null) {
            VALUES("creator", "#{creator,jdbcType=VARCHAR}");
        }

        if (record.getCompanyCode() != null) {
            VALUES("company_code", "#{companyCode,jdbcType=VARCHAR}");
        }

        if (record.getSysCode() != null) {
            VALUES("sys_code", "#{sysCode,jdbcType=VARCHAR}");
        }

        return SQL();
    }
    public String updateMinBidTableByPrimaryKeySelective(JbxtBiddingDO record) {
        BEGIN();
        UPDATE("t_jbxt_minbid");

        if (record.getIsdelay() != null) {
            SET("isdelay = #{isdelay, jdbcType=INTEGER}");
        }

        if (record.getGoodsId() != null) {
            SET("goods_id = #{goodsId,jdbcType=INTEGER}");
        }

        if (record.getUserCode() != null) {
            SET("user_code = #{userCode,jdbcType=VARCHAR}");
        }

        if (record.getActivityCode() != null) {
            SET("activity_code = #{activityCode,jdbcType=VARCHAR}");
        }

        if (record.getBidTime() != null) {
            SET("bid_time = #{bidTime,jdbcType=VARCHAR}");
        }

        if (record.getBid() != null) {
            SET("bid = #{bid,jdbcType=DECIMAL}");
        }

        if (record.getCreatedTime() != null) {
            SET("created_time = #{createdTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdatedTime() != null) {
            SET("updated_time = #{updatedTime,jdbcType=TIMESTAMP}");
        }

        if (record.getUpdator() != null) {
            SET("updator = #{updator,jdbcType=VARCHAR}");
        }

        if (record.getCreator() != null) {
            SET("creator = #{creator,jdbcType=VARCHAR}");
        }

        if (record.getCompanyCode() != null) {
            SET("company_code = #{companyCode,jdbcType=VARCHAR}");
        }

        if (record.getSysCode() != null) {
            SET("sys_code = #{sysCode,jdbcType=VARCHAR}");
        }

        WHERE("code = #{code,jdbcType=VARCHAR}");

        return SQL();
    }


    public String insertSelective(JbxtBiddingDO record) {
        BEGIN();
        INSERT_INTO("t_jbxt_bidding");
        
        if (record.getCode() != null) {
            VALUES("code", "#{code,jdbcType=VARCHAR}");
        }

        if (record.getIsdelay() != null) {
            VALUES("isdelay", "#{isdelay,jdbcType=INTEGER}");
        }


        if (record.getBidTime() != null) {
            VALUES("bid_time", "#{bidTime,jdbcType=VARCHAR}");
        }

        if (record.getGoodsId() != null) {
            VALUES("goods_id", "#{goodsId,jdbcType=INTEGER}");
        }
        
        if (record.getUserCode() != null) {
            VALUES("user_code", "#{userCode,jdbcType=VARCHAR}");
        }
        
        if (record.getActivityCode() != null) {
            VALUES("activity_code", "#{activityCode,jdbcType=VARCHAR}");
        }
        
        if (record.getBid() != null) {
            VALUES("bid", "#{bid,jdbcType=DECIMAL}");
        }
        
        if (record.getCreatedTime() != null) {
            VALUES("created_time", "#{createdTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatedTime() != null) {
            VALUES("updated_time", "#{updatedTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdator() != null) {
            VALUES("updator", "#{updator,jdbcType=VARCHAR}");
        }
        
        if (record.getCreator() != null) {
            VALUES("creator", "#{creator,jdbcType=VARCHAR}");
        }
        
        if (record.getCompanyCode() != null) {
            VALUES("company_code", "#{companyCode,jdbcType=VARCHAR}");
        }
        
        if (record.getSysCode() != null) {
            VALUES("sys_code", "#{sysCode,jdbcType=VARCHAR}");
        }
        
        return SQL();
    }

    public String updateByPrimaryKeySelective(JbxtBiddingDO record) {
        BEGIN();
        UPDATE("t_jbxt_bidding");

        if (record.getIsdelay() != null) {
            SET("isdelay = #{isdelay, jdbcType=INTEGER}");
        }

        if (record.getGoodsId() != null) {
            SET("goods_id = #{goodsId,jdbcType=INTEGER}");
        }

        if (record.getBidTime() != null) {
            SET("bid_time = #{bidTime,jdbcType=VARCHAR}");
        }

        if (record.getUserCode() != null) {
            SET("user_code = #{userCode,jdbcType=VARCHAR}");
        }
        
        if (record.getActivityCode() != null) {
            SET("activity_code = #{activityCode,jdbcType=VARCHAR}");
        }
        
        if (record.getBid() != null) {
            SET("bid = #{bid,jdbcType=DECIMAL}");
        }
        
        if (record.getCreatedTime() != null) {
            SET("created_time = #{createdTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatedTime() != null) {
            SET("updated_time = #{updatedTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdator() != null) {
            SET("updator = #{updator,jdbcType=VARCHAR}");
        }
        
        if (record.getCreator() != null) {
            SET("creator = #{creator,jdbcType=VARCHAR}");
        }
        
        if (record.getCompanyCode() != null) {
            SET("company_code = #{companyCode,jdbcType=VARCHAR}");
        }
        
        if (record.getSysCode() != null) {
            SET("sys_code = #{sysCode,jdbcType=VARCHAR}");
        }
        
        WHERE("code = #{code,jdbcType=VARCHAR}");
        
        return SQL();
    }
}
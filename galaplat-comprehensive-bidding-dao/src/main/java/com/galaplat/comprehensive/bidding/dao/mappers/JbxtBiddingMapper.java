package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.BiddingDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 15:11
 */
public interface JbxtBiddingMapper {

    @Delete({
            "delete from t_jbxt_bidding",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String code);

    @Insert({
            "insert into t_jbxt_bidding (code, goods_id, ",
            "user_code, activity_code, ",
            "bid, created_time, ",
            "updated_time, updator, ",
            "creator, company_code, ",
            "sys_code, bid_time,isdelay, bid_percent)",
            "values (#{code,jdbcType=VARCHAR}, #{goodsId,jdbcType=INTEGER}, ",
            "#{userCode,jdbcType=VARCHAR}, #{activityCode,jdbcType=VARCHAR}, ",
            "#{bid,jdbcType=DECIMAL}, #{createdTime,jdbcType=TIMESTAMP}, ",
            "#{updatedTime,jdbcType=TIMESTAMP}, #{updator,jdbcType=VARCHAR}, ",
            "#{creator,jdbcType=VARCHAR}, #{companyCode,jdbcType=VARCHAR}, ",
            "#{sysCode,jdbcType=VARCHAR},#{bidTime,jdbcType=VARCHAR},#{isdelay,jdbcType=INTEGER}, #{bidPercent,jdbcType=INTEGER})"
    })
    int insert(BiddingDO record);

    @InsertProvider(type=JbxtBiddingDOSqlProvider.class, method="insertSelective")
    int insertSelective(BiddingDO record);

    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time,isdelay, bid_percent",
            "from t_jbxt_bidding",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType= JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR),
            @Result(column="isdelay", property="isdelay", jdbcType=JdbcType.INTEGER),
            @Result(column="bid_percent", property="bidPercent", jdbcType=JdbcType.INTEGER)
    })
    BiddingDO selectByPrimaryKey(String code);

    @UpdateProvider(type=JbxtBiddingDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(BiddingDO record);

    @Update({
            "update t_jbxt_bidding",
            "set goods_id = #{goodsId,jdbcType=INTEGER},",
            "user_code = #{userCode,jdbcType=VARCHAR},",
            "activity_code = #{activityCode,jdbcType=VARCHAR},",
            "bid = #{bid,jdbcType=DECIMAL},",
            "created_time = #{createdTime,jdbcType=TIMESTAMP},",
            "updated_time = #{updatedTime,jdbcType=TIMESTAMP},",
            "updator = #{updator,jdbcType=VARCHAR},",
            "creator = #{creator,jdbcType=VARCHAR},",
            "company_code = #{companyCode,jdbcType=VARCHAR},",
            "sys_code = #{sysCode,jdbcType=VARCHAR},",
            "bid_time = #{bidTime,jdbcType=VARCHAR},",
            "isdelay = #{isdelay,jdbcType=INTEGER}",
            "bid_percent = #{bidPercent,jdbcType=INTEGER}",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(BiddingDO record);

}

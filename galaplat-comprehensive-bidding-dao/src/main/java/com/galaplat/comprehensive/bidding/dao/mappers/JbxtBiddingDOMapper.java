package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtBiddingDOMapper {
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
        "sys_code)",
        "values (#{code,jdbcType=VARCHAR}, #{goodsId,jdbcType=INTEGER}, ",
        "#{userCode,jdbcType=VARCHAR}, #{activityCode,jdbcType=VARCHAR}, ",
        "#{bid,jdbcType=DECIMAL}, #{createdTime,jdbcType=TIMESTAMP}, ",
        "#{updatedTime,jdbcType=TIMESTAMP}, #{updator,jdbcType=VARCHAR}, ",
        "#{creator,jdbcType=VARCHAR}, #{companyCode,jdbcType=VARCHAR}, ",
        "#{sysCode,jdbcType=VARCHAR})"
    })
    int insert(JbxtBiddingDO record);

    @InsertProvider(type=JbxtBiddingDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtBiddingDO record);

    @Select({
        "select",
        "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
        "creator, company_code, sys_code",
        "from t_jbxt_bidding",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
        @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
        @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDO selectByPrimaryKey(String code);

    @UpdateProvider(type=JbxtBiddingDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(JbxtBiddingDO record);

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
          "sys_code = #{sysCode,jdbcType=VARCHAR}",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(JbxtBiddingDO record);

    List<JbxtBiddingDVO> getJbxtBiddingList(JbxtBiddingParam jbxtbiddingParam);


    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code",
            "from t_jbxt_bidding",
            "where goods_id = #{goodsId,jdbcType=INTEGER} ORDER BY bid ASC LIMIT 0,1000"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> getJbxtListBiddingByGoodsId( Integer goodsId);




    //获取当前用户最小竞价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code",
            "from t_jbxt_bidding",
            "where user_code=#{userCode,jdbcType=VARCHAR} and goods_id=#{goodsId,jdbcType=INTEGER} AND activity_code =#{activityCode,jdbcType=VARCHAR} ORDER BY bid ASC LIMIT 0,1"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode);


    //获取当前竞品的最小提交价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code",
            "from t_jbxt_bidding",
            "where user_code=#{userCode,jdbcType=VARCHAR} AND goods_id=#{goodsId,jdbcType=INTEGER} AND activity_code =#{activityCode,jdbcType=VARCHAR} ORDER BY bid ASC LIMIT 0,1"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDVO gerCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode);



    //获取所有用户信息
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code",
            "from t_jbxt_bidding",
            "where goods_id = #{goodsId,jdbcType=INTEGER} and activity_code = #{activityCode}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> getAllBidUserInfo( Integer goodsId, String activityCode);


}
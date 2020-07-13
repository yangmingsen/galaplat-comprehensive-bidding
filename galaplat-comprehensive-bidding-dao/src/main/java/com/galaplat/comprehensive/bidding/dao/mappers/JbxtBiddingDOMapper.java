package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.BidDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtBiddingDOMapper {
    //最低竞价表操作
    @Delete({
            "delete from t_jbxt_minbid",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteMinbidTableByPrimaryKey(String code);

    @Delete({
            "delete from t_jbxt_minbid",
            "where goods_id = #{goodsId,jdbcType=INTEGER} AND activity_code = #{activityCode,jdbcType=VARCHAR}"
    })
    int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

    @InsertProvider(type=JbxtBiddingDOSqlProvider.class, method="insertMinBidTableSelective")
    int insertMinBidTableSelective(JbxtBiddingDO record);


    //获取当前用户最小竞价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_minbid",
            "where user_code=#{userCode,jdbcType=VARCHAR} and goods_id=#{goodsId,jdbcType=INTEGER} AND activity_code =#{activityCode,jdbcType=VARCHAR}"
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
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDO selectMinBidTableByOne(String userCode, Integer goodsId, String activityCode);

    //获取当前竞品所有用户最小竞价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_minbid",
            "where goods_id=#{goodsId,jdbcType=INTEGER} AND activity_code =#{activityCode,jdbcType=VARCHAR} ORDER BY bid"
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
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> selectMinBidTableByList(Integer goodsId, String activityCode);


    @UpdateProvider(type=JbxtBiddingDOSqlProvider.class, method="updateMinBidTableByPrimaryKeySelective")
    int updateMinBidTableByPrimaryKeySelective(JbxtBiddingDO record);

    //end of  最低竞价表操作 code




    @Delete({
        "delete from t_jbxt_bidding",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String code);

    @Delete({
            "delete from t_jbxt_minbid",
            "where goods_id = #{goodsId,jdbcType=INTEGER} AND activity_code = #{activityCode,jdbcType=VARCHAR}"
    })
    int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

    @Insert({
        "insert into t_jbxt_bidding (code, goods_id, ",
        "user_code, activity_code, ",
        "bid, created_time, ",
        "updated_time, updator, ",
        "creator, company_code, ",
        "sys_code, bid_time)",
        "values (#{code,jdbcType=VARCHAR}, #{goodsId,jdbcType=INTEGER}, ",
        "#{userCode,jdbcType=VARCHAR}, #{activityCode,jdbcType=VARCHAR}, ",
        "#{bid,jdbcType=DECIMAL}, #{createdTime,jdbcType=TIMESTAMP}, ",
        "#{updatedTime,jdbcType=TIMESTAMP}, #{updator,jdbcType=VARCHAR}, ",
        "#{creator,jdbcType=VARCHAR}, #{companyCode,jdbcType=VARCHAR}, ",
        "#{sysCode,jdbcType=VARCHAR},#{bidTime,jdbcType=VARCHAR})"
    })
    int insert(JbxtBiddingDO record);

    @InsertProvider(type=JbxtBiddingDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtBiddingDO record);

    @Select({
        "select",
        "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
        "creator, company_code, sys_code, bid_time",
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
        @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
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
          "sys_code = #{sysCode,jdbcType=VARCHAR},",
            "bid_time = #{bidTime,jdbcType=VARCHAR}",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(JbxtBiddingDO record);

    List<JbxtBiddingDVO> getJbxtBiddingList(JbxtBiddingParam jbxtbiddingParam);


    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
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
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> getJbxtListBiddingByGoodsId( Integer goodsId);




    //获取当前用户最小竞价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
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
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode);


    //获取当前竞品的最小提交价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
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
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDVO gerCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode);



    //获取所有用户信息
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
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
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> getAllBidUserInfo( Integer goodsId, String activityCode);



    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_bidding",
            "where user_code = #{userCode,jdbcType=VARCHAR} and activity_code = #{activityCode,jdbcType=VARCHAR} ",
            "ORDER BY bid ASC"
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
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode);

    @Select({
            "<script>",
            " SELECT DISTINCT(user_code) FROM `t_jbxt_bidding` " ,
            " WHERE 1=1 ",
            " <if test='param.goodsId != null' > " ,
            " and goods_id = #{param.goodsId,jdbcType=INTEGER}",
            " </if>",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode, jdbcType=INTEGER}",
            " </if>",
            "</script>"
    })
    List<String> listBidActivityUsers(@Param("param") JbxtBiddingParam biddingParam);


    @Select({
            "<script>",
            " SELECT code,goods_id,user_code,activity_code,bid,created_time FROM t_jbxt_bidding " ,
            " WHERE 1=1 ",
            " <if test='param.goodsId != null' > " ,
            " and goods_id = #{param.goodsId,jdbcType=INTEGER}",
            " </if>",
            " <if test='param.userCode != null' > " ,
            " and user_code = #{param.userCode,jdbcType = VARCHAR}",
            " </if>",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType = VARCHAR}",
            " </if>",
            " ORDER BY bid ASC LIMIT 0,1",
            "</script>"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
    })
    BidDVO getBidActivity(@Param("param")JbxtBiddingParam biddingParam);


    @Select({
            "<script>",
            " SELECT code,goods_id,user_code,activity_code,bid,created_time FROM t_jbxt_bidding " ,
            " WHERE 1=1 ",
            " <if test='param.goodsId != null' > " ,
            " and goods_id = #{param.goodsId,jdbcType=INTEGER}",
            " </if>",
            " <if test='param.userCode != null' > " ,
            " and user_code = #{param.userCode,jdbcType = VARCHAR}",
            " </if>",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType = VARCHAR}",
            " </if>",
            "  ORDER BY created_time ASC",
            "</script>"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
    })
    List<BidDVO> getOneSupplierBidPriceDeatil(@Param("param")JbxtBiddingParam biddingParam);

}
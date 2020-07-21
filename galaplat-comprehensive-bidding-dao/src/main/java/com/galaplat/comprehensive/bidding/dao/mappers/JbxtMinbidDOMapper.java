package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtMinbidDO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtMinbidParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface JbxtMinbidDOMapper {
    @Delete({
        "delete from t_jbxt_minbid",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String code);

    @Insert({
        "insert into t_jbxt_minbid (code, goods_id, ",
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
        "#{sysCode,jdbcType=VARCHAR}, #{bidTime,jdbcType=VARCHAR})"
    })
    int insert(JbxtMinbidDO record);

    @InsertProvider(type=JbxtMinbidDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtMinbidDO record);

    @Select({
        "select",
        "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
        "creator, company_code, sys_code, bid_time",
        "from t_jbxt_minbid",
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
    JbxtMinbidDO selectByPrimaryKey(String code);

    @UpdateProvider(type=JbxtMinbidDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(JbxtMinbidDO record);

    @Update({
        "update t_jbxt_minbid",
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
    int updateByPrimaryKey(JbxtMinbidDO record);


    @Delete({
            "<script>",
            "delete from t_jbxt_minbid",
            "where 1=1 ",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType = VARCHAR}",
            " </if>",
            "<if test='param.userCodeList !=null '>",
            "  and user_code in " ,
            " <foreach item='item' index='index' collection='param.userCodeList' open='(' separator=',' close=')'> ",
            "  #{item} ",
            " </foreach>",
            "</if>",
            "</script>"
    })
    int deleteBidding(@Param("param") JbxtMinbidParam minbidParam);
}
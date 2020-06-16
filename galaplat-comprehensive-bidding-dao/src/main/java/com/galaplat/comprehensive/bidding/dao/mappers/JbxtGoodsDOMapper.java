package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

public interface JbxtGoodsDOMapper {
    @Delete({
        "delete from t_jbxt_goods",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String code);

    @Insert({
        "insert into t_jbxt_goods (code, name, ",
        "num, activity_code, ",
        "created_time, updated_time, ",
        "updator, creator, ",
        "company_code, sys_code)",
        "values (#{code,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{num,jdbcType=INTEGER}, #{activityCode,jdbcType=VARCHAR}, ",
        "#{createdTime,jdbcType=TIMESTAMP}, #{updatedTime,jdbcType=TIMESTAMP}, ",
        "#{updator,jdbcType=VARCHAR}, #{creator,jdbcType=VARCHAR}, ",
        "#{companyCode,jdbcType=VARCHAR}, #{sysCode,jdbcType=VARCHAR})"
    })
    int insert(JbxtGoodsDO record);

    @InsertProvider(type=JbxtGoodsDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtGoodsDO record);

    @Select({
        "select",
        "code, name, num, activity_code, created_time, updated_time, updator, creator, ",
        "company_code, sys_code",
        "from t_jbxt_goods",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="num", property="num", jdbcType=JdbcType.INTEGER),
        @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    JbxtGoodsDO selectByPrimaryKey(String code);

    @UpdateProvider(type=JbxtGoodsDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(JbxtGoodsDO record);

    @Update({
        "update t_jbxt_goods",
        "set name = #{name,jdbcType=VARCHAR},",
          "num = #{num,jdbcType=INTEGER},",
          "activity_code = #{activityCode,jdbcType=VARCHAR},",
          "created_time = #{createdTime,jdbcType=TIMESTAMP},",
          "updated_time = #{updatedTime,jdbcType=TIMESTAMP},",
          "updator = #{updator,jdbcType=VARCHAR},",
          "creator = #{creator,jdbcType=VARCHAR},",
          "company_code = #{companyCode,jdbcType=VARCHAR},",
          "sys_code = #{sysCode,jdbcType=VARCHAR}",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(JbxtGoodsDO record);
}
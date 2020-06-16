package com.galaplat.comprehensive.bidding.dao.mappers;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtAtivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtAtivityDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtAtivityParam;

public interface JbxtAtivityDOMapper {
    @Delete({
        "delete from t_jbxt_activity",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String code);

    @Insert({
        "insert into t_jbxt_activity (code, name, ",
        "start_time, end_time, ",
        "created_time, updated_time, ",
        "updator, creator, ",
        "company_code, sys_code)",
        "values (#{code,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
        "#{startTime,jdbcType=INTEGER}, #{endTime,jdbcType=VARCHAR}, ",
        "#{createdTime,jdbcType=TIMESTAMP}, #{updatedTime,jdbcType=TIMESTAMP}, ",
        "#{updator,jdbcType=VARCHAR}, #{creator,jdbcType=VARCHAR}, ",
        "#{companyCode,jdbcType=VARCHAR}, #{sysCode,jdbcType=VARCHAR})"
    })
    int insert(JbxtAtivityDO record);

    @InsertProvider(type=JbxtAtivityDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtAtivityDO record);

    @Select({
        "select",
        "code, name, start_time, end_time, created_time, updated_time, updator, creator, ",
        "company_code, sys_code",
        "from t_jbxt_activity",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.INTEGER),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    JbxtAtivityDO selectByPrimaryKey(JbxtAtivityParam jbxtativityParam);
    
    
    @Select({
        "select",
        "code, name, start_time, end_time, created_time, updated_time, updator, creator, ",
        "company_code, sys_code",
        "from t_jbxt_activity",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
        @Result(column="start_time", property="startTime", jdbcType=JdbcType.INTEGER),
        @Result(column="end_time", property="endTime", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtAtivityDVO> getJbxtAtivityList(JbxtAtivityParam jbxtativityParam);
    
    

    @UpdateProvider(type=JbxtAtivityDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(JbxtAtivityDO record);

    @Update({
        "update t_jbxt_activity",
        "set name = #{name,jdbcType=VARCHAR},",
          "start_time = #{startTime,jdbcType=INTEGER},",
          "end_time = #{endTime,jdbcType=VARCHAR},",
          "created_time = #{createdTime,jdbcType=TIMESTAMP},",
          "updated_time = #{updatedTime,jdbcType=TIMESTAMP},",
          "updator = #{updator,jdbcType=VARCHAR},",
          "creator = #{creator,jdbcType=VARCHAR},",
          "company_code = #{companyCode,jdbcType=VARCHAR},",
          "sys_code = #{sysCode,jdbcType=VARCHAR}",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(JbxtAtivityDO record);
}
package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtUserDOMapper {
    @Delete({
        "delete from t_jbxt_user",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(String code);

    @Insert({
        "insert into t_jbxt_user (code, username, ",
        "password, admin, ",
        "created_time, updated_time, ",
        "updator, creator, ",
        "company_code, sys_code, activity_code, supplier_name, code_name)",
        "values (#{code,jdbcType=VARCHAR}, #{username,jdbcType=VARCHAR}, ",
        "#{password,jdbcType=VARCHAR}, #{admin,jdbcType=VARCHAR}, ",
        "#{createdTime,jdbcType=TIMESTAMP}, #{updatedTime,jdbcType=TIMESTAMP}, ",
        "#{updator,jdbcType=VARCHAR}, #{creator,jdbcType=VARCHAR}, ",
        "#{companyCode,jdbcType=VARCHAR}, #{sysCode,jdbcType=VARCHAR})",
            "#{activityCode,jdbcType=VARCHAR}, #{supplierName,jdbcType=VARCHAR}, #{codeName,jdbcType=VARCHAR})"
    })
    int insert(JbxtUserDO record);

    @InsertProvider(type=JbxtUserDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtUserDO record);

    @Select({
        "select",
        "code, username, password, admin, created_time, updated_time, updator, creator, ",
        "company_code, sys_code,  activity_code, supplier_name, code_name",
        "from t_jbxt_user",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
        @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
        @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
        @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
        @Result(column="admin", property="admin", jdbcType=JdbcType.VARCHAR),
        @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
        @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
        @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="supplier_name", property="supplierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="code_name", property="codeName", jdbcType=JdbcType.VARCHAR)
    })
    JbxtUserDO selectByPrimaryKey(String code);

    @Select({
            "select",
            "code, username, password, admin, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, activity_code, supplier_name, code_name",
            "from t_jbxt_user",
            "where username = #{username,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="admin", property="admin", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR)
    })
    JbxtUserDO selectByUsernameKey(String username);



    @Select({
            "select",
            "code, username, password, admin, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, activity_code, supplier_name, code_name",
            "from t_jbxt_user",
            "where code = #{userCode,jdbcType=VARCHAR} AND activity_code = #{activityCode,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="username", property="username", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="admin", property="admin", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="supplier_name", property="supplierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="code_name", property="codeName", jdbcType=JdbcType.VARCHAR)
    })
    JbxtUserDO selectByuserCodeAndActivityCode(String userCode, String activityCode);


    @UpdateProvider(type=JbxtUserDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(JbxtUserDO record);

    @Update({
        "update t_jbxt_user",
        "set username = #{username,jdbcType=VARCHAR},",
          "password = #{password,jdbcType=VARCHAR},",
          "admin = #{admin,jdbcType=VARCHAR},",
          "created_time = #{createdTime,jdbcType=TIMESTAMP},",
          "updated_time = #{updatedTime,jdbcType=TIMESTAMP},",
          "updator = #{updator,jdbcType=VARCHAR},",
          "creator = #{creator,jdbcType=VARCHAR},",
          "company_code = #{companyCode,jdbcType=VARCHAR},",
          "sys_code = #{sysCode,jdbcType=VARCHAR}",
        "where code = #{code,jdbcType=VARCHAR}"
    })
    @Deprecated
    int updateByPrimaryKey(JbxtUserDO record);

    List<JbxtUserDVO> getJbxtUserList(JbxtUserParam jbxtuserParam);
}
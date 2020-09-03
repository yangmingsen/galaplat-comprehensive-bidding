package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 14:23
 */
public interface JbxtUserMapper {

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
            "company_code, sys_code, ",
            "activity_code, supplier_name, ",
            "code_name, contact_person, ",
            "phone, email_address, ",
            "login_status, send_sms, ",
            "send_mail)",
            "values (#{code,jdbcType=VARCHAR}, #{username,jdbcType=VARCHAR}, ",
            "#{password,jdbcType=VARCHAR}, #{admin,jdbcType=VARCHAR}, ",
            "#{createdTime,jdbcType=TIMESTAMP}, #{updatedTime,jdbcType=TIMESTAMP}, ",
            "#{updator,jdbcType=VARCHAR}, #{creator,jdbcType=VARCHAR}, ",
            "#{companyCode,jdbcType=VARCHAR}, #{sysCode,jdbcType=VARCHAR}, ",
            "#{activityCode,jdbcType=VARCHAR}, #{supplierName,jdbcType=VARCHAR}, ",
            "#{codeName,jdbcType=VARCHAR}, #{contactPerson,jdbcType=VARCHAR}, ",
            "#{phone,jdbcType=VARCHAR}, #{emailAddress,jdbcType=VARCHAR}, ",
            "#{loginStatus,jdbcType=INTEGER}, #{sendSms,jdbcType=INTEGER}, ",
            "#{sendMail,jdbcType=INTEGER})"
    })
    int insert(JbxtUserDO record);

    @InsertProvider(type=JbxtUserDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtUserDO record);

    @Select({
            "select",
            "code, username, password, admin, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, activity_code, supplier_name, code_name, contact_person, ",
            "phone, email_address, login_status, send_sms, send_mail",
            "from t_jbxt_user",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType= JdbcType.VARCHAR, id=true),
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
            @Result(column="code_name", property="codeName", jdbcType=JdbcType.VARCHAR),
            @Result(column="contact_person", property="contactPerson", jdbcType=JdbcType.VARCHAR),
            @Result(column="phone", property="phone", jdbcType=JdbcType.VARCHAR),
            @Result(column="email_address", property="emailAddress", jdbcType=JdbcType.VARCHAR),
            @Result(column="login_status", property="loginStatus", jdbcType=JdbcType.INTEGER),
            @Result(column="send_sms", property="sendSms", jdbcType=JdbcType.INTEGER),
            @Result(column="send_mail", property="sendMail", jdbcType=JdbcType.INTEGER)
    })
    JbxtUserDO selectByPrimaryKey(String code);

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
            "sys_code = #{sysCode,jdbcType=VARCHAR},",
            "activity_code = #{activityCode,jdbcType=VARCHAR},",
            "supplier_name = #{supplierName,jdbcType=VARCHAR},",
            "code_name = #{codeName,jdbcType=VARCHAR},",
            "contact_person = #{contactPerson,jdbcType=VARCHAR},",
            "phone = #{phone,jdbcType=VARCHAR},",
            "email_address = #{emailAddress,jdbcType=VARCHAR},",
            "login_status = #{loginStatus,jdbcType=INTEGER},",
            "send_sms = #{sendSms,jdbcType=INTEGER},",
            "send_mail = #{sendMail,jdbcType=INTEGER}",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(JbxtUserDO record);
}

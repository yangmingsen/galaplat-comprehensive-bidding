package com.galaplat.comprehensive.bidding.dao.mappers.custs;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.SupplierAccountExportDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtUserMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtUserCustMapper extends JbxtUserMapper {

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


    @Select({
            "select",
            "code, username, password, admin, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, activity_code, supplier_name, code_name",
            "from t_jbxt_user",
            "where username = #{username,jdbcType=VARCHAR} AND activity_code = #{activityCode,jdbcType=VARCHAR}"
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
    JbxtUserDO selectByUsernameAndActivityCode(String username, String activityCode);


    List<JbxtUserDVO> getJbxtUserList(JbxtUserParam jbxtuserParam);

    @Select({
            "select",
            "code, username, password, admin, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, activity_code, supplier_name, code_name",
            "from t_jbxt_user",
            "where activity_code = #{activityCode,jdbcType=VARCHAR}"
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
    List<JbxtUserDVO> findAllByActivityCode(String activityCode);


    @Insert({"<script>",
            "insert into t_jbxt_user (code, username, ",
            "password, admin, ",
            "created_time, updated_time, ",
            "updator, creator, ",
            "company_code, sys_code, activity_code, supplier_name, code_name) ",
            "values ",
            " <foreach collection=\"list\" item=\"item\" separator=\",\"> ",
            "  (#{item.code,jdbcType=VARCHAR}, #{item.username,jdbcType=VARCHAR}, ",
            "#{item.password,jdbcType=VARCHAR}, #{item.admin,jdbcType=VARCHAR}, ",
            "#{item.createdTime,jdbcType=TIMESTAMP}, #{item.updatedTime,jdbcType=TIMESTAMP}, ",
            "#{item.updator,jdbcType=VARCHAR}, #{item.creator,jdbcType=VARCHAR}, ",
            "#{item.companyCode,jdbcType=VARCHAR}, #{item.sysCode,jdbcType=VARCHAR},",
            "#{item.activityCode,jdbcType=VARCHAR}, #{item.supplierName,jdbcType=VARCHAR}, #{item.codeName,jdbcType=VARCHAR})",
            " </foreach>ON DUPLICATE KEY UPDATE " ,
            " username = values(username)," ,
            " password = values(password)," ,
            " updated_time = values(updated_time)," ,
            " updator = values(updator)," ,
            " supplier_name = values(supplier_name)," ,
            " code_name = values(code_name)",
            "</script>"
    })
    int btachInsertAndUpdate(@Param("list")List<JbxtUserParam> userParams);

    @Select({
            "<script>",
            "select",
            "code, username, password, admin, created_time, updated_time, updator, creator, ",
            "company_code, sys_code,  activity_code, supplier_name, code_name",
            "from t_jbxt_user",
            "where 1=1 ",
            "<if test='param.username != null' > " ,
            " and username = #{param.username,jdbcType=VARCHAR}",
            "</if>",
            "<if test='param.supplierName != null' > " ,
            " and supplier_name = #{param.supplierName,jdbcType=VARCHAR}",
            "</if>",
            "<if test='param.codeName != null' > " ,
            " and code_name = #{param.codeName,jdbcType=VARCHAR}",
            "</if>",
            "<if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType=VARCHAR}",
            "</if>",
            "<if test='param.password != null' > " ,
            " and password = #{param.password,jdbcType=VARCHAR}",
            "</if>",
            "</script>"
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
    List<JbxtUserDO> getUser(@Param("param")JbxtUserParam userParam);

    @Select({
            "<script>",
            "select" ,
            "u.username," ,
            "u.`password`," ,
            "u.supplier_name," ,
            "u.code_name," ,
            "u.activity_code," ,
            "date_format(a.created_time,'%Y-%m-%d') activity_create_date," ,
            "date_format(NOW(),'%Y-%m-%d')  activity_export_date " ,
            "from" ,
            "t_jbxt_user u" ,
            "left join t_jbxt_activity a ON u.activity_code = a.`code`",
            " where 1=1 ",
            " <if test='param.activityCode != null' > " ,
            " and u.activity_code = #{param.activityCode,jdbcType=VARCHAR}",
            " </if>",
            "</script>"
    })
    @Results({
            @Result(column="username", property="account", jdbcType=JdbcType.VARCHAR),
            @Result(column="password", property="password", jdbcType=JdbcType.VARCHAR),
            @Result(column="supplier_name", property="supplierName", jdbcType=JdbcType.VARCHAR),
            @Result(column="code_name", property="codeName", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_create_date", property="activityCreateDate", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_export_date", property="activityExportDate", jdbcType=JdbcType.VARCHAR)
    })
    List<SupplierAccountExportDVO> getAccountByActivityCode(@Param("param")JbxtUserParam userParam);

    @Delete({
            "<script>",
            "delete from t_jbxt_user",
            "where 1=1 ",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType=VARCHAR}",
            " </if>",
            "</script>"
    })
    int deleteUser(@Param("param")JbxtUserParam userParam);


    @Delete({
            "<script>",
            "delete from t_jbxt_user",
            "where 1=1 ",
            "<if test='list !=null '>",
            "  and code in " ,
            " <foreach item='item' index='index' collection='list' open='(' separator=',' close=')'> ",
            "  #{item} ",
            " </foreach>",
            "</if>",
            " <if test='activityCode != null' > " ,
            " and activity_code = #{activityCode,jdbcType=VARCHAR}",
            " </if>",
            "</script>"
    })
    int batchDeleteUser(@Param("list")List<String> userCodes,@Param("activityCode")String activityCode);



    @Select({"<script>",
            " select",
            " code, username, password, admin, created_time, updated_time, updator, creator, company_code, sys_code, activity_code, supplier_name, code_name, contact_person, phone, email_address, login_status, send_sms, send_mail ",
            " from t_jbxt_user ",
            " where 1=1 ",
            "<if test='param.username != null and param.username != \"\"' > and username = #{param.username,jdbcType=VARCHAR}</if>",
            "<if test='param.password != null and param.password != \"\"' > and password = #{param.password,jdbcType=VARCHAR}</if>",
            "<if test='param.admin != null and param.admin != \"\"' > and admin = #{param.admin,jdbcType=VARCHAR}</if>",
            "<if test='param.createdTime != null' > and created_time = #{param.createdTime,jdbcType=TIMESTAMP}</if>",
            "<if test='param.updatedTime != null' > and updated_time = #{param.updatedTime,jdbcType=TIMESTAMP}</if>",
            "<if test='param.updator != null and param.updator != \"\"' > and updator = #{param.updator,jdbcType=VARCHAR}</if>",
            "<if test='param.creator != null and param.creator != \"\"' > and creator = #{param.creator,jdbcType=VARCHAR}</if>",
            "<if test='param.companyCode != null and param.companyCode != \"\"' > and company_code = #{param.companyCode,jdbcType=VARCHAR}</if>",
            "<if test='param.sysCode != null and param.sysCode != \"\"' > and sys_code = #{param.sysCode,jdbcType=VARCHAR}</if>",
            "<if test='param.activityCode != null and param.activityCode != \"\"' > and activity_code = #{param.activityCode,jdbcType=VARCHAR}</if>",
            "<if test='param.supplierName != null and param.supplierName != \"\"' > and supplier_name = #{param.supplierName,jdbcType=VARCHAR}</if>",
            "<if test='param.codeName != null and param.codeName != \"\"' > and code_name = #{param.codeName,jdbcType=VARCHAR}</if>",
            "<if test='param.contactPerson != null and param.contactPerson != \"\"' > and contact_person = #{param.contactPerson,jdbcType=VARCHAR}</if>",
            "<if test='param.phone != null and param.phone != \"\"' > and phone = #{param.phone,jdbcType=VARCHAR}</if>",
            "<if test='param.emailAddress != null and param.emailAddress != \"\"' > and email_address = #{param.emailAddress,jdbcType=VARCHAR}</if>",
            "<if test='param.loginStatus != null' > and login_status = #{param.loginStatus,jdbcType=INTEGER}</if>",
            "<if test='param.sendSms != null' > and send_sms = #{param.sendSms,jdbcType=INTEGER}</if>",
            "<if test='param.sendMail != null' > and send_mail = #{param.sendMail,jdbcType=INTEGER}</if>",
            "</script>",
    })
    @Results({
            @Result(column = "code", property = "code", jdbcType = JdbcType.VARCHAR, id = true),
            @Result(column = "username", property = "username", jdbcType = JdbcType.VARCHAR),
            @Result(column = "password", property = "password", jdbcType = JdbcType.VARCHAR),
            @Result(column = "admin", property = "admin", jdbcType = JdbcType.VARCHAR),
            @Result(column = "created_time", property = "createdTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "updated_time", property = "updatedTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "updator", property = "updator", jdbcType = JdbcType.VARCHAR),
            @Result(column = "creator", property = "creator", jdbcType = JdbcType.VARCHAR),
            @Result(column = "company_code", property = "companyCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sys_code", property = "sysCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "activity_code", property = "activityCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "supplier_name", property = "supplierName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "code_name", property = "codeName", jdbcType = JdbcType.VARCHAR),
            @Result(column = "contact_person", property = "contactPerson", jdbcType = JdbcType.VARCHAR),
            @Result(column = "phone", property = "phone", jdbcType = JdbcType.VARCHAR),
            @Result(column = "email_address", property = "emailAddress", jdbcType = JdbcType.VARCHAR),
            @Result(column = "login_status", property = "loginStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "send_sms", property = "sendSms", jdbcType = JdbcType.INTEGER),
            @Result(column = "send_mail", property = "sendMail", jdbcType = JdbcType.INTEGER),
    })
    List<JbxtUserDO> listJbxtUser(@Param("param") JbxtUserParam param);

    @Update({
            " <script>",
            " update  t_jbxt_user set",
            "<if test='updateParam.username != null' >",
            "username = #{updateParam.username,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.password != null' >",
            "password = #{updateParam.password,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.admin != null' >",
            "admin = #{updateParam.admin,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.createdTime != null' >",
            "created_time = #{updateParam.createdTime,jdbcType=TIMESTAMP} ,  ",
            "</if>",
            "<if test='updateParam.updatedTime != null' >",
            "updated_time = #{updateParam.updatedTime,jdbcType=TIMESTAMP} ,  ",
            "</if>",
            "<if test='updateParam.updator != null' >",
            "updator = #{updateParam.updator,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.creator != null' >",
            "creator = #{updateParam.creator,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.companyCode != null' >",
            "company_code = #{updateParam.companyCode,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.sysCode != null' >",
            "sys_code = #{updateParam.sysCode,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.activityCode != null' >",
            "activity_code = #{updateParam.activityCode,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.supplierName != null' >",
            "supplier_name = #{updateParam.supplierName,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.codeName != null' >",
            "code_name = #{updateParam.codeName,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.contactPerson != null' >",
            "contact_person = #{updateParam.contactPerson,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.phone != null' >",
            "phone = #{updateParam.phone,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.emailAddress != null' >",
            "email_address = #{updateParam.emailAddress,jdbcType=VARCHAR} ,  ",
            "</if>",
            "<if test='updateParam.loginStatus != null' >",
            "login_status = #{updateParam.loginStatus,jdbcType=INTEGER} ,  ",
            "</if>",
            "<if test='updateParam.sendSms != null' >",
            "send_sms = #{updateParam.sendSms,jdbcType=INTEGER} ,  ",
            "</if>",
            "<if test='updateParam.sendMail != null' >",
            "send_mail = #{updateParam.sendMail,jdbcType=INTEGER}  ",
            "</if>",
            " where 1=1 ",
            "<if test='conditionParam.code != null' >",
            " and code = #{conditionParam.code,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.username != null' >",
            " and username = #{conditionParam.username,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.password != null' >",
            " and password = #{conditionParam.password,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.admin != null' >",
            " and admin = #{conditionParam.admin,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.createdTime != null' >",
            " and created_time = #{conditionParam.createdTime,jdbcType=TIMESTAMP} ",
            "</if>",
            "<if test='conditionParam.updatedTime != null' >",
            " and updated_time = #{conditionParam.updatedTime,jdbcType=TIMESTAMP} ",
            "</if>",
            "<if test='conditionParam.updator != null' >",
            " and updator = #{conditionParam.updator,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.creator != null' >",
            " and creator = #{conditionParam.creator,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.companyCode != null' >",
            " and company_code = #{conditionParam.companyCode,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.sysCode != null' >",
            " and sys_code = #{conditionParam.sysCode,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.activityCode != null' >",
            " and activity_code = #{conditionParam.activityCode,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.supplierName != null' >",
            " and supplier_name = #{conditionParam.supplierName,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.codeName != null' >",
            " and code_name = #{conditionParam.codeName,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.contactPerson != null' >",
            " and contact_person = #{conditionParam.contactPerson,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.phone != null' >",
            " and phone = #{conditionParam.phone,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.emailAddress != null' >",
            " and email_address = #{conditionParam.emailAddress,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.loginStatus != null' >",
            " and login_status = #{conditionParam.loginStatus,jdbcType=INTEGER} ",
            "</if>",
            "<if test='conditionParam.sendSms != null' >",
            " and send_sms = #{conditionParam.sendSms,jdbcType=INTEGER} ",
            "</if>",
            "<if test='conditionParam.sendMail != null' >",
            " and send_mail = #{conditionParam.sendMail,jdbcType=INTEGER} ",
            "</if>",
            " </script>",
    })
    int updateBySomeParam(@Param("updateParam") JbxtUserParam updateParam, @Param("conditionParam") JbxtUserParam conditionParam);

}


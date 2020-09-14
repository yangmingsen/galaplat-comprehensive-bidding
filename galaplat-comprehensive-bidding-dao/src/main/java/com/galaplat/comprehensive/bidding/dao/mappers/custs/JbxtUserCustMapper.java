package com.galaplat.comprehensive.bidding.dao.mappers.custs;

import com.galaplat.comprehensive.bidding.dao.dos.UserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.UserDVO;
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
    UserDO selectByUsernameKey(String username);



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
    UserDO selectByuserCodeAndActivityCode(String userCode, String activityCode);


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
    UserDO selectByUsernameAndActivityCode(String username, String activityCode);


    List<UserDVO> getJbxtUserList(JbxtUserParam jbxtuserParam);

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
    List<UserDVO> findAllByActivityCode(String activityCode);


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
    List<UserDO> getUser(@Param("param")JbxtUserParam userParam);

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
}

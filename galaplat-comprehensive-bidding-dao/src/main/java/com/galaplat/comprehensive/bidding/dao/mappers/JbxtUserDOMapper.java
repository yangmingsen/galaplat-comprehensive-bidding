package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtUserDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.SupplierAccountExportDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtUserParam;
import org.apache.ibatis.annotations.*;
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
            "<if test='param.supplierName != null' > " ,
            " and supplier_name = #{param.supplierName,jdbcType=VARCHAR}",
            "</if>",
            "<if test='param.codeName != null' > " ,
            " and code_name = #{param.codeName,jdbcType=VARCHAR}",
            "</if>",
            "<if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType=VARCHAR}",
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

}
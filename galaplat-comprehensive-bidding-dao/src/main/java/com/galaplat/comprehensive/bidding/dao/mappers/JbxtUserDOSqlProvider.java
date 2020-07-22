package com.galaplat.comprehensive.bidding.dao.mappers;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtUserDO;

public class JbxtUserDOSqlProvider {

    public String insertSelective(JbxtUserDO record) {
        BEGIN();
        INSERT_INTO("t_jbxt_user");
        
        if (record.getCode() != null) {
            VALUES("code", "#{code,jdbcType=VARCHAR}");
        }
        
        if (record.getUsername() != null) {
            VALUES("username", "#{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            VALUES("password", "#{password,jdbcType=VARCHAR}");
        }
        
        if (record.getAdmin() != null) {
            VALUES("admin", "#{admin,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatedTime() != null) {
            VALUES("created_time", "#{createdTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatedTime() != null) {
            VALUES("updated_time", "#{updatedTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdator() != null) {
            VALUES("updator", "#{updator,jdbcType=VARCHAR}");
        }
        
        if (record.getCreator() != null) {
            VALUES("creator", "#{creator,jdbcType=VARCHAR}");
        }
        
        if (record.getCompanyCode() != null) {
            VALUES("company_code", "#{companyCode,jdbcType=VARCHAR}");
        }
        
        if (record.getSysCode() != null) {
            VALUES("sys_code", "#{sysCode,jdbcType=VARCHAR}");
        }

        if (record.getActivityCode() != null) {
            VALUES("activity_code", "#{activityCode,jdbcType=VARCHAR}");
        }

        if (record.getSupplierName() != null) {
            VALUES("supplier_name", "#{supplierName,jdbcType=VARCHAR}");
        }

        if (record.getCodeName() != null) {
            VALUES("code_name", "#{codeName,jdbcType=VARCHAR}");
        }

        
        return SQL();
    }

    public String updateByPrimaryKeySelective(JbxtUserDO record) {
        BEGIN();
        UPDATE("t_jbxt_user");

        if (record.getActivityCode() != null) {
            SET("activity_code = #{activityCode,jdbcType=VARCHAR}");
        }

        if (record.getSupplierName() != null) {
            SET("supplier_name = #{supplierName,jdbcType=VARCHAR}");
        }

        if (record.getCodeName() != null) {
            SET("code_name = #{codeName,jdbcType=VARCHAR}");
        }


        if (record.getUsername() != null) {
            SET("username = #{username,jdbcType=VARCHAR}");
        }
        
        if (record.getPassword() != null) {
            SET("password = #{password,jdbcType=VARCHAR}");
        }
        
        if (record.getAdmin() != null) {
            SET("admin = #{admin,jdbcType=VARCHAR}");
        }
        
        if (record.getCreatedTime() != null) {
            SET("created_time = #{createdTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdatedTime() != null) {
            SET("updated_time = #{updatedTime,jdbcType=TIMESTAMP}");
        }
        
        if (record.getUpdator() != null) {
            SET("updator = #{updator,jdbcType=VARCHAR}");
        }
        
        if (record.getCreator() != null) {
            SET("creator = #{creator,jdbcType=VARCHAR}");
        }
        
        if (record.getCompanyCode() != null) {
            SET("company_code = #{companyCode,jdbcType=VARCHAR}");
        }
        
        if (record.getSysCode() != null) {
            SET("sys_code = #{sysCode,jdbcType=VARCHAR}");
        }
        
        WHERE("code = #{code,jdbcType=VARCHAR}");
        
        return SQL();
    }
}
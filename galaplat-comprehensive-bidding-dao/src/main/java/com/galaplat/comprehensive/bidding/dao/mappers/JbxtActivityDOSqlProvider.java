package com.galaplat.comprehensive.bidding.dao.mappers;

import static org.apache.ibatis.jdbc.SqlBuilder.BEGIN;
import static org.apache.ibatis.jdbc.SqlBuilder.INSERT_INTO;
import static org.apache.ibatis.jdbc.SqlBuilder.SET;
import static org.apache.ibatis.jdbc.SqlBuilder.SQL;
import static org.apache.ibatis.jdbc.SqlBuilder.UPDATE;
import static org.apache.ibatis.jdbc.SqlBuilder.VALUES;
import static org.apache.ibatis.jdbc.SqlBuilder.WHERE;

import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;

public class JbxtActivityDOSqlProvider {

    public String insertSelective(ActivityDO record) {
        BEGIN();
        INSERT_INTO("t_jbxt_activity");

        if (record.getCode() != null) {
            VALUES("code", "#{code,jdbcType=VARCHAR}");
        }

        if (record.getName() != null) {
            VALUES("name", "#{name,jdbcType=VARCHAR}");
        }

        if (record.getStartTime() != null) {
            VALUES("start_time", "#{startTime,jdbcType=TIMESTAMP}");
        }

        if (record.getEndTime() != null) {
            VALUES("end_time", "#{endTime,jdbcType=TIMESTAMP}");
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

        if (record.getSupplierNum() != null) {
            VALUES("supplier_num", "#{supplierNum,jdbcType=INTEGER}");
        }

        if (record.getStatus() != null) {
            VALUES("status", "#{status,jdbcType=INTEGER}");
        }

        if (record.getRecordStatus() != null) {
            VALUES("record_status", "#{recordStatus,jdbcType=INTEGER}");
        }

        if (record.getDeleteTime() != null) {
            VALUES("delete_time", "#{deleteTime,jdbcType=TIMESTAMP}");
        }

        if (record.getDeleter() != null) {
            VALUES("deleter", "#{deleter,jdbcType=VARCHAR}");
        }

        if (record.getBidingType() != null) {
            VALUES("biding_type", "#{bidingType,jdbcType=INTEGER}");
        }

        if (record.getPredictBidDatetime() != null) {
            VALUES("predict_bid_datetime", "#{predictBidDatetime,jdbcType=TIMESTAMP}");
        }

        if (record.getBidActivityInfo() != null) {
            VALUES("bid_activity_info", "#{bidActivityInfo,jdbcType=VARCHAR}");
        }

        if (record.getPromiseTitle() != null) {
            VALUES("promise_title", "#{promiseTitle,jdbcType=VARCHAR}");
        }

        if (record.getFilePath() != null) {
            VALUES("file_path", "#{filePath,jdbcType=VARCHAR}");
        }

        if (record.getPromiseText() != null) {
            VALUES("promise_text", "#{promiseText,jdbcType=LONGVARCHAR}");
        }

        return SQL();
    }

    public String updateByPrimaryKeySelective(ActivityDO record) {
        BEGIN();
        UPDATE("t_jbxt_activity");

        if (record.getName() != null) {
            SET("name = #{name,jdbcType=VARCHAR}");
        }

        if (record.getStartTime() != null) {
            SET("start_time = #{startTime,jdbcType=TIMESTAMP}");
        }

        if (record.getEndTime() != null) {
            SET("end_time = #{endTime,jdbcType=TIMESTAMP}");
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

        if (record.getSupplierNum() != null) {
            SET("supplier_num = #{supplierNum,jdbcType=INTEGER}");
        }

        if (record.getStatus() != null) {
            SET("status = #{status,jdbcType=INTEGER}");
        }

        if (record.getRecordStatus() != null) {
            SET("record_status = #{recordStatus,jdbcType=INTEGER}");
        }

        if (record.getDeleteTime() != null) {
            SET("delete_time = #{deleteTime,jdbcType=TIMESTAMP}");
        }

        if (record.getDeleter() != null) {
            SET("deleter = #{deleter,jdbcType=VARCHAR}");
        }

        if (record.getBidingType() != null) {
            SET("biding_type = #{bidingType,jdbcType=INTEGER}");
        }

        if (record.getPredictBidDatetime() != null) {
            SET("predict_bid_datetime = #{predictBidDatetime,jdbcType=TIMESTAMP}");
        }

        if (record.getBidActivityInfo() != null) {
            SET("bid_activity_info = #{bidActivityInfo,jdbcType=VARCHAR}");
        }

        if (record.getPromiseTitle() != null) {
            SET("promise_title = #{promiseTitle,jdbcType=VARCHAR}");
        }

        if (record.getFilePath() != null) {
            SET("file_path = #{filePath,jdbcType=VARCHAR}");
        }

        if (record.getPromiseText() != null) {
            SET("promise_text = #{promiseText,jdbcType=LONGVARCHAR}");
        }

        WHERE("code = #{code,jdbcType=VARCHAR}");

        return SQL();
    }
}

package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

/**
 * @Description:
 * @Author: weiyuxuan
 * @CreateDate: 2020/9/3 14:18
 */
public interface JbxtActivityMapper {
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
            "company_code, sys_code, ",
            "supplier_num, status, ",
            "record_status, delete_time, ",
            "deleter, biding_type, ",
            "predict_bid_datetime, bid_activity_info, ",
            "promise_title, file_path, ",
            "promise_text)",
            "values (#{code,jdbcType=VARCHAR}, #{name,jdbcType=VARCHAR}, ",
            "#{startTime,jdbcType=TIMESTAMP}, #{endTime,jdbcType=TIMESTAMP}, ",
            "#{createdTime,jdbcType=TIMESTAMP}, #{updatedTime,jdbcType=TIMESTAMP}, ",
            "#{updator,jdbcType=VARCHAR}, #{creator,jdbcType=VARCHAR}, ",
            "#{companyCode,jdbcType=VARCHAR}, #{sysCode,jdbcType=VARCHAR}, ",
            "#{supplierNum,jdbcType=INTEGER}, #{status,jdbcType=INTEGER}, ",
            "#{recordStatus,jdbcType=INTEGER}, #{deleteTime,jdbcType=TIMESTAMP}, ",
            "#{deleter,jdbcType=VARCHAR}, #{bidingType,jdbcType=INTEGER}, ",
            "#{predictBidDatetime,jdbcType=TIMESTAMP}, #{bidActivityInfo,jdbcType=VARCHAR}, ",
            "#{promiseTitle,jdbcType=VARCHAR}, #{filePath,jdbcType=VARCHAR}, ",
            "#{promiseText,jdbcType=LONGVARCHAR})"
    })
    int insert(ActivityDO record);

    @InsertProvider(type=JbxtActivityDOSqlProvider.class, method="insertSelective")
    int insertSelective(ActivityDO record);

    @Select({
            "select",
            "code, name, start_time, end_time, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, supplier_num, status, record_status, delete_time, deleter, ",
            "biding_type, predict_bid_datetime, bid_activity_info, promise_title, file_path, ",
            "promise_text",
            "from t_jbxt_activity",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="start_time", property="startTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="end_time", property="endTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="supplier_num", property="supplierNum", jdbcType=JdbcType.INTEGER),
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER),
            @Result(column="record_status", property="recordStatus", jdbcType=JdbcType.INTEGER),
            @Result(column="delete_time", property="deleteTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="deleter", property="deleter", jdbcType=JdbcType.VARCHAR),
            @Result(column="biding_type", property="bidingType", jdbcType=JdbcType.INTEGER),
            @Result(column="predict_bid_datetime", property="predictBidDatetime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="bid_activity_info", property="bidActivityInfo", jdbcType=JdbcType.VARCHAR),
            @Result(column="promise_title", property="promiseTitle", jdbcType=JdbcType.VARCHAR),
            @Result(column="file_path", property="filePath", jdbcType=JdbcType.VARCHAR),
            @Result(column="promise_text", property="promiseText", jdbcType=JdbcType.LONGVARCHAR)
    })
    ActivityDO selectByPrimaryKey(String code);

    @UpdateProvider(type=JbxtActivityDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(ActivityDO record);

    @Update({
            "update t_jbxt_activity",
            "set name = #{name,jdbcType=VARCHAR},",
            "start_time = #{startTime,jdbcType=TIMESTAMP},",
            "end_time = #{endTime,jdbcType=TIMESTAMP},",
            "created_time = #{createdTime,jdbcType=TIMESTAMP},",
            "updated_time = #{updatedTime,jdbcType=TIMESTAMP},",
            "updator = #{updator,jdbcType=VARCHAR},",
            "creator = #{creator,jdbcType=VARCHAR},",
            "company_code = #{companyCode,jdbcType=VARCHAR},",
            "sys_code = #{sysCode,jdbcType=VARCHAR},",
            "supplier_num = #{supplierNum,jdbcType=INTEGER},",
            "status = #{status,jdbcType=INTEGER},",
            "record_status = #{recordStatus,jdbcType=INTEGER},",
            "delete_time = #{deleteTime,jdbcType=TIMESTAMP},",
            "deleter = #{deleter,jdbcType=VARCHAR},",
            "biding_type = #{bidingType,jdbcType=INTEGER},",
            "predict_bid_datetime = #{predictBidDatetime,jdbcType=TIMESTAMP},",
            "bid_activity_info = #{bidActivityInfo,jdbcType=VARCHAR},",
            "promise_title = #{promiseTitle,jdbcType=VARCHAR},",
            "file_path = #{filePath,jdbcType=VARCHAR},",
            "promise_text = #{promiseText,jdbcType=LONGVARCHAR}",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKeyWithBLOBs(ActivityDO record);

    @Update({
            "update t_jbxt_activity",
            "set name = #{name,jdbcType=VARCHAR},",
            "start_time = #{startTime,jdbcType=TIMESTAMP},",
            "end_time = #{endTime,jdbcType=TIMESTAMP},",
            "created_time = #{createdTime,jdbcType=TIMESTAMP},",
            "updated_time = #{updatedTime,jdbcType=TIMESTAMP},",
            "updator = #{updator,jdbcType=VARCHAR},",
            "creator = #{creator,jdbcType=VARCHAR},",
            "company_code = #{companyCode,jdbcType=VARCHAR},",
            "sys_code = #{sysCode,jdbcType=VARCHAR},",
            "supplier_num = #{supplierNum,jdbcType=INTEGER},",
            "status = #{status,jdbcType=INTEGER},",
            "record_status = #{recordStatus,jdbcType=INTEGER},",
            "delete_time = #{deleteTime,jdbcType=TIMESTAMP},",
            "deleter = #{deleter,jdbcType=VARCHAR},",
            "biding_type = #{bidingType,jdbcType=INTEGER},",
            "predict_bid_datetime = #{predictBidDatetime,jdbcType=TIMESTAMP},",
            "bid_activity_info = #{bidActivityInfo,jdbcType=VARCHAR},",
            "promise_title = #{promiseTitle,jdbcType=VARCHAR},",
            "file_path = #{filePath,jdbcType=VARCHAR}",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(ActivityDO record);


}

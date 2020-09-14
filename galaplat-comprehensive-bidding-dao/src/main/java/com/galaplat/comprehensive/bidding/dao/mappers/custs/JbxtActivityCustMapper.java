package com.galaplat.comprehensive.bidding.dao.mappers.custs;

import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dvos.CompetitiveListDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.ActivityDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtActivityMapper;
import com.galaplat.comprehensive.bidding.dao.params.CompetitiveListParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtActivityCustMapper extends JbxtActivityMapper {

    @Select({
            "select",
            "code, name, start_time, end_time, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, supplier_num, status",
            "from t_jbxt_activity"
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
            @Result(column="status", property="status", jdbcType=JdbcType.INTEGER)

    })
    List<ActivityDVO> selectAll();

    List<ActivityDVO> getJbxtActivityList(JbxtActivityParam ActivityParam);


    @Select({
            "<script>",
            "select" ,
            "act.`status`," ,
            "act.`code`," ,
            "act.creator," ,
            "DATE_FORMAT(act.created_time,'%Y-%m-%d') created_date," ,
            "GROUP_CONCAT(goods.`code`) as bid_product_code," ,
            "act.supplier_num join_supplier_num," ,
            "concat(act.predict_bid_datetime) as predict_bid_datetime, " ,
            "act.biding_type " ,
            "from t_jbxt_activity act" ,
            "left join t_jbxt_goods goods ON act.`code` = goods.activity_code" ,
            "where record_status =1",
            "<if test='param.startTime !=null and param.startTime !=\"\"'>",
            "and DATE_FORMAT(act.created_time,'%Y-%m') <![CDATA[ >= ]]>   DATE_FORMAT(str_to_date(#{param.startTime}, '%Y-%m-%d'), '%Y-%m') ",
            "</if>",
            "<if test='param.endTime !=null and param.endTime !=\"\"'>",
            "and DATE_FORMAT(act.created_time,'%Y-%m') <![CDATA[ <= ]]>   DATE_FORMAT(str_to_date(#{param.endTime}, '%Y-%m-%d'), '%Y-%m')",
            "</if>",
            "<if test='param.activityStatusList !=null '>",
            "  and act.status in " ,
            " <foreach item='item' index='index' collection='param.activityStatusList' open='(' separator=',' close=')'> ",
            "  #{item} ",
            " </foreach>",
            "</if>",
            "<if test='param.bidActivityCodeList !=null '>",
            "  and act.code in " ,
            " <foreach item='item' index='index' collection='param.bidActivityCodeList' open='(' separator=',' close=')'> ",
            "  #{item} ",
            " </foreach>",
            "</if>",
            "GROUP BY act.`code`",
            " order by act.created_time desc",
            "</script>",
    })
    @Results({
            @Result(column="code", property="activityCode", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="status", property="activityStatus", jdbcType=JdbcType.INTEGER),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_date", property="createdDate", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_product_code", property="bidProductCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="join_supplier_num", property="joinSupplierNum", jdbcType=JdbcType.VARCHAR),
            @Result(column="predict_bid_datetime", property="predictBidDateTime", jdbcType=JdbcType.VARCHAR),
            @Result(column="biding_type", property="bidingType", jdbcType=JdbcType.INTEGER),
    })
    List<CompetitiveListDVO> listCompetitiveListPage(@Param("param")CompetitiveListParam param);


    @Select({
            "select",
            "code, name, start_time, end_time, created_time, updated_time, updator, creator, ",
            "company_code, sys_code, supplier_num, status, record_status, delete_time, deleter, ",
            "biding_type, predict_bid_datetime, bid_activity_info, promise_title, file_path, ",
            "promise_text",
            "from t_jbxt_activity",
            "where status = #{status,jdbcType=INTEGER}"
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
    List<ActivityDVO> selectAllByStatus(Integer status);




    @Select({"<script>",
            " select",
            " code, name, start_time, end_time, created_time, updated_time, updator, " ,
            " creator, company_code, sys_code, supplier_num, status, record_status, delete_time, " ,
            " deleter, biding_type, predict_bid_datetime, bid_activity_info, promise_title, promise_text, file_path ",
            " from t_jbxt_activity ",
            " where 1=1 ",
            "<if test='param.code != null' > and code = #{param.code,jdbcType=VARCHAR}</if>",
            "<if test='param.name != null' > and name = #{param.name,jdbcType=VARCHAR}</if>",
            "<if test='param.updator != null' > and updator = #{param.updator,jdbcType=VARCHAR}</if>",
            "<if test='param.creator != null' > and creator = #{param.creator,jdbcType=VARCHAR}</if>",
            "<if test='param.companyCode != null' > and company_code = #{param.companyCode,jdbcType=VARCHAR}</if>",
            "<if test='param.sysCode != null' > and sys_code = #{param.sysCode,jdbcType=VARCHAR}</if>",
            "<if test='param.deleter != null' > and deleter = #{param.deleter,jdbcType=VARCHAR}</if>",
            "<if test='param.bidActivityInfo != null' > and bid_activity_info = #{param.bidActivityInfo,jdbcType=VARCHAR}</if>",
            "<if test='param.promiseTitle != null' > and promise_title = #{param.promiseTitle,jdbcType=VARCHAR}</if>",
            "<if test='param.promiseText != null' > and promise_text = #{param.promiseText,jdbcType=VARCHAR}</if>",
            "<if test='param.filePath != null' > and file_path = #{param.filePath,jdbcType=VARCHAR}</if>",
            "</script>",
    })
    @Results({
            @Result(column = "code", property = "code", jdbcType = JdbcType.VARCHAR, id = true),
            @Result(column = "name", property = "name", jdbcType = JdbcType.VARCHAR),
            @Result(column = "start_time", property = "startTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "end_time", property = "endTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "created_time", property = "createdTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "updated_time", property = "updatedTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "updator", property = "updator", jdbcType = JdbcType.VARCHAR),
            @Result(column = "creator", property = "creator", jdbcType = JdbcType.VARCHAR),
            @Result(column = "company_code", property = "companyCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "sys_code", property = "sysCode", jdbcType = JdbcType.VARCHAR),
            @Result(column = "supplier_num", property = "supplierNum", jdbcType = JdbcType.INTEGER),
            @Result(column = "status", property = "status", jdbcType = JdbcType.INTEGER),
            @Result(column = "record_status", property = "recordStatus", jdbcType = JdbcType.INTEGER),
            @Result(column = "delete_time", property = "deleteTime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "deleter", property = "deleter", jdbcType = JdbcType.VARCHAR),
            @Result(column = "biding_type", property = "bidingType", jdbcType = JdbcType.INTEGER),
            @Result(column = "predict_bid_datetime", property = "predictBidDatetime", jdbcType = JdbcType.TIMESTAMP),
            @Result(column = "bid_activity_info", property = "bidActivityInfo", jdbcType = JdbcType.VARCHAR),
            @Result(column = "promise_title", property = "promiseTitle", jdbcType = JdbcType.VARCHAR),
            @Result(column = "promise_text", property = "promiseText", jdbcType = JdbcType.VARCHAR),
            @Result(column = "file_path", property = "filePath", jdbcType = JdbcType.VARCHAR),
    })
    ActivityDO getJbxtActivityByParam(@Param("param") JbxtActivityParam param);

    @Update({
            " <script>",
            " update  t_jbxt_activity set",
            "<if test='updateParam.name != null' >",
            "name = #{updateParam.name,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.startTime != null' >",
            "start_time = #{updateParam.startTime,jdbcType=TIMESTAMP} , ",
            "</if>",
            "<if test='updateParam.endTime != null' >",
            "end_time = #{updateParam.endTime,jdbcType=TIMESTAMP} , ",
            "</if>",
            "<if test='updateParam.createdTime != null' >",
            "created_time = #{updateParam.createdTime,jdbcType=TIMESTAMP} , ",
            "</if>",
            "<if test='updateParam.updatedTime != null' >",
            "updated_time = #{updateParam.updatedTime,jdbcType=TIMESTAMP} , ",
            "</if>",
            "<if test='updateParam.updator != null' >",
            "updator = #{updateParam.updator,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.creator != null' >",
            "creator = #{updateParam.creator,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.companyCode != null' >",
            "company_code = #{updateParam.companyCode,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.sysCode != null' >",
            "sys_code = #{updateParam.sysCode,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.supplierNum != null' >",
            "supplier_num = #{updateParam.supplierNum,jdbcType=INTEGER} , ",
            "</if>",
            "<if test='updateParam.status != null' >",
            "status = #{updateParam.status,jdbcType=INTEGER} , ",
            "</if>",
            "<if test='updateParam.recordStatus != null' >",
            "record_status = #{updateParam.recordStatus,jdbcType=INTEGER} , ",
            "</if>",
            "<if test='updateParam.deleteTime != null' >",
            "delete_time = #{updateParam.deleteTime,jdbcType=TIMESTAMP} , ",
            "</if>",
            "<if test='updateParam.deleter != null' >",
            "deleter = #{updateParam.deleter,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.bidingType != null' >",
            "biding_type = #{updateParam.bidingType,jdbcType=INTEGER} , ",
            "</if>",
            "<if test='updateParam.predictBidDatetime != null' >",
            "predict_bid_datetime = #{updateParam.predictBidDatetime,jdbcType=TIMESTAMP} , ",
            "</if>",
            "<if test='updateParam.bidActivityInfo != null' >",
            "bid_activity_info = #{updateParam.bidActivityInfo,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.promiseTitle != null' >",
            "promise_title = #{updateParam.promiseTitle,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.promiseText != null' >",
            "promise_text = #{updateParam.promiseText,jdbcType=VARCHAR} , ",
            "</if>",
            "<if test='updateParam.filePath != null' >",
            "file_path = #{updateParam.filePath,jdbcType=VARCHAR} , ",
            "</if>",
            " code = #{updateParam.code,jdbcType=VARCHAR}  ",
            " where 1=1 ",
            "<if test='conditionParam.code != null' >",
            " and code = #{conditionParam.code,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.name != null' >",
            " and name = #{conditionParam.name,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.startTime != null' >",
            " and start_time = #{conditionParam.startTime,jdbcType=TIMESTAMP} ",
            "</if>",
            "<if test='conditionParam.endTime != null' >",
            " and end_time = #{conditionParam.endTime,jdbcType=TIMESTAMP} ",
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
            "<if test='conditionParam.supplierNum != null' >",
            " and supplier_num = #{conditionParam.supplierNum,jdbcType=INTEGER} ",
            "</if>",
            "<if test='conditionParam.status != null' >",
            " and status = #{conditionParam.status,jdbcType=INTEGER} ",
            "</if>",
            "<if test='conditionParam.recordStatus != null' >",
            " and record_status = #{conditionParam.recordStatus,jdbcType=INTEGER} ",
            "</if>",
            "<if test='conditionParam.deleteTime != null' >",
            " and delete_time = #{conditionParam.deleteTime,jdbcType=TIMESTAMP} ",
            "</if>",
            "<if test='conditionParam.deleter != null' >",
            " and deleter = #{conditionParam.deleter,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.bidingType != null' >",
            " and biding_type = #{conditionParam.bidingType,jdbcType=INTEGER} ",
            "</if>",
            "<if test='conditionParam.predictBidDatetime != null' >",
            " and predict_bid_datetime = #{conditionParam.predictBidDatetime,jdbcType=TIMESTAMP} ",
            "</if>",
            "<if test='conditionParam.bidActivityInfo != null' >",
            " and bid_activity_info = #{conditionParam.bidActivityInfo,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.promiseTitle != null' >",
            " and promise_title = #{conditionParam.promiseTitle,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.promiseText != null' >",
            " and promise_text = #{conditionParam.promiseText,jdbcType=VARCHAR} ",
            "</if>",
            "<if test='conditionParam.filePath != null' >",
            " and file_path = #{conditionParam.filePath,jdbcType=VARCHAR} ",
            "</if>",
            " </script>",
    })
    int updateJbxtActivityBySomeParam(@Param("updateParam") JbxtActivityParam updateParam, @Param("conditionParam") JbxtActivityParam conditionParam);

}

package com.galaplat.comprehensive.bidding.dao.mappers.custs;

import com.galaplat.comprehensive.bidding.dao.dvos.CompetitiveListDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtActivityDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtActivityMapper;
import com.galaplat.comprehensive.bidding.dao.params.CompetitiveListParam;
import com.galaplat.comprehensive.bidding.dao.params.JbxtActivityParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;

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
    List<JbxtActivityDVO> selectAll();

    List<JbxtActivityDVO> getJbxtActivityList(JbxtActivityParam jbxtactivityParam);


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
    JbxtActivityDO getJbxtActivityByParam(@Param("param") JbxtActivityParam param);
}

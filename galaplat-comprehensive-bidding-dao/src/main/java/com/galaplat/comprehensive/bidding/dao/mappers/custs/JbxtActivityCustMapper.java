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
            "act.supplier_num join_supplier_num" ,
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
    })
    List<CompetitiveListDVO> listCompetitiveListPage(@Param("param")CompetitiveListParam param);

}

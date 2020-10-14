package com.galaplat.comprehensive.bidding.dao.mappers.custs;

import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.GoodsDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtGoodsMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtGoodsCustMapper extends JbxtGoodsMapper {

    List<GoodsDVO> getJbxtGoodsList(JbxtGoodsParam jbxtgoodsParam);

    @Select({
            "select",
            "goods_id, code, name, num, activity_code, created_time, updated_time, first_price, ",
            "creator, company_code, sys_code, status, time_num, last_chang_time, per_delay_time, delay_times, add_delay_times, retain_price",
            "from t_jbxt_goods",
            "where activity_code = #{activityCode,jdbcType=VARCHAR} ORDER BY goods_id ASC LIMIT 0,1000"
    })
    @Results({
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="num", property="num", jdbcType=JdbcType.INTEGER),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="first_price", property="firstPrice", jdbcType=JdbcType.DECIMAL),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
            @Result(column="time_num", property="timeNum", jdbcType=JdbcType.INTEGER),
            @Result(column="last_chang_time", property="lastChangTime", jdbcType=JdbcType.INTEGER),
            @Result(column="per_delay_time", property="perDelayTime", jdbcType=JdbcType.INTEGER),
            @Result(column="delay_times", property="delayTimes", jdbcType=JdbcType.INTEGER),
            @Result(column="add_delay_times", property="addDelayTimes", jdbcType=JdbcType.INTEGER),
            @Result(column="retain_price", property="retainPrice", jdbcType=JdbcType.DECIMAL)
    })
    List<GoodsDVO> getListJbxtGoodsByActivityCode(String activityCode);



    @Select({
            "select",
            "goods_id, code, name, num, activity_code, created_time, updated_time, first_price, ",
            "creator, company_code, sys_code, status, time_num,last_chang_time, per_delay_time, delay_times, add_delay_times, retain_price ",
            "from t_jbxt_goods",
            "where status = 1 AND activity_code=#{activityCode,jdbcType=VARCHAR} "
    })
    @Results({
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="num", property="num", jdbcType=JdbcType.INTEGER),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="first_price", property="firstPrice", jdbcType=JdbcType.DECIMAL),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
            @Result(column="time_num", property="timeNum", jdbcType=JdbcType.INTEGER),
            @Result(column="last_chang_time", property="lastChangTime", jdbcType=JdbcType.INTEGER),
            @Result(column="per_delay_time", property="perDelayTime", jdbcType=JdbcType.INTEGER),
            @Result(column="delay_times", property="delayTimes", jdbcType=JdbcType.INTEGER),
            @Result(column="add_delay_times", property="addDelayTimes", jdbcType=JdbcType.INTEGER),
            @Result(column="retain_price", property="retainPrice", jdbcType=JdbcType.DECIMAL)
    })
    public GoodsDO selectActiveGoods(String activityCode);


    @Insert({
            "<script>",
            "insert into t_jbxt_goods (goods_id, code, ",
            "name, num, activity_code, ",
            "created_time, updated_time, ",
            "updator, creator, ",
            "company_code, sys_code, ",
            "status, time_num, first_price," ,
            "last_chang_time, per_delay_time, " ,
            "delay_times, add_delay_times,retain_price )",
            " values ",
            " <foreach collection=\"list\" item=\"item\" separator=\",\"> ",
            " (#{item.goodsId,jdbcType=INTEGER}, #{item.code,jdbcType=VARCHAR}, ",
            "#{item.name,jdbcType=VARCHAR}, #{item.num,jdbcType=INTEGER}, #{item.activityCode,jdbcType=VARCHAR}, ",
            "#{item.createdTime,jdbcType=TIMESTAMP}, #{item.updatedTime,jdbcType=TIMESTAMP}, ",
            "#{item.firstPrice,jdbcType=DECIMAL}, #{item.creator,jdbcType=VARCHAR}, ",
            "#{item.companyCode,jdbcType=VARCHAR}, #{item.sysCode,jdbcType=VARCHAR}, ",
            "#{item.status,jdbcType=VARCHAR}, #{item.timeNum,jdbcType=INTEGER}, #{item.firstPrice,jdbcType=DECIMAL}, ",
            "#{item.lastChangTime,jdbcType=INTEGER}, #{item.perDelayTime,jdbcType=INTEGER}, #{item.delayTimes,jdbcType=INTEGER}, ",
            "#{item.addDelayTimes,jdbcType=INTEGER}, #{item.retainPrice,jdbcType=DECIMAL})",
            " </foreach> ",
            " </script>",
    })
    int batchInsert(@Param("list") List<JbxtGoodsParam> goodsParam);

    @Select({
            " <script>",
            " select",
            " goods_id, code, name, num, activity_code, created_time, updated_time, first_price, ",
            " creator, company_code, sys_code, status, time_num, last_chang_time, per_delay_time, delay_times, add_delay_times, retain_price ",
            " from t_jbxt_goods",
            " where 1=1 " ,
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType=VARCHAR}",
            " </if>",
            " <if test='param.name != null' > " ,
            " and name = #{param.name,jdbcType=VARCHAR}",
            " </if>",
            " <if test='param.code != null' > " ,
            " and code = #{param.code,jdbcType=VARCHAR}",
            " </if>",
            " </script>",
    })
    @Results({
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER, id=true),
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR),
            @Result(column="name", property="name", jdbcType=JdbcType.VARCHAR),
            @Result(column="num", property="num", jdbcType=JdbcType.INTEGER),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="first_price", property="firstPrice", jdbcType=JdbcType.DECIMAL),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
            @Result(column="time_num", property="timeNum", jdbcType=JdbcType.INTEGER),
            @Result(column="last_chang_time", property="lastChangTime", jdbcType=JdbcType.INTEGER),
            @Result(column="per_delay_time", property="perDelayTime", jdbcType=JdbcType.INTEGER),
            @Result(column="delay_times", property="delayTimes", jdbcType=JdbcType.INTEGER),
            @Result(column="add_delay_times", property="addDelayTimes", jdbcType=JdbcType.INTEGER),
            @Result(column="retain_price", property="retainPrice", jdbcType=JdbcType.DECIMAL)
    })
    List<GoodsDO> listGoods(@Param("param") JbxtGoodsParam goodsParam);

    @Delete({
            " <script>",
            " delete from t_jbxt_goods",
            " where  1=1 " ,
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType=VARCHAR}",
            " </if>",
            " </script>",
    })
    int delete(@Param("param") JbxtGoodsParam goodsParam);
}

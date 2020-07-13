package com.galaplat.comprehensive.bidding.dao.mappers;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.dao.params.JbxtGoodsParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtGoodsDOMapper {
    @Delete({
            "delete from t_jbxt_goods",
            "where goods_id = #{goodsId,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer goodsId);

    @Insert({
            "insert into t_jbxt_goods (goods_id, code, ",
            "name, num, activity_code, ",
            "created_time, updated_time, ",
            "updator, creator, ",
            "company_code, sys_code, ",
            "status, time_num)",
            "values (#{goodsId,jdbcType=INTEGER}, #{code,jdbcType=VARCHAR}, ",
            "#{name,jdbcType=VARCHAR}, #{num,jdbcType=INTEGER}, #{activityCode,jdbcType=VARCHAR}, ",
            "#{createdTime,jdbcType=TIMESTAMP}, #{updatedTime,jdbcType=TIMESTAMP}, ",
            "#{firstPrice,jdbcType=DECIMAL}, #{creator,jdbcType=VARCHAR}, ",
            "#{companyCode,jdbcType=VARCHAR}, #{sysCode,jdbcType=VARCHAR}, ",
            "#{status,jdbcType=VARCHAR}, #{timeNum,jdbcType=INTEGER})"
    })
    int insert(JbxtGoodsDO record);

    @InsertProvider(type=JbxtGoodsDOSqlProvider.class, method="insertSelective")
    int insertSelective(JbxtGoodsDO record);

    @Select({
            "select",
            "goods_id, code, name, num, activity_code, created_time, updated_time, first_price, ",
            "creator, updator, company_code, sys_code, status, time_num",
            "from t_jbxt_goods",
            "where goods_id = #{goodsId,jdbcType=INTEGER}"
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
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="status", property="status", jdbcType=JdbcType.VARCHAR),
            @Result(column="time_num", property="timeNum", jdbcType=JdbcType.INTEGER)
    })
    JbxtGoodsDO selectByPrimaryKey(Integer goodsId);


    @UpdateProvider(type=JbxtGoodsDOSqlProvider.class, method="updateByPrimaryKeySelective")
    int updateByPrimaryKeySelective(JbxtGoodsDO record);

    @Update({
            "update t_jbxt_goods",
            "set code = #{code,jdbcType=VARCHAR},",
            "name = #{name,jdbcType=VARCHAR},",
            "num = #{num,jdbcType=INTEGER},",
            "activity_code = #{activityCode,jdbcType=VARCHAR},",
            "created_time = #{createdTime,jdbcType=TIMESTAMP},",
            "updated_time = #{updatedTime,jdbcType=TIMESTAMP},",
            "first_price = #{firstPrice,jdbcType=DECIMAL},",
            "creator = #{creator,jdbcType=VARCHAR},",
            "updator = #{updator,jdbcType=VARCHAR},",
            "company_code = #{companyCode,jdbcType=VARCHAR},",
            "sys_code = #{sysCode,jdbcType=VARCHAR},",
            "status = #{status,jdbcType=VARCHAR},",
            "time_num = #{timeNum,jdbcType=INTEGER}",
            "where goods_id = #{goodsId,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(JbxtGoodsDO record);

    List<JbxtGoodsDVO> getJbxtGoodsList(JbxtGoodsParam jbxtgoodsParam);

    @Select({
            "select",
            "goods_id, code, name, num, activity_code, created_time, updated_time, first_price, ",
            "creator, company_code, sys_code, status, time_num",
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
            @Result(column="time_num", property="timeNum", jdbcType=JdbcType.INTEGER)
    })
    List<JbxtGoodsDVO> getListJbxtGoodsByActivityCode(String activityCode);



    @Select({
            "select",
            "goods_id, code, name, num, activity_code, created_time, updated_time, first_price, ",
            "creator, company_code, sys_code, status, time_num",
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
            @Result(column="time_num", property="timeNum", jdbcType=JdbcType.INTEGER)
    })
    public JbxtGoodsDO selectActiveGoods(String activityCode);


    @Insert({
            "<script>",
            "insert into t_jbxt_goods (goods_id, code, ",
            "name, num, activity_code, ",
            "created_time, updated_time, ",
            "updator, creator, ",
            "company_code, sys_code, ",
            "status, time_num)",
            " values ",
            " <foreach collection=\"list\" item=\"item\" separator=\",\"> ",
            " (#{item.goodsId,jdbcType=INTEGER}, #{item.code,jdbcType=VARCHAR}, ",
            "#{item.name,jdbcType=VARCHAR}, #{item.num,jdbcType=INTEGER}, #{item.activityCode,jdbcType=VARCHAR}, ",
            "#{item.createdTime,jdbcType=TIMESTAMP}, #{item.updatedTime,jdbcType=TIMESTAMP}, ",
            "#{item.firstPrice,jdbcType=DECIMAL}, #{item.creator,jdbcType=VARCHAR}, ",
            "#{item.companyCode,jdbcType=VARCHAR}, #{item.sysCode,jdbcType=VARCHAR}, ",
            "#{item.status,jdbcType=VARCHAR}, #{item.timeNum,jdbcType=INTEGER})",
            " </foreach> ON DUPLICATE KEY UPDATE " ,
            " code = values(code)," ,
            " name = values(name)," ,
            " num = values(num)," ,
            " activity_code = values(activity_code)," ,
            " updated_time = values(updated_time)," ,
            " updator = values(updator)," ,
            " status = values(status)," ,
            " time_num = values(time_num)" ,
            " </script>",
    })
    int batchInsertOrUpdate(@Param("list") List<JbxtGoodsParam> goodsParam);

    @Select({
            " <script>",
            " select",
            " goods_id, code, name, num, activity_code, created_time, updated_time, first_price, ",
            " creator, company_code, sys_code, status, time_num",
            " from t_jbxt_goods",
            " where status = 1 " ,
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType=VARCHAR}",
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
            @Result(column="time_num", property="timeNum", jdbcType=JdbcType.INTEGER)
    })
    List<JbxtGoodsDO> getGoods(@Param("param") JbxtGoodsParam goodsParam);
}
package com.galaplat.comprehensive.bidding.dao.mappers.custs;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtBiddingDO;
import com.galaplat.comprehensive.bidding.dao.dvos.BidDVO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtBiddingDVO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtBiddingDOSqlProvider;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtBiddingMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtBiddingParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface JbxtBiddingCustMapper extends JbxtBiddingMapper {
    //最低竞价表操作
    @Delete({
            "delete from t_jbxt_minbid",
            "where code = #{code,jdbcType=VARCHAR}"
    })
    int deleteMinbidTableByPrimaryKey(String code);

    @Delete({
            "delete from t_jbxt_minbid",
            "where activity_code = #{activityCode,jdbcType=VARCHAR}  AND goods_id = #{goodsId,jdbcType=INTEGER}"
    })
    int deleteMinbidTableByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

    @InsertProvider(type= JbxtBiddingDOSqlProvider.class, method="insertMinBidTableSelective")
    int insertMinBidTableSelective(JbxtBiddingDO record);


    //获取当前用户最小竞价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_minbid",
            "where activity_code =#{activityCode,jdbcType=VARCHAR} AND  goods_id=#{goodsId,jdbcType=INTEGER} AND user_code=#{userCode,jdbcType=VARCHAR}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDO selectMinBidTableByOne(String userCode, Integer goodsId, String activityCode);

    //获取当前竞品所有用户最小竞价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_minbid",
            "where activity_code =#{activityCode,jdbcType=VARCHAR} AND goods_id=#{goodsId,jdbcType=INTEGER} ORDER BY bid ASC"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> selectMinBidTableByList(Integer goodsId, String activityCode);


    @UpdateProvider(type=JbxtBiddingDOSqlProvider.class, method="updateMinBidTableByPrimaryKeySelective")
    int updateMinBidTableByPrimaryKeySelective(JbxtBiddingDO record);

    //end of  最低竞价表操作 code

    @Delete({
            "delete from t_jbxt_bidding",
            "where activity_code =#{activityCode,jdbcType=VARCHAR} AND goods_id=#{goodsId,jdbcType=INTEGER}"
    })
    int deleteByGoodsIdAndActivityCode(Integer goodsId, String activityCode);

    List<JbxtBiddingDVO> getJbxtBiddingList(JbxtBiddingParam jbxtbiddingParam);


    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_bidding",
            "where goods_id = #{goodsId,jdbcType=INTEGER} ORDER BY bid ASC LIMIT 0,1000"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> getJbxtListBiddingByGoodsId( Integer goodsId);




    //获取当前用户最小竞价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_bidding",
            "where activity_code =#{activityCode,jdbcType=VARCHAR} AND goods_id=#{goodsId,jdbcType=INTEGER} AND user_code=#{userCode,jdbcType=VARCHAR} ORDER BY bid ASC LIMIT 0,1"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDVO getUserMinBid(String userCode, Integer goodsId, String activityCode);


    //获取当前竞品的最小提交价
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_bidding",
            "where activity_code =#{activityCode,jdbcType=VARCHAR} AND  goods_id=#{goodsId,jdbcType=INTEGER} AND user_code=#{userCode,jdbcType=VARCHAR} ORDER BY bid ASC LIMIT 0,1"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    JbxtBiddingDVO gerCurrentGoodsMinSubmitPrice(String userCode, Integer goodsId, String activityCode);



    //获取所有用户信息
    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_bidding",
            "where activity_code =#{activityCode,jdbcType=VARCHAR} AND goods_id=#{goodsId,jdbcType=INTEGER}"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> getAllBidUserInfo( Integer goodsId, String activityCode);



    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_bidding",
            "where activity_code = #{activityCode,jdbcType=VARCHAR} AND user_code = #{userCode,jdbcType=VARCHAR} ",
            "ORDER BY bid ASC"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> findAllByUserCodeAndActivityCode(String userCode, String activityCode);


    @Select({
            "select",
            "code, goods_id, user_code, activity_code, bid, created_time, updated_time, updator, ",
            "creator, company_code, sys_code, bid_time",
            "from t_jbxt_bidding",
            "where activity_code =#{activityCode,jdbcType=VARCHAR} AND  goods_id=#{goodsId,jdbcType=INTEGER} AND user_code=#{userCode,jdbcType=VARCHAR} ",
            "ORDER BY bid ASC"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updated_time", property="updatedTime", jdbcType=JdbcType.TIMESTAMP),
            @Result(column="updator", property="updator", jdbcType=JdbcType.VARCHAR),
            @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
            @Result(column="company_code", property="companyCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="sys_code", property="sysCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid_time", property="bidTime", jdbcType=JdbcType.VARCHAR)
    })
    List<JbxtBiddingDVO> findAllByUserCodeAndGooodsIdAndActivityCode(String userCode, Integer goodsId, String activityCode);


    @Select({
            "<script>",
            " SELECT DISTINCT(user_code) FROM `t_jbxt_bidding` " ,
            " WHERE 1=1 ",
            " <if test='param.goodsId != null' > " ,
            " and goods_id = #{param.goodsId,jdbcType=INTEGER}",
            " </if>",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode, jdbcType=INTEGER}",
            " </if>",
            "</script>"
    })
    List<String> listBidActivityUsers(@Param("param") JbxtBiddingParam biddingParam);


    @Select({
            "<script>",
            " SELECT code,goods_id,user_code,activity_code,bid,created_time FROM t_jbxt_bidding " ,
            " WHERE 1=1 ",
            " <if test='param.goodsId != null' > " ,
            " and goods_id = #{param.goodsId,jdbcType=INTEGER}",
            " </if>",
            " <if test='param.userCode != null' > " ,
            " and user_code = #{param.userCode,jdbcType = VARCHAR}",
            " </if>",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType = VARCHAR}",
            " </if>",
            " ORDER BY bid ASC LIMIT 0,1",
            "</script>"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
    })
    BidDVO getBidActivity(@Param("param")JbxtBiddingParam biddingParam);


    @Select({
            "<script>",
            " SELECT code,goods_id,user_code,activity_code,bid,created_time FROM t_jbxt_bidding " ,
            " WHERE 1=1 ",
            " <if test='param.goodsId != null' > " ,
            " and goods_id = #{param.goodsId,jdbcType=INTEGER}",
            " </if>",
            " <if test='param.userCode != null' > " ,
            " and user_code = #{param.userCode,jdbcType = VARCHAR}",
            " </if>",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType = VARCHAR}",
            " </if>",
            "  ORDER BY created_time ASC",
            "</script>"
    })
    @Results({
            @Result(column="code", property="code", jdbcType=JdbcType.VARCHAR, id=true),
            @Result(column="goods_id", property="goodsId", jdbcType=JdbcType.INTEGER),
            @Result(column="user_code", property="userCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="activity_code", property="activityCode", jdbcType=JdbcType.VARCHAR),
            @Result(column="bid", property="bid", jdbcType=JdbcType.DECIMAL),
            @Result(column="created_time", property="createdTime", jdbcType=JdbcType.TIMESTAMP),
    })
    List<BidDVO> getOneSupplierBidPriceDeatil(@Param("param")JbxtBiddingParam biddingParam);

    @Delete({
            "<script>",
            "delete from t_jbxt_bidding",
            "where 1=1 ",
            " <if test='param.activityCode != null' > " ,
            " and activity_code = #{param.activityCode,jdbcType = VARCHAR}",
            " </if>",
            "<if test='param.userCodeList !=null '>",
            "  and user_code in " ,
            " <foreach item='item' index='index' collection='param.userCodeList' open='(' separator=',' close=')'> ",
            "  #{item} ",
            " </foreach>",
            "</if>",
            "</script>"
    })
    int  deleteBidding(@Param("param")JbxtBiddingParam biddingParam);

}

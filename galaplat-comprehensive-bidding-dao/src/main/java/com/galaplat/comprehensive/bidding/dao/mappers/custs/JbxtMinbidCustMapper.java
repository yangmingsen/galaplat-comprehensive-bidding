package com.galaplat.comprehensive.bidding.dao.mappers.custs;

import com.galaplat.comprehensive.bidding.dao.dos.JbxtMinbidDO;
import com.galaplat.comprehensive.bidding.dao.mappers.JbxtMinbidMapper;
import com.galaplat.comprehensive.bidding.dao.params.JbxtMinbidParam;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

public interface JbxtMinbidCustMapper extends JbxtMinbidMapper {

    @Delete({
            "<script>",
            "delete from t_jbxt_minbid",
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
    int deleteBidding(@Param("param") JbxtMinbidParam minbidParam);
}

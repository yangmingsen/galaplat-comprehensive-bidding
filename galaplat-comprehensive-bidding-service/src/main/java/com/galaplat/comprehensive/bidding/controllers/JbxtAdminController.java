package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.comprehensive.bidding.dao.IJbxtGoodsDao;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.MyResult;
import io.swagger.models.auth.In;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jbxt/admin")
public class JbxtAdminController {

    @Autowired
    IJbxtGoodsService jbxtgoodsService;

    Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsController.class);

    @RequestMapping("/goods/findAll")
    @RestfulResult
    public Object findAll(String activityCode) {
        LOGGER.info("JbxtAdminController(findAll): activityCode=" + activityCode);

        if (activityCode != null && (!activityCode.equals(""))) {
            return new MyResult(true, "获取data成功", jbxtgoodsService.findAll(activityCode));
        } else {
            return new MyResult(false, "出错: activityCode不能为空哦(*￣︶￣)", null);
        }
    }


    @Autowired
    private IJbxtGoodsService iJbxtGoodsService;


    private Object handlerTheExistActiveGoods(List<JbxtGoodsDVO> jgbacs) {
        Integer oldGoodsId = -1;
        Integer newGoodsId = -1;

        for (int i = 0; i < jgbacs.size(); i++) {
            JbxtGoodsDVO tgd = jgbacs.get(i);
            if ("1".equals(tgd.getStatus())) {
                oldGoodsId = tgd.getGoodsId();
            }

            if ("0".equals(tgd.getStatus())) {
                newGoodsId = tgd.getGoodsId();
                break;
            }
        }
        LOGGER.info("JbxtAdminController(next): oldGoodsId=" + oldGoodsId + " newGoodsId=" + newGoodsId);

        try {
            if (oldGoodsId.intValue() != -1) {
                JbxtGoodsVO tj = new JbxtGoodsVO();
                tj.setGoodsId(oldGoodsId);
                tj.setStatus("2");
                jbxtgoodsService.updateJbxtGoods(tj);
            }

            if (newGoodsId.intValue() != -1) {
                JbxtGoodsVO tj = new JbxtGoodsVO();
                tj.setGoodsId(newGoodsId);
                tj.setStatus("1");
                jbxtgoodsService.updateJbxtGoods(tj);
            }

            Map<String, String> map = new HashMap<>();
            map.put("goodsId", newGoodsId.toString());

            return new MyResult(true, "切换成功", map);
        } catch (Exception e) {
            return new MyResult(false, "切换失败", null);
        }
    }

    private Object handlerTheNotExistActiveGoods(List<JbxtGoodsDVO> jgbacs) {
        JbxtGoodsDVO tjgd = jgbacs.get(0);
        String status = tjgd.getStatus();

        if (status.equals("0")) {
            JbxtGoodsVO tj = new JbxtGoodsVO();
            tj.setGoodsId(tjgd.getGoodsId());
            tj.setStatus("1");

            try {
                jbxtgoodsService.updateJbxtGoods(tj);

                Map<String, String> map = new HashMap<>();
                map.put("goodsId", tjgd.getGoodsId().toString());

                return new MyResult(true, "切换成功", map);
            } catch (Exception e) {
                return new MyResult(false, "切换失败", null);
            }

        } else if (status.equals("2")) {
            Map<String, String> map = new HashMap<>();
            map.put("goodsId", "-1");

            return new MyResult(true, "所有竞品已结束", map);
        } else {
            return new MyResult(false, "status异常：status="+status, null);
        }
    }


    @RequestMapping("/goods/next")
    @RestfulResult
    @Transactional(rollbackFor = Exception.class)
    public Object next(String activityCode) {
        //业务处理逻辑:
        // 1. 获取当前正在进行的竞品
        // 2.如果1.返回的结果存在 那么切换下一个
            //2.1. 获取所有竞品数据 => A
            //2.2. 获取old_goods_id 和 new_goods_id
            //2.3. 将old_goods_id的竞品状态置3. 将new_goods_id的竞品状态置1
            //2.4. 返回 新的goods_id
        // 3.如果1.返回的结果不存在
            // 3.1 查询根据活动code查询所有竞品 => list
            // 3.2 如果 list(0)的status为0则表示竞品都还没有开始 置数据库第一个竞品为当前进行竞价竞品 返回它的id
            // 3.3 如果 list(0)的status为2则表示竞品都已经结束  返回goodsId为-1
        if (activityCode == null || activityCode.equals("")) {
            return new MyResult(false,"错误: activityCode不能为空哦(*￣︶￣)", null);
        }

        JbxtGoodsDO jbxtGoodsDO = iJbxtGoodsService.selectActiveGoods(activityCode); //get 正在进行goods
        List<JbxtGoodsDVO> jgbacs = jbxtgoodsService.getListJbxtGoodsByActivityCode(activityCode); //get all goods by activityCode

        if (jbxtGoodsDO != null) {
            return handlerTheExistActiveGoods(jgbacs);
        } else {
            return handlerTheNotExistActiveGoods(jgbacs);
        }
    }

}

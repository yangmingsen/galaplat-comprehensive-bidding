package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.base.core.common.exception.BaseException;
import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.baseplatform.permissions.controllers.BaseController;
import com.galaplat.baseplatform.permissions.vos.UserVO;
import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.CurrentActivity;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
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
public class JbxtAdminController extends BaseController {

    @Autowired
    IJbxtGoodsService jbxtgoodsService;

    Logger LOGGER = LoggerFactory.getLogger(JbxtGoodsController.class);

   @Autowired
    private ActivityMap activityMap;

    @Autowired
    private IJbxtGoodsService iJbxtGoodsService;

    @Autowired
    private PushQueue pushQueue;


    @RequestMapping("/activity/goodsStatus")
    @RestfulResult
    public Object updateCurrentBidActivityStatus(String activityCode, Integer status) {

        try {
            CurrentActivity currentActivity = activityMap.get(activityCode);
            currentActivity.setStatus(status);

            return new MyResult(true, "更新成功");

        } catch (Exception e) {
            e.printStackTrace();
            return new MyResult(false, "更新faild");
        }

    }


    @RequestMapping("/goods/findAll")
    @RestfulResult
    public Object findAll(String activityCode) {
        if (activityCode != null && (!activityCode.equals(""))) {
            return new MyResult(true, "获取data成功", jbxtgoodsService.findAll(activityCode));
        } else {
            return new MyResult(false, "出错: activityCode不能为空哦(*￣︶￣)", null);
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
            return handlerTheExistActiveGoods(jgbacs, activityCode);
        } else {
            return handlerTheNotExistActiveGoods(jgbacs, activityCode);
        }
    }

    private void notify200Event( String activityCode, Integer goodsId) {
        Map<String, String> map200 = new HashMap();

        map200.put("activityCode", activityCode);
        map200.put("goodsId", goodsId.toString());
        pushQueue.offer(new QueueMessage(200,map200));
    }

    private Object handlerTheExistActiveGoods(List<JbxtGoodsDVO> jgbacs, String activityCode) {
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

        try {

            JbxtGoodsDO goods1 = jbxtgoodsService.selectByGoodsId(newGoodsId);
            boolean isStart = startActivity(activityCode, goods1.getGoodsId().toString(),goods1.getTimeNum());
            if (isStart) {
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

                notify200Event(activityCode, newGoodsId); //通知供应商端更新


                Map<String, String> map = new HashMap<>();
                map.put("goodsId", newGoodsId.toString());

                return new MyResult(true, "切换成功", map);
            } else {
                return new MyResult(false, "切换失败", null);
            }

        } catch (Exception e) {
            return new MyResult(false, "切换失败", null);
        }
    }

    private void closeLastActivity(String activityCode) {
        CurrentActivity currentActivity = activityMap.get(activityCode);
        if (currentActivity != null) { //停止上一个goods的活动
            currentActivity.setStatus(1);
            currentActivity.setRemainingTime(0);
        }
    }

    private boolean startActivity(String activityCode, String goodsId, int initTime) {
        try {
            closeLastActivity(activityCode);

            CurrentActivity ca1 = new CurrentActivity(activityCode, goodsId, initTime * 60, 1);
            activityMap.put(activityCode, ca1);
            ca1.start();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    private Object handlerTheNotExistActiveGoods(List<JbxtGoodsDVO> jgbacs, String activityCode) {
        JbxtGoodsDVO tjgd = jgbacs.get(0);
        String status = tjgd.getStatus();

        if (status.equals("0")) {
            JbxtGoodsVO tj = new JbxtGoodsVO();
            tj.setGoodsId(tjgd.getGoodsId());
            tj.setStatus("1");

            try {
                JbxtGoodsDO goods1 = jbxtgoodsService.selectByGoodsId(tj.getGoodsId());
                boolean isStart = startActivity(activityCode, goods1.getGoodsId().toString(),goods1.getTimeNum());
                if (isStart) {
                    jbxtgoodsService.updateJbxtGoods(tj);

                    notify200Event(activityCode, tjgd.getGoodsId()); //通知客户端切换

                    Map<String, String> map = new HashMap<>();
                    map.put("goodsId", tjgd.getGoodsId().toString());
                    return new MyResult(true, "切换成功", map);
                } else {
                    return new MyResult(false, "切换失败", null);
                }
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

}

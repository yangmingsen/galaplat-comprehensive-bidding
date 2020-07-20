package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.baseplatform.permissions.controllers.BaseController;
import com.galaplat.comprehensive.bidding.activity.ActivityMap;
import com.galaplat.comprehensive.bidding.activity.ActivityThread;
import com.galaplat.comprehensive.bidding.activity.queue.PushQueue;
import com.galaplat.comprehensive.bidding.activity.queue.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
import com.galaplat.comprehensive.bidding.service.IJbxtBiddingService;
import com.galaplat.comprehensive.bidding.service.IJbxtGoodsService;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.MyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    Logger LOGGER = LoggerFactory.getLogger(JbxtAdminController.class);

    @Autowired
    private ActivityMap activityMap;

    @Autowired
    private IJbxtGoodsService iJbxtGoodsService;

    @Autowired
    private IJbxtActivityService iJbxtActivityService;

    @Autowired
    private IJbxtBiddingService iJbxtBiddingService;

    @Autowired
    private PushQueue pushQueue;


    private boolean resetBidData(String activityCode, Integer goodsId) {
        boolean delOk = true;
        try {
            iJbxtBiddingService.deleteByGoodsIdAndActivityCode(goodsId, activityCode);
            iJbxtBiddingService.deleteMinbidTableByGoodsIdAndActivityCode(goodsId, activityCode);
        } catch (Exception e) {
            delOk = false;
            LOGGER.info("resetBidData(ERROR): "+e.getMessage());
        }

        return delOk;
    }

    private MyResult handlerTheAcitvityThreadExistCondition(String activityCode, Integer goodsId, Integer status, ActivityThread activityThread) {
        if (status == 3) { //处理重置问题
            final int remainingTime = activityThread.getRemainingTime();
            //删除历史竞价数据
            final boolean delOk = this.resetBidData(activityCode, goodsId);
            if (delOk) {
                if (remainingTime < 0) {
                    final String gid = goodsId.toString();
                    final int initTime = activityThread.getInitTime();
                    final boolean startOk = this.startActivityThread(activityCode, gid, initTime);

                    if (!startOk) {
                        String info = "handlerTheAcitvityThreadExistCondition(msg): 更新失败: 启动活动线程失败!";
                        LOGGER.info(info);
                        return new MyResult(false, info);
                    }
                }
            } else {
                String info = "handlerTheAcitvityThreadExistCondition(msg): 更新失败: 历史数据删除失败!";
                LOGGER.info(info);
                return new MyResult(false, info);
            }
        }
        activityThread.setStatus(status);
        return new MyResult(true, "更新成功");
    }

    private MyResult handlerTheAcitvityThreadNotExistCondition(String activityCode, Integer goodsId) {
        final JbxtActivityDO activity = iJbxtActivityService.findOneByCode(activityCode);
        final JbxtGoodsDO goods = iJbxtGoodsService.selectByGoodsId(goodsId);
        if (activity != null && goods != null) {
            Integer curActivityStatus = activity.getStatus();
            if (curActivityStatus == 3) { //如果为进行状态
                this.startActivityThread(activityCode, goodsId.toString(), goods.getTimeNum());
            }
            return new MyResult(true, "更新成功");
        } else {
            LOGGER.info("updateCurrentBidActivityStatus(msg): 更新失败 activity 或 goods为 无数据");
            return new MyResult(false, "更新失败 activity 或 goods为 无数据");
        }
    }

    @RequestMapping("/activity/goodsStatus")
    @RestfulResult
    public Object updateCurrentActivityStatus(String activityCode, Integer goodsId,  Integer status) {
        if (activityCode == null || "".equals(activityCode)) return new MyResult(false, "activityCode不能为空");
        if (status == null) return new MyResult(false, "status不能为null");

        final ActivityThread currentActivity = activityMap.get(activityCode);

        MyResult myResult = null;
        if (currentActivity != null) {
            myResult = this.handlerTheAcitvityThreadExistCondition(activityCode, goodsId, status, currentActivity);
        } else {
            myResult = this.handlerTheAcitvityThreadNotExistCondition(activityCode, goodsId);
        }
        return myResult;
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
    public Object next(String activityCode) {
        MyResult checkNextRes = checkNextReq(activityCode);
        if (!checkNextRes.isSuccess()) return checkNextRes;

        JbxtGoodsDO jbxtGoodsDO = iJbxtGoodsService.selectActiveGoods(activityCode); //get 正在进行goods
        if (jbxtGoodsDO != null) {
            JbxtGoodsVO tj = new JbxtGoodsVO();
            tj.setGoodsId(jbxtGoodsDO.getGoodsId());
            tj.setStatus("2");
            try {
                jbxtgoodsService.updateJbxtGoods(tj);
            } catch (Exception e) {
                String info = "next(ERROR): 切换活动: " + activityCode + " goodsId: " + jbxtGoodsDO.getGoodsId() + " 状态为2失败. info=" + e.getMessage();
                LOGGER.info(info);
                return new MyResult(false, info);
            }
        }
        return switchActivityGoods(activityCode);
    }

    private MyResult checkNextReq(String activityCode) {
        if (activityCode == null || activityCode.equals("")) {
            return new MyResult(false, "错误: activityCode不能为空哦(*￣︶￣)", null);
        }

        JbxtActivityDO activityEntity = iJbxtActivityService.findOneByCode(activityCode);
        if (activityEntity == null) {
            String errorInfo = "错误: 当前活动" + activityCode + "不存在";
            LOGGER.info("next(msg): " + errorInfo);
            return new MyResult(false, errorInfo);
        }

        return new MyResult(true, "");
    }

    private Object switchActivityGoods(String activityCode) {
        List<JbxtGoodsDVO> goodsList = jbxtgoodsService.getListJbxtGoodsByActivityCode(activityCode); //get all goods by activityCode
        for (int i = 0; i < goodsList.size(); i++) {
            JbxtGoodsDVO jbxtGoodsDVO1 = goodsList.get(i);
            if ("0".equals(jbxtGoodsDVO1.getStatus())) {
                //更新当前竞品状态为 1
                JbxtGoodsVO newGoodsStatus = new JbxtGoodsVO();
                newGoodsStatus.setGoodsId(jbxtGoodsDVO1.getGoodsId());
                newGoodsStatus.setStatus("1");
                boolean switchOk = false;
                try {
                    if (i == 0) {
                        //更新竞品单 当前竞品单状态为进行中
                        JbxtActivityDO tActivity = new JbxtActivityDO();
                        tActivity.setCode(activityCode);
                        tActivity.setStatus(3);

                        iJbxtActivityService.updateByPrimaryKeySelective(tActivity);
                    }
                    iJbxtGoodsService.updateJbxtGoods(newGoodsStatus);
                    switchOk = true;
                } catch (Exception e) {
                    LOGGER.info("next(ERROR): 更新状态为开始失败");
                }

                if (switchOk) {
                    startActivityThread(activityCode, jbxtGoodsDVO1.getGoodsId().toString(), jbxtGoodsDVO1.getTimeNum());
                    notify214Event(activityCode, jbxtGoodsDVO1.getGoodsId());

                    return new MyResult(true, "切换成功");
                } else {
                    return new MyResult(false, "错误: 切换失败");
                }
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("goodsId", "-1");
        JbxtActivityDO tActivity = new JbxtActivityDO();
        tActivity.setCode(activityCode);
        tActivity.setStatus(4);
        boolean updateActivityStatus = false;
        try {
            iJbxtActivityService.updateByPrimaryKeySelective(tActivity);
            updateActivityStatus = true;
        } catch (Exception e) {
            LOGGER.info("switchActivityGoods(ERROR): 更新活动("+activityCode+")结束结束失败");
        }

        if (updateActivityStatus) {
            closeLastActivityThread(activityCode);
            //通知所有供应商端 退出登录
            notify216Event(activityCode);
        }

        return new MyResult(false, "所有竞品已结束", map);
    }

    private void notify216Event(String activityCode) {
        Map<String, String> map216 = new HashMap();
        map216.put("activityCode", activityCode);
        pushQueue.offer(new QueueMessage(216, map216));
    }

    /***
     * 通知所有供应商端 更新下一个竞品活动
     * @param activityCode
     * @param goodsId
     */
    private void notify214Event(String activityCode, Integer goodsId) {
        Map<String, String> map214 = new HashMap();

        map214.put("activityCode", activityCode);
        map214.put("goodsId", goodsId.toString());
        pushQueue.offer(new QueueMessage(214, map214));
    }

    private void closeLastActivityThread(String activityCode) {
        ActivityThread lastActivityThread = activityMap.get(activityCode);
        if (lastActivityThread != null) { //停止上一个goods的活动
            lastActivityThread.setStatus(1);
            lastActivityThread.setRemainingTime(0);
            LOGGER.info("closeLastActivity(msg): 活动" + activityCode + " 商品(" + lastActivityThread.getCurrentGoodsId() + ")结束");
        }
    }

    /**
     * 启动 一个活动线程
     * @param activityCode
     * @param goodsId
     * @param initTime
     * @return
     */
    private boolean startActivityThread(String activityCode, String goodsId, int initTime) {
        boolean startOK = true;
        try {
            closeLastActivityThread(activityCode);
            ActivityThread newActivityThread = new ActivityThread(activityCode, goodsId, initTime * 60, 1);
            activityMap.put(activityCode, newActivityThread);
            newActivityThread.start();

            LOGGER.info("startActivity(msg): 启动" + activityCode + " 商品(" + newActivityThread.getCurrentGoodsId() + ")活动成功");
        } catch (Exception e) {
            LOGGER.info("startActivity(ERROR): 启动" + activityCode + " 活动失败" + e.getMessage());
            startOK = false;
        }

        return startOK;
    }
}

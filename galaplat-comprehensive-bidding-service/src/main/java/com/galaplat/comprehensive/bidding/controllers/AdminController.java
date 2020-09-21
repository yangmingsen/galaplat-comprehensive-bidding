package com.galaplat.comprehensive.bidding.controllers;

import com.galaplat.base.core.springboot.annotations.RestfulResult;
import com.galaplat.baseplatform.permissions.controllers.BaseController;
import com.galaplat.comprehensive.bidding.activity.ActivityTask;
import com.galaplat.comprehensive.bidding.activity.ActivityThreadManager;
import com.galaplat.comprehensive.bidding.activity.queue.MessageQueue;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.dao.dos.ActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.GoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.GoodsDVO;
import com.galaplat.comprehensive.bidding.service.ActivityService;
import com.galaplat.comprehensive.bidding.service.BiddingService;
import com.galaplat.comprehensive.bidding.service.GoodsService;
import com.galaplat.comprehensive.bidding.vos.JbxtGoodsVO;
import com.galaplat.comprehensive.bidding.vos.pojo.MyResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/jbxt/admin")
public class AdminController extends BaseController {

    Logger LOGGER = LoggerFactory.getLogger(AdminController.class);

    @Autowired
    private ActivityThreadManager activityThreadManager;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private BiddingService biddingService;

    @Autowired
    private MessageQueue messageQueue;

    private Lock lock = new ReentrantLock();

    @RequestMapping("/activity/goodsStatus")
    @RestfulResult
    public Object updateCurrentActivityStatus(String activityCode, Integer goodsId,  Integer status) {
        if (activityCode == null || "".equals(activityCode)) return new MyResult(false, "activityCode不能为空");
        if (status == null) return new MyResult(false, "status不能为null");

        final ActivityTask currentActivity = activityThreadManager.get(activityCode);
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
            return new MyResult(true, "获取data成功", goodsService.findAll(activityCode));
        } else {
            return new MyResult(false, "出错: activityCode不能为空哦(*￣︶￣)");
        }
    }


    @RequestMapping("/goods/next")
    @RestfulResult
    public Object next(String activityCode) {
        final MyResult checkNextRes = checkNextReq(activityCode);
        if (!checkNextRes.isSuccess()) return checkNextRes;

        final GoodsDO jbxtGoodsDO = goodsService.selectActiveGoods(activityCode); //get 正在进行goods
        if (jbxtGoodsDO != null) {
            final JbxtGoodsVO tj = new JbxtGoodsVO();
            tj.setGoodsId(jbxtGoodsDO.getGoodsId());
            tj.setStatus("2");
            try {
                goodsService.updateJbxtGoods(tj);
            } catch (Exception e) {
                final String info = "next(ERROR): 切换活动: " + activityCode + " goodsId: " + jbxtGoodsDO.getGoodsId() + " 状态为2失败. info=" + e.getMessage();
                LOGGER.info(info);
                return new MyResult(false, info);
            }
            LOGGER.info("next(msg): 修改goodsId("+tj.getGoodsId()+")为2");
        }
        return switchActivityGoods(activityCode);
    }


    /**
     * 删除竞品历史竞价数据
     * @param activityCode
     * @param goodsId
     * @return
     */
    private boolean resetBidData(String activityCode, Integer goodsId) {
        boolean delOk = true;
        try {
            biddingService.deleteByGoodsIdAndActivityCode(goodsId, activityCode);
            biddingService.deleteMinbidTableByGoodsIdAndActivityCode(goodsId, activityCode);
        } catch (Exception e) {
            delOk = false;
            LOGGER.info("resetBidData(ERROR): "+e.getMessage());
        }

        return delOk;
    }

    /**
     * 处理存在活动线程情况
     * @param activityCode
     * @param goodsId
     * @param status
     * @param activityTask 当前活动线程
     * @return
     */
    private MyResult handlerTheAcitvityThreadExistCondition(String activityCode, Integer goodsId, Integer status, ActivityTask activityTask) {
        MyResult result = new MyResult(true,"更新成功");
        if (status == 3) { //处理重置问题
            Lock lock = this.lock;
            lock.lock();
            try {
                final int remainingTime = activityTask.getRemainingTime();
                activityTask.setStatus(status);
                if (remainingTime < 0) {
                    String gid = goodsId.toString();
                    int initTime = activityTask.getInitTime() / 60;
                    int delayedCondition = activityTask.getDelayedCondition();
                    int allowDelayedLength = activityTask.getAllowDelayedLength();
                    int allowDelayedTime = activityTask.getInitAllowDelayedTime();
                    int supplierNum = activityTask.getSupplierNum();
                    int bidType = activityTask.getBidType();
                    boolean startOk = this.startActivityTask(activityCode, gid, initTime,delayedCondition,
                            allowDelayedLength,allowDelayedTime, supplierNum,
                            bidType);

                    if (!startOk) {
                        String info = "handlerTheAcitvityThreadExistCondition(msg): 更新失败: 启动活动线程失败!";
                        LOGGER.info(info);
                        result.setInfo(false, info);
                    }
                }
            } catch (Exception e) {
                LOGGER.info("handlerTheAcitvityThreadExistCondition(ERROR): "+e.getMessage());
                result.setInfo(false, "更新失败："+e.getMessage());
            } finally {
                lock.unlock();
            }
        }

        return result;
    }

    /**
     * 处理不存在活动线程情况
     * @param activityCode
     * @param goodsId
     * @return
     */
    private MyResult handlerTheAcitvityThreadNotExistCondition(String activityCode, Integer goodsId) {
        final ActivityDO activity = activityService.findOneByCode(activityCode);
        final GoodsDO goods = goodsService.selectByGoodsId(goodsId);
        if (activity != null && goods != null) {
            final Integer curActivityStatus = activity.getStatus();
            if (curActivityStatus == 3) { //如果为进行状态
                final int delayedCondition = goods.getLastChangTime();
                final int allowDelayedLength = goods.getPerDelayTime();
                final int allowDelayedTime = goods.getDelayTimes();
                final int supplierNum = activity.getSupplierNum();
                final int bidType = activity.getBidingType();

                this.startActivityTask(activityCode, goodsId.toString(), goods.getTimeNum(),
                        delayedCondition,
                        allowDelayedLength,allowDelayedTime, supplierNum,
                        bidType);
            }
            return new MyResult(true, "更新成功");
        } else {
            LOGGER.info("updateCurrentBidActivityStatus(msg): 更新失败 activity 或 goods为 无数据");
            return new MyResult(false, "更新失败 activity 或 goods为 无数据");
        }
    }


    private MyResult checkNextReq(String activityCode) {
        if (activityCode == null || activityCode.equals("")) {
            return new MyResult(false, "错误: activityCode不能为空哦(*￣︶￣)", null);
        }

        final ActivityDO activityEntity = activityService.findOneByCode(activityCode);
        if (activityEntity == null) {
            final String errorInfo = "错误: 当前活动" + activityCode + "不存在";
            LOGGER.info("next(msg): " + errorInfo);
            return new MyResult(false, errorInfo);
        }

        return new MyResult(true, "");
    }

    private Object switchActivityGoods(String activityCode) {
        final List<GoodsDVO> goodsList = goodsService.getListJbxtGoodsByActivityCode(activityCode); //get all goods by activityCode
        for (int i = 0; i < goodsList.size(); i++) {
            final GoodsDVO currentGoods = goodsList.get(i);
            if ("0".equals(currentGoods.getStatus())) {
                //更新当前竞品状态为 1
                final JbxtGoodsVO newGoodsStatus = new JbxtGoodsVO();
                newGoodsStatus.setGoodsId(currentGoods.getGoodsId());
                newGoodsStatus.setStatus("1");
                boolean switchOk = false;
                try {
                    if (i == 0) {
                        //更新竞品单 当前竞品单状态为进行中
                        final ActivityDO tActivity = new ActivityDO();
                        tActivity.setCode(activityCode);
                        tActivity.setStatus(3);
                        tActivity.setPracticalBidTime(new Date());

                        activityService.updateByPrimaryKeySelective(tActivity);
                    }
                    goodsService.updateJbxtGoods(newGoodsStatus);
                    LOGGER.info("switchActivityGoods(msg): 切换下一竞品goodsId("+newGoodsStatus.getGoodsId()+")为1");
                    switchOk = true;
                } catch (Exception e) {
                    LOGGER.info("next(ERROR): 更新状态为开始失败");
                }

                if (switchOk) {
                    final int delayedCondition = currentGoods.getLastChangTime();
                    final int allowDelayedLength = currentGoods.getPerDelayTime();
                    final int allowDelayedTime = currentGoods.getDelayTimes();
                    ActivityDO activityInfoDO = activityService.findOneByCode(activityCode);
                    final int supplierNum = activityInfoDO.getSupplierNum();
                    final int bidType = activityInfoDO.getBidingType();

                    startActivityTask(activityCode, currentGoods.getGoodsId().toString(),
                            currentGoods.getTimeNum(), delayedCondition,
                            allowDelayedLength,allowDelayedTime, supplierNum, bidType);

                    notify214Event(activityCode, currentGoods.getGoodsId());

                    return new MyResult(true, "切换成功");
                } else {
                    return new MyResult(false, "错误: 切换失败");
                }
            }
        }

        //处理手动切换最后一个的情况
        final ActivityDO activity = activityService.findOneByCode(activityCode);
        if (activity != null) {
            if (activity.getStatus() == 3) {
                final ActivityDO tActivity = new ActivityDO();
                tActivity.setCode(activityCode);
                tActivity.setStatus(4);
                boolean updateActivityStatus = false;

                try {
                    activityService.updateByPrimaryKeySelective(tActivity);
                    updateActivityStatus = true;
                } catch (Exception e) {
                    LOGGER.info("switchActivityGoods(ERROR): 更新活动("+activityCode+")结束结束失败");
                }

                if (updateActivityStatus) {
                    closeLastActivityThread(activityCode);
                    //通知所有供应商端 退出登录
                    notify216Event(activityCode);
                }
            }
        }

        final Map<String, String> map = new HashMap<>();
        map.put("goodsId", "-1");
        return new MyResult(false, "所有竞品已结束", map);
    }

    /**
     * 消息事件：通知所有供应商端 退出登录
     * @param activityCode
     */
    private void notify216Event(String activityCode) {
        final Map<String, String> map216 = new HashMap();
        map216.put("activityCode", activityCode);
        messageQueue.offer(new QueueMessage(216, map216));
    }

    /**
     * 通知所有供应商端 更新下一个竞品活动
     * @param activityCode
     * @param goodsId
     */
    private void notify214Event(String activityCode, Integer goodsId) {
        final Map<String, String> map214 = new HashMap();

        map214.put("activityCode", activityCode);
        map214.put("goodsId", goodsId.toString());
        messageQueue.offer(new QueueMessage(214, map214));
    }

    /**
     * 关闭上一个活动线程
     * @param activityCode
     */
    private void closeLastActivityThread(String activityCode) {
        final ActivityTask lastActivityThread = activityThreadManager.get(activityCode);
        if (lastActivityThread != null) { //停止上一个goods的活动
            lastActivityThread.setStatus(1);
            lastActivityThread.setRemainingTime(0);
            LOGGER.info("closeLastActivity(msg): 活动" + activityCode + " 商品(" + lastActivityThread.getCurrentGoodsId() + ")结束");
        }
    }


    /**
     * 启动一个活动线程
     * @param activityCode
     * @param goodsId
     * @param initTime
     * @param delayedCondition
     * @param allowDelayedLength
     * @param allowDelayedTime
     * @param supplierNum
     * @param bidType
     * @return
     */
    private boolean startActivityTask(String activityCode, String goodsId, int initTime, int delayedCondition,
                                      int allowDelayedLength,  int allowDelayedTime, int supplierNum, int bidType) {
        boolean startOK = true;
        try {
            closeLastActivityThread(activityCode);
            //final ActivityTask newActivityThread = new ActivityThread(activityCode, goodsId, initTime * 60, 1);
            final ActivityTask.Builder newActivityTaskBuiler = new ActivityTask.Builder();
            newActivityTaskBuiler.activityCode(activityCode).
                    goodsId(Integer.parseInt(goodsId)).
                    initTime(initTime*60).
                    delayedCondition(delayedCondition).
                    allowDelayedLength(allowDelayedLength).
                    allowDelayedTime(allowDelayedTime).
                    supplierNum(supplierNum).
                    bidType(bidType);

            ActivityTask newActivityTask = newActivityTaskBuiler.build();
            activityThreadManager.put(activityCode, newActivityTask);
            activityThreadManager.doTask(newActivityTask);
            //  newActivityThread.start();

            LOGGER.info("startActivity(msg): 启动" + activityCode + " 商品(" + newActivityTask.getCurrentGoodsId() + ")活动成功");
        } catch (Exception e) {
            LOGGER.info("startActivity(ERROR): 启动" + activityCode + " 活动失败" + e.getMessage());
            startOK = false;
        }

        return startOK;
    }

}



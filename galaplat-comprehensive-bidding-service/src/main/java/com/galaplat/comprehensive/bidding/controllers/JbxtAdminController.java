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
import com.galaplat.comprehensive.bidding.dao.dos.JbxtActivityDO;
import com.galaplat.comprehensive.bidding.dao.dos.JbxtGoodsDO;
import com.galaplat.comprehensive.bidding.dao.dvos.JbxtGoodsDVO;
import com.galaplat.comprehensive.bidding.service.IJbxtActivityService;
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

    Logger LOGGER = LoggerFactory.getLogger(JbxtAdminController.class);

   @Autowired
    private ActivityMap activityMap;

    @Autowired
    private IJbxtGoodsService iJbxtGoodsService;

    @Autowired
    private IJbxtActivityService iJbxtActivityService;

    @Autowired
    private PushQueue pushQueue;


    @RequestMapping("/activity/goodsStatus")
    @RestfulResult
    public Object updateCurrentBidActivityStatus(String activityCode, Integer status) {

        if (activityCode == null || "".equals(activityCode)) return new MyResult(false, "activityCode不能为空");
        if (status == null ) return new MyResult(false, "status不能为null");

        try {
            CurrentActivity currentActivity = activityMap.get(activityCode);

            if (currentActivity != null) {
                if (status == 3) { //处理剩余时间为0 且管理端点击重置问题
                    int remainingTime = currentActivity.getRemainingTime();
                    if (remainingTime < 0 ) {
                        CurrentActivity newActivity = new CurrentActivity(currentActivity.getCurrentActivityCode(),
                                currentActivity.getCurrentGoodsId(),
                                currentActivity.getInitTime());
                        activityMap.put(currentActivity.getCurrentActivityCode(), newActivity);
                        newActivity.start();

                        return new MyResult(true,"更新成功");
                    }
                }

                currentActivity.setStatus(status);
            } else {
                return new MyResult(false, "currentActivity("+activityCode+")不存在");
            }

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
    public Object next(String activityCode) {
        MyResult checkNextRes = checkNextReq(activityCode);
        if (! checkNextRes.isSuccess()) return checkNextRes;

        JbxtGoodsDO jbxtGoodsDO = iJbxtGoodsService.selectActiveGoods(activityCode); //get 正在进行goods
        if (jbxtGoodsDO != null) {
            JbxtGoodsVO tj = new JbxtGoodsVO();
            tj.setGoodsId(jbxtGoodsDO.getGoodsId());
            tj.setStatus("2");
            try {
                jbxtgoodsService.updateJbxtGoods(tj);
            } catch (Exception e) {
                String info = "next(ERROR): 切换活动: "+activityCode+" goodsId: "+jbxtGoodsDO.getGoodsId()+" 状态为2失败. info="+e.getMessage();
                LOGGER.info(info);
                return new MyResult(false, info);
            }
            return switchActivityGoods(activityCode);
        } else {
            return switchActivityGoods(activityCode);
        }
    }
    private MyResult checkNextReq(String activityCode) {
        if (activityCode == null || activityCode.equals("")) {
            return new MyResult(false,"错误: activityCode不能为空哦(*￣︶￣)", null);
        }

        JbxtActivityDO activityEntity = iJbxtActivityService.findOneByCode(activityCode);
        if (activityEntity == null)  {
            String errorInfo = "错误: 当前活动"+activityCode+"不存在";
            LOGGER.info("next(msg): "+errorInfo);
            return new MyResult(false, errorInfo);
        }

        return new MyResult(true, "");
    }
    private Object switchActivityGoods(String activityCode) {
        List<JbxtGoodsDVO> jgbacs = jbxtgoodsService.getListJbxtGoodsByActivityCode(activityCode); //get all goods by activityCode
        for (int i = 0; i < jgbacs.size(); i++) {
            JbxtGoodsDVO jbxtGoodsDVO1 = jgbacs.get(i);
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
                    startActivity(activityCode, jbxtGoodsDVO1.getGoodsId().toString(),jbxtGoodsDVO1.getTimeNum());
                    notify214Event(activityCode, jbxtGoodsDVO1.getGoodsId());

                    return new MyResult(true, "切换成功");
                } else {
                    return new MyResult(false,"错误: 切换失败");
                }
            }
        }

        Map<String, String> map = new HashMap<>();
        map.put("goodsId", "-1");
        JbxtActivityDO tActivity = new JbxtActivityDO();
        tActivity.setCode(activityCode);
        tActivity.setStatus(4);
        iJbxtActivityService.updateByPrimaryKeySelective(tActivity);
        closeLastActivity(activityCode);
        return new MyResult(true, "所有竞品已结束", map);
    }
    private void notify214Event( String activityCode, Integer goodsId) {
        Map<String, String> map214 = new HashMap();

        map214.put("activityCode", activityCode);
        map214.put("goodsId", goodsId.toString());
        pushQueue.offer(new QueueMessage(214, map214));
    }
    private void closeLastActivity(String activityCode) {
        CurrentActivity currentActivity = activityMap.get(activityCode);
        if (currentActivity != null) { //停止上一个goods的活动
            currentActivity.setStatus(1);
            currentActivity.setRemainingTime(0);
            LOGGER.info("closeLastActivity(msg): 活动"+activityCode+" 商品("+currentActivity.getCurrentGoodsId()+")结束");
        }
    }
    private boolean startActivity(String activityCode, String goodsId, int initTime) {
        try {
            closeLastActivity(activityCode);

            CurrentActivity ca1 = new CurrentActivity(activityCode, goodsId, initTime * 60, 1);
            activityMap.put(activityCode, ca1);
            ca1.start();
            LOGGER.info("startActivity(msg): 启动"+activityCode+" 商品("+ca1.getCurrentGoodsId()+")活动成功");
            return true;
        } catch (Exception e) {
            LOGGER.info("startActivity(ERROR): 启动"+activityCode+" 活动失败"+e.getMessage());
            return false;
        }

    }
}

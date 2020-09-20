package com.galaplat.comprehensive.bidding.activity.queue;

import static com.galaplat.comprehensive.bidding.activity.queue.handler.ProblemHandlerFactory.*;
import com.galaplat.comprehensive.bidding.activity.queue.handler.ProblemHandler;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;
import com.galaplat.comprehensive.bidding.enums.HandlerEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/***
 * 外观者模式
 * 提供统一处理者接口
 */
public class HandlerFacade implements ProblemHandler{
    private final ProblemHandler supplierOutHandler;
    private final ProblemHandler supplierInHandler;
    private final ProblemHandler adminOutHandler;
    private final ProblemHandler adminInHandler;
    private final Map<Integer, HandlerEnum> handlerRecords;
    private final Logger LOGGER = LoggerFactory.getLogger(HandlerFacade.class);

    public HandlerFacade() {
        this.supplierOutHandler = getSupplierOutProblemHandler();
        this.supplierInHandler = getSupplierInProblemHandler();
        this.adminOutHandler = getAdminOutProblemHandler();
        this.adminInHandler = getAdminInProblemHandler();
        this.handlerRecords = new HandlerFacade.HandlerRecordsMap<>();
        this.initHandlerRecords();
    }

    public void supplierOutHandler(final QueueMessage queueMsg) {
        this.supplierOutHandler.problem(queueMsg.getType(), queueMsg);
    }

    public void supplierInHandler(final QueueMessage queueMsg) {
        this.supplierInHandler.problem(queueMsg.getType(), queueMsg);
    }

    public void adminOutHandler(final QueueMessage queueMsg) {
        this.adminOutHandler.problem(queueMsg.getType(), queueMsg);
    }

    public void adminInHandler(final QueueMessage queueMsg) {
        this.adminInHandler.problem(queueMsg.getType(), queueMsg);
    }

    @Override
    public void problem(int type, QueueMessage queueMessage) {
        switch (handlerRecords.get(type)) { //匹配相应的策略算法
            case SUPPLIER_IN: {
                supplierInHandler(queueMessage);
            }
            break;

            case SUPPLER_OUT: {
                supplierOutHandler(queueMessage);
            }
            break;

            case ADMIN_IN: {
                adminInHandler(queueMessage);
            }
            break;

            case ADMIN_OUT: {
                adminOutHandler(queueMessage);
            }
            break;

            case OTHER: {
                LOGGER.info("handler(msg): 错误消息" + queueMessage.toString());
            }
        }
    }

    /**
     * 配置映射关系
     */
    private void initHandlerRecords() {
        //处理第一名发生变化时 同步数据给供应商
        handlerRecords.put(111, HandlerEnum.SUPPLER_OUT);
        //当某个供应商端中途加入（或掉线从新加入） 同步供应商数据
        handlerRecords.put(200, HandlerEnum.SUPPLER_OUT);
        //当某个供应商端中途加入（或掉线从新加入） 同步供应商数据
        handlerRecords.put(211, HandlerEnum.SUPPLER_OUT);
        //当管理端数据重置时，通知供应商端清理排名数据
        handlerRecords.put(212, HandlerEnum.SUPPLER_OUT);
        //处理供应商端提交竞价
        handlerRecords.put(213, HandlerEnum.SUPPLIER_IN);
        //处理当管理端切换下一个竞品时，提示所有供应商端更新
        handlerRecords.put(214, HandlerEnum.SUPPLER_OUT);
        //处理当管理端点击暂停或者继续后，通知供应商端暂停某个正在进行的竞品
        handlerRecords.put(215, HandlerEnum.SUPPLER_OUT);
        //处理当本场活动结束，通知供应商端退出登录
        handlerRecords.put(216, HandlerEnum.SUPPLER_OUT);
        //当某个管理端中途加入（或掉线从新加入） 同步数据给管理端
        handlerRecords.put(300, HandlerEnum.ADMIN_OUT);
        //当供应商端提交最新竞价信息时，推数据给管理端（同步一些数据给管理端）
        handlerRecords.put(301, HandlerEnum.ADMIN_OUT);
        //处理管理端主动请求获取某个竞品数据时
        handlerRecords.put(302, HandlerEnum.ADMIN_OUT);
    }

    private static class HandlerRecordsMap<K, V> extends HashMap<K, V> {
        public HandlerRecordsMap() {
            super();
        }

        public V put(K key, V value) {
            return super.put(key, value);
        }

        public V get(Object key) {
            V value = super.get(key);
            if (value == null) {
                return (V) HandlerEnum.OTHER;
            }
            return value;
        }
    }

}

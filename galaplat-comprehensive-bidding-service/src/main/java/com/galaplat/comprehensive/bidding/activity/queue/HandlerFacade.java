package com.galaplat.comprehensive.bidding.activity.queue;

import static com.galaplat.comprehensive.bidding.activity.queue.handler.ProblemHandlerFactory.*;
import com.galaplat.comprehensive.bidding.activity.queue.handler.ProblemHandler;
import com.galaplat.comprehensive.bidding.activity.queue.msg.QueueMessage;

/***
 * 外观者模式
 * 提供统一处理者接口
 */
public class HandlerFacade {
    private final ProblemHandler supplierOutHandler;
    private final ProblemHandler supplierInHandler;
    private final ProblemHandler adminOutHandler;
    private final ProblemHandler adminInHandler;

    public HandlerFacade() {
        this.supplierOutHandler = getSupplierOutProblemHandler();
        this.supplierInHandler = getSupplierInProblemHandler();
        this.adminOutHandler = getAdminOutProblemHandler();
        this.adminInHandler = getAdminInProblemHandler();
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
}

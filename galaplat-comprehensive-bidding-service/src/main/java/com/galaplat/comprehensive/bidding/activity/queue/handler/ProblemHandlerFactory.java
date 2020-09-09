package com.galaplat.comprehensive.bidding.activity.queue.handler;

import com.galaplat.comprehensive.bidding.enums.HandlerEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * 处理者工厂
 */
public class ProblemHandlerFactory {
    private final static Logger LOGGER = LoggerFactory.getLogger(ProblemHandlerFactory.class);
    private static HandlerEnum getNameMapper(final String name) {
        switch (name) {
            case "supplier_in":
                return HandlerEnum.SUPPLIER_IN;
            case "supplier_out":
                return HandlerEnum.SUPPLER_OUT;
            case "admin_out":
                return HandlerEnum.ADMIN_OUT;
            case "admin_in":
                return HandlerEnum.ADMIN_IN;
            default:
                return HandlerEnum.OTHER;
        }
    }
    /***
     * 通过传入名称获取 相应的处理者 实例
     *  <p>name => 实例</p>
     *  <p>supplier_in => SupplierInProblemHandler</p>
     *  <p>supplier_out => SupplierOutProblemHandler</p>
     *  <p>admin_out => AdminOutProblemHandler</p>
     *  <p>admin_in => AdminInProblemHandler</p>
     * @param name
     * @return
     */
    public static ProblemHandler getSpecifixProblemHandler(final String name) {
        final HandlerEnum nameEnum = getNameMapper(name);
        switch (nameEnum) {
            case SUPPLIER_IN: {
                return getSupplierInProblemHandler();
            }

            case SUPPLER_OUT: {
                return getSupplierOutProblemHandler();
            }

            case ADMIN_IN: {
                return getAdminInProblemHandler();
            }

            case ADMIN_OUT: {
                return getAdminOutProblemHandler();
            }

            case OTHER: {
                LOGGER.info("getSpecifixProblemHandler(ERROR): 传入的name有误("+name);
            }
        }

        return null;
    }

    public static ProblemHandler getAdminOutProblemHandler() {
        return new AdminOutProblemHandler();
    }

    public static ProblemHandler getAdminInProblemHandler() {
        return new AdminInProblemHandler();
    }

    public static ProblemHandler getSupplierOutProblemHandler() {
        return new SupplierOutProblemHandler();
    }

    public static ProblemHandler getSupplierInProblemHandler() {
        return new SupplierInProblemHandler();
    }
}

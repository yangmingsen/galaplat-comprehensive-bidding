package com.galaplat.comprehensive.bidding.vos;

import com.galaplat.comprehensive.bidding.utils.GsonUtil;
import com.google.common.base.Joiner;

import java.util.Map;

public class ValidateResultVO {

    //校验结果是否有错
    private boolean hasErrors;

    //校验错误信息
    private Map<String, String> errorMsg;

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(Map<String, String> errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMessageDetail() {
        return GsonUtil.SINGLE.getGson().toJson(this.getErrorMsg());
    }

    public String getErrorMessage() {
        if (null != this.errorMsg && !errorMsg.isEmpty()) {
            return Joiner.on(",").join(errorMsg.values());
        }
        return null;
    }

    @Override
    public String toString() {
        return "ValidateResultVO [hasErrors=" + hasErrors + ", errorMsg="
                + errorMsg + "]";
    }
}

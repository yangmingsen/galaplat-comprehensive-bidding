package com.galaplat.comprehensive.bidding.vos.pojo;

public class MyResult {
    private boolean success; // 是否操作成功
    private String message; // 返回消息
    private Object result; // 返回附件的对象

    public MyResult(boolean success, String message) {
        setInfo(success,message);
        this.result = null;
    }

    public MyResult(boolean success, String message, Object result) {
        setInfo(success,message);
        this.result = result;
    }

    public MyResult() {

    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setInfo(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}

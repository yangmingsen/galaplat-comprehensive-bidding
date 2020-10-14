package com.galaplat.comprehensive.bidding.dao.dos;


public class MessageLogDO {

	/**
	 * id
	 */
	private String _id;
	/**
	 * 类型
	 */
	private String type;
	/**
	 * 发送人
	 */
	private String from;
	/**
	 * 接收人
	 */
	private String to;
	/**
	 * 请求参数
	 */
	private String request;
	/**
	 * 返回值
	 */
	private String response;
	/**
	 * 状态 0 失败 1成功
	 */
	private String status;
	/**
	 * 返回结果
	 */
	private String result;
	/**
	 * 创建时间
	 */
	private String createdTime;
	/**
	 * 更新时间
	 */
	private String updatedTime;

	/**
	 * 尝试发送次数
	 */
	private int retryCount;

	/**
	 * 消息编码
	 */
	private String msgCode;

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(String createdTime) {
		this.createdTime = createdTime;
	}

	public String getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(String updatedTime) {
		this.updatedTime = updatedTime;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getMsgCode() {
		return msgCode;
	}

	public void setMsgCode(String msgCode) {
		this.msgCode = msgCode;
	}

}

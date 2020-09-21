package com.galaplat.comprehensive.bidding.enums;

import com.galaplat.base.core.common.enums.ICodeEnum;

/**
 *
 * @author yx
 * @time 2018年3月6日
 */
public enum PhoneTempleteEnum implements ICodeEnum {
	// 竞标通知短信
	BIDDINGMSG("SMS_202808518", "notify", "${supplierName} 您好！现邀请贵公司参与ESR举行的竞标活动，以下是本次竞标活动相关信息：竞标单编号：${bidActivityCode}；预计竞标时间：${bidActivityDateTime}；账号：${bidActivityAccount}；密码：${bidActivityPassword}；温馨提示：请提前登录系统，熟悉系统操作与本次竞标的产品，如有疑问可联系对口业务人员。");

	private String code;
	private String msg;
	private String ruleKey;

	private PhoneTempleteEnum(String code, String ruleKey, String msg) {
		this.code = code;
		this.msg = msg;
		this.ruleKey = ruleKey;
	}

	@Override
	public String getCode() {
		return this.code;
	}

	@Override
	public String getMsg() {
		return this.msg;
	}

	public String getRuleKey() {
		return this.ruleKey;
	}

}

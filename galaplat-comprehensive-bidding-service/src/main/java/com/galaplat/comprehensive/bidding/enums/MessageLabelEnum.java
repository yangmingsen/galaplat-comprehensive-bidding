package com.galaplat.comprehensive.bidding.enums;

/**
 * 信息发送 key标识
 *
 * @author fcl
 *
 */
public enum MessageLabelEnum {

	/**
	 * 登录图片验证码
	 */
	LOGIN_CAPTCHA("login:captcha:", "登录图片验证码"),

	/**
	 * 登录密码 错误次数 用来判断是否显示 图片验证码
	 */
	LOGIN_ERROR_TIMES("register:phone:error:times:", "登录手机验证码错误次数"),

	/**
	 * 注册邮件验证码
	 */
	REGISTER_EMAIL("register:email:", "登录邮件验证码"),
	/**
	 * 注册手机验证码
	 */
	REGISTER_PHONE("register:phone:", "登录手机验证码"),
	/**
	 * 注册手机错误次数 用来判断是否显示 图片验证码
	 */
	REGISTER_ERROR_TIMES("register:phone:error:times:", "注册手机验证码错误次数"),
	/**
	 * 登录图片验证码
	 */
	REGISTER_CAPTCHA("register:captcha:", "登录图片验证码"),

	/**
	 * 找回密码邮件验证码
	 */
	FINDPWD_EMAIL("findpwd:email:", "密码邮件验证码"),
	/**
	 * 找回密码手机验证码
	 */
	FINDPWD_PHONE("findpwd:phone:", "密码手机验证码"),
	/**
	 * 找回密码手机验证 图片验证码
	 */
	FINDPWD_CAPTCHA("findpwd:captcha:", "密码图片验证码"),
	/**
	 * 找回密码手机或邮件 错误次数 用来判断是否显示 图片验证码
	 */
	FINDPWD_ERROR_TIMES("register:phone:error:times:", "登录手机验证码错误次数"),

	/**
	 * 绑定手机验证码
	 */
	BINDING_PHONE("binding:phone:", "绑定手机号验证码"),

	/**
	 * 重新绑定原手机验证码
	 */
	RESET_BINDING_OLDPHONE("resetbinding:oldphone:", "重新绑定原手机号验证码"),

	/**
	 * 重新绑定新手机验证码
	 */
	RESET_BINDING_NEWPHONE("resetbinding:newphone:", "重新绑定新手机号验证码"),

	/**
	 * 绑定邮件验证码
	 */
	BINDING_EMAIL("binding:email:", "登录邮件验证码"),

	INVITATION_EMAIL("invitation:email:", "邀请邮箱"),

	/**
	 * 重新绑定原邮箱验证码
	 */
	RESET_BINDING_OLDEMAIL("resetbinding:oldemail:", "重新绑定原邮箱验证码"),

	/**
	 * 重新绑定新邮箱验证码
	 */
	RESET_BINDING_NEWEMAIL("resetbinding:newemail:", "重新绑定新邮箱验证码"),

	/**
	 * 绑定手机图片验证码
	 */
	BINDING_PHONE_CAPTCHA("binding:phone:captcha:", "绑定手机图片验证码"),
	/**
	 * 绑定邮箱图片验证码
	 */
	BINDING_EMAIL_CAPTCHA("binding:email:captcha:", "绑定邮箱图片验证码"),
	/**
	 * 重绑手机号或邮箱验证码
	 */
	RESET_PHONE_EMAIL_CAPTCHA("rebinding:phoneoremail:captcha:", "重绑手机号或邮箱验证码");
	private String code;
	private String msg;

	MessageLabelEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode(String id) {
		return this.code + id;
	}

	public String getMsg() {
		return msg;
	}
}

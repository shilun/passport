package com.passport.domain;

import com.common.util.AbstractBaseEntity;

/**
 *
 * @desc 短信内容
 *
 */
public class SMSInfo extends AbstractBaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 签名
	 */
	private String sign;
	/**
	 * 调用业务系统
	 */
	private String sender;
	/**
	 * 手机
	 */
	private String mobile;
	/**
	 * 内容
	 */
	private String content;
	/**
	 * 发送次数
	 */
	private Integer executeCount;
	/**
	 * 最小发送次数
	 */
	private Integer minExecuteCount;
	/**
	 * 状态 1 发送成功 2 发送失败
	 */
	private Integer status;

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	private String result;

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public Integer getMinExecuteCount() {
		return minExecuteCount;
	}

	public void setMinExecuteCount(Integer minExecuteCount) {
		this.minExecuteCount = minExecuteCount;
	}

	public String getSender() {
		return this.sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getMobile() {
		return this.mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return this.content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getExecuteCount() {
		return this.executeCount;
	}

	public void setExecuteCount(Integer executeCount) {
		this.executeCount = executeCount;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}
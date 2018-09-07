package com.passport.domain;

import com.common.util.AbstractBaseEntity;

import java.util.Date;

/**
 *
 * @desc 短信内容
 *
 */
public class SMSInfo extends AbstractBaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 发送方
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
	/**
	 * 删除状态 1 常常 2 删除
	 */
	private Integer delStatus;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 更新时间
	 */
	private Date updateTime;

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

	public Integer getDelStatus() {
		return this.delStatus;
	}

	public void setDelStatus(Integer delStatus) {
		this.delStatus = delStatus;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
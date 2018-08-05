package com.shilun.passport.domain;

import com.common.annotation.QueryField;
import com.common.mongo.QueryType;
import com.common.util.AbstractBaseEntity;
import org.springframework.data.annotation.Transient;

import java.util.Date;

/**
 * 
 * @desc 邮件队列 mail_info mongodb中
 *
 */
public class MailInfo extends AbstractBaseEntity implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/**标题*/
	private String title;
	/**发送者*/
	private String sender;
	/**接收者*/
	private String recipient;
	/**内容*/
	private String content;
	/**发送时间*/
	private Date sendTime;
	/**执行次数*/
	private Integer execCount;


	@Transient
	@QueryField(name="execCount",type = QueryType.GTE)
	private Integer minExecCount;

	@Transient
	@QueryField(name="execCount",type = QueryType.LTE)
	private Integer maxExecCount;
	private byte[] attchMentFile;
	private String fileName;

	public byte[] getAttchMentFile() {
		return attchMentFile;
	}

	public Integer getMinExecCount() {
		return minExecCount;
	}



	public void setMinExecCount(Integer minExecCount) {
		this.minExecCount = minExecCount;
	}

	public Integer getMaxExecCount() {
		return maxExecCount;
	}

	public void setMaxExecCount(Integer maxExecCount) {
		this.maxExecCount = maxExecCount;
	}

	public void setAttchMentFile(byte[] attchMentFile) {
		this.attchMentFile = attchMentFile;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**1 待发送 2 发送成功 3 发送失败*/
	private Integer status;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getRecipient() {
		return recipient;
	}

	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Integer getExecCount() {
		return execCount;
	}

	public void setExecCount(Integer execCount) {
		this.execCount = execCount;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
}

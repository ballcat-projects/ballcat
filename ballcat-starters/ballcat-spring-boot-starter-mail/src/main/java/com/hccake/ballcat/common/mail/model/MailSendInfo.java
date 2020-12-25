package com.hccake.ballcat.common.mail.model;

import java.time.LocalDateTime;

/**
 * 邮件发送详情
 *
 * @author Hccake 2020/12/23
 * @version 1.0
 */
public class MailSendInfo {

	public MailSendInfo(MailDetails mailDetails) {
		this.mailDetails = mailDetails;
	}

	/**
	 * 邮件信息
	 */
	private final MailDetails mailDetails;

	/**
	 * 发送时间
	 */
	private LocalDateTime sentDate;

	/**
	 * 是否发送成功
	 */
	private Boolean success;

	/**
	 * 错误信息 errorMsg
	 */
	private String errorMsg;

	public MailDetails getMailDetails() {
		return mailDetails;
	}

	public LocalDateTime getSentDate() {
		return sentDate;
	}

	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}

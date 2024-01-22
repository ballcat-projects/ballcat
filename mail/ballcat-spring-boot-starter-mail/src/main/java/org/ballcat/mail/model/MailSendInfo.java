/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.mail.model;

import java.time.LocalDateTime;

/**
 * 邮件发送详情
 *
 * @author Hccake 2020/12/23
 *
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
		return this.mailDetails;
	}

	public LocalDateTime getSentDate() {
		return this.sentDate;
	}

	public void setSentDate(LocalDateTime sentDate) {
		this.sentDate = sentDate;
	}

	public Boolean getSuccess() {
		return this.success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	public String getErrorMsg() {
		return this.errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}

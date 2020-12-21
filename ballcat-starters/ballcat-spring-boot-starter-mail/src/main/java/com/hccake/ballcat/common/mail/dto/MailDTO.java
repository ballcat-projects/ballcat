package com.hccake.ballcat.common.mail.dto;

import lombok.Data;

import java.io.File;
import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/2/27 17:06
 */
@Data
public class MailDTO {

	/**
	 * 发件人
	 */
	private String from;

	/**
	 * 收件人
	 */
	private String[] to;

	/**
	 * 邮件主题
	 */
	private String subject;

	/**
	 * 是否渲染html
	 */
	private Boolean showHtml;

	/**
	 * 邮件内容
	 */
	private String content;

	/**
	 * 抄送
	 */
	private String[] cc;

	/**
	 * 密送
	 */
	private String[] bcc;

	/**
	 * 附件
	 */
	private File[] files;

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

}

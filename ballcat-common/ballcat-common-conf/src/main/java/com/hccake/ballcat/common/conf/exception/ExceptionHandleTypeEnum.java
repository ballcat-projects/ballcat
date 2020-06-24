package com.hccake.ballcat.common.conf.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 异常处理类型
 *
 * @author lingting 2020/6/12 0:18
 */
@Getter
@AllArgsConstructor
public enum ExceptionHandleTypeEnum {

	/**
	 * 异常处理通知类型 说明
	 */
	NONE("不通知"), DING_TALK("通过钉钉通知"), MAIL("邮件通知"),;

	private final String text;

}

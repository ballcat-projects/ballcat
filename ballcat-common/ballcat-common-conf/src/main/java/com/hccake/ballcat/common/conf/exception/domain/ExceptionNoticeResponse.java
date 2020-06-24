package com.hccake.ballcat.common.conf.exception.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 异常消息通知响应
 *
 * @author lingting 2020/6/12 19:07
 */
@Getter
@Setter
@Accessors(chain = true)
public class ExceptionNoticeResponse {

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 错误信息
	 */
	private String errMsg;

}

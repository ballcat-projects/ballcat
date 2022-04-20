package com.hccake.ballcat.auth.filter.captcha.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Getter
@Setter
@Accessors(chain = true)
public class CaptchaResponse {

	/**
	 * 是否成功
	 */
	private boolean success;

	/**
	 * 错误信息
	 */
	private String errMsg;

}

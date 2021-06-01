package com.hccake.ballcat.oauth.exception;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/29 10:31 自定义的异常处理
 */
@JsonSerialize(using = CustomOAuth2ExceptionSerializer.class)
public class CustomOAuth2Exception extends OAuth2Exception {

	public CustomOAuth2Exception(String msg, Throwable t) {
		super(msg, t);
	}

	public CustomOAuth2Exception(String msg) {
		super(msg);
	}

}

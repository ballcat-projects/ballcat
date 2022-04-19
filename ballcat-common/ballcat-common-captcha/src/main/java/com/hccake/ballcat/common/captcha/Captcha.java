package com.hccake.ballcat.common.captcha;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ValidateCode
 *
 * @author xm.z
 */
@AllArgsConstructor
public class Captcha implements Serializable {

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private LocalDateTime expireTime;

	public Captcha(String code, int expireIn) {
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}

}

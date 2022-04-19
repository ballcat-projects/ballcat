package com.hccake.ballcat.common.code;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * ValidateCode
 *
 * @author xm.z
 */
public class ValidateCode implements Serializable {

	private static final long serialVersionUID = 1588203828504660915L;

	@Getter
	@Setter
	private String code;

	@Getter
	@Setter
	private LocalDateTime expireTime;

	public ValidateCode(String code, int expireIn) {
		this.code = code;
		this.expireTime = LocalDateTime.now().plusSeconds(expireIn);
	}

	public ValidateCode(String code, LocalDateTime expireTime) {
		this.code = code;
		this.expireTime = expireTime;
	}

}

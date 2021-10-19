package com.hccake.starter.ftp;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author lingting 2021/10/18 10:03
 */
@Getter
@AllArgsConstructor
public enum FtpMode {

	/**
	 * 主动模式
	 */
	ACTIVE,
	/**
	 * 被动模式
	 */
	PASSIVE

	;

}

package com.hccake.ballcat.common.oss.exception;

/**
 * @author lingting 2021/5/27 10:44
 */
public class OssDisabledException extends RuntimeException {

	public OssDisabledException() {
		super("oss 未启用!");
	}

}

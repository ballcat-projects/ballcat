package com.hccake.ballcat.commom.oss.exception;

/**
 * @author lingting 2021/5/27 10:44
 */
public class OssDisabledException extends Exception {

	public OssDisabledException() {
		super("oss 未启用!");
	}

}

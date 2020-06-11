package com.hccake.extend.mybatis.plus.exception;

/**
 * mybatis plus 方法 实例化获取异常
 *
 * @author lingting  2020/5/28 15:06
 */
public class MybatisMethodInstanceException extends RuntimeException {

	public MybatisMethodInstanceException(String message, Throwable cause) {
		super(message, cause);
	}
}

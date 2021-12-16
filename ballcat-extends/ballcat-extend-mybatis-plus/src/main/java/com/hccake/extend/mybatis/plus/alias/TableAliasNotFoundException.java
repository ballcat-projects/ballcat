package com.hccake.extend.mybatis.plus.alias;

/**
 * TableAlias 注解没有找到时抛出的异常
 *
 * @see TableAlias
 * @author hccake
 */
public class TableAliasNotFoundException extends RuntimeException {

	public TableAliasNotFoundException() {
	}

	public TableAliasNotFoundException(String message) {
		super(message);
	}

	public TableAliasNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public TableAliasNotFoundException(Throwable cause) {
		super(cause);
	}

}

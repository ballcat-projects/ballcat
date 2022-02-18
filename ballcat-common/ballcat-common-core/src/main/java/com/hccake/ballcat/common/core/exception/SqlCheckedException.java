package com.hccake.ballcat.common.core.exception;

import com.hccake.ballcat.common.model.result.SystemResultCode;

/**
 * sql防注入校验异常
 *
 * @author Hccake
 * @version 1.0
 * @date 2019/10/19 16:52
 */
public class SqlCheckedException extends BusinessException {

	public SqlCheckedException(SystemResultCode systemResultMsg) {
		super(systemResultMsg);
	}

	public SqlCheckedException(SystemResultCode systemResultMsg, Throwable e) {
		super(systemResultMsg, e);
	}

	public SqlCheckedException(int code, String msg) {
		super(code, msg);
	}

	public SqlCheckedException(int code, String msg, Throwable e) {
		super(code, msg, e);
	}

}

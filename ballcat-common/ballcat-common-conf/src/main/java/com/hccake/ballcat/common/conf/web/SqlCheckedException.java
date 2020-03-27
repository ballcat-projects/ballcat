package com.hccake.ballcat.common.conf.web;

import com.hccake.ballcat.common.core.exception.BallCatException;
import com.hccake.ballcat.common.core.result.SystemResultCode;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/19 16:52
 * sql防注入校验异常
 */
public class SqlCheckedException extends BallCatException {
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

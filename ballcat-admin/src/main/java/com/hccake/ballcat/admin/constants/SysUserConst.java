package com.hccake.ballcat.admin.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/17 14:54
 */
public final class SysUserConst {

    private SysUserConst() {}

    @Getter
    @AllArgsConstructor
    public enum  Status {
        /**
         * 正常
         */
        NORMAL(1),
        /**
         * 锁定的
         */
        LOCKED(0);

        private Integer value;
    }

}

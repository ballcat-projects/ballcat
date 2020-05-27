package com.hccake.ballcat.commom.log.operation.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/27 17:56
 * 操作类型
 */
@Getter
@RequiredArgsConstructor
public enum OperationTypeEnum {

    /**
     * 登陆操作
     */
    LOGIN(1),

    /**
     * 新建操作
     */
    CREATE(2),

    /**
     * 修改操作
     */
    UPDATE(3),

    /**
     * 删除操作
     */
    DELETE(4);

    private final Integer value;
}

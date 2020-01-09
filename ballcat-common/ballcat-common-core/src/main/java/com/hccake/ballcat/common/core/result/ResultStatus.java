package com.hccake.ballcat.common.core.result;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/12 12:19
 */
@Getter
@AllArgsConstructor
public enum ResultStatus {

    //  ================ 基础部分，参考 HttpStatus =============

    /**
     * 成功
     */
    OK(200,"Success"),
    /**
     * 参数错误
     */
    BAD_REQUEST(400, "Bad Request"),
    /**
     * 未认证
     */
    UNAUTHORIZED(401, "Unauthorized"),
    /**
     * 未授权
     */
    FORBIDDEN(403, "Forbidden"),
    /**
     * 服务异常
     */
    SERVER_ERROR(500, "Internal Server Error"),


    /**
     * ===========项目部分，业务错误码开始===================
     * TODO 设计
     * 业务错误码规范：
     *
     *
     */

    /**
     * 保存失败
     */
    SAVE_ERROR(90000, "Insert Or Update Data Failed"),

    /**
     * 通用的逻辑校验异常
     */
    LOGIC_CHECK_ERROR(90001, "Logic Check Error"),

    /**
     * 恶意请求
     */
    MALICIOUS_REQUEST(90002, "Malicious Request"),

    /**
     * 文件上传异常
     */
    FILE_UPLOAD_ERROR(90003, "File Upload Error"),

    /**
     * 未知异常
     */
    UNKNOWN_ERROR(99999, "Unknown Error");



    private Integer code;
    private String message;
}

package com.hccake.ballcat.commom.log.operation.service;

import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;

/**
 * 操作日志业务类
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 19:57
 */
public interface OperationLogSaveService {

    /**
     * 保存操作日志
     * @param operationLogDTO
     * @return true/false
     */
    boolean saveLog(OperationLogDTO operationLogDTO);
}

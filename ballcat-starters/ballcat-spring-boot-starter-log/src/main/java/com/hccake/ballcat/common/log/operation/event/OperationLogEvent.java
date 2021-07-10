package com.hccake.ballcat.common.log.operation.event;

import com.hccake.ballcat.common.log.operation.model.OperationLogInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author 系统日志事件
 */
@Getter
@AllArgsConstructor
public class OperationLogEvent {

	private final OperationLogInfo operationLogInfo;

}

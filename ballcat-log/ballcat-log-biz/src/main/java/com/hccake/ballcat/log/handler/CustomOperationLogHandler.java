package com.hccake.ballcat.log.handler;

import com.hccake.ballcat.common.log.operation.model.OperationLogDTO;
import com.hccake.ballcat.common.log.operation.service.OperationLogHandler;
import com.hccake.ballcat.log.converter.OperationLogConverter;
import com.hccake.ballcat.log.model.entity.OperationLog;
import com.hccake.ballcat.log.service.OperationLogService;
import lombok.RequiredArgsConstructor;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 20:38
 */
@RequiredArgsConstructor
public class CustomOperationLogHandler implements OperationLogHandler {

	private final OperationLogService operationLogService;

	/**
	 * 保存操作日志
	 * @param operationLogDTO 操作日志DTO
	 */
	@Override
	public void saveLog(OperationLogDTO operationLogDTO) {
		OperationLog operationLog = OperationLogConverter.INSTANCE.dtoToPo(operationLogDTO);
		operationLogService.save(operationLog);
	}

}

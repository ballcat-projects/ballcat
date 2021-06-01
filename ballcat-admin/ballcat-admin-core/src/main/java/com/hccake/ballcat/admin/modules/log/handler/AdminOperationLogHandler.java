package com.hccake.ballcat.admin.modules.log.handler;

import com.hccake.ballcat.admin.modules.log.converter.AdminOperationLogConverter;
import com.hccake.ballcat.admin.modules.log.model.entity.AdminOperationLog;
import com.hccake.ballcat.admin.modules.log.service.OperationLogAdminService;
import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;
import com.hccake.ballcat.commom.log.operation.service.OperationLogHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 20:38
 */
@Component
@RequiredArgsConstructor
public class AdminOperationLogHandler implements OperationLogHandler {

	private final OperationLogAdminService operationLogAdminService;

	/**
	 * 保存操作日志
	 * @param operationLogDTO 操作日志DTO
	 */
	@Override
	public void saveLog(OperationLogDTO operationLogDTO) {
		AdminOperationLog adminOperationLog = AdminOperationLogConverter.INSTANCE.dtoToPo(operationLogDTO);
		operationLogAdminService.save(adminOperationLog);
	}

}

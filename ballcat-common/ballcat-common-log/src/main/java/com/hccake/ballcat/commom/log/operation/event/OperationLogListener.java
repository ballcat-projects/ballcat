package com.hccake.ballcat.commom.log.operation.event;

import com.hccake.ballcat.commom.log.operation.service.OperationLogSaveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;

/**
 * @author
 * 异步监听日志事件
 */
@Slf4j
public class OperationLogListener {

	@Autowired
	private OperationLogSaveService operationLogSaveService;

	@Async
	@Order
	@EventListener(OperationLogEvent.class)
	public void saveSysLog(OperationLogEvent event) {
		operationLogSaveService.saveLog(event.getOperationLogDTO());
	}
}

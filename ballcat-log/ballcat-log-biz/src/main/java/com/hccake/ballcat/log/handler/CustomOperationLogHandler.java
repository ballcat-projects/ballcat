package com.hccake.ballcat.log.handler;

import cn.hutool.core.util.URLUtil;
import com.hccake.ballcat.common.log.constant.LogConstant;
import com.hccake.ballcat.common.log.operation.annotation.OperationLogging;
import com.hccake.ballcat.common.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.common.log.operation.handler.AbstractOperationLogHandler;
import com.hccake.ballcat.common.log.operation.handler.OperationLogHandler;
import com.hccake.ballcat.common.log.util.LogUtils;
import com.hccake.ballcat.common.security.userdetails.User;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.common.util.IpUtils;
import com.hccake.ballcat.log.model.entity.OperationLog;
import com.hccake.ballcat.log.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 20:38
 */
@RequiredArgsConstructor
public class CustomOperationLogHandler extends AbstractOperationLogHandler<OperationLog> {

	private final OperationLogService operationLogService;

	/**
	 * 保存操作日志
	 * @param operationLog 操作日志
	 */
	@Override
	public void saveLog(OperationLog operationLog) {
		// 异步保存
		operationLogService.saveAsync(operationLog);
	}

	@Override
	public OperationLog buildLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint, long executionTime,
			Throwable throwable) {
		// 获取 Request
		HttpServletRequest request = LogUtils.getHttpServletRequest();

		// @formatter:off
		OperationLog operationLog = new OperationLog()
				.setCreateTime(LocalDateTime.now())
				.setIp(IpUtils.getIpAddr(request))
				.setMethod(request.getMethod())
				.setUserAgent(request.getHeader("user-agent"))
				.setUri(URLUtil.getPath(request.getRequestURI()))
				.setType(operationLogging.type().getValue())
				.setMsg(operationLogging.msg())
				.setParams(getParams(joinPoint))
				.setTraceId(MDC.get(LogConstant.TRACE_ID));
		// @formatter:on

		// 操作用户
		User user = SecurityUtils.getUser();
		if (user != null) {
			operationLog.setOperator(user.getUsername());
		}

		// 执行时长
		operationLog.setTime(executionTime);

		// 执行状态
		LogStatusEnum logStatusEnum = throwable == null ? LogStatusEnum.SUCCESS : LogStatusEnum.FAIL;
		operationLog.setStatus(logStatusEnum.getValue());

		return operationLog;
	}

}

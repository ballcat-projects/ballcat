package com.hccake.ballcat.log.handler;

import cn.hutool.core.util.URLUtil;
import com.hccake.ballcat.common.core.util.WebUtils;
import com.hccake.ballcat.common.log.constant.LogConstant;
import com.hccake.ballcat.common.log.operation.annotation.OperationLogging;
import com.hccake.ballcat.common.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.common.log.operation.handler.AbstractOperationLogHandler;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.common.util.IpUtils;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.log.model.entity.OperationLog;
import com.hccake.ballcat.log.service.OperationLogService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.MDC;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 20:38
 */
@RequiredArgsConstructor
public class CustomOperationLogHandler extends AbstractOperationLogHandler<OperationLog> {

	private final OperationLogService operationLogService;

	@Override
	public OperationLog buildLog(OperationLogging operationLogging, ProceedingJoinPoint joinPoint) {
		// 获取 Request
		HttpServletRequest request = WebUtils.getRequest();

		// @formatter:off
		OperationLog operationLog = new OperationLog()
				.setCreateTime(LocalDateTime.now())
				.setIp(IpUtils.getIpAddr(request))
				.setMethod(request.getMethod())
				.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT))
				.setUri(URLUtil.getPath(request.getRequestURI()))
				.setType(operationLogging.type())
				.setMsg(operationLogging.msg())
				.setTraceId(MDC.get(LogConstant.TRACE_ID));
		// @formatter:on

		// 请求参数
		if (operationLogging.recordParams()) {
			operationLog.setParams(getParams(joinPoint));
		}

		// 操作用户
		Optional.ofNullable(SecurityUtils.getUser()).ifPresent(x -> operationLog.setOperator(x.getUsername()));

		return operationLog;
	}

	@Override
	public OperationLog recordExecutionInfo(OperationLog operationLog, ProceedingJoinPoint joinPoint,
			long executionTime, Throwable throwable, boolean isSaveResult, Object result) {
		// 执行时长
		operationLog.setTime(executionTime);
		// 执行状态
		LogStatusEnum logStatusEnum = throwable == null ? LogStatusEnum.SUCCESS : LogStatusEnum.FAIL;
		operationLog.setStatus(logStatusEnum.getValue());
		// 执行结果
		if (isSaveResult) {
			Optional.ofNullable(result).ifPresent(x -> operationLog.setResult(JsonUtils.toJson(x)));
		}
		return operationLog;
	}

	@Override
	public void handleLog(OperationLog operationLog) {
		// 异步保存
		operationLogService.saveAsync(operationLog);
	}

}

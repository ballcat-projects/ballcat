package com.hccake.ballcat.admin.oauth.listener;

import cn.hutool.core.util.URLUtil;
import cn.hutool.json.JSONUtil;
import com.hccake.ballcat.commom.log.constant.LogConstant;
import com.hccake.ballcat.commom.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.commom.log.operation.enums.OperationTypeEnum;
import com.hccake.ballcat.commom.log.operation.model.OperationLogDTO;
import com.hccake.ballcat.commom.log.operation.service.OperationLogHandler;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.core.util.IPUtil;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 登陆成功监听器
 *
 * @author Hccake
 * @version 1.0
 * @date 2020 /6/9 20:52
 */
@RequiredArgsConstructor
@Component
public class LoginEventListener {

	private final OperationLogHandler operationLogHandler;

	/**
	 * 登陆成功时间监听 记录用户登录日志
	 * @param event 登陆成功 event
	 */
	@EventListener(AuthenticationSuccessEvent.class)
	public void onAuthenticationSuccessEvent(AuthenticationSuccessEvent event) {

		AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
		Object details = source.getDetails();
		if (!(details instanceof HashMap)) {
			return;
		}
		// TODO 暂时只记录了 password 模式，其他的第三方登陆，等 spring-authorization-server 孵化后重构
		// https://github.com/spring-projects-experimental/spring-authorization-server
		if ("password".equals(((HashMap) details).get("grant_type"))) {
			// 记录登陆日志
			OperationLogDTO operationLogDTO = prodOperationLogDTO(source).setType(OperationTypeEnum.LOGIN.getValue())
					.setMsg("Login Success");
			operationLogHandler.saveLog(operationLogDTO);
		}
	}

	/**
	 * On logout success event.
	 * @param event the event
	 */
	@EventListener(AbstractAuthenticationFailureEvent.class)
	public void onAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
		AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
		// 记录登出日志
		OperationLogDTO operationLogDTO = prodOperationLogDTO(source).setType(OperationTypeEnum.LOGIN.getValue())
				.setMsg("Login Error：" + event.getException().getMessage()).setStatus(LogStatusEnum.FAIL.getValue());
		operationLogHandler.saveLog(operationLogDTO);
	}

	/**
	 * On logout success event.
	 * @param event the event
	 */
	@EventListener(LogoutSuccessEvent.class)
	public void onLogoutSuccessEvent(LogoutSuccessEvent event) {
		AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
		// 记录登出日志
		OperationLogDTO operationLogDTO = prodOperationLogDTO(source).setType(OperationTypeEnum.LOGOUT.getValue())
				.setMsg("Logout Success");
		operationLogHandler.saveLog(operationLogDTO);
	}

	/**
	 * 根据token和请求信息产生一个操作日志
	 * @param source AbstractAuthenticationToken 当前token
	 * @return OperationLogDTO 操作日志DTO
	 */
	private OperationLogDTO prodOperationLogDTO(AbstractAuthenticationToken source) {
		// 获取 Request
		HttpServletRequest request = LogUtils.getHttpServletRequest();
		return new OperationLogDTO().setCreateTime(LocalDateTime.now()).setIp(IPUtil.getIpAddr(request))
				.setMethod(request.getMethod()).setStatus(LogStatusEnum.SUCCESS.getValue())
				.setUserAgent(request.getHeader("user-agent")).setUri(URLUtil.getPath(request.getRequestURI()))
				.setTraceId(MDC.get(LogConstant.TRACE_ID)).setParams(JSONUtil.toJsonStr(request.getParameterMap()))
				.setTime(0L).setOperator(source.getName());
	}

}

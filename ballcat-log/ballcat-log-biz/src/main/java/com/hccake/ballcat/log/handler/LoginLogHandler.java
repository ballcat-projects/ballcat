package com.hccake.ballcat.log.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.hccake.ballcat.commom.log.constant.LogConstant;
import com.hccake.ballcat.commom.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.commom.log.util.LogUtils;
import com.hccake.ballcat.common.util.IpUtils;
import com.hccake.ballcat.log.enums.LoginEventTypeEnum;
import com.hccake.ballcat.log.model.entity.LoginLog;
import com.hccake.ballcat.log.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.slf4j.MDC;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;

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
public class LoginLogHandler {

	private final LoginLogService loginLogService;

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
			LoginLog loginLog = prodLoginLog(source).setMsg("登陆成功").setStatus(LogStatusEnum.SUCCESS.getValue())
					.setEventType(LoginEventTypeEnum.LOGIN.getValue());
			loginLogService.save(loginLog);
		}
	}

	/**
	 * 监听鉴权失败事件
	 * @param event the event
	 */
	@EventListener(AbstractAuthenticationFailureEvent.class)
	public void onAuthenticationFailureEvent(AbstractAuthenticationFailureEvent event) {
		AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
		LoginLog loginLog = prodLoginLog(source).setMsg(event.getException().getMessage())
				.setEventType(LoginEventTypeEnum.LOGIN.getValue()).setStatus(LogStatusEnum.FAIL.getValue());
		loginLogService.save(loginLog);
	}

	/**
	 * On logout success event.
	 * @param event the event
	 */
	@EventListener(LogoutSuccessEvent.class)
	public void onLogoutSuccessEvent(LogoutSuccessEvent event) {
		AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
		LoginLog loginLog = prodLoginLog(source).setMsg("登出成功").setEventType(LoginEventTypeEnum.LOGOUT.getValue());
		loginLogService.save(loginLog);
	}

	/**
	 * 根据token和请求信息产生一个登陆日志
	 * @param source AbstractAuthenticationToken 当前token
	 * @return LoginLog 登陆日志
	 */
	private LoginLog prodLoginLog(AbstractAuthenticationToken source) {
		// 获取 Request
		HttpServletRequest request = LogUtils.getHttpServletRequest();
		LoginLog loginLog = new LoginLog().setLoginTime(LocalDateTime.now()).setIp(IpUtils.getIpAddr(request))
				.setStatus(LogStatusEnum.SUCCESS.getValue()).setTraceId(MDC.get(LogConstant.TRACE_ID))
				.setUsername(source.getName());
		// 根据 ua 获取浏览器和操作系统
		UserAgent ua = UserAgentUtil.parse(request.getHeader("user-agent"));
		if (ua != null) {
			loginLog.setBrowser(ua.getBrowser().getName()).setOs(ua.getOs().getName());
		}
		return loginLog;
	}

}

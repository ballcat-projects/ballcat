package com.hccake.ballcat.admin.upms.log;

import com.hccake.ballcat.common.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.log.enums.LoginEventTypeEnum;
import com.hccake.ballcat.log.model.entity.LoginLog;
import com.hccake.ballcat.log.service.LoginLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.LogoutSuccessEvent;

import java.util.HashMap;

import static com.hccake.ballcat.log.handler.LoginLogUtils.prodLoginLog;

/**
 * 登陆成功监听器
 *
 * @author Hccake
 * @version 1.0
 * @date 2020 /6/9 20:52
 */
@Deprecated
@RequiredArgsConstructor
public class SpringOauth2LoginLogHandler implements LoginLogHandler {

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
			LoginLog loginLog = prodLoginLog(source.getName()).setMsg("登陆成功")
				.setStatus(LogStatusEnum.SUCCESS.getValue())
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
		if (source instanceof UsernamePasswordAuthenticationToken) {
			LoginLog loginLog = prodLoginLog(source.getName()).setMsg(event.getException().getMessage())
				.setEventType(LoginEventTypeEnum.LOGIN.getValue())
				.setStatus(LogStatusEnum.FAIL.getValue());
			loginLogService.save(loginLog);
		}
	}

	/**
	 * On logout success event.
	 * @param event the event
	 */
	@EventListener(LogoutSuccessEvent.class)
	public void onLogoutSuccessEvent(LogoutSuccessEvent event) {
		AbstractAuthenticationToken source = (AbstractAuthenticationToken) event.getSource();
		LoginLog loginLog = prodLoginLog(source.getName()).setMsg("登出成功")
			.setEventType(LoginEventTypeEnum.LOGOUT.getValue());
		loginLogService.save(loginLog);
	}

}

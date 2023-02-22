package com.hccake.ballcat.log.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.hccake.ballcat.common.core.util.WebUtils;
import com.hccake.ballcat.common.log.constant.LogConstant;
import com.hccake.ballcat.common.log.operation.enums.LogStatusEnum;
import com.hccake.ballcat.common.util.IpUtils;
import com.hccake.ballcat.log.model.entity.LoginLog;
import org.slf4j.MDC;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author hccake
 */
public final class LoginLogUtils {

	private LoginLogUtils() {
	}

	/**
	 * 根据token和请求信息产生一个登陆日志
	 * @param username 用户名
	 * @return LoginLog 登陆日志
	 */
	public static LoginLog prodLoginLog(String username) {
		// 获取 Request
		HttpServletRequest request = WebUtils.getRequest();
		LoginLog loginLog = new LoginLog().setLoginTime(LocalDateTime.now())
			.setIp(IpUtils.getIpAddr(request))
			.setStatus(LogStatusEnum.SUCCESS.getValue())
			.setTraceId(MDC.get(LogConstant.TRACE_ID))
			.setUsername(username);
		// 根据 ua 获取浏览器和操作系统
		UserAgent ua = UserAgentUtil.parse(request.getHeader("user-agent"));
		if (ua != null) {
			loginLog.setBrowser(ua.getBrowser().getName()).setOs(ua.getOs().getName());
		}
		return loginLog;
	}

}

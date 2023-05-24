package com.hccake.ballcat.log.handler;

import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.hccake.ballcat.common.core.constant.MDCConstants;
import com.hccake.ballcat.common.core.util.WebUtils;
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
	 * 根据token和请求信息产生一个登录日志
	 * @param username 用户名
	 * @return LoginLog 登录日志
	 */
	public static LoginLog prodLoginLog(String username) {
		// 获取 Request
		HttpServletRequest request = WebUtils.getRequest();
		LoginLog loginLog = new LoginLog().setLoginTime(LocalDateTime.now())
			.setIp(IpUtils.getIpAddr(request))
			.setStatus(LogStatusEnum.SUCCESS.getValue())
			.setTraceId(MDC.get(MDCConstants.TRACE_ID_KEY))
			.setUsername(username);
		// 根据 ua 获取浏览器和操作系统
		UserAgent ua = UserAgentUtil.parse(request.getHeader("user-agent"));
		if (ua != null) {
			loginLog.setBrowser(ua.getBrowser().getName()).setOs(ua.getOs().getName());
		}
		return loginLog;
	}

}

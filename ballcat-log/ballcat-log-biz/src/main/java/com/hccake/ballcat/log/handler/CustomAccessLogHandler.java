package com.hccake.ballcat.log.handler;

import cn.hutool.core.util.URLUtil;
import com.hccake.ballcat.common.desensitize.DesensitizationHandlerHolder;
import com.hccake.ballcat.common.desensitize.enums.RegexDesensitizationTypeEnum;
import com.hccake.ballcat.common.log.access.handler.AccessLogHandler;
import com.hccake.ballcat.common.log.constant.LogConstant;
import com.hccake.ballcat.common.log.util.LogUtils;
import com.hccake.ballcat.common.security.util.SecurityUtils;
import com.hccake.ballcat.common.util.IpUtils;
import com.hccake.ballcat.common.util.JsonUtils;
import com.hccake.ballcat.log.model.entity.AccessLog;
import com.hccake.ballcat.log.thread.AccessLogSaveThread;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 访问日志
 *
 * @author hccake
 * @date 2019-10-16 16:09:25
 */
@Slf4j
public class CustomAccessLogHandler implements AccessLogHandler<AccessLog> {

	private static final String APPLICATION_JSON = "application/json";

	private final AccessLogSaveThread accessLogSaveThread;

	public CustomAccessLogHandler(AccessLogSaveThread accessLogSaveThread) {
		if (!accessLogSaveThread.isAlive()) {
			accessLogSaveThread.start();
		}
		this.accessLogSaveThread = accessLogSaveThread;
	}

	/**
	 * 需要脱敏记录的参数
	 */
	private final List<String> needDesensitizeParams = Arrays.asList("password", "pass", "passConfirm");

	/**
	 * 生产一个日志
	 * @return accessLog
	 * @param request 请求信息
	 * @param response 响应信息
	 * @param time 执行时长
	 * @param myThrowable 异常信息
	 */
	@Override
	public AccessLog buildLog(HttpServletRequest request, HttpServletResponse response, Long time,
			Throwable myThrowable) {
		Object matchingPatternAttr = request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
		String matchingPattern = matchingPatternAttr == null ? "" : String.valueOf(matchingPatternAttr);
		// @formatter:off
		String uri = URLUtil.getPath(request.getRequestURI());
		AccessLog accessLog = new AccessLog()
				.setTraceId(MDC.get(LogConstant.TRACE_ID))
				.setCreateTime(LocalDateTime.now())
				.setTime(time)
				.setIp(IpUtils.getIpAddr(request))
				.setMethod(request.getMethod())
				.setUserAgent(request.getHeader("user-agent"))
				.setUri(uri)
				.setMatchingPattern(matchingPattern)
				.setErrorMsg(Optional.ofNullable(myThrowable).map(Throwable::getMessage).orElse(""))
				.setHttpStatus(response.getStatus());
		// @formatter:on

		// 参数获取
		String params = getParams(request);
		accessLog.setReqParams(params);

		// 非文件上传请求，记录body，用户改密时不记录body
		// TODO 使用注解控制此次请求是否记录body，更方便个性化定制
		if (!LogUtils.isMultipartContent(request) && !"/system/user/pass/{userId}".equals(matchingPattern)) {
			accessLog.setReqBody(LogUtils.getRequestBody(request));
		}

		// 只记录响应头为 application/json 的返回数据
		// 后台日志对于分页数据请求，不记录返回值
		if (!uri.endsWith("/page") && response.getContentType() != null
				&& response.getContentType().contains(APPLICATION_JSON)) {
			accessLog.setResult(LogUtils.getResponseBody(request, response));
		}

		// 如果登陆用户 则记录用户名和用户id
		Optional.ofNullable(SecurityUtils.getUser()).ifPresent(x -> {
			accessLog.setUserId(x.getUserId());
			accessLog.setUsername(x.getUsername());
		});

		return accessLog;
	}

	/**
	 * 获取参数信息
	 * @param request 请求信息
	 * @return 请求参数
	 */
	public String getParams(HttpServletRequest request) {
		String params;
		try {
			Map<String, String[]> parameterMap = request.getParameterMap();
			for (String paramKey : needDesensitizeParams) {
				String[] values = parameterMap.get(paramKey);
				if (values != null && values.length != 0) {
					String value = DesensitizationHandlerHolder.getRegexDesensitizationHandler().handle(values[0],
							RegexDesensitizationTypeEnum.ENCRYPTED_PASSWORD);
					parameterMap.put(paramKey, new String[] { value });
				}
			}
			params = JsonUtils.toJson(parameterMap);
		}
		catch (Exception e) {
			params = "记录参数异常";
			log.error("[prodLog]，参数获取序列化异常", e);
		}
		return params;
	}

	/**
	 * 记录日志
	 * @param accessLog 访问日志
	 */
	@Override
	public void saveLog(AccessLog accessLog) {
		accessLogSaveThread.put(accessLog);
	}

}

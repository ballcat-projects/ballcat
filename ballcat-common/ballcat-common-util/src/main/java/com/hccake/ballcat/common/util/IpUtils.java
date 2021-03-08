package com.hccake.ballcat.common.util;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.extra.servlet.ServletUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * IP 工具类
 *
 * @author Hccake
 */
public final class IpUtils {

	private IpUtils() {
	}

	/**
	 * 如果在前端和服务端中间还有一层Node服务 在Node对前端数据进行处理并发起新请求时，需携带此头部信息 便于获取真实IP
	 */
	public static final String NODE_FORWARDED_IP = "Node-Forwarded-IP";

	/**
	 * 获取客户端IP
	 */
	public static String getIpAddr(HttpServletRequest request) {
		return getIpAddr(request, NODE_FORWARDED_IP);
	}

	/**
	 * 获取客户端IP
	 *
	 * 参考 huTool 稍微调整了下headers 顺序
	 */
	public static String getIpAddr(HttpServletRequest request, String... otherHeaderNames) {
		String[] headers = { "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}
		return ServletUtil.getClientIPByHeader(request, headers);
	}

}

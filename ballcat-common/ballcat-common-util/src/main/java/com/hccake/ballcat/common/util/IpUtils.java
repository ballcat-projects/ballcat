package com.hccake.ballcat.common.util;

import cn.hutool.core.net.NetUtil;
import cn.hutool.core.util.ArrayUtil;

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
	 * <p>
	 * 参考 huTool 稍微调整了下headers 顺序
	 */
	public static String getIpAddr(HttpServletRequest request, String... otherHeaderNames) {
		String[] headers = { "X-Real-IP", "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP",
				"HTTP_X_FORWARDED_FOR" };
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}
		return getClientIpByHeader(request, headers);
	}

	/**
	 * 获取客户端IP
	 *
	 * <p>
	 * headerNames参数用于自定义检测的Header<br>
	 * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造。
	 * </p>
	 * @param request 请求对象{@link HttpServletRequest}
	 * @param headerNames 自定义头，通常在Http服务器（例如Nginx）中配置
	 * @return IP地址
	 * @since 4.4.1
	 */
	public static String getClientIpByHeader(HttpServletRequest request, String... headerNames) {
		String ip;
		for (String header : headerNames) {
			ip = request.getHeader(header);
			if (!NetUtil.isUnknown(ip)) {
				return NetUtil.getMultistageReverseProxyIp(ip);
			}
		}

		ip = request.getRemoteAddr();
		return NetUtil.getMultistageReverseProxyIp(ip);
	}

}

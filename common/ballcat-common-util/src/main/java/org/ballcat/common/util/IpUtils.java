/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.common.util;

import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;

import org.ballcat.common.constant.Symbol;
import org.springframework.util.ObjectUtils;

/**
 * IP 工具类
 *
 * @author Hccake
 */
public final class IpUtils {

	private IpUtils() {
	}

	private static final String[] CLIENT_IP_HEADERS = { "X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP",
			"WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };

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
		return getClientIpByHeader(request, mergeClientIpHeaders(otherHeaderNames));
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
			if (ip != null && checkNotUnknown(ip)) {
				return getMultistageReverseProxyIp(ip);
			}
		}
		ip = request.getRemoteAddr();
		return getMultistageReverseProxyIp(ip);
	}

	private static String[] mergeClientIpHeaders(String... otherHeaderNames) {
		if (ObjectUtils.isEmpty(otherHeaderNames)) {
			return CLIENT_IP_HEADERS;
		}
		return Stream.concat(Stream.of(CLIENT_IP_HEADERS), Stream.of(otherHeaderNames)).toArray(String[]::new);
	}

	/**
	 * 多次反向代理后会有多个ip值，第一个ip才是真实ip
	 * @param ip ip
	 * @return 真实ip
	 */
	private static String getMultistageReverseProxyIp(String ip) {
		if (null == ip || ip.indexOf(Symbol.COMMA) <= 0) {
			return ip;
		}
		String[] ips = ip.trim().split(Symbol.COMMA);
		for (String subIp : ips) {
			if (checkNotUnknown(subIp)) {
				return subIp;
			}
		}
		return ip;
	}

	private static boolean checkNotUnknown(String checkString) {
		return !"unknown".equalsIgnoreCase(checkString);
	}

}

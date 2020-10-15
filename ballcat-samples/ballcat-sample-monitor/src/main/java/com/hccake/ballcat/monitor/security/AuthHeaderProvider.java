package com.hccake.ballcat.monitor.security;

import cn.hutool.crypto.SecureUtil;
import com.hccake.ballcat.common.core.constant.HeaderConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import de.codecentric.boot.admin.server.domain.entities.Instance;
import de.codecentric.boot.admin.server.web.client.HttpHeadersProvider;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/31 11:03
 */
@Component
public class AuthHeaderProvider implements HttpHeadersProvider {

	@Value("${ballcat.actuator.auth:false}")
	private Boolean auth;

	@Value("${ballcat.actuator.secret-id:ballcat-monitor}")
	private String secretId;

	@Value("${ballcat.actuator.secret-key:=BallCat-Monitor}")
	private String secretKey;

	/**
	 * 当授权中心剥离时，目前BallCat的设计是简单启动 所以不做独立的授权中心，在此记录下拓展使用方式 可以考虑 client_credentials 客户端授权模式
	 *
	 * @param instance
	 * @return
	 */
	@Override
	public HttpHeaders getHeaders(Instance instance) {
		HttpHeaders headers = new HttpHeaders();

		if (auth) {
			// 当前时间戳
			long reqTime = System.currentTimeMillis();
			headers.set(HeaderConstants.REQ_TIME, String.valueOf(reqTime));
			// 客户端ID
			headers.set(HeaderConstants.SECRET_ID, secretId);
			// sign
			String tempSign =
					new StringBuilder().append(reqTime).reverse().append(secretId).append(secretKey).toString();
			String sign = SecureUtil.md5(tempSign);
			headers.set(HeaderConstants.SIGN, sign);
		}
		return headers;
	}

}

package org.ballcat.security.web.resource;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.ballcat.common.util.JsonUtils;
import org.ballcat.security.SecurityToken;
import org.ballcat.security.mapstruct.SecurityMapstruct;
import org.ballcat.security.resources.SecurityResourceService;
import org.ballcat.security.resources.SecurityScope;
import org.ballcat.security.vo.AuthorizationVO;
import org.ballcat.security.web.constant.SecurityWebConstants;
import org.springframework.util.StringUtils;

/**
 * @author lingting 2023-03-31 10:53
 */
@Slf4j
public class DefaultRemoteResourceServiceImpl implements SecurityResourceService {

	public static final String SUFFIX = "/";

	private final OkHttpClient client;

	private final String urlResolve;

	public DefaultRemoteResourceServiceImpl(String host, OkHttpClient client) {
		this.client = client;
		this.urlResolve = join(host, SecurityWebConstants.URI_RESOLVE);
	}

	@Override
	public SecurityScope resolve(SecurityToken token) {
		Request.Builder builder = new Request.Builder();
		builder.get().url(urlResolve);
		builder = fillSecurity(builder, token);
		AuthorizationVO vo = request(builder, AuthorizationVO.class);
		return SecurityMapstruct.INSTANCE.ofVo(vo);
	}

	protected <T> T request(Request.Builder builder, Class<T> cls) {
		Call call = client.newCall(builder.build());
		try (Response execute = call.execute()) {
			ResponseBody responseBody = execute.body();
			if (responseBody == null) {
				return null;
			}
			String string = responseBody.string();
			return JsonUtils.toObj(string, cls);
		}
		catch (Exception e) {
			log.error("远端token解析失败!", e);
			return null;
		}
	}

	protected Request.Builder fillSecurity(Request.Builder builder, SecurityToken token) {
		builder.header(SecurityWebConstants.TOKEN_HEADER,
				String.format("%s %s",
						StringUtils.hasText(token.getType()) ? token.getType() : SecurityWebConstants.TOKEN_TYPE_BEARER,
						token.getToken()));
		return builder;
	}

	protected String join(String host, String uri) {
		if (!StringUtils.hasText(host)) {
			throw new IllegalArgumentException("remoteHost is not Null!");
		}
		StringBuilder builder = new StringBuilder(host);
		if (!host.endsWith(SUFFIX)) {
			builder.append(SUFFIX);
		}

		if (uri.startsWith(SUFFIX)) {
			uri = uri.substring(1);
		}
		builder.append(uri);
		return builder.toString();
	}

}

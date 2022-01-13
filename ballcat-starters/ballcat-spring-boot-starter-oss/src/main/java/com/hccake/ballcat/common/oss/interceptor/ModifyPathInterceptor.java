package com.hccake.ballcat.common.oss.interceptor;

import static com.hccake.ballcat.common.oss.OssConstants.SLASH;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.http.SdkHttpRequest;

/**
 * <p>
 * oss 拦截器
 * </p>
 * <p>
 * 修改发出请求的路径, 如果路径前携带了 bucket , 则移除
 * </p>
 *
 * @author lingting 2021/5/12 18:46
 */
@RequiredArgsConstructor
public class ModifyPathInterceptor implements ExecutionInterceptor {

	private final String bucket;

	@Override
	public SdkHttpRequest modifyHttpRequest(Context.ModifyHttpRequest context,
			ExecutionAttributes executionAttributes) {

		SdkHttpRequest request = context.httpRequest();
		final SdkHttpRequest.Builder rb = SdkHttpRequest.builder()

				.protocol(request.protocol())

				.host(request.host())

				.port(request.port())

				.headers(request.headers())

				.method(request.method())

				.rawQueryParameters(request.rawQueryParameters());

		// 移除 path 前的 bucket 声明
		if (request.encodedPath().startsWith(SLASH + bucket)) {
			rb.encodedPath(request.encodedPath().substring((SLASH + bucket).length()));
		}
		else {
			rb.encodedPath(request.encodedPath());
		}

		return rb.build();
	}

}

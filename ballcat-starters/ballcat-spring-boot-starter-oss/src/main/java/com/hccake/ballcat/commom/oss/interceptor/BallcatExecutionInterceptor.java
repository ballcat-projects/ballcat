package com.hccake.ballcat.commom.oss.interceptor;

import static com.hccake.ballcat.commom.oss.OssConstants.DOT;
import static com.hccake.ballcat.commom.oss.OssConstants.SLASH;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import software.amazon.awssdk.core.interceptor.Context;
import software.amazon.awssdk.core.interceptor.ExecutionAttributes;
import software.amazon.awssdk.core.interceptor.ExecutionInterceptor;
import software.amazon.awssdk.http.SdkHttpRequest;

/**
 * oss 拦截器
 *
 * @author lingting 2021/5/12 18:46
 */
@Getter
@RequiredArgsConstructor
public class BallcatExecutionInterceptor implements ExecutionInterceptor {

	private final String endpoint;

	private final String accessKey;

	private final String accessSecret;

	private final String bucket;

	private final String root;

	@SneakyThrows
	@Override
	public SdkHttpRequest modifyHttpRequest(Context.ModifyHttpRequest context,
			ExecutionAttributes executionAttributes) {

		SdkHttpRequest request = context.httpRequest();
		final SdkHttpRequest.Builder rb = SdkHttpRequest.builder()

				.protocol(request.protocol())

				.port(request.port())

				.headers(request.headers())

				.method(request.method())

				.rawQueryParameters(request.rawQueryParameters());

		// 根据文档
		// https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/VirtualHosting.html
		// 把 host 改为 bucket.region... 形式
		if (!request.host().startsWith(bucket)) {
			rb.host(bucket + DOT + request.host());
		}
		else {
			rb.host(request.host());
		}

		// host 修改后, 需要移除 path 前的 bucket 声明
		if (request.encodedPath().startsWith(SLASH + bucket)) {
			rb.encodedPath(request.encodedPath().substring((SLASH + bucket).length()));
		}
		else {
			rb.encodedPath(request.encodedPath());
		}

		return rb.build();
	}

}

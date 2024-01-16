package org.ballcat.grpc.client.configuration;

import io.grpc.ClientInterceptor;
import lombok.RequiredArgsConstructor;
import org.ballcat.grpc.client.GrpcClientProvide;
import org.ballcat.grpc.client.interceptor.GrpcClientTraceIdInterceptor;
import org.ballcat.grpc.client.properties.GrpcClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2023-04-06 15:21
 */
@AutoConfiguration
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcClientProperties.class)
public class GrpcClientAutoConfiguration {

	private final GrpcClientProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientProvide grpcClientProvide(List<ClientInterceptor> interceptors) {
		return new GrpcClientProvide(properties, interceptors);
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcClientTraceIdInterceptor grpcClientTraceIdInterceptor() {
		return new GrpcClientTraceIdInterceptor(properties);
	}

}

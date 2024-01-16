package org.ballcat.grpc.server.configuration;

import io.grpc.BindableService;
import io.grpc.ServerInterceptor;
import lombok.RequiredArgsConstructor;
import org.ballcat.grpc.server.GrpcServer;
import org.ballcat.grpc.server.interceptor.GrpcServerTraceIdInterceptor;
import org.ballcat.grpc.server.properties.GrpcServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author lingting 2023-04-17 09:15
 */
@RequiredArgsConstructor
@EnableConfigurationProperties(GrpcServerProperties.class)
public class GrpcServerConfiguration {

	private final GrpcServerProperties properties;

	@Bean
	@ConditionalOnMissingBean
	public GrpcServer grpcServer(List<ServerInterceptor> interceptors, List<BindableService> services) {
		return new GrpcServer(properties, interceptors, services);
	}

	@Bean
	@ConditionalOnMissingBean
	public GrpcServerTraceIdInterceptor grpcServerTraceIdInterceptor() {
		return new GrpcServerTraceIdInterceptor(properties);
	}

}

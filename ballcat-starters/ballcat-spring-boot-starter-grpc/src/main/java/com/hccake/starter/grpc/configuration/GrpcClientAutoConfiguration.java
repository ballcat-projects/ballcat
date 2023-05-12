package com.hccake.starter.grpc.configuration;

import com.hccake.starter.grpc.GrpcClientChannel;
import com.hccake.starter.grpc.interceptor.TraceIdInterceptor;
import com.hccake.starter.grpc.properties.GrpcClientProperties;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @author lingting 2023-04-06 15:21
 */
@AutoConfiguration
@EnableConfigurationProperties(GrpcClientProperties.class)
public class GrpcClientAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	@ConditionalOnProperty(prefix = GrpcClientProperties.PREFIX, name = "host")
	public GrpcClientChannel grpcChannel(GrpcClientProperties properties) {
		if (properties.getPort() == null || properties.getPort() < 0) {
			throw new IllegalArgumentException(GrpcClientProperties.PREFIX + ".port值不能为: " + properties.getPort());
		}
		return new GrpcClientChannel(properties);
	}

	@Bean
	@ConditionalOnMissingBean
	public TraceIdInterceptor traceIdInterceptor() {
		return new TraceIdInterceptor();
	}

}

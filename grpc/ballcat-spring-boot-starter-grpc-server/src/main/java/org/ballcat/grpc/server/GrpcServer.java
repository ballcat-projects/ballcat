package org.ballcat.grpc.server;

import io.grpc.BindableService;
import io.grpc.MethodDescriptor;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.ServerInterceptor;
import io.grpc.ServerMethodDefinition;
import io.grpc.ServerServiceDefinition;
import io.grpc.ServiceDescriptor;
import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.core.compose.ContextComponent;
import org.ballcat.common.thread.ThreadPool;
import org.ballcat.common.util.ClassUtils;
import org.ballcat.grpc.server.properties.GrpcServerProperties;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author lingting 2023-04-14 17:38
 */
@Slf4j
@Getter
public class GrpcServer implements ContextComponent {

	private final Server server;

	private final Map<String, Class<? extends BindableService>> serviceNameMap;

	private final Map<String, Method> fullMethodNameMap;

	public GrpcServer(GrpcServerProperties properties, List<ServerInterceptor> interceptors,
			List<BindableService> services) {
		this(ServerBuilder.forPort(properties.getPort()), properties, interceptors, services);
	}

	public GrpcServer(ServerBuilder<?> builder, GrpcServerProperties properties, List<ServerInterceptor> interceptors,
			List<BindableService> services) {
		builder
			// 单个消息最大大小
			.maxInboundMessageSize((int) properties.getMessageSize().toBytes())
			.keepAliveTime(properties.getKeepAliveTime(), TimeUnit.MILLISECONDS)
			.keepAliveTimeout(properties.getKeepAliveTimeout(), TimeUnit.MILLISECONDS);

		// 按照spring生态的 @Order 排序
		interceptors.sort(AnnotationAwareOrderComparator.INSTANCE);
		// 获取一个游标在尾部的迭代器
		ListIterator<ServerInterceptor> iterator = interceptors.listIterator(interceptors.size());
		// 服务端是最后注册的拦截器最先执行, 所以要倒序注册
		while (iterator.hasPrevious()) {
			builder.intercept(iterator.previous());
		}

		this.serviceNameMap = new HashMap<>();
		this.fullMethodNameMap = new HashMap<>();

		// 注册服务
		for (BindableService service : services) {
			builder.addService(service);

			Class<? extends BindableService> cls = service.getClass();

			ServerServiceDefinition serverServiceDefinition = service.bindService();
			ServiceDescriptor serviceDescriptor = serverServiceDefinition.getServiceDescriptor();

			serviceNameMap.put(serviceDescriptor.getName(), cls);

			for (ServerMethodDefinition<?, ?> serverMethodDefinition : serverServiceDefinition.getMethods()) {
				MethodDescriptor<?, ?> methodDescriptor = serverMethodDefinition.getMethodDescriptor();
				String fullMethodName = methodDescriptor.getFullMethodName();
				fullMethodNameMap.put(fullMethodName, resolve(methodDescriptor, cls));
			}
		}

		this.server = builder.build();
	}

	public boolean isRunning() {
		return !server.isShutdown() && !server.isTerminated();
	}

	public int port() {
		return server.getPort();
	}

	public Class<? extends BindableService> findClass(ServiceDescriptor descriptor) {
		return serviceNameMap.get(descriptor.getName());
	}

	@SuppressWarnings("unchecked")
	public Class<? extends BindableService> findClass(MethodDescriptor<?, ?> descriptor) {
		Method method = findMethod(descriptor);
		return (Class<? extends BindableService>) method.getDeclaringClass();
	}

	public Method findMethod(MethodDescriptor<?, ?> descriptor) {
		return fullMethodNameMap.get(descriptor.getFullMethodName());
	}

	protected Method resolve(MethodDescriptor<?, ?> descriptor, Class<? extends BindableService> cls) {
		String bareMethodName = descriptor.getBareMethodName();

		for (Method method : ClassUtils.methods(cls)) {
			if (Objects.equals(method.getName(), bareMethodName)) {
				return method;
			}
		}

		return null;
	}

	@Override
	@SneakyThrows
	public void onApplicationStart() {
		server.start();
		log.debug("grpc服务启动. 端口: {}", server.getPort());
		ThreadPool.instance().execute("GrpcServer", server::awaitTermination);
	}

	@Override
	public void onApplicationStop() {
		log.warn("grpc服务开始关闭");
		server.shutdownNow();
		log.warn("grpc服务关闭");
	}

}

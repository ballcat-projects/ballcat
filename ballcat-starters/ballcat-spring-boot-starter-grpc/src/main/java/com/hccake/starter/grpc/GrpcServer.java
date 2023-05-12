package com.hccake.starter.grpc;

import com.hccake.ballcat.common.core.compose.ContextComponent;
import com.hccake.ballcat.common.thread.ThreadPool;
import io.grpc.Server;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

/**
 * @author lingting 2023-04-14 17:38
 */
@Slf4j
public class GrpcServer implements ContextComponent {

	private static final ThreadPool THREAD_POOL = ThreadPool.instance();

	private final Server server;

	public GrpcServer(Server server) {
		this.server = server;
	}

	@Override
	@SneakyThrows
	public void onApplicationStart() {
		server.start();
		log.debug("grpc服务启动. 端口: {}", server.getPort());
		THREAD_POOL.execute(() -> {
			Thread.currentThread().setName("GrpcServer");
			try {
				server.awaitTermination();
			}
			catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("GrpcServer线程被中断!");
			}
			catch (Exception e) {
				log.error("GrpcServer线程异常!", e);
			}
		});
	}

	@Override
	public void onApplicationStop() {
		log.warn("grpc服务开始关闭");
		server.shutdownNow();
		log.warn("grpc服务关闭");
	}

}

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

package org.ballcat.grpc;

import io.grpc.Server;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.core.compose.ContextComponent;
import org.ballcat.common.thread.ThreadPool;

/**
 * @deprecated use ballcat-spring-boot-starter-grpc-server
 * @author lingting 2023-04-14 17:38
 */
@Slf4j
@Deprecated
public class GrpcServer implements ContextComponent {

	private static final ThreadPool THREAD_POOL = ThreadPool.instance();

	private final Server server;

	public GrpcServer(Server server) {
		this.server = server;
	}

	@Override
	@SneakyThrows
	public void onApplicationStart() {
		this.server.start();
		log.debug("grpc服务启动. 端口: {}", this.server.getPort());
		THREAD_POOL.execute(() -> {
			Thread.currentThread().setName("GrpcServer");
			try {
				this.server.awaitTermination();
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
		this.server.shutdownNow();
		log.warn("grpc服务关闭");
	}

}

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

package org.ballcat.desensitize.autoconfigure;

import org.ballcat.desensitize.logging.logback.LoggerDesensitizationFilter;
import org.ballcat.desensitize.logging.logback.LoggerDesensitizationFilterHolder;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.SmartLifecycle;

/**
 * Logger 过滤器 Holder 绑定器：容器就绪后，将当前过滤器设置到 Holder。
 * <p>
 * Spring Cloud 刷新事件由 Cloud 配置监听，并复用 {@link #bind()} 重新绑定。
 *
 * @author Hccake
 */
public class LoggerDesensitizationFilterHolderBinder implements SmartLifecycle {

	private final ObjectProvider<LoggerDesensitizationFilter> provider;

	private volatile boolean running = false;

	public LoggerDesensitizationFilterHolderBinder(ObjectProvider<LoggerDesensitizationFilter> provider) {
		this.provider = provider;
	}

	@Override
	public void start() {
		bind();
		this.running = true;
	}

	public void bind() {
		LoggerDesensitizationFilter f = this.provider.getIfAvailable();
		if (f != null) {
			LoggerDesensitizationFilterHolder.set(f);
		}
	}

	@Override
	public void stop() {
		this.running = false;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

}

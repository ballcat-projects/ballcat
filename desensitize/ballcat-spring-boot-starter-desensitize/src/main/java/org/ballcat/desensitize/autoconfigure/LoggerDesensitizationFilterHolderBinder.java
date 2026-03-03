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
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.EventListener;

/**
 * Logger 过滤器 Holder 绑定器： - 容器就绪后，将过滤器设置到 Holder； - 收到 RefreshScopeRefreshedEvent
 * 时，重新设置，确保 holder 指向最新代理。
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
		LoggerDesensitizationFilter f = this.provider.getIfAvailable();
		if (f != null) {
			LoggerDesensitizationFilterHolder.set(f);
		}
		this.running = true;
	}

	@Override
	public void stop() {
		this.running = false;
	}

	@Override
	public boolean isRunning() {
		return this.running;
	}

	@EventListener(RefreshScopeRefreshedEvent.class)
	public void onRefresh() {
		LoggerDesensitizationFilter f = this.provider.getIfAvailable();
		if (f != null) {
			LoggerDesensitizationFilterHolder.set(f);
		}
	}

}

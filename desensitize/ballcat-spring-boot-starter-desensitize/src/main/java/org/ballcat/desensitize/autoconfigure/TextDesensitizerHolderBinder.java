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

import org.ballcat.desensitize.logging.logback.TextDesensitizerHolder;
import org.ballcat.desensitize.text.TextDesensitizer;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.cloud.context.scope.refresh.RefreshScopeRefreshedEvent;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.event.EventListener;

/**
 * TextDesensitizer 绑定器： - 容器就绪后，将代理 bean 设置到 TextDesensitizerHolder； - 收到
 * RefreshScopeRefreshedEvent 时，重新设置，确保 holder 指向最新代理。
 *
 * @see TextDesensitizerHolder
 * @author Hccake
 */
public class TextDesensitizerHolderBinder implements SmartLifecycle {

	private final ObjectProvider<TextDesensitizer> engineProvider;

	private volatile boolean running = false;

	public TextDesensitizerHolderBinder(ObjectProvider<TextDesensitizer> engineProvider) {
		this.engineProvider = engineProvider;
	}

	@Override
	public void start() {
		TextDesensitizer engine = this.engineProvider.getIfAvailable();
		if (engine != null) {
			TextDesensitizerHolder.set(engine);
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
		TextDesensitizer engine = this.engineProvider.getIfAvailable();
		if (engine != null) {
			TextDesensitizerHolder.set(engine);
		}
	}

}

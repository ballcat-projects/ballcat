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

package org.ballcat.common.core.thread;

import org.ballcat.common.core.compose.ContextComponent;
import org.slf4j.Logger;

/**
 * @author lingting 2023-04-22 10:40
 */
public abstract class AbstractThreadContextComponent extends Thread implements ContextComponent {

	protected final Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

	protected void init() {

	}

	public boolean isRun() {
		return !isInterrupted() && isAlive();
	}

	@Override
	public void onApplicationStart() {
		setName(getClass().getSimpleName());
		if (!isAlive()) {
			start();
		}
	}

	@Override
	public void onApplicationStop() {
		this.log.warn("{} 线程: {}; 开始关闭!", getClass().getSimpleName(), getId());
		interrupt();
	}

	public String getSimpleName() {
		return getClass().getSimpleName();
	}

	@Override
	public abstract void run();

}

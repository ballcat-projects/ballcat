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

package org.ballcat.common.core.spring.compose;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ballcat.common.core.compose.ContextComponent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

/**
 * @author lingting 2022/10/22 17:45
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpringContextClosed implements ApplicationListener<ContextClosedEvent> {

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		log.debug("spring context closed");
		ApplicationContext applicationContext = event.getApplicationContext();

		// 上下文容器停止
		contextComponentStop(applicationContext);
	}

	private static void contextComponentStop(ApplicationContext applicationContext) {
		Map<String, ContextComponent> contextComponentMap = applicationContext.getBeansOfType(ContextComponent.class);

		for (Map.Entry<String, ContextComponent> entry : contextComponentMap.entrySet()) {
			entry.getValue().onApplicationStop();
		}
	}

}

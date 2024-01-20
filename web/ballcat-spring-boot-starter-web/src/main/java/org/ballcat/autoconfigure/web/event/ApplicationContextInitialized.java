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

package org.ballcat.autoconfigure.web.event;

import org.ballcat.common.util.SpringUtils;
import org.springframework.boot.context.event.ApplicationContextInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * 监听spring上下文初始化完成事件
 *
 * @author lingting 2022/10/15 15:27
 */
public class ApplicationContextInitialized implements ApplicationListener<ApplicationContextInitializedEvent> {

	@Override
	public void onApplicationEvent(ApplicationContextInitializedEvent event) {
		/*
		 * 给 SpringUtils 注入 spring 上下文, 这样子就在任何地方都可以直接使用 SpringUtils了, 不需要额外声明依赖 处于兼容考虑,
		 * 未删除 SpringUtils 中的 @Component 注解, 且由于此实现依赖 spring-boot 所以先放在 starter-web 这里.
		 */
		SpringUtils.setContext(event.getApplicationContext());
	}

}

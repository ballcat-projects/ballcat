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

package org.ballcat.i18n;

import java.util.List;

import org.springframework.context.ApplicationEvent;

/**
 * I18nMessage 的创建事件，Listener 监听此事件，进行 I18nMessage 的存储
 *
 * @author hccake
 */
public class I18nMessageCreateEvent extends ApplicationEvent {

	public I18nMessageCreateEvent(List<I18nMessage> i18nMessages) {
		super(i18nMessages);
	}

	@SuppressWarnings("unchecked")
	public List<I18nMessage> getI18nMessages() {
		return (List<I18nMessage>) super.getSource();
	}

}

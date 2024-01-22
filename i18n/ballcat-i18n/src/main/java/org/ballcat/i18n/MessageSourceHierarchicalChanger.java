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

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * 用于修改 MessageSource 的层级关系，保证 DynamicMessageSource 在父级位置，减少开销
 *
 * @author hccake
 */
public class MessageSourceHierarchicalChanger {

	@Resource(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
	private MessageSource messageSource;

	@Resource(name = DynamicMessageSource.DYNAMIC_MESSAGE_SOURCE_BEAN_NAME)
	private DynamicMessageSource dynamicMessageSource;

	/**
	 * 将 dynamicMessageSource 置为 messageSource 的父级<br/>
	 * 若 messageSource 非层级，则将 messageSource 置为 dynamicMessageSource 的父级
	 */
	@PostConstruct
	public void changeMessageSourceParent() {
		// 优先走 messageSource，从资源文件中查找
		if (this.messageSource instanceof HierarchicalMessageSource) {
			HierarchicalMessageSource hierarchicalMessageSource = (HierarchicalMessageSource) this.messageSource;
			MessageSource parentMessageSource = hierarchicalMessageSource.getParentMessageSource();
			this.dynamicMessageSource.setParentMessageSource(parentMessageSource);
			hierarchicalMessageSource.setParentMessageSource(this.dynamicMessageSource);
		}
		else {
			this.dynamicMessageSource.setParentMessageSource(this.messageSource);
		}
	}

}

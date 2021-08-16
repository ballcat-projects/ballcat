package com.hccake.ballcat.common.i18n;

import org.springframework.context.HierarchicalMessageSource;
import org.springframework.context.MessageSource;
import org.springframework.context.support.AbstractApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

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
		if (messageSource instanceof HierarchicalMessageSource) {
			HierarchicalMessageSource hierarchicalMessageSource = (HierarchicalMessageSource) messageSource;
			MessageSource parentMessageSource = hierarchicalMessageSource.getParentMessageSource();
			dynamicMessageSource.setParentMessageSource(parentMessageSource);
			hierarchicalMessageSource.setParentMessageSource(dynamicMessageSource);
		}
		else {
			dynamicMessageSource.setParentMessageSource(messageSource);
		}
	}

}
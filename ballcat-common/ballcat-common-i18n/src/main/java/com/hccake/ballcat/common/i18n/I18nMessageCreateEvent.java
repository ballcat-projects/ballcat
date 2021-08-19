package com.hccake.ballcat.common.i18n;

import org.springframework.context.ApplicationEvent;

import java.util.List;

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

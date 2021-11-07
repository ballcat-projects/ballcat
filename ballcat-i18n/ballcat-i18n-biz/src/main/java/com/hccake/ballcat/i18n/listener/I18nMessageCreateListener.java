package com.hccake.ballcat.i18n.listener;

import com.hccake.ballcat.common.i18n.I18nMessage;
import com.hccake.ballcat.common.i18n.I18nMessageCreateEvent;
import com.hccake.ballcat.i18n.converter.I18nDataConverter;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.service.I18nDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hccake
 */
@Component
@RequiredArgsConstructor
public class I18nMessageCreateListener {

	private final I18nDataService i18nDataService;

	/**
	 * 监听鉴权失败事件
	 * @param event the event
	 */
	@EventListener(I18nMessageCreateEvent.class)
	public void onAuthenticationFailureEvent(I18nMessageCreateEvent event) {
		List<I18nMessage> i18nMessages = event.getI18nMessages();
		List<I18nData> list = i18nMessages.stream().map(I18nDataConverter.INSTANCE::messageToPo)
				.collect(Collectors.toList());
		i18nDataService.saveBatch(list);
	}

}

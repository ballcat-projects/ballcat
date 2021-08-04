package com.hccake.ballcat.i18n.provider;

import com.hccake.ballcat.common.i18n.I18nMessage;
import com.hccake.ballcat.common.i18n.I18nMessageProvider;
import com.hccake.ballcat.i18n.model.entity.I18nData;
import com.hccake.ballcat.i18n.service.I18nDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @author hccake
 */
@Component
@RequiredArgsConstructor
public class CustomI18nMessageProvider implements I18nMessageProvider {

	private final I18nDataService i18nDataService;

	@Override
	public I18nMessage getI18nMessage(String code, Locale locale) {
		String languageTag = locale.toLanguageTag();
		I18nData i18nData = i18nDataService.getByCodeAndLanguageTag(code, languageTag);
		if (i18nData == null) {
			return null;
		}
		I18nMessage i18nMessage = new I18nMessage();
		i18nMessage.setMessage(i18nData.getMessage());
		i18nMessage.setCode(i18nData.getCode());
		i18nMessage.setLanguageTag(i18nData.getCode());
		return i18nMessage;
	}

}

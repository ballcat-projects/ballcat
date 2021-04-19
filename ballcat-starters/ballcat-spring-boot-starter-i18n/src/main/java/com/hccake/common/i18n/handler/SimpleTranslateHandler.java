package com.hccake.common.i18n.handler;

import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * simple translate handler
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class SimpleTranslateHandler implements TranslateHandler {

	@Override
	public String translateText(String originalText, Map<String, String> paramValues) {
		return originalText;
	}

}

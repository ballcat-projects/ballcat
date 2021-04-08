package com.hccake.common.i18n.handler;

import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;

import java.util.Map;

/**
 * template translate handler
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class TemplateTranslateHandler implements TranslateHandler {

	@Override
	public String translateText(String originalText, Map<String, String> paramValues) {
		return StrUtil.format(originalText, paramValues);
	}

}

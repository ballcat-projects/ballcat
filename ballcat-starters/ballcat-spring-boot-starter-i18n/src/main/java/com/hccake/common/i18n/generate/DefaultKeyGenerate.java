package com.hccake.common.i18n.generate;

import cn.hutool.core.lang.Assert;
import com.hccake.common.i18n.I18nProperties;
import lombok.RequiredArgsConstructor;

/**
 * 默认key生成
 *
 * @author Yakir
 */
@RequiredArgsConstructor
public class DefaultKeyGenerate implements KeyGenerate {

	private final I18nProperties i18nProperties;

	@Override
	public String generateKey(String... params) {
		Assert.noNullElements(params, "params size must be greater than 0 ");
		StringBuilder stringBuilder = new StringBuilder();
		I18nProperties.Generate generate = i18nProperties.getGenerate();
		for (String param : params) {
			stringBuilder.append(param).append(generate.getDelimiter());
		}
		return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
	}

}

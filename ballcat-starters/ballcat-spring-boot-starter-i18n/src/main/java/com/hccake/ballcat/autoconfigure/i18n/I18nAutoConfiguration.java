package com.hccake.ballcat.autoconfigure.i18n;

import com.hccake.ballcat.common.i18n.I18nResponseAdvice;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;

/**
 * @author hccake
 */
@AutoConfiguration(after = I18nMessageSourceAutoConfiguration.class)
@EnableConfigurationProperties(I18nProperties.class)
public class I18nAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public I18nResponseAdvice i18nResponseAdvice(MessageSource messageSource, I18nProperties i18nProperties) {
		return new I18nResponseAdvice(messageSource, i18nProperties);
	}

}

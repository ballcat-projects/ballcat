package com.hccake.ballcat.autoconfigure.i18n;

import com.hccake.ballcat.common.i18n.DynamicMessageSource;
import com.hccake.ballcat.common.i18n.I18nMessageProvider;
import com.hccake.ballcat.common.i18n.MessageSourceHierarchicalChanger;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.AbstractApplicationContext;

/**
 * @author hccake
 */
@Configuration(proxyBeanMethods = false)
@Import(CustomMessageSourceAutoConfiguration.class)
public class I18nMessageSourceConfiguration {

	@Bean(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
	@ConditionalOnMissingBean(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
	public DynamicMessageSource messageSource(I18nMessageProvider i18nMessageProvider) {
		return new DynamicMessageSource(i18nMessageProvider);
	}

	@Bean(name = DynamicMessageSource.DYNAMIC_MESSAGE_SOURCE_BEAN_NAME)
	@ConditionalOnBean(name = AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME)
	public DynamicMessageSource dynamicMessageSource(I18nMessageProvider i18nMessageProvider) {
		return new DynamicMessageSource(i18nMessageProvider);
	}

	@ConditionalOnBean(name = { AbstractApplicationContext.MESSAGE_SOURCE_BEAN_NAME,
			DynamicMessageSource.DYNAMIC_MESSAGE_SOURCE_BEAN_NAME })
	@Bean
	public MessageSourceHierarchicalChanger messageSourceHierarchicalChanger() {
		return new MessageSourceHierarchicalChanger();
	}

}

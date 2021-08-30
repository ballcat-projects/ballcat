package com.hccake.ballcat.admin.i18n.config;

import com.hccake.ballcat.common.i18n.I18nMessageProvider;
import com.hccake.ballcat.i18n.provider.CustomI18nMessageProvider;
import com.hccake.ballcat.i18n.service.I18nDataService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * 注册一个 I18nMessageProvider
 *
 * @author hccake
 */
@MapperScan("com.hccake.ballcat.i18n.mapper")
@ComponentScan("com.hccake.ballcat.i18n")
@Configuration(proxyBeanMethods = false)
public class AdminI18nAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean(I18nMessageProvider.class)
	public I18nMessageProvider i18nMessageProvider(I18nDataService i18nDataService,
			StringRedisTemplate stringRedisTemplate) {
		return new CustomI18nMessageProvider(i18nDataService, stringRedisTemplate);
	}

}

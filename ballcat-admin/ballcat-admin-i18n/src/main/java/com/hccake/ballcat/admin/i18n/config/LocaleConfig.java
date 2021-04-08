package com.hccake.ballcat.admin.i18n.config;

import cn.hutool.core.util.StrUtil;
import com.hccake.ballcat.common.conf.exception.GlobalExceptionHandlerResolver;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * 语言解析器 主要负责从头提取语言环境
 *
 * @author Yakir
 */
@RequiredArgsConstructor
@Configuration
public class LocaleConfig {

	private final I18nAdminProperties i18nAdminProperties;

	/**
	 * 区域解析器
	 * @return @{link LocaleResolver}
	 */
	@Bean
	public LocaleResolver localeResolver() {
		MyLocaleResolver localeResolver = new MyLocaleResolver();
		return localeResolver;
	}

	class MyLocaleResolver implements LocaleResolver {

		@Override
		public Locale resolveLocale(HttpServletRequest request) {
			String language = request.getHeader(i18nAdminProperties.getLangHeader());
			if (StrUtil.isEmpty(language)) {
				// 路径上没有国际化语言参数，采用默认的（从请求头中获取）
				return request.getLocale();
			}
			else {
				// 格式语言_国家 en_US
				return StringUtils.parseLocale(language);
			}
		}

		@Override
		public void setLocale(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
				Locale locale) {

		}

	}

}

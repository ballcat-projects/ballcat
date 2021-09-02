package com.hccake.ballcat.admin.i18n.config;

import com.hccake.ballcat.autoconfigure.i18n.I18nMessageSourceConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

/**
 * 注册一个 I18nMessageProvider
 *
 * @author hccake
 */
@AutoConfigureOrder(value = Ordered.HIGHEST_PRECEDENCE)
@Import({ AdminI18nMessageProviderConfiguration.class, I18nMessageSourceConfiguration.class })
@MapperScan("com.hccake.ballcat.i18n.mapper")
@ComponentScan("com.hccake.ballcat.i18n")
@Configuration(proxyBeanMethods = false)
public class AdminI18nAutoConfiguration {

}

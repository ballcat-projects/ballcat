package com.hccake.ballcat.common.conf.config;

import com.hccake.ballcat.commom.log.access.service.AccessLogHandlerService;
import com.hccake.ballcat.common.core.filter.XSSFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/17 20:26
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
public class FilterConfig {

    @Bean
    @ConditionalOnClass(AccessLogHandlerService.class)
    public FilterRegistrationBean<XSSFilter> xssFilterRegistrationBean(){
        log.debug("XSS 过滤已开启====");
        FilterRegistrationBean<XSSFilter> registrationBean = new FilterRegistrationBean<>(new XSSFilter());
        registrationBean.setOrder(-1);
        return registrationBean;
    }

}

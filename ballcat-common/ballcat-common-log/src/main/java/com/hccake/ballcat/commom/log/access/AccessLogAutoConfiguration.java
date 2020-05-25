package com.hccake.ballcat.commom.log.access;

import com.hccake.ballcat.commom.log.access.filter.AccessLogFilter;
import com.hccake.ballcat.commom.log.access.handler.AccessLogHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
@Slf4j
@Configuration
@ConditionalOnWebApplication
@RequiredArgsConstructor
public class AccessLogAutoConfiguration {
    private final AccessLogHandler accessLogService;

    @Bean
    @ConditionalOnClass(AccessLogHandler.class)
    public FilterRegistrationBean<AccessLogFilter> accessLogFilterRegistrationBean(){
        log.debug("access log 记录拦截器已开启====");
        FilterRegistrationBean<AccessLogFilter> registrationBean =
                new FilterRegistrationBean<>(new AccessLogFilter(accessLogService));
        registrationBean.setOrder(0);
        return registrationBean;
    }

}


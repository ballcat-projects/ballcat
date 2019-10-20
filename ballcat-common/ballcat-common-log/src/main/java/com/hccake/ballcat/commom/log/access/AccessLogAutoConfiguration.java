package com.hccake.ballcat.commom.log.access;

import com.hccake.ballcat.commom.log.access.filter.AccessLogFilter;
import com.hccake.ballcat.commom.log.access.service.AccessLogHandlerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class AccessLogAutoConfiguration {

    @Autowired
    private AccessLogHandlerService accessLogService;

    @Bean
    @ConditionalOnClass(AccessLogHandlerService.class)
    public FilterRegistrationBean<AccessLogFilter> accessLogFilterRegistrationBean(){
        log.debug("access log 记录拦截器已开启====");
        FilterRegistrationBean<AccessLogFilter> registrationBean =
                new FilterRegistrationBean<>(new AccessLogFilter(accessLogService));
        registrationBean.setOrder(0);
        return registrationBean;
    }

}


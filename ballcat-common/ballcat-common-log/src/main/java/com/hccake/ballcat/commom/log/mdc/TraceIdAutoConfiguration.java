package com.hccake.ballcat.commom.log.mdc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 17:29
 */
@Slf4j
@ConditionalOnWebApplication
public class TraceIdAutoConfiguration {

    @Bean
    public FilterRegistrationBean<TraceIdFilter> traceIdFilterRegistrationBean(){
        FilterRegistrationBean<TraceIdFilter> registrationBean =
                new FilterRegistrationBean<>(new TraceIdFilter());
        registrationBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return registrationBean;
    }

}

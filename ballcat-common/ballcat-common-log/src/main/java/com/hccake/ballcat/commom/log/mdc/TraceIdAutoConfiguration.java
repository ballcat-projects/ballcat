package com.hccake.ballcat.commom.log.mdc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2020/5/25 17:29
 */
@Configuration
@ConditionalOnWebApplication
public class TraceIdAutoConfiguration {

    @Bean
    public TraceIdMvcConfigurer traceIdMvcConfigurer(){
        return new TraceIdMvcConfigurer();
    }

}

package com.hccake.ballcat.common.conf.exception;

import com.hccake.ballcat.common.core.exception.handler.DefaultGlobalExceptionHandler;
import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
@EnableAsync
@Configuration
public class ExceptionHandleAutoConfiguration {

    /**
     * 默认的日志处理器
     * @return DefaultExceptionHandler
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandler.class)
    public GlobalExceptionHandler globalExceptionHandler(){
        return new DefaultGlobalExceptionHandler();
    }


    /**
     * 默认的日志处理器
     * @return DefaultExceptionHandler
     */
    @Bean
    @ConditionalOnMissingBean(GlobalExceptionHandlerResolver.class)
    public GlobalExceptionHandlerResolver globalExceptionHandlerResolver(GlobalExceptionHandler globalExceptionHandler){
        return new GlobalExceptionHandlerResolver(globalExceptionHandler);
    }

}


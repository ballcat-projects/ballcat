package com.hccake.ballcat.commom.log.error;

import com.hccake.ballcat.commom.log.error.service.ErrorLogHandlerService;
import com.hccake.ballcat.commom.log.error.service.impl.DefaultErrorLogHandlerServiceImpl;
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
public class ErrorLogAutoConfiguration {

    /**
     * 默认的日志处理器
     * @return
     */
    @Bean
    @ConditionalOnMissingBean(ErrorLogHandlerService.class)
    public ErrorLogHandlerService errorLogHandlerService(){
        return new DefaultErrorLogHandlerServiceImpl();
    }

}


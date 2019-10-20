package com.hccake.ballcat.commom.log.operation;

import com.hccake.ballcat.commom.log.operation.aspect.OperationLogAspect;
import com.hccake.ballcat.commom.log.operation.event.OperationLogListener;
import com.hccake.ballcat.commom.log.operation.service.OperationLogSaveService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/15 18:20
 */
@Configuration
public class OperationLogAutoConfiguration {

    /**
     * 注册日志保存事件监听器
     * @return
     */
    @Bean
    @ConditionalOnBean(OperationLogSaveService.class)
    public OperationLogListener operationLogListener() {
        return new OperationLogListener();
    }

    /**
     * 注册操作日志Aspect
     * @return
     */
    @Bean
    @ConditionalOnBean(OperationLogSaveService.class)
    public OperationLogAspect operationLogAspect() {
        return new OperationLogAspect();
    }

}


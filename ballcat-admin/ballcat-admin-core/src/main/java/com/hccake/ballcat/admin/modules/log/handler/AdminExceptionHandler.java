package com.hccake.ballcat.admin.modules.log.handler;

import com.hccake.ballcat.common.core.exception.handler.GlobalExceptionHandler;
import org.springframework.stereotype.Component;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/18 17:41
 */
@Component
public class AdminExceptionHandler implements GlobalExceptionHandler {
    /**
     * 在此处理错误信息
     * 进行落库，入ES， 发送报警通知等信息
     *
     * @param throwable
     */
    @Override
    public void handle(Throwable throwable) {
        System.out.println("后台错误日志处理：" + throwable.getMessage());
    }
}

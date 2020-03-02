package com.hccake.ballcat.admin.modules.log.service.impl;

import com.hccake.ballcat.commom.log.error.service.ErrorLogHandlerService;
import org.springframework.stereotype.Component;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/18 17:41
 */
@Component
public class AdminErrorLogHandlerServiceImpl implements ErrorLogHandlerService {
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

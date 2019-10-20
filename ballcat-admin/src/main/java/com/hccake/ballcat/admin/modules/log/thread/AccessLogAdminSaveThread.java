package com.hccake.ballcat.admin.modules.log.thread;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.service.AdminAccessLogService;
import com.hccake.ballcat.common.core.thread.AbstractQueueThread;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 15:30
 */
@Slf4j
@AllArgsConstructor
@Component
public class AccessLogAdminSaveThread extends AbstractQueueThread<AdminAccessLog> {
    private final AdminAccessLogService adminAccessLogService;

    /**
     * 线程启动时的日志打印
     */
    @Override
    public void startLog() {
        log.info("后台访问日志存储线程已启动===");
    }

    /**
     * 错误日志打印
     *
     * @param e
     * @param list
     */
    @Override
    public void errorLog(Throwable e, List<AdminAccessLog> list) {
        log.error("后台访问日志记录异常, [msg]:{}, [data]:{}", e.getMessage(), list);
    }

    /**
     * 数据保存
     *
     * @param list
     */
    @Override
    public void save(List<AdminAccessLog> list) throws Exception {
        adminAccessLogService.saveBatch(list);
    }
}

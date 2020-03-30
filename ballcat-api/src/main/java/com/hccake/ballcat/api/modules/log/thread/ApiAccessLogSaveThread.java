package com.hccake.ballcat.api.modules.log.thread;

import com.hccake.ballcat.common.modules.log.model.entity.ApiAccessLog;
import com.hccake.ballcat.common.modules.log.service.ApiAccessLogService;
import com.hccake.ballcat.common.core.thread.AbstractQueueThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 15:30
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApiAccessLogSaveThread extends AbstractQueueThread<ApiAccessLog> {
    private final ApiAccessLogService apiAccessLogService;

    /**
     * 线程启动时的日志打印
     */
    @Override
    public void startLog() {
        log.info("访问日志存储线程已启动===");
    }

    /**
     * 错误日志打印
     *
     * @param e
     * @param list
     */
    @Override
    public void errorLog(Throwable e, List<ApiAccessLog> list) {
        log.error("访问日志记录异常, [msg]:{}, [data]:{}", e.getMessage(), list);
    }

    /**
     * 数据保存
     *
     * @param list
     */
    @Override
    public void save(List<ApiAccessLog> list) throws Exception {
        apiAccessLogService.saveBatch(list);
    }
}

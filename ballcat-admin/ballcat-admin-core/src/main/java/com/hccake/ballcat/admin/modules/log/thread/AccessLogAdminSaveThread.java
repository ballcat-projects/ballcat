package com.hccake.ballcat.admin.modules.log.thread;

import com.hccake.ballcat.admin.modules.log.model.entity.AdminAccessLog;
import com.hccake.ballcat.admin.modules.log.service.AdminAccessLogService;
import com.hccake.ballcat.common.core.thread.AbstractBlockingQueueThread;
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
public class AccessLogAdminSaveThread extends AbstractBlockingQueueThread<AdminAccessLog> {

	private final AdminAccessLogService adminAccessLogService;

	/**
	 * 线程启动时的日志打印
	 */
	@Override
	public void init() {
		log.info("后台访问日志存储线程已启动===");
	}

	/**
	 * 错误日志打印
	 * @param e 错误堆栈
	 * @param list 后台访问日志列表
	 */
	@Override
	public void error(Throwable e, List<AdminAccessLog> list) {
		log.error("后台访问日志记录异常, [msg]:{}, [data]:{}", e.getMessage(), list);
	}

	/**
	 * 数据保存
	 * @param list 后台访问日志列表
	 */
	@Override
	public void process(List<AdminAccessLog> list) throws Exception {
		adminAccessLogService.saveBatchSomeColumn(list);
	}

}

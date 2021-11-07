package com.hccake.ballcat.log.thread;

import com.hccake.ballcat.common.core.thread.AbstractBlockingQueueThread;
import com.hccake.ballcat.log.model.entity.AccessLog;
import com.hccake.ballcat.log.service.AccessLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/10/16 15:30
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLogSaveThread extends AbstractBlockingQueueThread<AccessLog> {

	private final AccessLogService accessLogService;

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
	public void error(Throwable e, List<AccessLog> list) {
		log.error("后台访问日志记录异常, [msg]:{}, [data]:{}", e.getMessage(), list);
	}

	/**
	 * 数据保存
	 * @param list 后台访问日志列表
	 */
	@Override
	public void process(List<AccessLog> list) throws Exception {
		accessLogService.saveBatch(list);
	}

}

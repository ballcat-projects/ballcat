package com.hccake.ballcat.admin.upms.config.task;

import cn.hutool.core.map.MapUtil;
import org.slf4j.MDC;
import org.springframework.core.task.TaskDecorator;

import java.util.Map;

/**
 * 使async异步任务支持traceId
 *
 * @author huyuanzhi 2021-11-06 23:14:27
 */
public class MdcTaskDecorator implements TaskDecorator {

	@Override
	public Runnable decorate(Runnable runnable) {
		final Map<String, String> copyOfContextMap = MDC.getCopyOfContextMap();
		return () -> {
			if (MapUtil.isNotEmpty(copyOfContextMap)) {
				// 现在：@Async线程上下文！ 恢复Web线程上下文的MDC数据
				MDC.setContextMap(copyOfContextMap);
			}

			try {
				runnable.run();
			}
			finally {
				MDC.clear();
			}
		};
	}

}

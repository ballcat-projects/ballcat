package com.hccake.ballcat.admin.config.task;

import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 线程池构建，原本的线程池的拒绝策略为直接抛出异常，不太友好
 *
 * @see org.springframework.boot.autoconfigure.task.TaskExecutionAutoConfiguration
 * @author hccake
 */
@Configuration(proxyBeanMethods = false)
public class TaskExecutionConfiguration {

	/**
	 * 修改 springboot 默认配置的 taskExecutor 的拒绝策略为使用当前线程执行
	 * @return TaskExecutorCustomizer
	 */
	@Bean
	public TaskExecutorCustomizer taskExecutorCustomizer() {
		// AbortPolicy: 直接抛出java.util.concurrent.RejectedExecutionException异常
		// CallerRunsPolicy: 主线程直接执行该任务，执行完之后尝试添加下一个任务到线程池中，可以有效降低向线程池内添加任务的速度
		// DiscardOldestPolicy: 抛弃旧的任务、暂不支持；会导致被丢弃的任务无法再次被执行
		// DiscardPolicy: 抛弃当前任务、暂不支持；会导致被丢弃的任务无法再次被执行
		// 这里使用主线程直接执行该任务
		return (taskExecutor -> taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()));
	}

}

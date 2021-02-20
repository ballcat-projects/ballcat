package com.your.packages.admin.modules.test;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * XxlJob开发示例（Bean模式）
 *
 * 开发步骤： 1、任务开发：在Spring Bean实例中，开发Job方法； 2、注解配置：为Job方法添加注解
 * "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy =
 * "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。 3、执行日志：需要通过 "XxlJobHelper.log"
 * 打印执行日志； 4、任务结果：默认任务结果为 "成功" 状态，不需要主动设置；如有诉求，比如设置任务结果为失败，可以通过
 * "XxlJobHelper.handleFail/handleSuccess" 自主设置任务结果；
 *
 * @author xuxueli 2019-12-11 21:52:51
 */
@Slf4j
@Component
public class AdminDemoJobHandler {

	@XxlJob(value = "adminDemoJobHandler")
	public void execute() throws Exception {
		// XxlJobLogger 改为使用 XxlJobHelper
		XxlJobHelper.log("AdminDemoJobHandler Invoke Success.");

		// param 获取改为使用 XxlJobHelper.getJobParam 方法
		String param = XxlJobHelper.getJobParam();
		XxlJobHelper.log("AdminDemoJobHandler param: " + param);

		for (int i = 0; i < 5; i++) {
			XxlJobHelper.log("beat at:" + i);
			TimeUnit.SECONDS.sleep(2);
		}

		XxlJobHelper.log(("执行成功！：" + LocalDateTime.now()));

		// 无异常情况下可省略
		XxlJobHelper.handleSuccess();
	}

}

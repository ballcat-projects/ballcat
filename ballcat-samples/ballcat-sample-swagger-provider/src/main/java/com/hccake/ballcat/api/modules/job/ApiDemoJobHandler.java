package com.hccake.ballcat.api.modules.job;

import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

/**
 * XxlJob开发示例（Bean模式）
 *
 * 开发步骤： 1、在Spring Bean实例中，开发Job方法，方式格式要求为 "public ReturnT<String> execute(String param)"
 * 2、为Job方法添加注解 "@XxlJob(value="自定义jobhandler名称", init = "JobHandler初始化方法", destroy =
 * "JobHandler销毁方法")"，注解value值对应的是调度中心新建任务的JobHandler属性的值。 3、执行日志：需要通过 "XxlJobLogger.log"
 * 打印执行日志；
 *
 * @author xuxueli 2019-12-11 21:52:51
 */
@Component
public class ApiDemoJobHandler {

	@XxlJob("apiDemoJobHandler")
	public void execute() throws Exception {
		// XxlJobLogger 改为使用 XxlJobHelper
		XxlJobHelper.log("ApiDemoJobHandler Invoke Success.");

		// param 获取改为使用 XxlJobHelper.getJobParam 方法
		String param = XxlJobHelper.getJobParam();
		XxlJobHelper.log("ApiDemoJobHandler param: " + param);

		for (int i = 0; i < 5; i++) {
			XxlJobHelper.log("beat at:" + i);
			TimeUnit.SECONDS.sleep(2);
		}

		XxlJobHelper.log(("执行成功！：" + LocalDateTime.now()));

		// 无异常情况下可省略
		XxlJobHelper.handleSuccess();

	}

}

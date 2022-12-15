package com.hccake.ballcat.common.datascope.util;

import com.hccake.ballcat.common.datascope.function.Task;
import com.hccake.ballcat.common.datascope.handler.DataPermissionRule;
import com.hccake.ballcat.common.datascope.holder.DataPermissionRuleHolder;

/**
 * @author hccake
 */
public final class DataPermissionUtils {

	private DataPermissionUtils() {

	}

	/**
	 * 使用指定的数据权限执行任务
	 * @param task 待执行的动作
	 */
	public static void executeAndIgnoreAll(Task task) {
		DataPermissionRule ignoreAll = new DataPermissionRule(true);
		executeWithDataPermissionRule(ignoreAll, task);
	}

	/**
	 * 使用指定的数据权限执行任务
	 * @param dataPermissionRule 当前任务执行时使用的数据权限规则
	 * @param task 待执行的动作
	 */
	public static void executeWithDataPermissionRule(DataPermissionRule dataPermissionRule, Task task) {
		DataPermissionRuleHolder.push(dataPermissionRule);
		try {
			task.perform();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

}

package com.hccake.ballcat.common.datascope.util;

import com.hccake.ballcat.common.datascope.function.Action;
import com.hccake.ballcat.common.datascope.function.ResultAction;
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
	 * @param action 待执行的动作
	 */
	public static void executeAndIgnoreAll(Action action) {
		DataPermissionRule ignoreAll = new DataPermissionRule(true);
		executeWithDataPermissionRule(ignoreAll, action);
	}

	/**
	 * 使用指定的数据权限执行任务
	 * @param resultAction 待执行的动作
	 */
	public static <T> T executeAndIgnoreAll(ResultAction<T> resultAction) {
		DataPermissionRule ignoreAll = new DataPermissionRule(true);
		return executeWithDataPermissionRule(ignoreAll, resultAction);
	}

	/**
	 * 使用指定的数据权限执行任务
	 * @param dataPermissionRule 当前任务执行时使用的数据权限规则
	 * @param action 待执行的动作
	 */
	public static void executeWithDataPermissionRule(DataPermissionRule dataPermissionRule, Action action) {
		DataPermissionRuleHolder.push(dataPermissionRule);
		try {
			action.execute();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

	/**
	 * 使用指定的数据权限执行任务
	 * @param dataPermissionRule 当前任务执行时使用的数据权限规则
	 * @param resultAction 待执行的动作
	 */
	public static <T> T executeWithDataPermissionRule(DataPermissionRule dataPermissionRule,
			ResultAction<T> resultAction) {
		DataPermissionRuleHolder.push(dataPermissionRule);
		try {
			return resultAction.execute();
		}
		finally {
			DataPermissionRuleHolder.poll();
		}
	}

}

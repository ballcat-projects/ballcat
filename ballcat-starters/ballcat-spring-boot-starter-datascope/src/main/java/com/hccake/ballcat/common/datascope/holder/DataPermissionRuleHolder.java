package com.hccake.ballcat.common.datascope.holder;

import com.hccake.ballcat.common.datascope.handler.DataPermissionRule;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 数据权限规则的持有者，使用栈存储调用链中的数据权限规则
 *
 * 区别于{@link com.hccake.ballcat.common.datascope.annotation.DataPermission}
 * {@link DataPermissionRule} 是编程式数据权限控制的使用，优先级高于注解
 *
 * @author hccake
 */
public final class DataPermissionRuleHolder {

	private DataPermissionRuleHolder() {
	}

	/**
	 * 使用栈存储 DataPermissionRule，便于在方法嵌套调用时使用不同的数据权限控制。
	 */
	private static final ThreadLocal<Deque<DataPermissionRule>> DATA_PERMISSION_RULES = ThreadLocal
		.withInitial(ArrayDeque::new);

	/**
	 * 获取当前的 DataPermissionRule 注解
	 * @return DataPermissionRule
	 */
	public static DataPermissionRule peek() {
		Deque<DataPermissionRule> deque = DATA_PERMISSION_RULES.get();
		return deque == null ? null : deque.peek();
	}

	/**
	 * 入栈一个 DataPermissionRule 注解
	 * @return DataPermissionRule
	 */
	public static DataPermissionRule push(DataPermissionRule dataPermissionRule) {
		Deque<DataPermissionRule> deque = DATA_PERMISSION_RULES.get();
		if (deque == null) {
			deque = new ArrayDeque<>();
		}
		deque.push(dataPermissionRule);
		return dataPermissionRule;
	}

	/**
	 * 弹出最顶部 DataPermissionRule
	 */
	public static void poll() {
		Deque<DataPermissionRule> deque = DATA_PERMISSION_RULES.get();
		deque.poll();
		// 当没有元素时，清空 ThreadLocal
		if (deque.isEmpty()) {
			clear();
		}
	}

	/**
	 * 清除 TreadLocal
	 */
	public static void clear() {
		DATA_PERMISSION_RULES.remove();
	}

}

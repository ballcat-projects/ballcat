package com.hccake.ballcat.common.desensitize.json;

/**
 * 脱敏工具类 定义开启脱敏规则
 *
 * @author Yakir
 */
public interface DesensitizeStrategy {

	/**
	 * 判断是否忽略字段
	 * @param fieldName {@code 当前字段名称}
	 * @return @{code true 忽略 |false 不忽略}
	 */
	boolean ignoreField(String fieldName);

}

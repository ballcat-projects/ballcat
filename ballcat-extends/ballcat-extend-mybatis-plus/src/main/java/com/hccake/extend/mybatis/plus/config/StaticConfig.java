package com.hccake.extend.mybatis.plus.config;

import java.util.HashSet;
import java.util.Set;

/**
 * 用于配置 需要 动态加载，但是 静态来使用的变量
 *
 * @author lingting 2020/6/9 20:05
 */
public class StaticConfig {

	/**
	 * 更新时忽略的字段
	 */
	public static final Set<String> UPDATE_IGNORE_FIELDS = new HashSet<>();

}

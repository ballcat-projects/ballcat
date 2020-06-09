package com.hccake.extend.mybatis.plus.config;

import com.baomidou.mybatisplus.core.injector.AbstractMethod;

import java.util.List;
import java.util.Set;

/**
 * 用于自定义 mybatis 配置
 * 自定义配置需 实现本类 并设置为bean
 *
 * @author lingting  2020/5/27 20:56
 */
public interface MybatisConfigurer {
	/**
	 * 添加 更新时 需要忽略的字段
	 *
	 * @param set 字段 set
	 * @author lingting 2020-05-27 20:58:32
	 */
	default void pushIgnoreFields(Set<String> set) {
	}

	/**
	 * 添加自定义的 全局方法
	 *
	 * @param list 方法list
	 * @author lingting 2020-05-27 23:47:10
	 */
	default void pushMethods(List<AbstractMethod> list) {
	}
}

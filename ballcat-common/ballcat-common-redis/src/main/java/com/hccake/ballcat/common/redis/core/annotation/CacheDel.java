package com.hccake.ballcat.common.redis.core.annotation;

import java.lang.annotation.*;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/8/31 16:08 利用Aop, 在方法执行后执行缓存删除操作
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface CacheDel {

	/**
	 * redis 存储的Key名
	 */
	String key();

	/**
	 * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式
	 */
	String keyJoint() default "";

	/**
	 * 清除多个 key，当值为 true 时，强制要求 keyJoint 有值，且 Spel 表达式解析结果为 Collection
	 * @return boolean
	 */
	boolean multiDel() default false;

}

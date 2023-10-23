package com.hccake.ballcat.common.redis.core.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

/**
 * 利用Aop, 在方法调用前先查询缓存 若缓存中没有数据，则调用方法本身，并将方法返回值放置入缓存中
 *
 * @author Hccake
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface Cached {

	/**
	 * redis 存储的Key名
	 */
	String key();

	/**
	 * 如果需要在key 后面拼接参数 则传入一个拼接数据的 SpEL 表达式
	 */
	String keyJoint() default "";

	/**
	 * 超时时间(S) ttl = 0 使用全局配置值 ttl < 0 : 不超时 ttl > 0 : 使用此超时间
	 */
	long ttl() default 0;

	/**
	 * 控制时长单位，默认为 SECONDS 秒
	 * @return {@link TimeUnit}
	 */
	TimeUnit timeUnit() default TimeUnit.SECONDS;

	/**
	 * 锁竞争失败时的重试次数
	 * @return 负数: 无限重试， 0: 不重试， 正数: 重试次数
	 */
	int retryCount() default 3;

}

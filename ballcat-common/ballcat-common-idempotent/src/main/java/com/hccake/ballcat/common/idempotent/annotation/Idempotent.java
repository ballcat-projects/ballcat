package com.hccake.ballcat.common.idempotent.annotation;

import java.lang.annotation.*;

/**
 * 幂等控制注解
 * @author hccake
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Idempotent {

	String KEY_PREFIX = "idem";

	/**
	 * <p>
	 * 幂等标识的前缀，可用于区分服务和业务，防止 key 冲突
	 * </p>
	 * ps: 完整的幂等标识 = {prefix}:{uniqueExpression.value}
	 * @return 业务标识
	 */
	String prefix() default KEY_PREFIX;

	/**
	 * 值为 SpEL 表达式，从上下文中提取幂等的唯一性标识。
	 * @return Spring-EL expression
	 */
	String uniqueExpression() default "";

	/**
	 * <p>
	 * 幂等的控制时长（秒），必须大于业务的处理耗时
	 * </p>
	 * 其值为幂等 key 的标记时长，超过标记时间，则幂等 key 可再次使用。
	 * @return 标记时长(秒)，默认 10 min
	 */
	long duration() default 10 * 60;

	/**
	 * 否在业务完成后立刻清除，幂等 key
	 * @return boolean true: 立刻清除 false: 不处理
	 */
	boolean removeKeyWhenFinished() default false;

}

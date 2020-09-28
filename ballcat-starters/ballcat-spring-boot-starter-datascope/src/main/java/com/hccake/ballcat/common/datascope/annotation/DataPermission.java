package com.hccake.ballcat.common.datascope.annotation;

import java.lang.annotation.*;

/**
 * 数据权限注解，注解在 Mapper类 或者 对应方法上 用于提供该 mapper 对应表，所需控制的实体信息
 * @author Hccake 2020/9/27
 * @version 1.0
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataPermission {

	/**
	 * 资源类型
	 * @return 资源类型数组
	 */
	String[] resources();

	/**
	 * 用于在全局开启或者关闭数据权限时，对指定类或者指定方法进行开关控制
	 * @return boolean 默认返回 true
	 */
	boolean enabled() default true;

}

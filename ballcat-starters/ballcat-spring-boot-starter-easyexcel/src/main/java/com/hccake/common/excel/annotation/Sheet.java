package com.hccake.common.excel.annotation;

import com.hccake.common.excel.head.HeadGenerator;

import java.lang.annotation.*;

/**
 * 用于指定导入导出的 excel 的 sheet 属性
 * @author Yakir
 * @date 2021/4/29 15:03
 * @version 1.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Sheet {

	int sheetNo() default -1;

	/**
	 * sheet name
	 */
	String sheetName();

	/**
	 * 包含字段
	 */
	String[] includes() default {};

	/**
	 * 排除字段
	 */
	String[] excludes() default {};

	/**
	 * 头生成器
	 */
	Class<? extends HeadGenerator> headGenerateClass() default HeadGenerator.class;

}

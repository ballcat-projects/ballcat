package com.hccake.extend.mybatis.plus.alias;

import com.hccake.extend.mybatis.plus.conditions.query.LambdaAliasQueryWrapperX;
import java.lang.annotation.*;

/**
 * 表别名注解，注解在 entity 上，便于构建带别名的查询条件或者查询列
 * @see LambdaAliasQueryWrapperX
 * @see TableAliasHelper
 * @author Hccake 2021/1/14
 * @version 1.0
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableAlias {

	/**
	 * 当前实体对应的表别名
	 * @return String 表别名
	 */
	String value();

}

package com.hccake.ballcat.common.redis.core.annotation;

import java.lang.annotation.*;

/**
 * 删除
 *
 * @author lishangbu
 * @date 2022/10/29
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface CacheDels {

	CacheDel[] value();

}

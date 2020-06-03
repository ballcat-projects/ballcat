package com.hccake.ballcat.common.redis.core.annotation;

import java.lang.annotation.*;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/8/31 16:08
 * 用于标识切点的元注解
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MetaCacheAnnotation {

}

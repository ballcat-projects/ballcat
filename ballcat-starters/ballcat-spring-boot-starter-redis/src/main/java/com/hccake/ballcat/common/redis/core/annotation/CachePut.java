package com.hccake.ballcat.common.redis.core.annotation;

import java.lang.annotation.*;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/8/31 16:08
 * 利用Aop, 在方法执行后执行缓存put操作
 * 将方法的返回值置入缓存中，若方法返回null，则会默认置入一个nullValue
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MetaCacheAnnotation
public @interface CachePut {

    /**
     * redis 存储的Key名
     */
    String key();

    /**
     * 如果需要在key 后面拼接参数
     * 则传入一个拼接数据的 SpEL 表达式
     */
    String keyJoint() default "";

    /**
     * 超时时间(S)
     * ttl = 0  使用全局配置值
     * ttl < 0 :  不超时
     * ttl > 0 :  使用此超时间
     */
    long ttl() default 0;


}

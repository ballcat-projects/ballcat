package com.hccake.ballcat.common.tenant.annotation;

import com.baomidou.dynamic.datasource.annotation.DS;

import java.lang.annotation.*;

/**
 * 切换租户数据源 注意 租户数据源名称一定要是 master
 * @author huyuanzhi
 */
@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@DS("master")
public @interface Tenant {

}

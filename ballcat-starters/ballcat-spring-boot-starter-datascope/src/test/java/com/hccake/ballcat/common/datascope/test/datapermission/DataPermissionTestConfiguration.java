package com.hccake.ballcat.common.datascope.test.datapermission;

import com.hccake.ballcat.common.datascope.interceptor.DataPermissionAnnotationAdvisor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hccake
 */
@EnableAspectJAutoProxy
@Configuration(proxyBeanMethods = false)
public class DataPermissionTestConfiguration {

	@Bean
	public TestServiceImpl testService() {
		return new TestServiceImpl();
	}

	/**
	 * 数据权限注解 Advisor，用于处理数据权限的链式调用关系
	 * @return DataPermissionAnnotationAdvisor
	 */
	@Bean
	@ConditionalOnMissingBean(DataPermissionAnnotationAdvisor.class)
	public DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor() {
		return new DataPermissionAnnotationAdvisor();
	}

}

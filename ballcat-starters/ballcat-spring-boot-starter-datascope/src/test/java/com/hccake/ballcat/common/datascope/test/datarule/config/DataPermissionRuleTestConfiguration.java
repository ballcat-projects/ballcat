package com.hccake.ballcat.common.datascope.test.datarule.config;

import com.hccake.ballcat.common.datascope.DataScope;
import com.hccake.ballcat.common.datascope.test.datarule.datascope.ClassDataScope;
import com.hccake.ballcat.common.datascope.test.datarule.datascope.SchoolDataScope;
import com.hccake.ballcat.common.datascope.test.datarule.datascope.StudentDataScope;
import com.hccake.ballcat.common.datascope.test.datarule.service.StudentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

/**
 * @author hccake
 */
@EnableAspectJAutoProxy
@Configuration(proxyBeanMethods = false)
public class DataPermissionRuleTestConfiguration {

	@Bean
	public DataScope classDataScope() {
		return new ClassDataScope();
	}

	@Bean
	public DataScope schoolDataScope() {
		return new SchoolDataScope();
	}

	@Bean
	public DataScope studentDataScope() {
		return new StudentDataScope();
	}

	@Bean
	public StudentService studentService() {
		return new StudentService();
	}

}

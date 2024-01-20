/*
 * Copyright 2023-2024 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ballcat.datascope.test.datarule.config;

import org.ballcat.datascope.DataScope;
import org.ballcat.datascope.test.datarule.datascope.ClassDataScope;
import org.ballcat.datascope.test.datarule.datascope.SchoolDataScope;
import org.ballcat.datascope.test.datarule.datascope.StudentDataScope;
import org.ballcat.datascope.test.datarule.service.StudentService;
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

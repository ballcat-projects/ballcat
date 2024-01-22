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

package org.ballcat.datascope.test.datapermission;

import org.ballcat.datascope.handler.DataPermissionRule;
import org.ballcat.datascope.holder.DataPermissionRuleHolder;
import org.ballcat.datascope.interceptor.DataPermissionAnnotationAdvisor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.aop.MethodMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author hccake
 */
@SpringJUnitConfig({ DataPermissionTestConfiguration.class })
class DataPermissionTest {

	@Autowired
	TestService testService;

	@Autowired
	DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor;

	@Test
	void testAnnotationMatchingPointcut() throws NoSuchMethodException {
		MethodMatcher methodMatcher = this.dataPermissionAnnotationAdvisor.getPointcut().getMethodMatcher();
		boolean match = methodMatcher.matches(TestServiceImpl.class.getMethod("methodA"), TestServiceImpl.class);
		Assertions.assertTrue(match, "切点未正确匹配被注解的方法");
	}

	@Test
	void test() {
		// 使用方法本身注解
		DataPermissionRule dataPermissionA = this.testService.methodA();
		Assertions.assertArrayEquals(new String[] { "order" }, dataPermissionA.excludeResources(),
				"dataPermission 解析错误");

		// 继承自类注解
		DataPermissionRule dataPermissionB = this.testService.methodB();
		Assertions.assertArrayEquals(new String[] { "class" }, dataPermissionB.excludeResources(),
				"dataPermission 解析错误");

		// 继承自类注解
		DataPermissionRule dataPermissionC = this.testService.methodC();
		Assertions.assertArrayEquals(new String[] { "class" }, dataPermissionC.excludeResources(),
				"dataPermission 解析错误");

		// 执行完成必须要被 clear
		DataPermissionRule dataPermissionRule = DataPermissionRuleHolder.peek();
		Assertions.assertNull(dataPermissionRule, "dataPermission 解析错误");
	}

}

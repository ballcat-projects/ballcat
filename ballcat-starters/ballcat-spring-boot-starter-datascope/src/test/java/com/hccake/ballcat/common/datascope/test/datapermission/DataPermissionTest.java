package com.hccake.ballcat.common.datascope.test.datapermission;

import com.hccake.ballcat.common.datascope.handler.DataPermissionRule;
import com.hccake.ballcat.common.datascope.holder.DataPermissionRuleHolder;
import com.hccake.ballcat.common.datascope.interceptor.DataPermissionAnnotationAdvisor;
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
		MethodMatcher methodMatcher = dataPermissionAnnotationAdvisor.getPointcut().getMethodMatcher();
		boolean match = methodMatcher.matches(TestServiceImpl.class.getMethod("methodA"), TestServiceImpl.class);
		Assertions.assertTrue(match, "切点未正确匹配被注解的方法");
	}

	@Test
	void test() {
		// 使用方法本身注解
		DataPermissionRule dataPermissionA = testService.methodA();
		Assertions.assertArrayEquals(new String[] { "order" }, dataPermissionA.excludeResources(),
				"dataPermission 解析错误");

		// 继承自类注解
		DataPermissionRule dataPermissionB = testService.methodB();
		Assertions.assertArrayEquals(new String[] { "class" }, dataPermissionB.excludeResources(),
				"dataPermission 解析错误");

		// 继承自类注解
		DataPermissionRule dataPermissionC = testService.methodC();
		Assertions.assertArrayEquals(new String[] { "class" }, dataPermissionC.excludeResources(),
				"dataPermission 解析错误");

		// 执行完成必须要被 clear
		DataPermissionRule dataPermissionRule = DataPermissionRuleHolder.peek();
		Assertions.assertNull(dataPermissionRule, "dataPermission 解析错误");
	}

}

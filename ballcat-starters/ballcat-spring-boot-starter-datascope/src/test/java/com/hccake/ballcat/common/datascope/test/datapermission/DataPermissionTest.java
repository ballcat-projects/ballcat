package com.hccake.ballcat.common.datascope.test.datapermission;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import com.hccake.ballcat.common.datascope.interceptor.DataPermissionAnnotationAdvisor;
import org.junit.jupiter.api.Test;
import org.springframework.aop.MethodMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.util.Assert;

import java.util.Arrays;

/**
 * @author hccake
 */
@SpringJUnitConfig({ DataPermissionTestConfiguration.class })
public class DataPermissionTest {

	@Autowired
	TestService testService;

	@Autowired
	DataPermissionAnnotationAdvisor dataPermissionAnnotationAdvisor;

	@Test
	public void testAnnotationMatchingPointcut() throws NoSuchMethodException {
		MethodMatcher methodMatcher = dataPermissionAnnotationAdvisor.getPointcut().getMethodMatcher();
		boolean match = methodMatcher.matches(TestServiceImpl.class.getMethod("methodA"), TestServiceImpl.class);
		Assert.isTrue(match, "切点未正确匹配被注解的方法");
	}

	@Test
	public void test() {
		// 使用方法本身注解
		DataPermission dataPermissionA = testService.methodA();
		Assert.isTrue(Arrays.equals(dataPermissionA.excludeResources(), new String[] { "order" }),
				"dataPermission 解析错误");

		// 继承自类注解
		DataPermission dataPermissionB = testService.methodB();
		Assert.isTrue(Arrays.equals(dataPermissionB.excludeResources(), new String[] { "class" }),
				"dataPermission 解析错误");
	}

}

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

package org.ballcat.datascope.test.datarule;

import java.util.Collections;
import java.util.List;

import org.ballcat.datascope.DataScope;
import org.ballcat.datascope.DataScopeAutoConfiguration;
import org.ballcat.datascope.handler.DataPermissionHandler;
import org.ballcat.datascope.handler.DataPermissionRule;
import org.ballcat.datascope.test.datarule.config.DataPermissionRuleTestConfiguration;
import org.ballcat.datascope.test.datarule.config.DataSourceConfiguration;
import org.ballcat.datascope.test.datarule.datascope.ClassDataScope;
import org.ballcat.datascope.test.datarule.datascope.SchoolDataScope;
import org.ballcat.datascope.test.datarule.datascope.StudentDataScope;
import org.ballcat.datascope.test.datarule.entity.Student;
import org.ballcat.datascope.test.datarule.service.StudentService;
import org.ballcat.datascope.test.datarule.user.LoginUser;
import org.ballcat.datascope.test.datarule.user.LoginUserHolder;
import org.ballcat.datascope.test.datarule.user.UserRoleType;
import org.ballcat.datascope.util.DataPermissionUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Sql({ "/student.ddl.sql", "/student.insert.sql" })
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = { DataSourceConfiguration.class, DataPermissionRuleTestConfiguration.class,
		DataScopeAutoConfiguration.class, MybatisAutoConfiguration.class })
@MapperScan(basePackageClasses = StudentMapperTest.class)
class StudentMapperTest {

	@Autowired
	private StudentService studentService;

	@Autowired
	private DataPermissionHandler dataPermissionHandler;

	@BeforeEach
	void login() {
		// 设置当前登录用户权限
		LoginUser loginUser = new LoginUser();
		loginUser.setId(1L);
		loginUser.setUserRoleType(UserRoleType.TEACHER);
		loginUser.setSchoolNameList(Collections.singletonList("实验中学"));
		loginUser.setClassNameList(Collections.singletonList("一班"));
		LoginUserHolder.set(loginUser);
	}

	@AfterEach
	void clear() {
		LoginUserHolder.remove();
	}

	@Test
	void testExclude() {
		// 首次查询时排除部分 datascope，但是保留下来的 datascope 不会匹配中当前的 sql
		DataPermissionRule dataPermissionRule = new DataPermissionRule()
			.setExcludeResources(new String[] { ClassDataScope.RESOURCE_NAME, SchoolDataScope.RESOURCE_NAME });
		DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule,
				() -> Assertions.assertEquals(10, this.studentService.listStudent().size()));

		Integer size = DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule,
				() -> this.studentService.listStudent().size());
		Assertions.assertEquals(10, size);

		// 再使用完整的 datascope 规则进行查询
		List<Student> studentList1 = this.studentService.listStudent();
		Assertions.assertEquals(2, studentList1.size());
	}

	@Test
	void testTeacherSelect() {
		// 实验中学，一班总共有 2 名学生
		List<Student> studentList1 = this.studentService.listStudent();
		Assertions.assertEquals(2, studentList1.size());

		// 更改当前登录用户权限，让它只能看德高中学的学生
		LoginUser loginUser = LoginUserHolder.get();
		loginUser.setSchoolNameList(Collections.singletonList("德高中学"));

		// 德高中学，一班总共有 3 名学生
		List<Student> studentList2 = this.studentService.listStudent();
		Assertions.assertEquals(3, studentList2.size());

		/* 忽略权限控制，一共有 10 名学生 */
		// === 编程式 ===
		DataPermissionUtils
			.executeAndIgnoreAll(() -> Assertions.assertEquals(10, this.studentService.listStudent().size()));
		// === 注解 ====
		List<Student> studentList4 = this.studentService.listStudentWithoutDataPermission();
		Assertions.assertEquals(10, studentList4.size());

		/* 只控制班级的数据权限，实验中学 + 德高中学 一班总共有 5 名学生 */
		// === 编程式 ===
		DataPermissionRule dataPermissionRule1 = new DataPermissionRule()
			.setIncludeResources(new String[] { ClassDataScope.RESOURCE_NAME });
		DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule1,
				() -> Assertions.assertEquals(5, this.studentService.listStudent().size()));
		// === 注解 ====
		List<Student> studentList5 = this.studentService.listStudentOnlyFilterClass();
		Assertions.assertEquals(5, studentList5.size());

		/* 只控制学校的数据权限，"德高中学"、一班、二班 总共有 6 名学生 */
		// === 编程式 ===
		DataPermissionRule dataPermissionRule2 = new DataPermissionRule()
			.setIncludeResources(new String[] { SchoolDataScope.RESOURCE_NAME });
		DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule2,
				() -> Assertions.assertEquals(6, this.studentService.listStudent().size()));
		// === 注解 ====
		List<Student> studentList6 = this.studentService.listStudentOnlyFilterSchool();
		Assertions.assertEquals(6, studentList6.size());

	}

	@Test
	void testStudentSelect() {
		// 更改当前登录用户权限，设置角色为学生
		LoginUser loginUser = LoginUserHolder.get();
		loginUser.setUserRoleType(UserRoleType.STUDENT);

		// id 为 1 的学生叫 张三
		loginUser.setId(1L);
		List<Student> studentList1 = this.studentService.listStudent();
		Assertions.assertEquals(1, studentList1.size());
		Assertions.assertEquals("张三", studentList1.get(0).getName());

		// id 为 2 的学生叫 李四
		loginUser.setId(2L);
		List<Student> studentList2 = this.studentService.listStudent();
		Assertions.assertEquals(1, studentList2.size());
		Assertions.assertEquals("李四", studentList2.get(0).getName());

	}

	/**
	 * 权限规则优先级，由近及远： 方法内部的编程式规则 > 当前方法查找出来的注解规则 > 调用者的权限规则 > 全局规则
	 */
	@Test
	void testRulePriority() {
		// 全局数据权限，默认是全部 DataScope 都控制
		List<Student> studentList = this.studentService.listStudent();
		Assertions.assertEquals(2, studentList.size());

		// 编程式数据权限，
		DataPermissionRule dataPermissionRule = new DataPermissionRule()
			.setIncludeResources(new String[] { ClassDataScope.RESOURCE_NAME });
		DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule, () -> {
			// 编程式数据权限内部方法，走指定的规则
			List<Student> studentList2 = this.studentService.listStudent();
			Assertions.assertEquals(5, studentList2.size());

			// 嵌套的权限控制
			DataPermissionRule dataPermissionRule1 = new DataPermissionRule(true);
			DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule1, () -> {
				// 规则嵌套时，优先使用内部规则
				List<Student> studentList1 = this.studentService.listStudent();
				Assertions.assertEquals(10, studentList1.size());

				// 由于调用的方法上添加了注解，走该方法注解的权限规则
				List<Student> students1 = this.studentService.listStudentOnlyFilterClass();
				Assertions.assertEquals(5, students1.size());
				// 注解权限控制
				List<Student> students2 = this.studentService.listStudentOnlyFilterSchool();
				Assertions.assertEquals(4, students2.size());
			});
		});
	}

	@Test
	void testExecuteWithDataPermissionRule() {

		DataPermissionUtils.executeAndIgnoreAll(() -> {
			List<DataScope> dataScopes = this.dataPermissionHandler.filterDataScopes(null);
			Assertions.assertTrue(dataScopes.isEmpty());
		});

		DataPermissionRule dataPermissionRule = new DataPermissionRule(true);
		DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule, () -> {
			List<DataScope> dataScopes = this.dataPermissionHandler.filterDataScopes(null);
			Assertions.assertTrue(dataScopes.isEmpty());
		});

		DataPermissionRule dataPermissionRule1 = new DataPermissionRule()
			.setIncludeResources(new String[] { ClassDataScope.RESOURCE_NAME });
		DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule1, () -> {
			List<DataScope> dataScopes = this.dataPermissionHandler.filterDataScopes(null);
			Assertions.assertFalse(dataScopes.isEmpty());
			Assertions.assertEquals(1, dataScopes.size());
			Assertions.assertEquals(ClassDataScope.RESOURCE_NAME, dataScopes.get(0).getResource());
		});

		DataPermissionRule dataPermissionRule2 = new DataPermissionRule()
			.setExcludeResources(new String[] { SchoolDataScope.RESOURCE_NAME, StudentDataScope.RESOURCE_NAME });
		DataPermissionUtils.executeWithDataPermissionRule(dataPermissionRule2, () -> {
			List<DataScope> dataScopes = this.dataPermissionHandler.filterDataScopes(null);
			Assertions.assertFalse(dataScopes.isEmpty());
			Assertions.assertEquals(1, dataScopes.size());
			Assertions.assertEquals(ClassDataScope.RESOURCE_NAME, dataScopes.get(0).getResource());
		});
	}

}

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

package org.ballcat.datascope.test.datarule.service;

import java.util.List;

import org.ballcat.datascope.annotation.DataPermission;
import org.ballcat.datascope.test.datarule.datascope.ClassDataScope;
import org.ballcat.datascope.test.datarule.datascope.SchoolDataScope;
import org.ballcat.datascope.test.datarule.entity.Student;
import org.ballcat.datascope.test.datarule.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hccake
 */
@Service
public class StudentService {

	@Autowired
	@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
	private StudentMapper studentMapper;

	public List<Student> listStudent() {
		return this.studentMapper.listStudent();
	}

	@DataPermission(includeResources = ClassDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterClass() {
		return this.studentMapper.listStudent();
	}

	@DataPermission(includeResources = SchoolDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterSchool() {
		return this.studentMapper.listStudent();
	}

	@DataPermission(ignore = true)
	public List<Student> listStudentWithoutDataPermission() {
		return this.studentMapper.listStudent();
	}

}

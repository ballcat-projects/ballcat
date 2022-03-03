package com.hccake.ballcat.common.datascope.test.datarule.service;

import com.hccake.ballcat.common.datascope.annotation.DataPermission;
import com.hccake.ballcat.common.datascope.test.datarule.datascope.ClassDataScope;
import com.hccake.ballcat.common.datascope.test.datarule.datascope.SchoolDataScope;
import com.hccake.ballcat.common.datascope.test.datarule.entity.Student;
import com.hccake.ballcat.common.datascope.test.datarule.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author hccake
 */
@Service
public class StudentService {

	@Autowired
	private StudentMapper studentMapper;

	public List<Student> listStudent() {
		return studentMapper.listStudent();
	}

	@DataPermission(includeResources = ClassDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterClass() {
		return studentMapper.listStudent();
	}

	@DataPermission(includeResources = SchoolDataScope.RESOURCE_NAME)
	public List<Student> listStudentOnlyFilterSchool() {
		return studentMapper.listStudent();
	}

	@DataPermission(ignore = true)
	public List<Student> listStudentWithoutDataPermission() {
		return studentMapper.listStudent();
	}

}

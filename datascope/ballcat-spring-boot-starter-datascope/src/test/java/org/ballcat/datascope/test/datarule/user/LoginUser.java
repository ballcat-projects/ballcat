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

package org.ballcat.datascope.test.datarule.user;

import java.util.List;

import lombok.Data;

/**
 * 登录用户
 *
 * @author hccake
 */
@Data
public class LoginUser {

	/**
	 * 登录用户 id
	 */
	private Long id;

	/**
	 * 用户角色
	 */
	private UserRoleType userRoleType;

	/**
	 * 当前登录用户所拥有的班级
	 */
	private List<String> classNameList;

	/**
	 * 当前登录用户所拥有的学校
	 */
	private List<String> schoolNameList;

}

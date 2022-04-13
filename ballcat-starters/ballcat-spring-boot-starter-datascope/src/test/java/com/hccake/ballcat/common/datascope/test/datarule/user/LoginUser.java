package com.hccake.ballcat.common.datascope.test.datarule.user;

import lombok.Data;

import java.util.List;

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
	private Integer id;

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

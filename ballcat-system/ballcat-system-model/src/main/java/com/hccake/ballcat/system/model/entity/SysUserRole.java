package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.extend.mybatis.plus.alias.TableAlias;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;

/**
 * 用户角色表
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Data
@TableAlias("ur")
@TableName("sys_user_role")
@Schema(title = "用户角色")
public class SysUserRole {

	private static final long serialVersionUID = 1L;

	@TableId(type = IdType.AUTO)
	private Long id;

	/**
	 * 用户ID
	 */
	@Schema(title = "用户id")
	private Integer userId;

	/**
	 * 角色Code
	 */
	@Schema(title = "角色Code")
	private String roleCode;

}

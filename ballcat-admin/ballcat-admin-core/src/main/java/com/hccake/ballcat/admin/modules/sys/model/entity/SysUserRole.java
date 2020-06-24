package com.hccake.ballcat.admin.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 用户角色表
 * </p>
 *
 * @author
 * @since 2017-10-29
 */
@Data
@TableName("sys_user_role")
@ApiModel(value = "用户角色")
@EqualsAndHashCode(callSuper = true)
public class SysUserRole extends Model<SysUserRole> {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID
	 */
	@ApiModelProperty(value = "用户id")
	private Integer userId;

	/**
	 * 角色ID
	 */
	@ApiModelProperty(value = "角色id")
	private Integer roleId;

}

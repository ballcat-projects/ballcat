package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.common.model.entity.LogicDeletedBaseEntity;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Objects;

/**
 * 角色
 *
 * @author ballcat code generator
 * @date 2019-10-14 17:42:23
 */
@Getter
@Setter
@ToString
@TableName("sys_role")
@Schema(title = "角色")
public class SysRole extends LogicDeletedBaseEntity {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	@Schema(title = "角色编号")
	private Integer id;

	@NotBlank(message = "角色名称不能为空")
	@Schema(title = "角色名称")
	private String name;

	@NotBlank(message = "角色标识不能为空")
	@Schema(title = "角色标识")
	private String code;

	@Schema(title = "角色类型，1：系统角色 2：业务角色")
	private Integer type;

	@Schema(title = "数据权限")
	private Integer scopeType;

	@Schema(title = "数据范围资源，当数据范围类型为自定义时使用")
	private String scopeResources;

	@Schema(title = "角色备注")
	private String remarks;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SysRole sysRole = (SysRole) o;
		return code.equals(sysRole.code);
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

}

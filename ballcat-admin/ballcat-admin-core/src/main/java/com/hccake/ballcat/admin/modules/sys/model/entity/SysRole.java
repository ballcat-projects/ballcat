package com.hccake.ballcat.admin.modules.sys.model.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author ballcat
 * @since 2017-10-29
 */
@Data
@TableName("sys_role")
@ApiModel(value = "角色")
@EqualsAndHashCode(callSuper = true)
public class SysRole extends Model<SysRole> {

	private static final long serialVersionUID = 1L;

	@TableId(value = "id", type = IdType.AUTO)
	@ApiModelProperty(value = "角色编号")
	private Integer id;

	@NotBlank(message = "角色名称不能为空")
	@ApiModelProperty(value = "角色名称")
	private String name;

	@NotBlank(message = "角色标识不能为空")
	@ApiModelProperty(value = "角色标识")
	private String code;

	@ApiModelProperty(value = "角色备注")
	private String note;

	@ApiModelProperty("角色类型，1：系统角色 2：业务角色")
	private Integer type;

	/**
	 * 逻辑删除标识，已删除:0，未删除：删除时间戳
	 */
	@TableLogic
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "逻辑删除标识，已删除:0，未删除：删除时间戳")
	private Long deleted;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	@TableField(fill = FieldFill.INSERT)
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@ApiModelProperty(value = "修改时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

}

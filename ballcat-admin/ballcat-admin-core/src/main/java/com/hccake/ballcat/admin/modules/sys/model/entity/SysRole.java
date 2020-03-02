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
 * @author
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
	/**
	 * 删除标识（0-正常,1-删除）
	 */
	@TableLogic
	@ApiModelProperty(value = "删除标记,1:已删除,0:正常")
	private String delFlag;
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
	@TableField(fill = FieldFill.UPDATE)
	private LocalDateTime updateTime;


}

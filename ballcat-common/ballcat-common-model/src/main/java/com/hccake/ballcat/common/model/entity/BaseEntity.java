package com.hccake.ballcat.common.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 实体类基类
 *
 * @author hccake
 */
@Getter
@Setter
public abstract class BaseEntity implements Serializable {

	/**
	 * 创建者
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建者")
	private Integer createBy;

	/**
	 * 更新者
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "更新者")
	private Integer updateBy;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 修改时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "修改时间")
	private LocalDateTime updateTime;

}

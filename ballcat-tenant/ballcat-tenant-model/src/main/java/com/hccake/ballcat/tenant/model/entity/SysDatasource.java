package com.hccake.ballcat.tenant.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据源表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Data
@TableName("sys_datasource")
@ApiModel(value = "数据源表")
public class SysDatasource {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Long id;

	/**
	 * 数据源名称
	 */
	@ApiModelProperty(value = "数据源名称")
	private String name;

	/**
	 * 地址
	 */
	@ApiModelProperty(value = "地址")
	private String url;

	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;

	/**
	 * 密码
	 */
	@ApiModelProperty(value = "密码")
	private String password;

	/**
	 * 类型
	 */
	@ApiModelProperty(value = "类型")
	private Integer type;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Integer maxHolds;

	/**
	 * 额外信息
	 */
	@ApiModelProperty(value = "额外信息")
	private String extra;

	/**
	 * 描述
	 */
	@ApiModelProperty(value = "描述")
	private String description;

	/**
	 * 创建时间
	 */
	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(fill = FieldFill.INSERT_UPDATE)
	@ApiModelProperty(value = "更新时间")
	private LocalDateTime updateTime;

}

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
 * 租户表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Data
@TableName("sys_tenant")
@ApiModel(value = "租户表")
public class SysTenant {

	private static final long serialVersionUID = 1L;

	/**
	 * 主键
	 */
	@TableId
	@ApiModelProperty(value = "主键")
	private Long tenantId;

	/**
	 * 租户code
	 */
	@ApiModelProperty(value = "租户code")
	private String tenantCode;

	/**
	 * 租户名称
	 */
	@ApiModelProperty(value = "租户名称")
	private String tenantName;

	/**
	 * 域名地址
	 */
	@ApiModelProperty(value = "域名地址")
	private String url;

	/**
	 * 联系人
	 */
	@ApiModelProperty(value = "联系人")
	private String duty;

	/**
	 * 联系电话
	 */
	@ApiModelProperty(value = "联系电话")
	private String phone;

	/**
	 * 联系地址
	 */
	@ApiModelProperty(value = "联系地址")
	private String address;

	/**
	 * logo地址
	 */
	@ApiModelProperty(value = "logo地址")
	private String logo;

	/**
	 * 状态(1-正常,0-冻结)
	 */
	@ApiModelProperty(value = "状态(1-正常,0-冻结)")
	private Integer status;

	/**
	 * 创建时间
	 */
	@ApiModelProperty(value = "创建时间")
	private LocalDateTime expireTime;

	/**
	 * 逻辑删除标识，未删除为 0，已删除为删除时间
	 */
	@ApiModelProperty(value = "逻辑删除标识，未删除为 0，已删除为删除时间")
	private Long deleted;

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

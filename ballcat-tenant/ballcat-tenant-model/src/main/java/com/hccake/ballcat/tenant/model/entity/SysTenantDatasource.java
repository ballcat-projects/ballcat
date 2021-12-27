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
 * 租户数据源映射表
 *
 * @author huyuanzhi 2021-12-15 21:33:42
 */
@Data
@TableName("sys_tenant_datasource")
@ApiModel(value = "租户数据源映射表")
public class SysTenantDatasource {

	private static final long serialVersionUID = 1L;

	/**
	 *
	 */
	@TableId
	@ApiModelProperty(value = "")
	private Long id;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long tenantId;

	/**
	 *
	 */
	@ApiModelProperty(value = "")
	private Long datasourceId;

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

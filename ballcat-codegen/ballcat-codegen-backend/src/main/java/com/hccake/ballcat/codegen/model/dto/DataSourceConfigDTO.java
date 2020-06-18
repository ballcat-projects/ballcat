package com.hccake.ballcat.codegen.model.dto;

import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 数据源
 *
 * @author hccake
 * @date 2020-06-17 10:24:47
 */
@Data
@ApiModel(value = "数据源配置信息")
public class DataSourceConfigDTO{
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@TableId
	@ApiModelProperty(value = "ID")
	private Integer id;
	/**
	 * 名称
	 */
	@ApiModelProperty(value = "名称")
	private String name;
	/**
	 * 用户名
	 */
	@ApiModelProperty(value = "用户名")
	private String username;
	/**
	 * 密码（未加密）
	 */
	@ApiModelProperty(value = "密码")
	private String pass;
	/**
	 * 数据源连接
	 */
	@ApiModelProperty(value = "数据源连接")
	private String url;
}

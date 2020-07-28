package com.hccake.ballcat.admin.modules.lov.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.hccake.ballcat.admin.modules.lov.enums.HttpMethod;
import com.hccake.ballcat.admin.modules.lov.enums.HttpParamsPosition;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * @author lingting 2020/7/5 16:04
 */
@Data
@TableName("sys_lov")
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ApiModel(value = "lov模块")
public class Lov extends Model<Lov> {

	@TableId
	@ApiModelProperty("编号")
	private Long id;

	@ApiModelProperty("关键字，唯一，加载lov数据时通过关键字加载")
	private String keyword;

	@NotBlank
	@ApiModelProperty("获取数据时请求路径")
	private String url;

	@NotBlank
	@ApiModelProperty("http请求方式")
	private HttpMethod method;

	@NotBlank
	@ApiModelProperty("http请求参数设置位置")
	private HttpParamsPosition position;

	@TableField("`key`")
	@ApiModelProperty("数据的key")
	private String key;

	@Pattern(regexp = "^\\{.*}")
	@ApiModelProperty("固定请求参数，请设置 jsonString, 默认值 {}")
	private String fixedParams;

	@ApiModelProperty("是否需要多选")
	private boolean multiple;

	@ApiModelProperty("是否需要搜索框")
	private boolean search;

	@ApiModelProperty("是否需要返回数据, false则不会有确定按钮`	`")
	private boolean ret;

	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

}

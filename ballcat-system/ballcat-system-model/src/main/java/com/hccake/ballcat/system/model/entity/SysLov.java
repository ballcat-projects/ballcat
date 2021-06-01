package com.hccake.ballcat.system.model.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hccake.ballcat.system.enums.HttpMethodEnum;
import com.hccake.ballcat.system.enums.HttpParamsPositionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

/**
 * list of value 值列表，又名弹窗选择器
 *
 * @author lingting 2020/7/5 16:04
 */
@Data
@TableName("sys_lov")
@Accessors(chain = true)
@ApiModel(value = "弹窗选择器")
public class SysLov {

	@TableId
	@ApiModelProperty("编号")
	private Long id;

	@ApiModelProperty("标题")
	private String title;

	@ApiModelProperty("关键字，唯一，加载lov数据时通过关键字加载")
	private String keyword;

	@NotBlank
	@ApiModelProperty("获取数据时请求路径")
	private String url;

	@NotBlank
	@ApiModelProperty("http请求方式")
	private HttpMethodEnum method;

	@NotBlank
	@ApiModelProperty("http请求参数位置")
	private HttpParamsPositionEnum position;

	@TableField("`key`")
	@ApiModelProperty("数据的key")
	private String key;

	@Pattern(regexp = "^\\{.*}")
	@ApiModelProperty("固定请求参数，请设置 jsonString, 默认值 {}")
	private String fixedParams;

	@ApiModelProperty("是否需要多选")
	private Boolean multiple;

	@ApiModelProperty("是否需要返回数据, false则不会有确定按钮")
	private Boolean ret;

	@ApiModelProperty("返回数据的字段")
	private String retField;

	/**
	 * 更新时间
	 */
	@ApiModelProperty(value = "更新时间")
	@TableField(fill = FieldFill.INSERT_UPDATE)
	private LocalDateTime updateTime;

	@TableField(fill = FieldFill.INSERT)
	@ApiModelProperty("创建时间")
	private LocalDateTime createTime;

}
